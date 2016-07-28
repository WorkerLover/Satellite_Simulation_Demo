package jat.application.orbitviewer;

import jat.core.plot.plot.FrameView;
import jat.core.plot.plot.Plot2DPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;

import java.util.List;
import java.util.ArrayList;
import core.DTNHost;
import core.Settings;

public class Function1 extends JFrame /*implements Runnable*/{
	private static ImageIcon image = new ImageIcon(
		//	"C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\MapOfTheWorld.jpg");
			"C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\worldmap1.jpg");
	private static final double WIDTH =Toolkit.getDefaultToolkit().getScreenSize().getWidth();// image.getIconWidth();
	private static final double HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();//image.getIconHeight();
	private static final int INCREMENT = 20;
	Plot2DPanel plot = new Plot2DPanel();
	
	int SIZE1 = 180;
	int SIZE2 = 90;
	List<DTNHost> hosts;
	int steps = 200;
	double[][] BL = new double[steps][2];
	
	public static void main(String[] args) {
	//	new Function();
	}
	
	public Function1(List<DTNHost> hosts) {
		
		this.hosts = hosts;
		
		this.setTitle("ÎÀÐÇ¹ì¼£µÄ¶þÎ¬×ø±êÍ¼");
		this.setLocation(50, 50);
	//	this.setSize(1000, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(null);
		
		
		JLabel label = new JLabel(image);
		label.setBounds(0, 0, (int)WIDTH, (int)HEIGHT);
		this.add(label,BorderLayout.CENTER);
		
		for(int i=0;i<hosts.size();++i) {
			plot.addLinePlot("orbit", hosts.get(i).getBL());
			
		}
		this.add(plot);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		this.setSize((int)WIDTH, (int)HEIGHT);
		}
}
	