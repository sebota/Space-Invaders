import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

/**
 * Class responsible for drawing menu
 * @author Sebastian
 */
public class Menu {
	/**
	 * Play button
	 */
	public Rectangle playButton = new Rectangle(Board.BOARD_WIDTH/2-100, 150, 200, 50);
	/**
	 * Score button
	 */
	public Rectangle scoreButton = new Rectangle(Board.BOARD_WIDTH/2-100, 250, 200, 50);
	/**
	 * Exit button
	 */
	public Rectangle exitButton = new Rectangle(Board.BOARD_WIDTH/2-100, 350, 200, 50);
	
	/**
	 * Set background and draw buttons
	 * @param g component on which menu will be drawn
	 */
	public void drawMenu(Graphics g){
		Image img = null;
		try {
			img = ImageIO.read(new File("src/images/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g.drawImage(img, 0, 0, null);
		g.setColor(Color.white);
		Font fnt0 = new Font("arial", Font.BOLD, 25);
		g.setFont(fnt0);
		g.drawString("SPACE INVADERS", Board.BOARD_WIDTH/2-110, 80);
		
		Font fnt1 = new Font("arial", Font.BOLD, 20);
		g.setFont(fnt1);
		g2d.draw(playButton);
		g.drawString("GRAJ", Board.BOARD_WIDTH/2-85, 180);
		g2d.draw(scoreButton);
		g.drawString("WYNIKI", Board.BOARD_WIDTH/2-85, 280);
		g2d.draw(exitButton);
		g.drawString("WYJDè", Board.BOARD_WIDTH/2-85, 380);
	}
	
	/**
	 * Display scores from file
	 * @param g display results in score view
	 */
	public void displayResults(Graphics g) {
		int i = 0;
		BufferedReader reader;
		g.setColor(Color.black);
		g.fillRect(0, 0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT);
		g.setColor(Color.white);
		Font fnt0 = new Font("arial", Font.BOLD, 25);
		g.setFont(fnt0);
		
		try {
			reader = new BufferedReader(new FileReader("wynik.txt"));
			String line = reader.readLine();
			while (line != null) {
				g.drawString(line, Board.BOARD_WIDTH/2 - 350, 40 + i * 30);
				line = reader.readLine();
				i++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Save data to file
	 * @param datatosave data to save in file
	 */
	public void saveScore(String datatosave) 
	{
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("wynik.txt", true);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	    PrintWriter save = new PrintWriter(fileWriter);
	    save.println(datatosave);
	    save.close();
	}
}