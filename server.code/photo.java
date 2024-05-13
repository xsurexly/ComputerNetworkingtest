package Computernetwoking;

import javax.swing.*;
import java.awt.*;

public class photo extends JPanel {
    final Image backgroundimage;
    public photo(String iamgepath){
        this.backgroundimage=new ImageIcon(iamgepath).getImage();
    }
    @Override
   protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundimage,0,0,getWidth(),getHeight(),this);
    }
}
