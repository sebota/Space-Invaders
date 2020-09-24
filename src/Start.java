import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Starting class
 * @author Sebastian
 */
public class Start extends JFrame {
	/**
	 * The name of the player.
	 */
	public static String playerName = "";
	
	/**
	 * Initialize game window and show dialog input to get player name,
	 * then initialize Board.
	 */
	public Start()
    {
		JFrame frame = new JFrame("Space Invaders");

		playerName = JOptionPane.showInputDialog(
		        frame, 
		        "Podaj nazwê gracza", 
		        "Nazwa gracza", 
		        JOptionPane.QUESTION_MESSAGE
		    );
		
		if (playerName == null) 
			playerName = "";
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new Board());
        setTitle("Space Invaders");
        setSize(800,600);
        setLocationRelativeTo(null);
        frame.pack();
        setVisible(true);
        setResizable(false);
    }
	
    public static void main(String[] args) {
        new Start();
    }
}