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



@SuppressWarnings("serial") //eclipse in serial uyarýsý vermemesi için
public class Panel extends JPanel implements ActionListener {
	
	private Timer timer;
	private Karakter pacman; //kontrol ettiðimiz karakter
	private final int DELAY = 20;
	private int ani = 1, hayaletR, ozelgS = 0, skor = 0, skor2, level = 1, hayaletSayisi = 4;
	private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
	private boolean anim = false, oyunAktif = false, reset = false;
	private List<Kupler> kups = new ArrayList<Kupler>(); //küpleri tuttuðumuz liste
	private List<Diskler> disk = new ArrayList<Diskler>(); //küçük diskleri tuttuðumuz liste
	private List<Diskler> buyukDisk = new ArrayList<Diskler>(); //büyük diskleri tuttuðumuz liste
	private List<Hayalet> hayalet = new ArrayList<Hayalet>(); //hayaletleri tuttuðumuz liste
	Random r = new Random(); //Hayalerlerin köþelerden hangi yöne döneceðini belirlemek için oluþturulan random
	
	
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
        
        pacman = new Karakter(); //Oyunda yöneteceðimiz karakteri oluþturduk
        
        
        timer = new Timer(DELAY, this);
        timer.start();
		
	}
	
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        oyunAktif = pacman.getOyunAktif(); //Oyunun oynanýr durumda olduðunu karakter sýnýfýndan aldýk
        cizdir(g);
        labirentCizdir(g);
        diskCizdir(g);
        
        
        if(reset) {
        	resetle(); //Karakter hayaletler tarafýndan öldürüldüðünde çalýþacak
        }
        
        if(oyunAktif) {
        	oyna(g); //Oyun oynanabilir durumda ise gerekli metod çaðýrýlacak
        }else
        	girisEkrani(g);
        
        reset = pacman.getReset(); 
        
        skorCiz(g);
        carpisma();

        Toolkit.getDefaultToolkit().sync();
    }
	
	private void girisEkrani(Graphics g2d) { //giriþ ekraný, level atlama ve oyuna yeniden baþlama ekranlarý

        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(220, 160, 340, 70);
        g2d.setColor(Color.white);
        g2d.drawRect(220, 160, 340, 70);
        
        	
        String r = "Kaybettiniz, puanýnýz: " + skor2;
        String r2 = "Tekrar oynamak için yön tuþlarýný kullanýn";
        String l = "Tebrikler, seviye atladýnýz";
        String l2 = "Devam etmek için yön tuþlarýný kullanýn";
        String s = "Baþlamak için yön tuþlarýný kullanýn";
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

	public void resetle() { //Karakterimizi öldüðünde çalýþan metod. Oyun panelini düzenler
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
	
	public void levelAtla() { // Seviye atladðýmýzda çalýþan metod
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
	
	private void skorCiz(Graphics g) { //Skor ve leveli ekrana yazdýran metod
		String s, l;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Puan: " + skor;
        l = "Level : " + level;
        g.drawString(s, 20,15);
        g.drawString(l, 120,15);
	}
	
	
	private void cizdir(Graphics g) { //karakterimiz ekrana bastýrýlýyo ve animasyon ile ilgili kontroller yapýlýyo

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(pacman.getImage(anim), pacman.getX(), pacman.getY(), this); //karakter ekrana basýlýyo
        
        
        ani++; //animasyon için kullanýldý. paintComponent her çalýþmasýnda ani deðeri artýyo
        if(ani % 7 == 0) // her 7 artýþta 2 pacman görüntüsü arasýnda deðiþim oluyo (pacman.getImage)
        	anim = !anim; //her deðiþim 0.14 saniyede bir oluyo. Çünkü DELAY deðeri 20
        
        if(ozelgS > 0) { //Karakter büyük diskleri yemiþ ise gerekli kontroller yapýlýr
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
	
	private void labirentEkle() { // labirentlerin koordinatlarýnýn ayarlandýðý ve listeye atýldýðý fonksiyon
		
		int artis = 20;
		
		for(int i = 1; i < 21; i++) {
			
			kups.add(new Kupler(20, i*artis));
			kups.add(new Kupler(750, i*artis));
			
		}
		
		for(int i = 1; i < 38; i++) {
			kups.add(new Kupler(i*artis, 20));
			kups.add(new Kupler(i*artis, 400));
			
		}
		
		//sol üst bölüm
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
		
		
		//sol alt bölüm
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
		
		
		//sað üst bölüm
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
		
		//sað alt bölüm
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
	
	public void labirentCizdir(Graphics g) { //koordinatlarýný aldýðýmýz listedeki elemanlarýn bulunduðu konuma küpleri yerleþtirerek labirenti çizdik
		
		Graphics2D g2d1 = (Graphics2D) g;
		
		for(Kupler kup : kups) {
			g2d1.drawImage(kup.getKup(), kup.x, kup.y, this);
		}
	}
	
	private void diskEkle() { //Ekrana çizilmesi için diskleri yerleþtiriyoruz ve listeye aktarýyoruz
		int artis = 30;
		
		for(int i = 1; i < 21; i++) {
			
			disk.add(new Diskler((75+(i*artis)) , 60));
			
			if(i == 10 || i == 11) { // karakterin baþlangýç kýsmýnda 2 adet diski eklemiyoruz
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
		
		//köþelerdeki 4 büyük disk
		buyukDisk.add(new Diskler(63,55));
		
		buyukDisk.add(new Diskler(63,357));
		
		buyukDisk.add(new Diskler(705,55));
		
		buyukDisk.add(new Diskler(705,357));
		
	}
	
	private void diskCizdir(Graphics g) { //Listedeki diskler ekrana bastýrýlýyo
		Graphics2D g2d1 = (Graphics2D) g;
		
		for(Diskler disk : disk) {
			g2d1.drawImage(disk.getKucukDisk(), disk.getX(), disk.getY(), this);
		}
		
		for(Diskler disk : buyukDisk) {
			g2d1.drawImage(disk.getBuyukDisk(), disk.getX(), disk.getY(), this);
		}
	}
	
	private void hayaletEkle() { //Hayalet listesine hayalet ekleme metodu
		//Hayaletlerin sayýsý 4'ten az olmamasý için ayarlandý.
		
		
		for(int i = 0; i < hayaletSayisi; i++) {
			hayalet.add(new Hayalet(380,200)); //Oyuna hayaletSayisi kadar hayalet eklenir.
			//Level atlanýrsa her levelde hayalet sayýsý da artar. Karakter ölürse hayalet sayýsý 4 e düþer
		}
	
	}
	
	private void hayaletCizdir(Graphics g) { // Hayalet listesine atýlan hayalet nesneleri burada ekrana bastýrýldý
		Graphics gd = (Graphics2D) g;
		for(Hayalet h : hayalet) {
			gd.drawImage(h.getImage(anim), h.getX(), h.getY(), this);
		}
	}
	
	public void carpisma() { //Karkaterimizin her çarpýþmasýný bu metod kontrol ediyo
		
		//karakterin kendisini ve dört kenarýný çarpýþma algýlamak için dörtgen nesnelere atadýk
		Rectangle2D p = pacman.getBounds();
		Rectangle2D pTop = pacman.getBoundsTop();
		Rectangle2D pLeft = pacman.getBoundsLeft();
		Rectangle2D pBottom = pacman.getBoundsBottom();
		Rectangle2D pRight = pacman.getBoundsRight();
		
		
		//karakter duvarlara çarptýðýnda oyuna devam edebilmesi için çarptýðý bölgeden 5 pixel uzaklaþtýrýyoruz
		for(Kupler kup : kups) {
				Rectangle2D r2 = kup.getBounds(); //küplerin boyutunu rectangle nesneye atadýk
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
		
		for(int i = 0; i < disk.size();i++) { // karakterin küçük disklere çarpmasý
			Rectangle2D r3 = disk.get(i).getBounds();
			if(p.intersects(r3)) {
				this.disk.remove(disk.get(i)); //karakterin çarptýðý disk listeden siliniyo
				skor++;
			}
		}
		
		for(int i = 0; i < buyukDisk.size();i++) { //karakterin büyük toplara çarpmasý
			Rectangle r4 = buyukDisk.get(i).getBoundsBuyuk();
			if(p.intersects(r4)) {
				this.buyukDisk.remove(buyukDisk.get(i)); //karakterin çarptýðý büyük disk listeden siliniyo
				ozelgS = 300; //bu deðerin 300 olmasý karakterimizin özel gücünün 6 saniye süreceði anlamýna geliyo. Çünkü DELAY deðerimiz 20ms
				pacman.setOzelGuc(true);
				skor++;
			}
		}
		
		for(int i = 0; i < hayalet.size();i++) { // Karakterin hayaletlere çarpmasý
			Rectangle hk = hayalet.get(i).getBounds();
			if(p.intersects(hk)) {
				if(pacman.getOzelGuc() == true) { //Karakter büyük diskler yemiþ durumdaysa hayaletleri yok edebilir.
					this.hayalet.remove(hayalet.get(i));
					skor += 20;
				}else {
					reset = true; //Karakter büyük disklerden yememiþse oyun baþarýsýzlýkla sonlanýr ve resetlenir
				}
					
			}
		}
		
		
		if(this.disk.isEmpty() && this.buyukDisk.isEmpty()) { // Tüm diskler bittiðinde level atlatan metod
			levelAtla();
		}
		
		
	}
	
	public void hayaletHareket() { // Hayaletlerin dönebileceði her köþe için kontrol yapýldý.
		//Her noktaya geldiðinde rastgele sayý üretildi, hayalet bu sayýlara göre dönüþ yaptý.
		for(Hayalet h : hayalet) {
			if((h.getX() == 380 && h.getY() == 200) || (h.getX() == 380 && h.getY() == 134) || (h.getX() == 380 && h.getY() == 284) || (h.getX() == 200 && h.getY() == 200) || (h.getX() == 560 && h.getY() == 200))  { // orta 4 yönü olan dönüþler
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
			
			if(h.getX() == 700 && h.getY() == 200) { //orta sað kenar
				hayaletR = r.nextInt(3) + 1;
				if(hayaletR == 1)	{ h.setDx(-2); 	h.setDy(0); }
				else if(hayaletR == 2)	{ h.setDx(0); 	h.setDy(2); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 380 && h.getY() == 56) { //orta üst kenar
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
			
			if(h.getX() == 60 && h.getY() == 356) { //sol alt köþe
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 700 && h.getY() == 356) { //sað alt köþe
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(-2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(-2); }
			}
			
			if(h.getX() == 60 && h.getY() == 56) { // sol üst köþe
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1) { h.setDx(2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(2); }
			}
			
			if(h.getX() == 700 && h.getY() == 56) { // sað üst köþe
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(-2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(2); }
			}
			
			if(h.getX() == 200 && h.getY() == 134) { // sol orta üst
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(2); 	h.setDy(0); }
				else	{ h.setDx(0); 	h.setDy(2); }
			}
			
			if(h.getX() == 200 && h.getY() == 284) { // sol orta alt
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(0); 	h.setDy(-2); }
				else	{ h.setDx(2); 	h.setDy(0); }
			}
			
			if(h.getX() == 560 && h.getY() == 134) { // sað orta üst
				hayaletR = r.nextInt(2) + 1;
				if(hayaletR == 1)	{ h.setDx(0); 	h.setDy(2); }
				else	{ h.setDx(-2); 	h.setDy(0); }
			}
			
			if(h.getX() == 560 && h.getY() == 284) { // sað orta alt
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
