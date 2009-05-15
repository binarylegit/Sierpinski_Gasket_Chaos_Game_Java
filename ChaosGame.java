import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * the rules of the game are as such:
 * 	1 - draw an equilateral triangle
 * 	2 - assign each vertice of the triangle a value (e.g. 1,2,3)
 * 	3 - mark a random point in the triangle for a start point
 * 	4 - get a random number/value that has the probability 1/3 and is 
 * 		assigned to one of the three vertices
 * 	5 - once the random value is attained draw a point halfway between
 * 		the previous point and the vertice that has the value of the 
 * 		random value.
 * 	6 - repeat step 5 ad infinitum or up to some defined number of iterations.
 * 	7 - note the "order" of the resulting points within the triangle.
 *
 */ 
// TODO: get rid of all the magic numbers
public class ChaosGame extends JComponent implements Runnable
{

	public static void main(String[] args)
	{

		// get the number of turns from the user
		Integer turns = new Integer(JOptionPane.showInputDialog("How many turns should be played (integer)"));
		JOptionPane.showMessageDialog(null, turns + " turns will be played");

		// create the game interface
		JFrame frame = new JFrame();

		// Add a listener for the close event
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
		       	 	// Exit the application
		      		System.exit(0);
			}
		});

		// add the game drawing component
		frame.getContentPane().add(new ChaosGame(turns));

		// set size and visibility of frame
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);

		
		
	}

	// global class variables
	private static int frameWidth = 560;
	private static int frameHeight = 560;
	


	// global object variables
	private Integer totalTurns;
	private Integer currentTurn;
	ArrayList<Point> points = new ArrayList();




	public ChaosGame(int turns)
	{ 
		totalTurns = turns;	
	}

	public void run()
	{

		// get random start point
		

	}


	public void paint(Graphics g)
	{
		// get the graphics component
		Graphics2D graphic = (Graphics2D)g;
		
		// draw some strings
		g.drawString("Playing the Chaos Game to Create a Sierpinski Gasket", 0, 10);
		
		// draw the points of the triangle
		g.fillOval(20, 510, 5, 5);
		g.fillOval(520, 510, 5, 5);
		g.fillOval(270, 77, 5, 5);


	}



	class Point{
		private int x;
		private int y;

		public Point(int newX, int newY) { x = newX; y = newY; }

		public int getY() { return y; };

		public int getX() { return x; };
	}

}
