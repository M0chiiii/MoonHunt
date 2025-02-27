// Imports
import processing.core.*;

/**
 * Player object is a child of entity, used to draw player in game
 * 
 * @author Tim
 * @since 2024-01-23
 * @version 1.0
 */
public class Player extends Entity{
	
	// Declaration of variables and constants
	// PApplet for drawing graphics to screen
	private PApplet app;
	
	// Constants
	private final double SPEED = 10;
	private final int BASE_HEIGHT = 126;
	
	// Player image 
	private static int x, y;
	private int width, height;
	
	// Direction
	private boolean left, right = false;
	private static int direction = 1; // positive number is right, negative is left
	
	// Action
	private boolean isAttack = false;
	private static boolean inAction = false;
	
	// Animation
	private Animation idle, run;
	private Animation attack;
	private Animation jump, fall;
	
	// Arrows
	private static Arrow [] arrows = new Arrow [10];
	private boolean arrowCooldown = false;
	
	// Ground collision and gravity/jumping
	private static boolean isGrounded = false;
	private boolean isJumping = false;
	private int jumpHeight;
	
	
	/**
	 * Default player constructor
	 * 
	 * @param app Sketch object
	 * @param x Player x coordinate
	 * @param y Player y coordinate
	 * @param width Player image width
	 * @param height Player image height
	 */
	public Player(PApplet app, int x, int y, int width, int height) {
		// Calls parent constructor
		super(app, x, y+17, width, height, 5);
		this.x = x;
		this.y = y;
		this.app = app;
		// Initialize animations
		initAnimations();
		
	}

	/**
	 * Initializes animation objects through constructor
	 * 
	 */
	public void initAnimations() {
		// Each animation used for each player action
		idle = new Animation(app, "Images/Player/Idle/idle", 8, 10, BASE_HEIGHT);
		run = new Animation(app, "Images/Player/Run/running", 8, 5, BASE_HEIGHT);
		attack = new Animation(app, "Images/Player/Attack/attack", 14, 5, BASE_HEIGHT);
		jump = new Animation(app, "Images/Player/JumpAndFall/jumpandfall", 0, 6, 10, BASE_HEIGHT);
		fall = new Animation(app, "Images/Player/JumpAndFall/jumpandfall", 6, 12, 10, BASE_HEIGHT);
	}
	
	
	/**
	 * Draws/Updates player model
	 * 
	 */
	public void draw() {		
		
		// Loop thorugh all arrows and draws them
		for (int i = 0; i < arrows.length; i++) {
			// Checks if arrow in arrow array is null, draws them if they arent
			if (arrows[i] != null) {
				arrows[i].draw();
				// Checks if arrows went past screen border, sets them to null if they go past
				if (1280 - arrows[i].getX() < 0 || arrows[i].getX() < 0) {
					arrows[i] = null;
				}
			}
		}
		
		// Checks if user is not jumping and not on the ground, falling
		if (!isGrounded && !isJumping) {
			// Action set to true so cant attack
			inAction = true;
			
			// Draws falling animation and sets width and height
			fall.draw(x, y, direction);
			width = fall.getWidth();
			height = fall.getHeight();
			
			// Moves player down depending on gravity value
			y = (int)(y + super.gravity());
			
		// If user is jumping
		} else if (isJumping) {
			// Action set to true, cant attack
			inAction = true;
			// Draw jumping animation
			jump.draw(x, y, direction);
			// move player up
			y = (int)(y - 15 - super.gravity());
			// Checks if player has reached max jump height, sets jumping to false if they reached
			if (y < jumpHeight) {
				isJumping = false;
			}
			
		// If user is not jumping and is on the ground
		} else if (!isJumping && isGrounded) {
			// Set player on ground to prevent broken animation frames
			y = 455;
		}
		
		//Checks if user is attacking and is on ground
		if (isAttack && isGrounded) {
			// Changes action to true
			inAction = true;
			// Checks attack animation loop to only attack once
			if (attack.getLoop() > 0) {
				// Resets arrow cooldown, stops attacking, and resets animation loop
				arrowCooldown = false;
				isAttack = false;
				attack.resetLoop();
				inAction = false;
			}
			// Draw attack animation
			attack.draw(x, y, direction);
			width = attack.getWidth();
			height = attack.getHeight();
			
			// If user attack frame is on 9 and arrow is not on cooldown
			// Shoots arrow/draws new arrow
			if (attack.getFrame() == 9 && !arrowCooldown) {
				shootArrow(0);
				arrowCooldown = true;
			}
		}
		
		
		// Move left and right
		// If moving right
		if (right) {
			x += SPEED;
			// Direction facing right
			direction = 1;
			// If not in action, not jumping
			if (!inAction) {
				// Draws running animation
				run.draw(x,  y, direction);
				width = run.getWidth();
				height = run.getHeight();
			}
		
			// If moving left
		} else if (left) {
			x -= SPEED;
			// Direction facing left
			direction = -1;
			// If not in action, not jumping
			if (!inAction) {
				// Draws running animation
				run.draw(x,  y, direction);
				width = run.getWidth();
				height = run.getHeight();
			}
		
			// If not moving, draws idle animation
		} else {
			if (!inAction) {
				idle.draw(x, y, direction);
				width = idle.getWidth();
				height = idle.getHeight();	
			}	
		}
		
		// Updates player hitbox coordinates depending on direction player is facing
		if (direction < 0) {
			super.updateHitbox(-x-width, y, width, height);
		} else {
			super.updateHitbox(x, y, width, height);
		}
		
		// Draw enetity hitbox
		super.drawHitbox();
	}
	
	/**
	 * Moves player left and right
	 * 
	 * @param direction Direction player is facing
	 */
	public void move(int direction) {
		// If facing right
		if (direction > 0) {
			right = true;
			// If facing left
		} else if (direction < 0) {
			left = true;
			// If not moving, direction 0
		} else {
			right = left = false;
		}
	}
	
	
	/**
	 * Recursive method to loop through Arrows array and draw arrow
	 * 
	 * @param numLoops
	 * @return Returns 
	 */
	public int shootArrow(int numLoops) {
		
		// Checks if the current iteration of the arrows array in the recursion is null
		if (arrows[numLoops] == null) {
			// If not null, arrow draws at player location
			arrows[numLoops] = new Arrow(app, x + (width * direction), y + height/2, direction);
		}
		// Base Case, if num of loops is max, returns 0, ends recursion
		if (numLoops == arrows.length - 1) {
			return 0;
		}
		// Returns 0 + call of itself with an increase in numLoops
		return 0 + shootArrow(numLoops + 1);
	}
	
	
	/**
	 * Sets attack to true if called
	 * 
	 */
	public void attack() {
		isAttack = true;
	}
	

	/**
	 * Called when player jumps, checks if player is on ground, calculates maximum jump height and sets jumping to true;
	 * 
	 */
	public void jump() {
		if (!isJumping && isGrounded) {
			jumpHeight = y - 200;
			isJumping = true;
		}
	}
	
	
	/**
	 * Retrieves the grounded state of the object
	 * 
	 * @return boolean True if the object is grounded, false otherwise
	 */
	public static boolean getIsGrounded() {
	    return isGrounded;
	}

	/**
	 * Sets the grounded state of the object
	 * 
	 * @param value The new grounded state to be set
	 */
	public static void setIsGrounded(boolean value) {
	    isGrounded = value; // Update the isGrounded flag with the provided value
	}

	
	/**
	 * Sets the action state of the object
	 * 
	 * @param value The new action state to be set
	 */
	public static void setInAction(boolean value) {
	    inAction = value; // Update the inAction with provided value
	}

	/**
	 * Retrieves the x coordinate of the object.
	 * 
	 * @return int The current xcoordinate.
	 */
	public static int getX() {
	    return x;
	}

	/**
	 * Sets the x coordinate of the object
	 * 
	 * @param newX The new X-coordinate to be set
	 */
	public static void setX(int newX) {
	    x = newX; // Update the x-coordinate with the provided value
	}

	/**
	 * Retrieves the width of the object
	 * 
	 * @return int The width of the object
	 */
	public int getWidth() {
	    return width;
	}

	/**
	 * Retrieves the height of the object
	 * 
	 * @return int The height of the object
	 */
	public int getHeight() {
	    return height;
	}

	/**
	 * Retrieves the current direction of the object
	 * 
	 * @return int The current direction
	 */
	public static int getDirection() {
	    return direction;
	}

	/**
	 * Retrieves the array of Arrow objects
	 * 
	 * @return Arrow[] An array of Arrow objects
	 */
	public static Arrow[] getArrows() {
	    return arrows;
	}
	

}
