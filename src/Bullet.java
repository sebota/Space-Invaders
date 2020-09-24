import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Represent the Bullet, inherit from Character class
 * @author Sebastian
 */
public class Bullet extends Character{
	/**
	 * Determine if bullet is launched
	 */
	boolean moveUp;
	/**
	 * Determine if bullet is still in game
	 */
	boolean isVisible;
	
	/**
	 * @param x position on the x axis
	 * @param y position on the y axis
	 * @param speed speed of bullet
	 */
	public Bullet(int x, int y, int speed) {
		super(x, y, speed);
		moveUp = false;
		isVisible = false;
		
		var bulletImg = "src/images/bullet.png";
        var ii = new ImageIcon(bulletImg);
        setImage(ii.getImage());
	}
	
	/**
	 * Move bullet up and check if bullet is outside board or hit alien
	 * @param g component on which alien will be drawn
	 * @param player reference to player object is needed to keep position of bullet 
	 *  corresponding to player position 
	 */
	public void moveBullet(Graphics g, Player player) {
		if(moveUp) {
			y -= speed;
			g.drawImage(getImage(), x, y, null);
		}
		else {
			x = player.x + 11;
			y = player.y;
		}

		if(y < 0) {
			moveUp = false;
			isVisible = false;
		}
		
		if(isVisible)
			moveUp = true;
	}
}
