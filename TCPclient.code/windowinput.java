package Computernetwoking;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class windowinput extends JFrame {
    JPanel jPanel=new photo("images/img_2.png");
     static String command1;
    JTextField jTextField=new JTextField(12);
    public windowinput(){
        setTitle("存钱或取钱窗");
        jPanel.setLayout(null);
        setBounds(300,200,500,250);
        setVisible(true);
        JLabel jLabel1=new JLabel("请输入金额");
        jPanel.setBounds(300,200,200,200);
        jLabel1.setBounds(80,40,150,150);
        jTextField.setBounds(180,100,200,30);
        jPanel.add(jLabel1);
        jPanel.add(jTextField);
        add(jPanel);
        listener listener1=new listener();
        jTextField.addActionListener(listener1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
   static class  listener implements ActionListener {
        public String commands;
        public  void actionPerformed(ActionEvent e){
            commands=e.getActionCommand();
            transfer(commands);
        }
    }
    public static void transfer(String a){
        command1=a;
    }
}
