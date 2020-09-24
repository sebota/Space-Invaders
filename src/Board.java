import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Draw game board and contain game logic
 * @author Sebastian
 */
public class Board extends JPanel implements Runnable{	
	/**
	 * Variable of Dimension type
	 */
	private final Dimension d;
	/**
	 * Width of the board
	 */
	public static short BOARD_WIDTH = 800;
	/**
	 * Height of the board
	 */
	public static short BOARD_HEIGHT = 600;
	/**
	 * Used to display score, energy and message when game ends
	 */
	String message = "";
	/**
	 * Contain the data to save in file
	 */
	String datatosave = "";
	/**
	 * Thread  for game
	 */
	private Thread animator; 
	/**
	 * Variable of Player type
	 */
	Player player;
	/**
	 * Variable of Menu type
	 */
	Menu menu;
	/**
	 * New instance of an array Alien type
	 */
	Alien[] alien = new Alien[40];
	/**
	 * New instance of an array Bomb type
	 */
	Bomb[] bomb = new Bomb[40];
	/**
	 * Variable of Player type
	 */
	Bullet bullet;
	/**
	 * Object of Random type, determine of new Aliens direction moving
	 */
	Random random = new Random();
	/**
	 * Object of Random type, determine of aliens shooting frequency
	 */
	Random randomAliens = new Random();
	/**
	 * Represent the direction in which aliens move
	 */
	boolean direction = false;
	/**
	 * This variable contain the state of data saving
	 */
	boolean saved = false;
	/**
	 * This variable contain the state of ESC button pressing
	 */
	boolean escPress = false; 
	/**
	 * This variable change state when user enter or leave score view
	 */
	boolean resultScreen = false; 
	/**
	 * This variable change state when PLAY button is pressed
	 */
	boolean startGame = false;
	/**
	 * Change state when game ends
	 */
	boolean alienWin = false;
	/**
	 * This variable contain the number of killed aliens
	 */
	byte newWave = 0;
	/**
	 * In this variable we track how many aliens left
	 */
	byte howManyleft = 0;
	/**
	 * This variable determine how long aliens will move in certain direction
	 */
	byte keepDirection = 0;
	/**
	 * This variable determine how often alien spawn bombs
	 */
	byte bombDelay = 0;
	/**
	 * In this variable drawn number is store
	 */
	int chance = 0;
	/**
	 * This variable contain the score of player
	 */
	short score = 0;
	
	/**
	 * Constructor of class, initialize all objects 
	 */
	public Board()
	{	
	    addKeyListener(new KeyboardInput());
	    addMouseListener(new MouseInput());
	    setFocusable(true);
	    d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
	    player = new Player(BOARD_WIDTH/2, BOARD_HEIGHT-55, 2);
	    
	    int alienx = BOARD_WIDTH/4;
	    int alieny = 10;
	    
	    for (int i = 0; i < alien.length; i++) {
	    	bomb[i] =  new Bomb(alienx + 8, alieny + 16, 2);
	        alien[i] =  new Alien(alienx, alieny, 1);
	    	alienx += 50;
	    	
	    	if(i == 9 || i == 19 || i == 29) {
	    		alienx = BOARD_WIDTH/4;
	    		alieny += 50;
	    	}
	    }
	    
	    bullet = new Bullet(0, 0, 10);
	    menu = new Menu();
	   
	    if (animator == null) {
	        animator = new Thread(this);
	        animator.start();
	    }
	                
	    setDoubleBuffered(true);
	}
	
	/**
	 * Paint board and is responsible for game logic
	 */
	public void paint(Graphics g)
	{
		if(!startGame || escPress) {
			menu.drawMenu(g);
			if(resultScreen)
	    		menu.displayResults(g);
		}
		
		else if((player.health != 0) && (!alienWin)){
			
			for (int i = alien.length - 1; i >= 0; i--) {
				if ((alien[i].y >= 540) && (alien[i].isVisible)) {
					alienWin = true;
					break;
				}
				}
			
			g.setColor(Color.black);
			g.fillRect(0, 0, d.width, d.height);
	
			checkforNewWave();
			makeAliensfaster();
			player.movePlayer(g);
			bullet.moveBullet(g, player);
			checkBulletHit();
			aliensBombs();
			for (int i = 0; i < alien.length; i++) { 
				bomb[i].moveBomb(g, alien[i]);
				alien[i].moveAlien(g);
			}
			checkAliensposition();
			checkBombHit();
			changeDirection();
					
		    Font small = new Font("Helvetica", Font.BOLD, 20);
		    g.setColor(Color.white);
		    g.setFont(small);
		    message = "Energia: " + player.health;
		    g.drawString(message, 10, d.height-60);
		   
		   	g.setColor(Color.white);
		    message = "Wynik: " + score;
		    g.drawString(message, 650, d.height-60);
	
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}
		else {
			Font small = new Font("Helvetica", Font.BOLD, 40);
		    g.setColor(Color.red);
		    g.setFont(small);
		    message = "Koniec gry";
		    g.drawString(message, d.width/2-90, d.height/2);
		    if(!saved) {
		    	datatosave += Start.playerName + ", ";
	    	    datatosave += score;
					try {
						menu.saveScore(datatosave);
					} catch (Exception e) {
						e.printStackTrace();
					}
	    	    saved = true;
	    	}
	    } 
	}
	   
	/**
	 * If half of aliens is killed, aliens which left move faster and bombs also move faster
	 */
	public void makeAliensfaster() {
		for (int i = 0; i < alien.length; i++) {
	    	if(howManyleft >= 20)
	    	{
	    		bomb[i].speed =  3;
	        	alien[i].speed =  2;
	    	}
		}
	}
	    
	/**
	 * Check if bullet hit alien
	 */
	public void checkBulletHit() {
		for (Alien value : alien) {
			if ((bullet.x <= value.x + 22) && (bullet.x >= value.x) && (bullet.y <= value.y + 22) && (bullet.y >= value.y) && (value.isVisible) ||
					((bullet.x + 4 >= value.x) && (bullet.x <= value.x) && (bullet.y <= value.y + 16) && (bullet.y >= value.y) && (value.isVisible)) ||
					((bullet.x + 4 >= value.x + 22) && (bullet.x <= value.x + 22) && (bullet.y <= value.y + 16) && (bullet.y >= value.y) && (value.isVisible))) {
				bullet.moveUp = false;
				bullet.isVisible = false;
				value.isVisible = false;
				score++;
				howManyleft++;
			}
		}
	}
	
	/**
	 * Check if bomb hit player
	 */
	public void checkBombHit() {
		for(int i = 0; i < alien.length; i++) {
			if((bomb[i].x <= player.x) && (bomb[i].x + 6 >= player.x) && (bomb[i].y + 6 >= player.y) && (bomb[i].y <= player.y) && (bomb[i].isVisible) ||
					((bomb[i].x >= player.x) && (bomb[i].x <= player.x + 26) && (bomb[i].y + 6 >= player.y) && (bomb[i].y <= player.y) && (bomb[i].isVisible)) ||
						((bomb[i].x <= player.x + 26) && (bomb[i].x + 6 >= player.x + 26) && (bomb[i].y + 10 >= player.y) && (bomb[i].y <= player.y) && (bomb[i].isVisible))) {
							bomb[i].moveDown = false;
							bomb[i].isVisible = false;
							player.health--;
						}
		}
	}
	
	/**
	 * Check if player kill all aliens and new aliens should be spawn
	 */
	public void checkforNewWave() {
		newWave = 0;

		for (Alien value : alien) {
			if (!value.isVisible)
				newWave++;
		}
		
		if(newWave == 40) {
			int alienx = BOARD_WIDTH/4;
	        int alieny = 10;
	        newWave = 0;
	        howManyleft = 0;
	        
	        for (int i = 0; i < alien.length; i++) {
	        	bomb[i].x = alienx + 8;
	        	bomb[i].y = alieny + 16;
	        	bomb[i].speed = 2;
	        	bomb[i].isVisible = false;
	        	bomb[i].moveDown = false;
	        	
	        	alien[i].x = alienx;
	        	alien[i].y = alieny;
	        	alien[i].speed = 1;
	        	alien[i].isVisible = true;
	
	        	alienx += 50;
	        	if(i == 9 || i == 19 || i == 29) {
	        		alienx = BOARD_WIDTH/4;
	        		alieny += 50;
	        	}
	        }
	    }
	}
	
	/**
	 * Check if aliens move outside board
	 */
	public void checkAliensposition() {
		for (Alien value : alien) {
			if (value.x > BOARD_WIDTH - 45 && value.isVisible) {
				for (Alien item : alien) {
					item.moveLeft = true;
					item.moveRight = false;
				}
			}

			if (value.x < 0 && value.isVisible) {
				for (Alien item : alien) {
					item.moveRight = true;
					item.moveLeft = false;
				}
			}
		}
	}
	
	/**
	 * Randomize the direction in which aliens move
	 */
	public void changeDirection() {
		
		if(keepDirection > 80)
		{
			direction = random.nextBoolean();
			keepDirection = 0;
			if(direction) {
				for (Alien value : alien) {
					value.moveRight = true;
					value.moveLeft = false;
				}
			}
			else {
				for (Alien value : alien) {
					value.moveRight = false;
					value.moveLeft = true;
				}
			}
			for (Alien value : alien) {
				value.y += 5;
			}
		}
		keepDirection++;
	}
	
	/**
	 * Spawn bombs and change the chance of spawning which depends on the amount of aliens left
	 */
	public void aliensBombs() {
		
		if(bombDelay > 80)
		{
			bombDelay = 0;
			
			for(int i = 0; i < alien.length; i++) {
				if(howManyleft >= 30)
					chance = randomAliens.nextInt(4);
				else if(howManyleft >= 20)
					chance = randomAliens.nextInt(15);
				else
					chance = randomAliens.nextInt(30);
				
				if(chance == 0 && !bomb[i].isVisible) {
					bomb[i].x = alien[i].x;
					bomb[i].y = alien[i].y;
					bomb[i].isVisible = true;
				}
			}
		}
		bombDelay++;
	}
	
	/**
	 * Reset all variables and positions of aliens, bombs, bullet and player
	 */
	public void resetGame() {
	
		datatosave = "";
		saved = false;
		alienWin = false;
		newWave = 0;
		howManyleft = 0;
		keepDirection = 0;
		bombDelay = 0;
		chance = 0;
		score = 0;
		player.health = 3;
		
		int alienx = BOARD_WIDTH/4;
	    int alieny = 10;
	    
	    for (int i = 0; i < alien.length; i++) {
	    	bomb[i] =  new Bomb(alienx + 8, alieny + 16, 2);
	        alien[i] =  new Alien(alienx, alieny, 1);
	    	alienx += 50;
	    	
	    	if(i == 9 || i == 19 || i == 29) {
	    		alienx = BOARD_WIDTH/4;
	    		alieny += 50;
	    	}
	    }
	}
	
	/**
	 * This class is responsible for input from keyboard
	 * @author Sebastian
	 */
	private class KeyboardInput extends KeyAdapter {
	
	/**
	 * Actions when specific key is released
	 */
	public void keyReleased(KeyEvent e) {
	     int key = e.getKeyCode();

		switch (key) {
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.moveLeft = false;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.moveRight = false;
		}
	}
	
	/**
	 * Actions when specific key is pressed
	 */
	public void keyPressed(KeyEvent e) {
	    int key = e.getKeyCode();

		switch (key) {
			case KeyEvent.VK_SPACE -> bullet.isVisible = true;
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.moveLeft = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.moveRight = true;
			case KeyEvent.VK_ESCAPE -> {
				escPress = true;
				resultScreen = false;
			}
		}
		}
	}
	
	/**
	 * This class is responsible for mouse input
	 * @author Sebastian
	 */
	private class MouseInput implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}
	
		/**
		 * Actions when user click on the specific button in menu
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			if(x >= BOARD_WIDTH/2-100 && x <= BOARD_WIDTH/2+100) {
				if(y >= 150 && y <= 200 ) {
					startGame = true;
					escPress = false;
					resetGame();
				}
			}
			
			if(x >= BOARD_WIDTH/2-100 && x <= BOARD_WIDTH/2+100) {
				if(y >= 250 && y <= 300 ) {
					resultScreen = true;
				}
			}
			
			if(x >= BOARD_WIDTH/2-100 && x <= BOARD_WIDTH/2+100) {
				if(y >= 350 && y <= 400 ) {
					System.exit(1);
				}
			}
		}
	
		@Override
		public void mouseReleased(MouseEvent e) {
		}
	
		@Override
		public void mouseEntered(MouseEvent e) {
		}
	
		@Override
		public void mouseExited(MouseEvent e) {
		}
	
	}
	
	/**
	 * Determine how fast board is repainted and repaint the board
	 */
	public void run() {
	
		int animationDelay = 10;
		long time = System.currentTimeMillis();
		
	    while (true) {
	      repaint();
	      try {
	    	  time += animationDelay;
	          Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
	      }catch (InterruptedException e) {
	    	  System.out.println(e);
	      }
	    }
		}
}
