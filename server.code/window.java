package Computernetwoking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class window extends JFrame {
    public static String commandx;
    public static String commandy;
    public static socket1 socket2=new socket1();
    JPanel jPanel=new photo("images/x.jpg");
    static JTextField jTextField=new JTextField(12);
     static JPasswordField jPasswordField=new JPasswordField(12);
    public window(){
        setTitle("ATM用户登录界面");
        setBounds(200,200,600,600);
        setVisible(true);
        //判断密码字符
        Font font=new Font("华文行楷",Font.PLAIN,40);
        jPanel.setLayout(null);

        JLabel jLabel1=new JLabel("用户名");
        JLabel jLabel2=new JLabel("密码");

        jLabel1.setFont(font);
        jLabel1.setForeground(Color.orange);
        jLabel2.setFont(font);
        jLabel2.setForeground(Color.black);

        JButton jButton1 = new JButton("确定");
        jButton1.setMargin(new Insets(0,0,0,0));
        jButton1.setFont(font);
        jButton1.setBounds(200,250,100,100);

        jLabel1.setBounds(50,50,120,30);
        jLabel2.setBounds(50,200,80,30);
        jTextField.setBounds(200,50,200,30);
        jPasswordField.setBounds(200,200,200,30);
        jPanel.setBounds(200,200,300,300);
        jPanel.add(jButton1);
        jPanel.add(jLabel1);
        jPanel.add(jLabel2);
        jPanel.add(jTextField);
        jPanel.add(jPasswordField);
        add(jPanel);

        setVisible(true);

       jTextField.addActionListener(new ActionListener() {
           @Override
           public  void actionPerformed(ActionEvent e) {
               String d=jTextField.getText();
               transfer(d);
               send(commandx);
               System.out.println(reive());
           }
       });
       jPasswordField.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              char[] password=jPasswordField.getPassword();
              String x=new String(password);
               transferx(x);
               send(commandy);
               System.out.println(reive());
           }
       });
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource( )==jButton1){
                    dispose();
                   new window2();
                }
            }
        });
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

    public static void transfer(String a){
        commandx="HELO "+a;
    }
    public static void transferx(String a){commandy="PASS "+a;}
    public static void send(String data){
        socket2.senddata(data);
    }
    public static String reive(){
       return socket2.receive();
    }
}


