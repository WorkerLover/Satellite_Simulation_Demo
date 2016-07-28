package jat.application.orbitviewer;

import java.awt.*;
import javax.swing.*;

public class First extends JDialog {
private final JPanel contentPanel = new JPanel();
//private JTextField textField;
/**
* Launch the application.
*/
public static void main(String[] args) {
try {
First dialog = new First();
dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
dialog.setVisible(true);
} catch (Exception e) {
e.printStackTrace();
}
}
/**
* Create the dialog.
*/
public First() {
setBounds(100, 100, 450, 300);
getContentPane().setLayout(new BorderLayout());
contentPanel.setLayout(new GridLayout(1, 1));
getContentPane().add(contentPanel, BorderLayout.CENTER);
JLabel bgLb = new JLabel();
bgLb.setIcon(new ImageIcon(
		"C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\jat\\application\\orbitviewer\\二维世界地图.jpg"));
bgLb.setHorizontalAlignment(SwingConstants.CENTER);
contentPanel.add(bgLb);
bgLb.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));
//textField = new JTextField();
JButton jb = new JButton("hello");
bgLb.add(jb);
//jb.setColumns(10);
}
}