package PacManProject;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Karakter{

    private int dx, dy;
    private int x = 380;
    private int y = 355;
    private int w = 27;
    private int h = 27;
    private boolean ozelGuc = false, oyunAktif = false, reset = false;
    private Image right1, right2, left1, left2, down1, down2, up1, up2;
    private int yon = 0; //Karateri çizerken yön kontrolü için bu deðiþken kullanýlacak

    public Karakter(){

        loadImage();
    }

    private void loadImage() {

        
    	right1 = new ImageIcon("src/images/right1.png").getImage();
    	right2 = new ImageIcon("src/images/right2.png").getImage();
    	left1 = new ImageIcon("src/images/left1.png").getImage();
    	left2 = new ImageIcon("src/images/left2.png").getImage();
    	down1 = new ImageIcon("src/images/down1.png").getImage();
    	down2 = new ImageIcon("src/images/down2.png").getImage();
    	up1 = new ImageIcon("src/images/up1.png").getImage();
    	up2 = new ImageIcon("src/images/up2.png").getImage();
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
    
    public void setX(int x) {
    	this.x = x;
    }
    
    public void setY(int y) {
    	this.y = y;
    }

    public int getWidth() {

        return w;
    }

    public int getHeight() {
        return h;
    }
    
    public void setdx(int dx) {
    	this.dx = dx;
    }
    
    public void setdy(int dy) {
    	this.dy = dy;
    }
    
    public void setOzelGuc(boolean ozelGuc) {
    	this.ozelGuc = ozelGuc;
    }
    
    public boolean getOzelGuc() {
    	return ozelGuc;
    }
    
    public void setOyunAktif(boolean oyunAktif) {
		this.oyunAktif = oyunAktif;
	}
    
    public boolean getOyunAktif() {
		return oyunAktif;
	}
    
    public boolean getReset() {
		return reset;
	}
    
    
    public Image getImage(boolean anim) {

        if(yon == 1) {
        	if(anim  == false || (dx == 0 && dy == 0))
        		return left1;
        	else
        		return left2;
        }
        if(yon == 2) {
        	if(anim  == false || (dx == 0 && dy == 0))
        		return right1;
        	else
        		return right2;
        }
        else if(yon == 3) {
        	if(anim  == false || (dx == 0 && dy == 0))
        		return up1;
        	else
        		return up2;
        }
        else if(yon == 4) {
        	if(anim  == false || (dx == 0 && dy == 0))
        		return down1;
        	else
        		return down2;
        }
		return up1;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
            dy = 0;
            yon = 1;
            oyunAktif = true;
            reset = false;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
            dy = 0;
            yon = 2;
            oyunAktif = true;
            reset = false;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -2;
            dx = 0;
            yon = 3;
            oyunAktif = true;
            reset = false;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 2;
            dx = 0;
            yon = 4;
            oyunAktif = true;
            reset = false;
        }
        
        
    }
    
    public Rectangle getBounds() {
    	return new Rectangle(x,y,w,h);
    }
    
    public Rectangle getBoundsBottom() {
    	return new Rectangle(x+5,y+26,15,2);
    }
    
    public Rectangle getBoundsTop() {
    	return new Rectangle(x+5,y,15,2);
    }
    
    public Rectangle getBoundsLeft() {
    	return new Rectangle(x,y+5,2,5);
    }
    
    public Rectangle getBoundsRight() {
    	return new Rectangle(x+26,y+5,2,15);
    }
}
