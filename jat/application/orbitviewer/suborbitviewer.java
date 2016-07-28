package jat.application.orbitviewer;

import java.util.List;
import java.util.ArrayList;
import core.DTNHost;
import core.Settings;

import java.util.Random;

import jat.core.plot.plot.Plot2DPanel;
import jat.core.plot.plot.Plot3DPanel;

import java.awt.BorderLayout;
import java.awt.Component;
 
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;

import core.SimScenario;
 
public class suborbitviewer extends JFrame {
	
	JLayeredPane layeredPane;
	
//    JPanel panel;
    JPanel panel2;
    JLabel label;
    ImageIcon image;
    Plot2DPanel plot;
    int steps = 200;
    int size1 = 180;
    int size2 = 90;
    
  //  List<DTNHost>hosts;
    double[][] BL = new double[steps][2];
    
 
    public suborbitviewer(List<DTNHost> hosts) {
		
    	System.out.println("test");
    	layeredPane = new JLayeredPane();
    	image = new ImageIcon(
                "C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\二维世界地图.jpg");
   // 	this.hosts = hosts;
    	
    	
        label = new JLabel(image);
		label.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		
		
		JButton jb = new JButton("test");
		jb.setBounds(100, 100, 100, 100);
		
		JLabel jl = new JLabel("hello");
		jl.setBounds(10, 10, 40, 20);
		
	
        plot = new Plot2DPanel("SOUTH");
       
        System.out.println("...1");     
    /*    plot.setFixedBounds(0,-180, 180);
        plot.setFixedBounds(1,-90, 90);*/
        
        System.out.println("...");
        for(int j=0;j<hosts.size()-23;++j) {
        	plot.addLinePlot("orbit"+j, hosts.get(j).getBL());
        	plot.addScatterPlot("satellite"+j, hosts.get(j).get2Dpoints());
        }
        
  /*      for(int i=0;i<steps;++i) {
        	BL[i][0] = i;
        	BL[i][1] = i;
        }*/
    //    plot.addLinePlot("orbit",BL);
        
        System.out.println("...");
		
        plot.setBackground(null);
        
        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        this.add(label);
//		panel2.add(plot);
        System.out.println("......");
        this.add(plot);
        System.out.println("......");
//		this.add(panel2);
//		layeredPane.add(label,JLayeredPane.DEFAULT_LAYER);
//		layeredPane.add(panel2,JLayeredPane.DEFAULT_LAYER);
		
//        this.setLayeredPane(layeredPane);
        
        System.out.println("width "+ image.getIconWidth());
        System.out.println("height "+ image.getIconHeight());
        this.setSize(image.getIconWidth(),image.getIconHeight());
        this.setLocation(image.getIconWidth(),image.getIconHeight());
        this.setVisible(true);
        }
    
    public static void main(String[] args) {
    //	List<DTNHost> hosts;
    //    suborbitviewer t = new suborbitviewer();
    }
}