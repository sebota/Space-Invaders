import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Represent the Bomb, inherit from Character class
 * @author Sebastian 
 */
public class Bomb extends Character{

	/**
	 * Determine if bomb is launched
	 */
	boolean moveDown;
	/**
	 * Determine if bomb is still in game
	 */
	boolean isVisible;
		
	/**
	 * @param x position on the x axis
	 * @param y position on the y axis
	 * @param speed speed of bomb
	 */
	public Bomb(int x, int y, int speed) {
		super(x, y, speed);
		moveDown = false;
		isVisible = false;
		
		var bombImg = "src/images/bomb.png";
	    var ii = new ImageIcon(bombImg);
	    setImage(ii.getImage());
	}
	
	/**
	 * Move bomb down and check if bomb is outside board and also check if alien is alive and bomb can be launched
	 * @param g component on which alien will be drawn
	 * @param alien reference to alien object is needed to check if bomb can be launched
	 */
	public void moveBomb(Graphics g, Alien alien) {
		if(y >= 600) {
			moveDown = false;
			isVisible = false;
			x = alien.x;
			y = alien.y;
		}
		
		if(moveDown)
		{
			y += speed;
			g.drawImage(getImage(), x, y, null);
		}
		
		if(((isVisible) && (alien.isVisible))) {
			moveDown = true;
		}
	}
}

