import java.lang.String;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Date;

public class ATMServer3 {
    // 定义服务器监听的端口
    private static final int SERVER_PORT = 2525;
    // 数据库连接字符串
    private static final String DB_URL = "jdbc:mysql://localhost:3306/server1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // 数据库用户名
    private static final String DB_USER = "root";
    // 数据库密码，需替换为实际密码
    private static final String DB_PASSWORD = "chy200441";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("服务器已启动，等待连接...");
            // 循环接收客户端连接
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端已连接.");
                // 为每个客户端创建一个新的线程以处理请求
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("服务器异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 内部类用于处理每个客户端的请求
    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                String command;
                // 接收客户端发送的第一行命令
                command = input.readLine();
                System.out.println("Received: " + command);

                String[] parts = command.split(" ");
                // 命令格式错误时返回错误信息
                if (parts.length < 2) {
                    output.writeBytes("401 ERROR!" + "\n");
                    return;
                }

                String cmdType = parts[0];
                int userId = -1;

                // 处理HELO命令，初始化会话
                if ("HELO".equals(cmdType)) {
                    userId = Integer.parseInt(parts[1]);
                    output.writeBytes("500 AUTH REQUIRE" + "\n");
                }

                // 循环读取并处理来自客户端的命令
                while (true) {
                    command = input.readLine();
                    if ("BYE".equals(command)) {
                        output.writeBytes("BYE" + "\n");
                        break;
                    }
                    processCommand(command, output, userId);
                }
            } catch (IOException e) {
                System.out.println("客户端连接异常: " + e.getMessage());
            } finally {
                // 确保最终关闭socket连接
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("无法关闭socket: " + e.getMessage());
                }
            }
        }

        // 根据接收的命令，调用相应的处理函数
        void processCommand(String command, DataOutputStream output, int userId) throws IOException {
            String[] parts = command.split(" ");
            String cmdType = parts[0];
            double amount;
            boolean success;

            try {
                switch (cmdType) {
                    case "PASS":
                        String pin = parts[1];
                        // 验证PIN码
                        if (verify(userId, pin)) {
                            output.writeBytes("525 OK!" + "\n");
                        } else {
                            output.writeBytes("401 ERROR!" + "\n");
                        }
                        break;
                    case "BALA":
                        // 获取并返回账户余额
                        double balance = getBalance(userId);
                        output.writeBytes("AMNT:" + balance + "\n");
                        break;
                    case "DEPOSIT":
                        // 处理存款请求
                        amount = Double.parseDouble(parts[1]);
                        success = updateBalance(userId, "DEPOSIT", amount);
                        output.writeBytes(success ? "525 OK!"+"\n" : "401 ERROR!"+"\n");
                        logTransaction(userId, "DEPOSIT", amount, success);
                        break;
                    case "WDRA":
                        // 处理取款请求
                        amount = Double.parseDouble(parts[1]);
                        if (checkBalance(userId, amount)) {
                            success = updateBalance(userId, "WITHDRAW", -amount);
                            output.writeBytes(success ? "525 OK!"+"\n" : "401 ERROR!"+"\n");
                            logTransaction(userId, "WITHDRAW", amount, success);
                        } else {
                            output.writeBytes("401 ERROR!"+"\n");
                        }
                        break;
                    default:
                        // 处理未知命令
                        output.writeBytes("401 ERROR! Unknown command"+"\n");
                        break;
                }
            } catch (SQLException e) {
                output.writeBytes("操作错误: " + e.getMessage());
            }
        }

        // 从数据库中获取指定用户的账户余额
        private double getBalance(int userId) throws SQLException {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("SELECT Balance FROM Accounts WHERE UserID = ?")) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("Balance");
                    }
                }
            }
            return 0;
        }

        // 验证用户密码
        private boolean verify(int userId, String pin) throws SQLException {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Users WHERE UserID = ?")) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("Password").equals(pin);
                    }
                    return false;
                }
            }
        }

        // 更新指定用户的账户余额
        private boolean updateBalance(int userId, String type, double amount) throws SQLException {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE Accounts SET Balance = Balance + ? WHERE UserID = ?")) {
                stmt.setDouble(1, amount);
                stmt.setInt(2, userId);
                return stmt.executeUpdate() > 0;
            }
        }

        // 检查用户账户是否有足够余额
        private boolean checkBalance(int userId, double amount) throws SQLException {
            return getBalance(userId) >= amount;
        }

        // 记录交易日志到数据库
        private void logTransaction(int userId, String type, double amount, boolean success) throws SQLException {
            String status = success ? "成功" : "失败";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO Logs (UserID, LogLevel, Message, LogDate) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, userId);
                stmt.setString(2, "INFO");
                stmt.setString(3, "交易类型: " + type + ", 金额: " + amount + ", 状态: " + status);
                stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("日志记录失败: " + e.getMessage());
            }
        }
    }
}
