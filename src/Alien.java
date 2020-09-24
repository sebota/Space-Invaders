import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Represent the Alien, inherit from Character class
 * @author Sebastian
 */
public class Alien extends Character{
	/**
	 * Determine the direction in which alien move
	 */
	boolean moveRight;
	/**
	 * Determine the direction in which alien move
	 */
	boolean moveLeft;
	/**
	 * Determine if alien is still in game
	 */
	boolean isVisible;
	/**
	 * Determine if alien draw a chance to shoot bomb
	 */
	boolean shoot;
	
	/**
	 * @param x position on the x axis
	 * @param y position on the y axis
	 * @param speed speed of alien
	 */
	public Alien(int x, int y, int speed) {
		super(x, y, speed);
		moveRight = true;
		moveLeft = false;
		isVisible = true;
		shoot = false;
		
		var alienImg = "src/images/alien.png";
        var ii = new ImageIcon(alienImg);
        setImage(ii.getImage());
	}
	
	/**
	 * Move alien and check if alien is still alive
	 * @param g component on which alien will be drawn
	 */
	public void moveAlien(Graphics g) {
		if(isVisible)
			g.drawImage(getImage(), x, y, null);
		
		if(moveLeft)
			x -= speed;
		
		if(moveRight)
			x += speed;
	}
}

