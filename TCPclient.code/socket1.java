package Computernetwoking;

import java.io.*;
import java.net.*;
public class socket1 {
    public static Socket socket;
    public socket1() {
        try{socket=new Socket("127.0.0.1",2525);
            System.out.println("服务器连接成功");}
        catch( IOException e){
            System.out.println("无法连接到服务器: " + e.getMessage());
        }
    }
    public  void senddata(String data){
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeBytes(data+"\n");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public  String receive(){
        String receivedata=null;
        try {
            BufferedReader reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            receivedata=reader.readLine();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return receivedata;
    }
}