package PacManProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;



@SuppressWarnings("serial") //eclipse in serial uyar�s� vermemesi i�in
public class Panel extends JPanel implements ActionListener {
	
	private Timer timer;
	private Karakter pacman; //kontrol etti�imiz karakter
	private final int DELAY = 20;
	private int ani = 1, hayaletR, ozelgS = 0, skor = 0, skor2, level = 1, hayaletSayisi = 4;
	private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
	private boolean anim = false, oyunAktif = false, reset = false;
	private List<Kupler> kups = new ArrayList<Kupler>(); //k�pleri tuttu�umuz liste
	private List<Diskler> disk = new ArrayList<Diskler>(); //k���k diskleri tuttu�umuz liste
	private List<Diskler> buyukDisk = new ArrayList<Diskler>(); //b�y�k diskleri tuttu�umuz liste
	private List<Hayalet> hayalet = new ArrayList<Hayalet>(); //hayaletleri tuttu�umuz liste
	Random r = new Random(); //Hayalerlerin k��elerden hangi y�ne d�nece�ini belirlemek i�in olu�turulan random
	
	
	public Panel() {
		
		eklePanel();
		labirentEkle();
		diskEkle();
		hayaletEkle();
	}
	
	public int getOzelgS() {
		return ozelgS;
	}
	
	private void eklePanel() {
		
		addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK); //arka plan siyah
        
        pacman = new Karakter(); //Oyunda y�netece�imiz karakteri olu�turduk
        
        
        timer = new Timer(DELAY, this);
        timer.start();
		
	}
	
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        oyunAktif = pacman.getOyunAktif(); //Oyunun oynan�r durumda oldu�unu karakter s�n�f�ndan ald�k
        cizdir(g);
        labirentCizdir(g);
        diskCizdir(g);
        
        
        if(reset) {
        	resetle(); //Karakter hayaletler taraf�ndan �ld�r�ld���nde �al��acak
        }
        
        if(oyunAktif) {
        	oyna(g); //Oyun oynanabilir durumda ise gerekli metod �a��r�lacak
        }else
        	girisEkrani(g);
        
        reset = pacman.getReset(); 
        
        skorCiz(g);
        carpisma();

        Toolkit.getDefaultToolkit().sync();
    }
	
	private void girisEkrani(Graphics g2d) { //giri� ekran�, level atlama ve oyuna yeniden ba�lama ekranlar�

        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(220, 160, 340, 70);
        g2d.setColor(Color.white);
        g2d.drawRect(220, 160, 340, 70);
        
        	
        String r = "Kaybettiniz, puan�n�z: " + skor2;
        String r2 = "Tekrar oynamak i�in y�n tu�lar�n� kullan�n";
        String l = "Tebrikler, seviye atlad�n�z";
        String l2 = "Devam etmek i�in y�n tu�lar�n� kullan�n";
        String s = "Ba�lamak i�in y�n tu�lar�n� kullan�n";
        Font small = new Font("Helvetica", Font.BOLD, 14);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        
        if(skor2 == 0 && skor == 0) {
        	g2d.drawString(s, 250, 200);
        }
        if(skor2>0) {
        	g2d.drawString(r, 240, 190);
        	g2d.drawString(r2, 240, 210);
        }
        if(skor>0) {
        	g2d.drawString(l, 240, 190);
        	g2d.drawString(l2, 240, 210);
        }
        	
    }

	public void resetle() { //Karakterimizi �ld���nde �al��an metod. Oyun panelini d�zenler
		oyunAktif = false;
		pacman.setOyunAktif(oyunAktif);
		pacman.setdx(0);
		pacman.setdy(0);
		pacman.setX(380);
		pacman.setY(355);
		ozelgS = 0;
		skor2 = skor;
		hayaletSayisi = 4;
		hayalet.clear();
		disk.clear();
		buyukDisk.clear();
		diskEkle();
		hayaletEkle();
		level = 1;
		skor = 0;
		
	}
	
	public void levelAtla() { // Seviye atlad��m�zda �al��an metod
		oyunAktif = false;
		pacman.setOyunAktif(oyunAktif);
		pacman.setdx(0);
		pacman.setdy(0);
		pacman.setX(380);
		pacman.setY(355);
		ozelgS = 0;
		hayalet.clear();
		disk.clear();
		buyukDisk.clear();
		hayaletSayisi++;
		diskEkle();
		hayaletEkle();
		skor2 = 0;
		level++;
		
	}
	
	public void oyna(Graphics g) {
		hayaletCizdir(g);
	}
	
	private void skorCiz(Graphics g) { //Skor ve leveli ekrana yazd�ran metod
		String s, l;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Puan: " + skor;
        l = "Level : " + level;
        g.drawString(s, 20,15);
        g.drawString(l, 120,15);
	}
	
	
	private void cizdir(Graphics g) { //karakterimiz ekrana bast�r�l�yo ve animasyon ile ilgili kontroller yap�l�yo

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(pacman.getImage(anim), pacman.getX(), pacman.getY(), this); //karakter ekrana bas�l�yo
        
        
        ani++; //animasyon i�in kullan�ld�. paintComponent her �al��mas�nda ani de�eri art�yo
        if(ani % 7 == 0) // her 7 art��ta 2 pacman g�r�nt�s� aras�nda de�i�im oluyo (pacman.getImage)
        	anim = !anim; //her de�i�im 0.14 saniyede bir oluyo. ��nk� DELAY de�eri 20
        
        if(ozelgS > 0) { //Karakter b�y�k diskleri yemi� ise gerekli kontroller yap�l�r
        	ozelgS--;
        	for(Hayalet h : hayalet) {
        		h.setOzelGuc(true);
        	}
        }else {
        	ozelgS = 0;
        	pacman.setOzelGuc(false);
        	for(Hayalet h : hayalet) {
        		h.setOzelGuc(false);
        	}
        }
        	
        
    }
	
	private void labirentEkle() { // labirentlerin koordinatlar�n�n ayarland��� ve listeye at�ld��� fonksiyon
		
		int artis = 20;
		
		for(int i = 1; i < 21; i++) {
			
			kups.add(new Kupler(20, i*artis));
			kups.add(new Kupler(750, i*artis));
			
		}
		
		for(int i = 1; i < 38; i++) {
			kups.add(new Kupler(i*artis, 20));
			kups.add(new Kupler(i*artis, 400));
			
		}
		
		//sol �st b�l�m
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 5; j++) {
				kups.add(new Kupler((105+(i*20)), (95+(j*20))));
			}
		}
		
		for(int i = 0; i < 9; i++) {
			kups.add(new Kupler((185+(i*20)), 95));
		}
		
		for(int i = 3; i < 9; i++) {
			kups.add(new Kupler((185+(i*20)), 175));
		}
		
		
		//sol alt b�l�m
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 5; j++) {
				kups.add(new Kupler((105+(i*20)), (245+(j*20))));
			}
		}
		
		for(int i = 0; i < 9; i++) {
			kups.add(new Kupler((185+(i*20)), 325));
		}
		
		for(int i = 3; i < 9; i++) {
			kups.add(new Kupler((185+(i*20)), 245));
		}
		
		
		//sa� �st b�l�m
		for(int i = 0; i < 9; i++) {
			kups.add(new Kupler((425+(i*20)), 95));
		}
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 5; j++) {
				kups.add(new Kupler((605+(i*20)), (95+(j*20))));
			}
		}
		
		for(int i = 0; i < 6; i++) {
			kups.add(new Kupler((425+(i*20)), 175));
		}
		
		//sa� alt b�l�m
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 5; j++) {
				kups.add(new Kupler((605+(i*20)), (245+(j*20))));
			}
		}
		
		for(int i = 0; i < 9; i++) {
			kups.add(new Kupler((425+(i*20)), 325));
		}
		
		for(int i = 0; i < 6; i++) {
			kups.add(new Kupler((425+(i*20)), 245));
		}
		
		
	}
	
	public void labirentCizdir(Graphics g) { //koordinatlar�n� ald���m�z listedeki elemanlar�n bulundu�u konuma k�pleri yerle�tirerek labirenti �izdik
		
		Graphics2D g2d1 = (Graphics2D) g;
		
		for(Kupler kup : kups) {
			g2d1.drawImage(kup.getKup(), kup.x, kup.y, this);
		}
	}
	
	private void diskEkle() { //Ekrana �izilmesi i�in diskleri yerle�tiriyoruz ve listeye aktar�yoruz
		int artis = 30;
		
		for(int i = 1; i < 21; i++) {
			
			disk.add(new Diskler((75+(i*artis)) , 60));
			
			if(i == 10 || i == 11) { // karakterin ba�lang�� k�sm�nda 2 adet diski eklemiyoruz
				continue;
			}else {
				disk.add(new Diskler((75+(i*artis)) , 365));
				disk.add(new Diskler((75+(i*artis)) , 215));
			}
			
		}
		
		for(int i = 1; i < 10; i++) {
			disk.add(new Diskler(65, (65+ (i*artis))));
			
			disk.add(new Diskler(715, (65+ (i*artis))));
			
			if(i == 5) {
				continue;
			}else {
				disk.add(new Diskler(390, (65+ (i*artis))));
			}
		}
		
		for(int i = 1; i < 7; i++) {
			disk.add(new Diskler((180+(i*artis)) , 140));
			
			disk.add(new Diskler((390+(i*artis)) , 140));
			
			disk.add(new Diskler((180+(i*artis)) , 290));
			
			disk.add(new Diskler((390+(i*artis)) , 290));
		}
		
		disk.add(new Diskler(210 , 250));
		
		disk.add(new Diskler(210 , 180));
		
		disk.add(new Diskler(570 , 250));
		
		disk.add(new Diskler(570 , 180));
		
		//k��elerdeki 4 b�y�k disk
		buyukDisk.add(new Diskler(63,55));
		
		buyukDisk.add(new Diskler(63,357));
		
		buyukDisk.add(new Diskler(705,55));
		
		buyukDisk.add(new Diskler(705,357));
		
	}
	
	private void diskCizdir(Graphics g) { //Listedeki diskler ekrana bast�r�l�yo
		Graphics2D g2d1 = (Graphics2D) g;
		
		for(Diskler disk : disk) {
			g2d1.drawImage(disk.getKucukDisk(), disk.getX(), disk.getY(), this);
		}
		
		for(Diskler disk : buyukDisk) {
			g2d1.drawImage(disk.getBuyukDisk(), disk.getX(), disk.getY(), this);
		}
	}
	
	private void hayaletEkle() { //Hayalet listesine hayalet ekleme metodu
		//Hayaletlerin say�s� 4'ten az olmamas� i�in ayarland�.
		
		
		for(int i = 0; i < hayaletSayisi; i++) {
			hayalet.add(new Hayalet(380,200)); //Oyuna hayaletSayisi kadar hayalet eklenir.
			//Level atlan�rsa her levelde hayalet say�s� da artar. Karakter �l�rse hayalet say�s� 4 e d��er
		}
	
	}
	
	private void hayaletCizdir(Graphics g) { // Hayalet listesine at�lan hayalet nesneleri burada ekrana bast�r�ld�
		Graphics gd = (Graphics2D) g;
		for(Hayalet h : hayalet) {
			gd.drawImage(h.getImage(anim), h.getX(), h.getY(), this);
		}
	}
	
	public void carpisma() { //Karkaterimizin her �arp��mas�n� bu metod kontrol ediyo
		
		//karakterin kendisini ve d�rt kenar�n� �arp��ma alg�lamak i�in d�rtgen nesnelere atad�k
		Rectangle2D p = pacman.getBounds();
		Rectangle2D pTop = pacman.getBoundsTop();
		Rectangle2D pLeft = pacman.getBoundsLeft();
		Rectangle2D pBottom = pacman.getBoundsBottom();
		Rectangle2D pRight = pacman.getBoundsRight();
		
		
		//karakter duvarlara �arpt���nda oyuna devam edebilmesi i�in �arpt��� b�lgeden 5 pixel uzakla�t�r�yoruz
		for(Kupler kup : kups) {
				Rectangle2D r2 = kup.getBounds(); //k�plerin boyutunu rectangle nesneye atad�k
				if (pTop.intersects(r2)) {
					pacman.setdx(0);
					pacman.setdy(0);
					pacman.setY(pacman.getY() + 5);
				}
				
				if (pLeft.intersects(r2)) {
					pacman.setdx(0);
					pacman.setdy(0);
					pacman.setX(pacman.getX() + 5);
				}
				
				if (pBottom.intersects(r2)) {
					pacman.setdx(0);
					pacman.setdy(0);
					pacman.setY(pacman.getY() - 5);
				}
				
				if (pRight.intersects(r2)) {
					pacman.setdx(0);
					pacman.setdy(0);
					pacman.setX(pacman.getX() - 5);
				}
		}
		
		for(int i = 0; i < disk.size();i++) { // karakterin k���k disklere �arpmas�
			Rectangle2D r3 = disk.get(i).getBounds();
			if(p.intersects(r3)) {
				this.disk.remove(disk.get(i)); //karakterin �arpt��� disk listeden siliniyo
				skor++;
			}
		}
		
		for(int i = 0; i < buyukDisk.size();i++) { //karakterin b�y�k toplara �arpmas�
			Rectangle r4 = buyukDisk.get(i).getBoundsBuyuk();
			if(p.intersects(r4)) {
				this.buyukDisk.remove(buyukDisk.get(i)); //karakterin �arpt��� b�y�k disk listeden siliniyo
				ozelgS = 300; //bu de�erin 300 olmas� karakterimizin �zel g�c�n�n 6 saniye s�rece�i anlam�na geliyo. ��nk� DELAY de�erimiz 20ms
				pacman.setOzelGuc(true);
				skor++;
			}
		}
		
		for(int i = 0; i < hayalet.size();i++) { // Karakterin hayaletlere �arpmas�
			Rectangle hk = hayalet.get(i).getBounds();
			if(p.intersects(hk)) {
				if(pacman.getOzelGuc() == true) { //Karakter b�y�k diskler yemi� durumdaysa hayaletleri yok edebilir.
					this.hayalet.remove(hayalet.get(i));
					skor += 20;
				}else {
					reset = true; //Karakter b�y�k disklerden yememi�se oyun ba�ar�s�zl�kla sonlan�r ve resetlenir
				}
					
			}
		}
		
		
		if(this.disk.isEmpty() && this.buyukDisk.isEmpty()) { // T�m diskler bitti�inde level atlatan metod
			levelAtla();
		}
		
		
	}
	
	public void hayaletHareket() { // Hayaletlerin d�nebilece�i her k��e i�in kontrol yap�ld�.
		//Her noktaya geldi�inde rastgele say� �retildi, hayalet bu say�lara g�re d�n�� yapt�.
		for(Hayalet h : hayalet) {
			if((h.getX() == 380 && h.getY() == 200) || (h.getX() == 380 && h.getY() == 134) || (h.getX() == 380 && h.getY() == 284) || (h.getX() == 200 && h.getY() == 200) || (h.getX() == 560 && h.getY() == 200))  { // orta 4 y�n� olan d�n��ler
				hayaletR = r.nextInt(4) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else if(hayaletR == 2)	{ h.setDx(0); 	h.setDy(2); }
				else if(hayaletR == 3)	{ h.setDx(-2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 60 && h.getY() == 200) { //orta sol kenar
				hayaletR = r.nextInt(3) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else if(hayaletR == 2)	{ h.setDx(0); 	h.setDy(2); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 700 && h.getY() == 200) { //orta sa� kenar
				hayaletR = r.nextInt(3) + 1;
				if(hayaletR == 1)	{ h.setDx(-2); 	h.setDy(0); }
				else if(hayaletR == 2)	{ h.setDx(0); 	h.setDy(2); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 380 && h.getY() == 56) { //orta �st kenar
				hayaletR = r.nextInt(3) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else if(hayaletR == 2)	{ h.setDx(0); 	h.setDy(2); }
				else	{ h.setDx(-2); 	h.setDy(0); }
			}
			
			if(h.getX() == 380 && h.getY() == 356) { //orta alt kenar
				hayaletR = r.nextInt(3) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else if(hayaletR == 2)	{ h.setDx(0); 	h.setDy(-2); }
				else	{ h.setDx(-2); 	h.setDy(0); }
			}
			
			if(h.getX() == 60 && h.getY() == 356) { //sol alt k��e
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 700 && h.getY() == 356) { //sa� alt k��e
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(-2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 60 && h.getY() == 56) { // sol �st k��e
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1) { h.setDx(2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(2); }
			}
			
			if(h.getX() == 700 && h.getY() == 56) { // sa� �st k��e
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(-2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(2); }
			}
			
			if(h.getX() == 200 && h.getY() == 134) { // sol orta �st
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(2); }
			}
			
			if(h.getX() == 200 && h.getY() == 284) { // sol orta alt
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(0); 	h.setDy(-2); }
				else	{ h.setDx(2); 	h.setDy(0); }
			}
			
			if(h.getX() == 560 && h.getY() == 134) { // sa� orta �st
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(0); 	h.setDy(2); }
				else	{ h.setDx(-2); 	h.setDy(0); }
			}
			
			if(h.getX() == 560 && h.getY() == 284) { // sa� orta alt
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(0); 	h.setDy(-2); }
				else	{ h.setDx(-2); 	h.setDy(0); }
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		pacman.hareket();
		if(oyunAktif) {
			hayaletHareket();
			for(Hayalet h : hayalet) {
				h.hareket();
			}
		}
		
        repaint();
		
	}


	private class TAdapter extends KeyAdapter {
		

        @Override
        public void keyPressed(KeyEvent e) {
            pacman.keyPressed(e);
        }
    }
}
