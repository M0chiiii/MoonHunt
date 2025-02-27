// Imports
import processing.core.*;

/**
 * Entity class, Parent class of any entity in game.
 * 
 * @author Tim
 * @version 1.0
 * @since 2024-01-23
 */
public class Entity {

	// Declaration of variables and constants
	// PApplet for drawing graphics to screen
	private PApplet app;
	
	// Hitbox
	private PShape hitbox;
	private boolean hitboxOn = false;
	private int hitboxX, hitboxWidth;
	
	// Healthbar
	private int healthBarX = 350;
	private int healthBarY = 40;
	private int healthBarWidth = 580;
	private int healthBarHeight = 40;
	private int maxHealth;
	private int currentHealth;
	
	// Falling velocity and gravity
	private double fallV; 
	private int gravityTimer = 0;
	private float yDif;
	
	// Entity coordinates and image size
	private int x, y;
	private int width, height;
	
	// Grace period of entity hit
	private boolean gracePeriod = false;
	private int graceTimer = 0;

	
	/**
	 * Default constructor for entity object.
	 * 
	 * @param app Sketch object
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width of entity
	 * @param height Height of entity
	 * @param health Health of entity
	 */
	public Entity(PApplet app, int x, int y, int width, int height, int health) {
		this.app = app;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxHealth = health;
		this.currentHealth = health;
		
		// Intializes hitbox 
		initHitbox();
	}
	
	/**
	 * Initialization of entity hitbox
	 * 
	 */
	public void initHitbox() {
		// Determines hitbox coordinates
		hitboxWidth = (int)(width*0.8);
		hitboxX = x + (int)(width*0.1);
		// Creates hitbox shape
		hitbox = app.createShape(PConstants.RECT, hitboxX, 0, hitboxWidth, height);
	}
	
	
	/**
	 * Updates hitbox coordinates
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void updateHitbox(int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		
		// Determines hitbox coordinates
		hitboxWidth = (int)(width*0.8); // Hitbox width = 80% of image width
		hitboxX = x + (int)(width*0.1); 
		
		// If entity is in grace period, timer increases
		if (gracePeriod)
			graceTimer++;
	}
	
	
	/**
	 * Checks if player or entity is on the ground.
	 * 
	 * @param groundX X coordinate of floor
	 * @param groundY Y cooridnate of floor
	 * @param groundWidth Width of floor
	 * @param groundHeight Height of floor
	 */
	public void checkOnGround(int groundX, int groundY, int groundWidth, int groundHeight) {
		
		// Distance from player height to ground
		yDif = groundY - y+height;
		
		// Check collision on bottom of hitbox
		if (yDif > 200) {
			Player.setIsGrounded(false);
		} else {
			Player.setInAction(false);
			Player.setIsGrounded(true);

			// Resets gravity timer
			gravityTimer = 0;
		}
		
		
	}
	
	
	/**
	 * If entity is hit
	 * 
	 */
	public void hit() {
		
		// Checks if graceTimer is greater than set time
		// Prevents entity from getting hit multiple times in a second
		if (graceTimer >= 30) {
			// If greater, resets grace timer
			graceTimer = 0;
			gracePeriod = false;
		}
		
		// If not grace period
		if (!gracePeriod) {
			// entity loses health
			currentHealth--;
		}
		
	}
	
	
	/**
	 * Vertical velocity depending on how long in air for. Increases velocity overtime
	 * 
	 * @return Returns the velocity
	 */
	public double gravity() {
		gravityTimer++;
		fallV = 9.8 * gravityTimer * 0.05;
		
		return fallV;
	}
	
	
	/**
	 * Toggles the entity hitbox, visible or not visible
	 * 
	 */
	public void toggleHitbox() {
		hitboxOn = !hitboxOn;
		System.out.print(hitboxOn);
	}
	
	
	/**
	 * Draws/updates hitbox
	 * 
	 */
	public void drawHitbox() {
		
		// checks if hitbox is on
		if (hitboxOn) {
			// Draws hitbox at hitbox coordinates
			app.shape(hitbox, hitboxX, y);
		}
	}
	
	
	/**
	 * Draws the entity health bar
	 * 
	 * @param player If entity is player or not
	 */
	public void drawHealthBar(boolean player) {
		
		// If entity is not player
		if (!player) {
			
			// Healthbar container
			app.noFill();
			app.stroke(0, 0, 0);
			app.rect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
			
			// Healthbar shape changes depending on direction due to negative scaling play screen
			// Is drawn reflected depending on player direction
			if (Player.getDirection() > 0) {
				
				// Draws red in healthbar
		    	app.fill(255, 0, 0);
		    	app.rect(healthBarX, healthBarY, (float)(healthBarWidth*currentHealth/maxHealth), healthBarHeight);
		    	
		    	// Text displaying currentHealth over maxHealth
		    	app.fill(255, 255, 255);
				app.textAlign(PConstants.CENTER, PConstants.CENTER);
				app.text(currentHealth + "/" + maxHealth, healthBarX, healthBarY, healthBarWidth, healthBarHeight);
		    	
			} else if (Player.getDirection() < 0) {
				// Draws red in healthbar
		    	app.fill(255, 0, 0);
		    	app.rect(-healthBarX-healthBarWidth*(currentHealth/maxHealth), healthBarY, healthBarWidth*(currentHealth/maxHealth), healthBarHeight);
			
		    	// Text displaying currentHealth over maxHealth
		    	app.fill(255, 255, 255);
				app.textAlign(PConstants.CENTER, PConstants.CENTER);
				app.text(currentHealth + "/" + maxHealth, -healthBarX-healthBarWidth, healthBarY, healthBarWidth, healthBarHeight);
			}
			
		}
		
	}
	
	
	//using scale on the player messes up other images
	
	/**
	 * Draws a flipped version of the hitbox if hitbox display is enabled
	 */
	public void drawFlipHitbox() {
	    if (hitboxOn) {
	        app.shape(hitbox, -hitboxX-1100, y); // Draw flipped hitbox at specified coordinates
	    }
	}

	/**
	 * Sets the grace period state
	 * 
	 * @param value new state of the grace period (true or false)
	 */
	public void setGracePeriod(boolean value) {
	    gracePeriod = value; // Update the gracePeriod
	}

	/**
	 * Retrieves the  x coordinate of the hitbox
	 * 
	 * @return current x coordinate of the hitbox
	 */
	public int getHitboxX() {
	    return hitboxX; // Return the x coordinate of the hitbox
	}

	/**
	 * Retrieves the y coordinate of the object
	 * 
	 * @return The current y coordinate
	 */
	public int getY() {
	    return y; // Return the y-coordinate
	}

	/**
	 * Retrieves the width of the hitbox
	 * 
	 * @return width of the hitbox
	 */
	public int getHitboxWidth() {
	    return hitboxWidth; // Return the width of the hitbox
	}

	/**
	 * Retrieves the height of the object
	 * 
	 * @return height of the object
	 */
	public int getHeight() {
	    return height; // Return the height of the object
	}

	/**
	 * Retrieves the current health of the object
	 * 
	 * @return current health value
	 */
	public int getHealth() {
	    return currentHealth; // Return the current health
	}

	/**
	 * Sets the current health of the object
	 * 
	 * @param health The new health value to be set
	 */
	public void setCurrentHealth(int health) {
	    currentHealth = health; // Update the current health
	}

	/**
	 * Sets the maximum health of the object
	 * 
	 * @param health The new maximum health value to be set
	 */
	public void setMaxHealth(int health) {
	    maxHealth = health; // Update the maximum health
	}
	
	
	
}
