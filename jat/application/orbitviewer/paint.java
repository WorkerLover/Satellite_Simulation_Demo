package jat.application.orbitviewer;

import java.awt.*;    
import javax.swing.*;

public class paint extends JFrame{  
String str = "C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\二维世界地图.jpg";  
MyPanel mp1 = new MyPanel();  
JLabel jl1 = new JLabel("hello");  
JLabel jl2 = new JLabel("hello");  
paint(){  
mp1.setLayout(null);  
setBounds(100, 100, 400, 400);  
setDefaultCloseOperation(EXIT_ON_CLOSE);  
add(mp1);  
jl1.setBounds(10,10,40,20);  
jl2.setBounds(40,40,40,20);  
mp1.add(jl1);  
mp1.add(jl2);  
setVisible(true);  
  
}  
class MyPanel extends JPanel{  
Image im = Toolkit.getDefaultToolkit().getImage(str);  
public void paintComponent(Graphics g){  
g.drawImage(im, 0, 0, null);  
}  
}  
public static void main(String[] args){  
new paint();  
}  
  
  
}  