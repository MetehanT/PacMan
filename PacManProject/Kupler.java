package PacManProject;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Kupler {
	int width = 20, height = 20, x, y;
	private Image kup;
	
	public Kupler(int x, int y) {
		this.x = x;
		this.y = y;
		
		loadImage();
	}
	
	private void loadImage() {
		kup = new ImageIcon("src/images/kup.png").getImage();
	}
	
	public Image getKup() {
		return kup;
	}
	
	public Rectangle getBounds() {
    	return new Rectangle(x,y,width,height);
    }
	
	
}
