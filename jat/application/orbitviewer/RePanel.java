package jat.application.orbitviewer;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class RePanel extends JPanel {
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon image = new ImageIcon(getClass().getResource(
				"C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\二维世界地图.jpg"));
		image.setImage(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_FAST));
		image.paintIcon(this, g, 0, 0);
	}
}