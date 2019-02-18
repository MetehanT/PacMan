package PacManProject;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Diskler {
	private int width = 10, height = 10, x, y;
	private Image kucukDisk, buyukDisk;
	
	public Diskler(int x, int y) {
		this.x = x;
		this.y = y;
		
		loadImage();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	private void loadImage() {
		kucukDisk = new ImageIcon("src/images/disk1.png").getImage();
		buyukDisk = new ImageIcon("src/images/disk2.png").getImage();
	}
	
	public Image getKucukDisk() {
		return kucukDisk;
	}
	
	public Image getBuyukDisk() {
		return buyukDisk;
	}
	
	public Rectangle getBounds() {
    	return new Rectangle(x,y,width,height);
    }
	public Rectangle getBoundsBuyuk() {
    	return new Rectangle(x,y,22,22);
    }
}
