package Computernetwoking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;

public class window2 extends JFrame {
    static String command = null;
   static BufferedReader reader;
    static DataOutputStream output;
    public window2() {

        setTitle("ATM用户服务界面");
        setBounds(200, 200, 600, 600);
        setVisible(true);
        Font font1 = new Font("华文行楷", Font.PLAIN, 30);
        JPanel jPanel1 = new photo("images/img_1.png");
        jPanel1.setLayout(null);
        JLabel jLabel1=new JLabel("请选择以下服务：");
        JButton jButton1 = new JButton("查询余额");
        JButton jButton2 = new JButton("存钱");
        JButton jButton3 = new JButton("取钱");
        JButton jButton4 = new JButton("退出");
        jButton1.setMargin(new Insets(0,0,0,0));
        jButton2.setMargin(new Insets(0,0,0,0));
        jButton3.setMargin(new Insets(0,0,0,0));
        jButton4.setMargin(new Insets(0,0,0,0));
        jLabel1.setBounds(20,20,250,60);
        jPanel1.setBounds(0, 0, 600, 600);
        jButton1.setBounds(110, 200, 150, 60);
        jButton1.setForeground(Color.orange);
        jButton2.setBounds(400, 200, 100, 60);
        jLabel1.setFont(font1);
        jButton1.setFont(font1);
        jButton2.setFont(font1);
        jButton3.setFont(font1);
        jButton4.setFont(font1);
        jButton3.setBounds(110, 400, 100, 60);
        jButton4.setBounds(400, 400, 100, 60);
        jPanel1.add(jLabel1);
        jPanel1.add(jButton1);
        jPanel1.add(jButton2);
        jPanel1.add(jButton3);
        jPanel1.add(jButton4);
        add(jPanel1);

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == jButton1) {
                    command = "BALA";
                    window.send(command);
                    System.out.println(window.reive());
                  other();
                }
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == jButton2) {
                    windowinput win1 = new windowinput();
                    Boy police = new Boy();
                    win1.addWindowListener(police);
                }
            }
        });
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == jButton3) {
                    windowinput win2 = new windowinput();
                    Boy1 police1 = new Boy1();
                    win2.addWindowListener(police1);
                }
            }
        });
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == jButton4) {
                    command = "BYE";
                     window.send(command);
                  System.out.println(window.reive());
                    other();
                }
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void continueProgram() {command = "DEPOSIT "+windowinput.command1;
      }
    public void continueProgram1() {
        command = "WDRA " + windowinput.command1;
    }

    class Boy extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            continueProgram();
            window.send(command);
            System.out.println(window.reive());
            other();
        }
    }

    class Boy1 extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            continueProgram1();
            window.send(command);
            System.out.println(window.reive());
            other();
        }
    }
     public static void setreader(BufferedReader read){
        reader=read;
    }
    public static void setoutput(DataOutputStream read){
        output=read;
    }
    public static void other() {
        command = null;
    }
    }