// Imports
import processing.core.*;

/**
 * Arrow class used to draw arrows shot by player.
 * 
 * @author Tim
 * @version 1.0
 * @since 2024-01-23
 */
public class Arrow {

	// Declaration of variables and constants
	// PApplet for drawing graphics to screen
	private PApplet app;
	
	// Arrow image specifications
	private int x, y;
	private int direction;
	
	// Constant, arrow image width
	private final int WIDTH = 69;
	
	// Image of arrow
	private PImage arrow;
	
	/**
	 * Default constructor for arrow object.
	 * 
	 * @param app Sketch object
	 * @param x X coordinate of arrow
	 * @param y Y coordinate of arrow
	 * @param direction Direction of arrow
	 */
	public Arrow(PApplet app, int x, int y, int direction) {
		this.app = app;
		this.x = x;
		this.y = y;
		this.direction = direction;
		
		// Initializes arrow image
		arrow = app.loadImage("Images/Player/Attack/arrow.png");
	}
	
	/**
	 * Draws/Updates arrow
	 * 
	 */
	public void draw() {
		// Move arrow depending on direction
		x = x + 15 * direction;
		app.image(arrow, x, y);
	}
	
	/**
	 * Retrivies X coordinate of arrow
	 * 
	 * @return x X coordinate of arrow
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Checks if arrow hits an entity
	 * 
	 * @param enemy Entity checked to see if collision
	 * @return Boolean depending on if arrow hit
	 */
	public boolean getHit(Entity enemy) {
		// Enemy coordinates
		int enemyX1 = enemy.getHitboxX();
		int enemyY1 = enemy.getY();
		int enemyX2 = enemy.getHitboxX() + enemy.getHitboxWidth();
		int enemyY2 = enemy.getY() + enemy.getHeight();
		
		// Depending on arrow direction, checks collision through intersection of coordinates
		// Returns true if arrow hits
		if (direction > 0) {
			if ((x +  WIDTH) > enemyX1 && (x + WIDTH) < enemyX2 && y > enemyY1 && y < enemyY2) {
				return true;
			}
		} else if (direction < 0) {
			if (x < enemyX2 && x > enemyX1 && y > enemyY1 && y < enemyY2 ) {
				return true;
			}
		}
		// False if arrow doesn't hit
		return false;
	}
	
	
}
