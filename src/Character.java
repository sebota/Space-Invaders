import java.awt.Image;

/**
 * Represent the general character in game
 * @author Sebastian
 */
public class Character {
	/**
	 * Represent position on the x axis.
	 */
	int x;
	/**
	 * Represent position on the y axis.
	 */
	int y;
	/**
	 * Represent speed of character.
	 */
	int speed;
	/**
	 * Represent image of character.
	 */
	private Image image;
	
	/**
	 * @param x position on the x axis
	 * @param y position on the y axis
	 * @param speed speed of character
	 */
	public Character(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
    /** Set the image of character
     * @param image represent graphical image
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /** Return the image of character
     * @return graphical image
     */
    public Image getImage() {
        return image;
    }
}
