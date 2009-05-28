import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.*;
import java.util.*;

/**
 * This Java class is a self-contained java program for running a "ChaosGame"
 * that creates a Sierpinski Gasket.  The program follows the rules of the 
 * game (below) to play and create the gasket.  The game was designed from
 * the rules described in Ian Stewarts book "The Magical Maze".
 *
 * The rules of the game are as such:
 * 	1 - draw an equilateral triangle
 * 	2 - assign each vertice of the triangle a value (e.g. 1,2,3)
 * 	3 - mark a random point in the triangle for a start point
 * 	4 - get a random number/value that has the probability 1/3 and is 
 * 		assigned to one of the three vertices
 * 	5 - once the random value is attained draw a point halfway between
 * 		the previous point and the vertice that has the value of the 
 * 		random value.
 * 	6 - repeat step 5 ad infinitum or up to some defined number of iterations.
 * 	7 - note the geometry of the resulting points within the triangle.
 *
 */ 
public class ChaosGame extends JComponent implements Runnable
{

	/**
	 * This is the main method that must be invocated to play the full game
	 * within the class.  
	 *
	 * @param args any command line arguments, all are ignored.
	 */
	public static void main(String[] args)
	{

		// get the number of turns from the user
		Integer turns = 1; 
		boolean tryAgain = true;
		while(tryAgain)
		{
			try {
				tryAgain = false;
				turns = new Integer(JOptionPane.showInputDialog("How many turns should be played (integer)"));

			} catch (NumberFormatException nfo)
			{
				tryAgain = true;
			}
		}

		boolean setShowTurns = false;
		boolean setShowLines = false;

		if(JOptionPane.showConfirmDialog(null, turns + " turns will be played.\n\n Shall I show each turn as it is played? (.5 seconds per turn)", "Chaos Game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{
			setShowTurns = true;

			if(JOptionPane.showConfirmDialog(null, "Each turn will be shown.\n\nShall I draw a line illustrating each turn? ", "Chaos Game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				setShowLines = true;
			}
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

		ChaosGame game = new ChaosGame(turns, setShowTurns, setShowLines);
		
		// add the game drawing component
		frame.getContentPane().add(game);

		// set size and visibility of frame
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);

		// start the thread that plays the game
		(new Thread(game)).start();
		
		
	}


	// global class variables
	private static int frameWidth = 560; // Width of the game window
	private static int frameHeight = 560; // Height of the game window
	private static int triXOffset = 20; // amount to move the triangle horizontally within the window
	private static int triYOffset = 77; // amount to move the triangle vertically within the window
	private static int triWidth = 500; // Width and length of the sides of the triangle
	private static int triHeight = 433; // height of the triangle from the midpoint of the bottom side to the highest point of the triangle
	


	// global object variables
	private Integer totalTurns; // total turns to take (set by user)
	private Integer currentTurn = 0; // the current turn we are on
	private ArrayList<Point> points = new ArrayList<Point>(); // container 
		// for all of the points to be drawn

	// The Triangle is an equilateral triangle with sides of 500px
	// Doing the math ( 433 = sqrt( 500^2 - 250^2 ) ) we get a height of ~433 px 
	private Point aVertex = new Point(((triWidth / 2)), 0); // location of the vertex a
	private Point bVertex = new Point(0, triHeight); // location of the vertex b
	private Point cVertex = new Point(triWidth, triHeight); // location of the vertex c
	private Random rand = new Random(); // global random object for generating random numbers
	private boolean showTurns = true; // shall each turn be shown to the user (set by the user)
	private boolean showLines = true; // shall a line be temporarily drawn illustrating each turn (set by the user)


	/**
	 * Constructor for the ChaosGame, constructor sets various global object variables.
	 *
	 * @param turns sets the number of turns to be played in the game
	 * @param setShowTurns if true a turn will be played and drawn every 500 milliseconds
	 * 			, if false all turns will be played but not displayed to the
	 * 			user until all turns have been played.
	 * @param setShowLines if true a line will be drawn between the last two points drawn,
	 * 			if false no line will be drawn.
	 */
	private ChaosGame(int turns, boolean setShowTurns, boolean setShowLines)
	{ 
		// Get an initial point from which to start the game
		points.add(getRandomPoint(triWidth, triHeight));

		showTurns = setShowTurns;
		showLines = setShowLines;

		totalTurns = turns;	
	}


	/**
	 * getRandomPoint generates a random point within an equilateral 
	 * triangle that is specified by the Height and Width, it then 
	 * will add any offsets to the points that are necessary.
	 *
	 *
	 * Implementation: 
	 * 	For clarity (since this is open source) I will provide some
	 * 	implementation explanation.
	 * 	
	 * 	The first step is to grab a random y value in the range of the
	 * 	height of the triangle.
	 *
	 * 	Then a relative x offset is calculated and a legal range of x
	 * 	values is calculated.  
	 *
	 * 	Lastly a random x value is calculated within that legal x range.
	 *
	 * 	As an illustration:
	 *
	 * 			*
	 * 		      * |
	 * 		    *   |
	 * 		  *     |
	 * 	        *       |
	 * 	      *         |
	 * 	    *           |
	 * 	  *-------------| &lt;-- random y value
	 *|-------|---------------------------|
	 *    ^relative x offset    ^legal x range
	 *  *			|
	 ***********************|
	 *
	 * @param triWidth the width of the triangle in which the random point
	 * 		is to be generated.
	 * @param triHeight the total height of the triangle in which random
	 * 		point is to be generated.
	 * @return returns a random point that is contained within the equilateral
	 * 	triangle described by the input parameters.
	 */
	private Point getRandomPoint(int triWidth, int triHeight)
	{
		// get a random point within the triangle

		// get a random Y value ( - 2 makes sure it is within the triangle)
		Integer yVal = new Integer(rand.nextInt(triHeight - 2) + 1);
		//System.out.println("yVal: " + yVal); //DEBUG

		// figure out what range of X-values are legal for yVal
		// tangent(30) = .5773502692
		Double xRange = ( .5773502692 * yVal) - 1;

		Integer relXOffset = (triWidth / 2) - xRange.intValue();

		xRange *= 2;

		//System.out.println("xRange: " + xRange); // DEBUG

		Integer xVal = new Integer( rand.nextInt(xRange.intValue()) + 1);
		//System.out.println("xVal: " + xVal); // DEBUG

		return new Point((relXOffset + xVal), (yVal));
	}

	
	/**
	 * This is the implementation of paint described by parent class
	 * JComponent.  This takes care of all of drawing of the triangle
	 * and its various components.
	 *
	 * @param g the graphics object that takes care of all of the drawing.
	 */
	public void paint(Graphics g)
	{
		// get the graphics component
		Graphics2D graphic = (Graphics2D)g;
		
		// draw some strings
		g.drawString("Playing the Chaos Game to Create a Sierpinski Gasket", 0, 10);
		g.drawString("Current Turn: " + currentTurn, 0, 20);
		
		// draw the points of the triangle
		g.fillOval((aVertex.getX() + triXOffset), (aVertex.getY() + triYOffset), 5, 5); // point a = 1
		g.fillOval((bVertex.getX() + triXOffset), (bVertex.getY() + triYOffset),  5, 5); // point b = 2
		g.fillOval((cVertex.getX() + triXOffset), (cVertex.getY() + triYOffset), 5, 5); // point c = 3

		// label the points of the triangle
		// extra "magic" numbers are slight offsets so that the 
		// vertex labels aren't overlapping the points they label
		g.drawString("a", (aVertex.getX() + triXOffset), (aVertex.getY() - 4 + triYOffset));
		g.drawString("b", (bVertex.getX() - 9 + triXOffset), (bVertex.getY() + 9 + triYOffset));
		g.drawString("c", (cVertex.getX() + 9 + triXOffset), (cVertex.getY() + 6 + triYOffset));


		// draw the points
		for( Point currPoint : points )
		{
			g.fillOval((currPoint.getX() + triXOffset), (currPoint.getY() + triYOffset), 2, 2);
		}

		if(showLines && (currentTurn != totalTurns))
		{
			Point point1 = points.get( points.size() - 1 );
			Point point2 = points.get( points.size() - 2 );
			g.drawLine((point1.getX() + triXOffset), 
				(point1.getY() + triYOffset), 
				(point2.getX() + triXOffset), 
				(point2.getY() + triYOffset));
		}


	}


	/**
	 * This is the implementation of run method described by the interface
	 * Runnable. This method runs the Chaos Game and calls the repaint method
	 * when required.
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


	/** 
	 * Given a point takeTurn will generate a new Point that lies 
	 * halfway in between prevPoint and a randomly chosen 
	 * vertex of the triangle.
	 *
	 * @param prevPoint The most recent previously generated point.
	 * @return Returns a point that lies inbetween prevPoint and a
	 * 	randomly chosen vertex of the triangle.
	 */
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


	/**
	 * getPoint translates between one of the values 1,2, or 3
	 * and returns a corresponding vertex of the triangle.  
	 * The vertex chosen corresponds to these rules:
	 * 	1 = a
	 * 	2 = b
	 * 	3 = c
	 *
	 * @param val An integer value of 1, 2, or 3 to be translated
	 * 	to a vertex
	 * @return Returns the corresponding vertex of the input val.
	 */
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
	


	/**
	 * Point is a simple object that holds two cartesian
	 * coordinates and allows them to be retrieved but not
	 * modified
	 */
	class Point{
		private int x;
		private int y;

		/** 
		 * Constructor for Point object that initializes 
		 * the cartesian coordinates
		 *
		 * @param newX the x part of the cartesian coordinate.
		 * @param newY the y part of the cartesian coordinate.
		 */
		public Point(int newX, int newY) { x = newX; y = newY; }

		/**
		 * Getter method for the Y part of the cartesian coordinate.
		 *
		 * @return The value of the Y part of the cartesian coordinate
		 */
		public int getY() { return y; };

		/**
		 * Getter method for the X part of the cartesian coordinate.
		 * 
		 * @return The value of the X part of the cartesian coordinate
		 */
		public int getX() { return x; };
	}
}
