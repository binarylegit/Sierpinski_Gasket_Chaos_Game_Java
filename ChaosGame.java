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
		// TODO: catch the exception of a non-integer value here
		Integer turns = new Integer(JOptionPane.showInputDialog("How many turns should be played (integer)"));

		boolean setShowTurns = false;

		if(JOptionPane.showConfirmDialog(null, turns + " turns will be played.\n\n Shall I show each turn as it is played?", "Chaos Game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{
			setShowTurns = true;
		}

		// create the game interface
		JFrame frame = new JFrame();

		// Add a listener for the close event
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
		       	 	// Exit the application
		      		System.exit(0);
			}
		});

		ChaosGame game = new ChaosGame(turns, setShowTurns);
		
		// add the game drawing component
		frame.getContentPane().add(game);

		// set size and visibility of frame
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);

		
		(new Thread(game)).start();
		
		
	}


	// global class variables
	private static int frameWidth = 560;
	private static int frameHeight = 560;
	private static int triXOffset = 20; // amount to add to x
	private static int triYOffset = 10; // amount to add to y
	private static int triWidth = 500;
	private static int triHeight = 433;
	


	// global object variables
	private Integer totalTurns;
	private Integer currentTurn = 0;
	private ArrayList<Point> points = new ArrayList();
	// TODO: remove magic numbers
	private Point aVertex = new Point((250 + triXOffset), (67 + triYOffset));
	private Point bVertex = new Point((0 + triXOffset), (500 + triYOffset));
	private Point cVertex = new Point((500 + triXOffset), (500 + triYOffset));
	private Random rand = new Random();
	private boolean showTurns = true;


	public ChaosGame(int turns, boolean setShowTurns)
	{ 
		// Get an initial point from which to start the game
		points.add(getRandomPoint(triWidth, triHeight));

		showTurns = setShowTurns;

		totalTurns = turns;	
	}


	public Point getRandomPoint(int triWidth, int triHeight)
	{
		// get a random point within the triangle
		// TODO: make this method actually do something
		return new Point(250,250);
	}

	public void paint(Graphics g)
	{
		// get the graphics component
		Graphics2D graphic = (Graphics2D)g;
		
		// draw some strings
		g.drawString("Playing the Chaos Game to Create a Sierpinski Gasket", 0, 10);
		g.drawString("Current Turn: " + currentTurn, 0, 20);
		
		// draw the points of the triangle
		g.fillOval(aVertex.getX(), aVertex.getY(), 5, 5); // point a = 1
		g.fillOval(bVertex.getX(), bVertex.getY(),  5, 5); // point b = 2
		g.fillOval(cVertex.getX(), cVertex.getY(), 5, 5); // point c = 3

		// label the points of the triangle
		g.drawString("a", 250 + triXOffset, 63 +triYOffset);
		g.drawString("b", -9 + triXOffset, 506 +triYOffset);
		g.drawString("c", 509 + triXOffset, 506 +triYOffset);


		// draw the points
		for( Point currPoint : points )
		{
			g.fillOval(currPoint.getX(), currPoint.getY(), 2, 2);
		}


	}


	/**
	 * This runs a thread to generate points for the Chaos Game according 
	 * to it's rules.
	 */
	public void run()
	{
		try{

			// get the last point
			Point last = points.get( points.size() - 1 );
			while(currentTurn < totalTurns )
			{

				// Take a turn (draw a random corner, get the associated point)
				Point newPoint = this.takeTurn(last);
				points.add(newPoint);
				
				last = newPoint;

				if(showTurns)
				{
					// repaint
					repaint();

					// wait
					Thread.sleep(500);
				}
			}
		
		} catch(InterruptedException iexc)
		{
			System.out.println("thread Interrupted code: 137");
		}
		repaint();
		return;
	}


	private Point takeTurn(Point prevPoint)
	{

		// get random int of 1,2,3
		int roll = rand.nextInt(3) + 1;
		//System.out.println("roll: " + roll); // DEBUG
		Point vertex = this.getPoint(roll);

		// get point between prevPoint and vertex
		Point nextPoint = new Point(((vertex.getX() + prevPoint.getX())/2), 
					((vertex.getY() + prevPoint.getY())/2));

		currentTurn++;

		return nextPoint;
	}


	// val is an int between 1 and 3
	private Point getPoint(int val)
	{
		if(val == 1)
		{
			return aVertex;
		} else if (val == 2)
		{
			return bVertex;
		} else if (val == 3)
		{
			return cVertex;
		} else {
			return null;
		}
	}
	


	class Point{
		private int x;
		private int y;

		public Point(int newX, int newY) { x = newX; y = newY; }

		public int getY() { return y; };

		public int getX() { return x; };
	}

}
