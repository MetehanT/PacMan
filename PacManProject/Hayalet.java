package PacManProject;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Hayalet {
	private int x, y, dx = 0, dy = 0;
	private int width = 32, height = 32;
	private Image hayalet1, hayalet2, hayalet3, hayalet4;
	boolean animOzelGuc = false;
	
	public Hayalet(int x, int y) {
		this.x = x;
		this.y = y;
		
		loadImage();
		
	}
	
	public void hareket() {
    	x += dx;
        y += dy;
        
    }
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
	}
	
	public void setDx(int dx) {
		this.dx = dx;
	}
	
	public void setDy(int dy) {
		this.dy = dy;
	}
	
	public void setOzelGuc(boolean ozelGuc) {
		animOzelGuc = ozelGuc;
	}
	
	private void loadImage() {
		hayalet1 = new ImageIcon("src/images/h1.png").getImage();
		hayalet2 = new ImageIcon("src/images/h2.png").getImage();
		hayalet3 = new ImageIcon("src/images/h3.png").getImage();
		hayalet4 = new ImageIcon("src/images/h4.png").getImage();
	}
	
	public Image getImage(boolean anim) {
		if(animOzelGuc == false) {
			if(anim  == false)
				return hayalet1;
	    	else
	    		return hayalet2;
		}else {
			if(anim == false)
				return hayalet3;
			else
				return hayalet4;
		}
		
		
	}
	
	public Rectangle getBounds() {
    	return new Rectangle(x,y,width,height);
    }
	
}
