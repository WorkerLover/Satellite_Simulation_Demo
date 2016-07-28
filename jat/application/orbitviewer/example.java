package jat.application.orbitviewer;

import java.util.List;
import java.util.ArrayList;

import java.util.Random;

import jat.core.plot.plot.Plot2DPanel;
import jat.core.plot.plot.Plot3DPanel;

import core.SimScenario;
import core.DTNHost;
import core.Settings;

import java.awt.*; 

import javax.swing.*;

public class example extends JFrame
{
//创建一个容器
Container ct;
//创建背景面板。
BackgroundPanel bgp;
/*JPanel jp;

//创建一个按钮，用来证明我们的确是创建了背景图片，而不是一张图片。
JButton jb;*/

Plot2DPanel plot;
int steps = 200;
int size1 = 180;
int size2 = 90;
double[][] BL = new double[steps][2];

public static void main(String[] args)
{
   new example();
}
public example()
{
   //不采用任何布局方式。
   ct=this.getContentPane();
   this.setLayout(new BorderLayout());;

   //在这里随便找一张400*300的照片既可以看到测试结果。
   bgp=new BackgroundPanel((new ImageIcon(
		"C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\二维世界地图.jpg")).getImage());
   bgp.setBounds(0,0,400,300);
   ct.add(bgp);
   
   
   plot = new Plot2DPanel("SOUTH");
//   plot.setBackground(null);
   for(int i=0;i<steps;++i) {
	   BL[i][0] = i;
	   BL[i][1] = i;
   }
   plot.addLinePlot("orbit",BL);
   
   //创建按钮
/*   jb=new JButton("测试按钮");
   jb.setBounds(60,30,160,30);
   ct.add(jb);*/
/*   jp = new JPanel();
   jp.add(jb);*/
   this.add(plot);
   

   this.setSize(400,300);
   this.setLocation(400,300);
   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   this.setVisible(true);
}
}
class BackgroundPanel extends JPanel
{
Image im;
public BackgroundPanel(Image im)
{
   this.im=im;
   this.setOpaque(true);
}
//Draw the back ground.
public void paintComponent(Graphics g)
{
   super.paintComponents(g);
   g.drawImage(im,0,0,this.getWidth(),this.getHeight(),this);

}
}