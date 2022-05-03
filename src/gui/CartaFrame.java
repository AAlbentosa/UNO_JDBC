package gui;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CartaFrame extends JFrame{
	
	public CartaFrame() {}
	
	public void showImage(String color, String number) {
		
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setUndecorated(true);
        
        ImageIcon image = new ImageIcon("cartas/"+color+"/"+number+".jpg");
        JLabel lbl = new JLabel(image);
        getContentPane().add(lbl);

        setSize(image.getIconWidth(), image.getIconHeight());

        int x = (screenSize.width - this.getSize().width)/2;
        int y = (screenSize.height - this.getSize().height)/2;

        setLocation(x, y);
        setVisible(true);
        
        
        
        addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				setVisible(false);
			}
            
        });
        
        try {
			System.in.read();
		} catch (IOException e1) {
			System.out.println("ERROR");
		}
	}
}