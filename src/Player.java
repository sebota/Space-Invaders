import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Represent the player, inherit from Character class
 * @author Sebastian
 */
public class Player extends Character{
	/**
	 * Determine the direction in which player move
	 */
	boolean moveRight;
	/**
	 * Determine the direction in which player move
	 */
	boolean moveLeft;
	/**
	 * Store the amount of player health
	 */
	byte health = 3;

	/**
	 * @param x position on the x axis
	 * @param y position on the y axis
	 * @param speed speed of player
	 */
	public Player(int x, int y, int speed) {
		super(x, y, speed);
		moveRight = false;
		moveLeft = false;
		
		var playerImg = "src/images/ship.png";
        var ii = new ImageIcon(playerImg);
        setImage(ii.getImage());
	}
	/** 
	 * Move player left or right and check if player move outside board
	 * @param g component on which player will be drawn
	 */
	public void movePlayer(Graphics g) {
		g.drawImage(getImage(), x, y, null);
		
		if(moveRight)
			x += speed;
		
		if(moveLeft)
			x -= speed;
		
		if(x <= 0)
			x = 0;
			
		if(x >= Board.BOARD_WIDTH-42)
			x = Board.BOARD_WIDTH-42;
	}
}
