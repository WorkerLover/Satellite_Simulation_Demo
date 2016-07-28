package jat.application.orbitviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.*;
public class move_example extends JFrame implements Runnable {
	static int i = 0;
	static int j = 250;
	static double x = 0;
	static double v = 10;
	static double w = 2*Math.PI;
	static double A = 50;
	static double t = 0;
	
	public move_example() {
		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillOval(i, j, 10, 10);
		g.fillOval(i, j+(int)x, 10, 10);
		
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
			} catch(InterruptedException e) {
				//e.printStackTrace();
			}
			i+=v;
			x = A*Math.cos(w*t);
			t+=0.1;
			this.repaint();
			if(i>500)
				i = 0;
		}
	}
	
	public static void main(String args[]) {
		new Thread(new move_example()).start();
	}
}