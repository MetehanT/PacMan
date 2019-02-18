package PacManProject;


import javax.swing.JFrame;


@SuppressWarnings("serial") //eclipse in serial uyarýsý vermemesi için
public class Pacman2d extends JFrame {
	

	public Pacman2d() {
		olustur();
	}
	
	public void olustur() {
		
		add(new Panel());
		
		setTitle("Pac-Man");
        setSize(800, 480);
        setResizable(false);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {

		Pacman2d pac = new Pacman2d();
		pac.setVisible(true);
		
	}

}
