// Imports
import processing.core.*;

/**
 * Boss1 object is a child of Entity, draws first boss into the game
 * 
 * @author Tim
 * @since 2024-01-23
 * @version 1.0
 */
public class Boss1 extends Entity {
	
	// Declaration of variables and constants
	// PApplet for drawing graphics to screen
	private PApplet app;
	
	// Constants
	private final int SPEED = 5;
	private final int BASE_HEIGHT = 207;
	
	// Screen limitations
	private final int BORDER = 200;
	private final int BORDER1 = 1200;
	
	// Boss 1 coordinates and size
	private int x = 800;
	private int y = 380;
	private int width = 111;
	private int height = 156;
	
	// Timer for how long an action has been ran
	private int actionTimer = 0;
	
	// Distance
	private int playerX;
	private int bossToPlayer;
	private int bossToBorder;
	private int bossToBorder1;
	
	// Boss hit 
	private boolean isHit = false;
	
	// Boss attacks
	private boolean attackCooldown = false;
	private boolean attacking = false;
	
	// Boss phase
	private int phase = 1;
	
	// Animations
	private Animation attack, idle, run, hit, death;
	
	/**
	 * Default constructor for Boss1 object.
	 * 
	 * @param app Sketch object
	 */
	public Boss1 (PApplet app) {
		// Calls parent constructor with coordinate and image size values
		super(app, 1000, 370, 111, 156, 5);
		this.app = app;

		// Initializes animations 
		initAnimations();
		
		// Initializes distance variables
		playerX = Player.getX();
		bossToPlayer = Math.abs(playerX - x);
		bossToBorder = Math.abs(x - BORDER);
		bossToBorder1 = Math.abs(x - BORDER1);
	}
	
	/**
	 * Initialization of animation objects
	 * 
	 */
	public void initAnimations() {
		// Each animation used for each player action
		attack = new Animation(app, "Images/Boss1/Attack/attack", 6, 10, BASE_HEIGHT);
		run = new Animation(app, "Images/Boss1/Run/run", 8, 10, BASE_HEIGHT);
		idle = new Animation(app, "Images/Boss1/Idle/idle", 8, 10, BASE_HEIGHT);
		hit = new Animation(app, "Images/Boss1/Hit/hit", 4, 10, BASE_HEIGHT);
		death = new Animation(app, "Images/Boss1/Death/death", 6, 10, BASE_HEIGHT);
	}
	
	/**
	 * Moves the boss across the screens
	 * 
	 * @param dist Distance boss is moved
	 * @param endPoint End point boss will reach before stop moving
	 * @param direction Direction boss moves
	 */
	public void move(int dist, int endPoint, int direction) {
		
		// if moving right and not past endpoint
		if (direction > 0 && endPoint - x - dist > 0) {
			// Move boss at constant SPEED
			x += SPEED;
			// Draw run animation
			run.draw(x, y);
			width = run.getWidth();
			height = run.getHeight();
		// If moving left and not past endpoint
		} else if (direction < 0 && x - endPoint - dist > 0) {
			// Move boss at constant SPEED
			x -= SPEED;
			// Draw run animation
			run.draw(x, y);
			width = run.getWidth();
			height = run.getHeight();
			// if boss not moving
		} else {
			// Draw idle animation
			idle.draw(x, y);
			width = idle.getWidth();
			height = idle.getHeight();
		}
	}
	
	/**
	 * Boss attack method
	 * 
	 */
	public void attack() {
		// Sets attacking to true to stop other actions
		attacking = true;
		
		// Checks if boss hans't attacked more than once
		if (attack.getLoop() == 0) {
			// On the 4th frame, boss moves due to image bugs
			if (attack.getFrame() >= 4 && !attackCooldown) {
				attackCooldown = true;
				x = x - 153;
			} 
			// Draw attack animation
			attack.draw(x, y);
			width = attack.getWidth();
			height = attack.getHeight();
			
		// If boss attacked more than once
		} else {
			
			// If attack is on cooldown, move boss back to original coordinates
			if (attackCooldown) {
				x = x + 153;
			}
			// Reset cooldown
			attackCooldown = false;
			attacking = false;
			// Draw boss animation as idle, prevents empty boss image for a frame
			idle.draw(x, y);
			width = idle.getWidth();
			height = idle.getHeight();
		}
	}
	
	
	/**
	 * Method for when the boss dies, current health reaches 0
	 * 
	 */
	public void death() {
		// If in phase 1, set to phase 0
		// Phase 0 is when boss is dead
		if (phase == 1)
			phase = 0;
	}
	
	
	/**
	 * Phase 1 of the boss, move patterns
	 * 
	 */
	public void phase1() {
		// Increase action timer
		actionTimer++;
		
		// If action timer is at 100, updates distances
		if (actionTimer % 100 == 0) {
			playerX = Player.getX();
			bossToPlayer = Math.abs(playerX - x);
			bossToBorder = Math.abs(x - BORDER);
			bossToBorder1 = Math.abs(x - BORDER1);
		}
		
		// move back a little more than half the distance from border to boss
		if (bossToBorder1 > 150 && actionTimer < 100) {
			move(bossToBorder1/2 - bossToBorder1/3, BORDER1, 1);
		// move forward half the distance from boss to player
		} else if (bossToBorder > 100 && actionTimer < 250) {
			move(bossToPlayer/2, playerX, -1);
		// Attack
		} else if(actionTimer < 350) {
			attack();
		// Reset attack and boss is idle
		} else if(actionTimer < 400) {
			attack.resetLoop();
			idle.draw(x, y);
		}
		
		// If no action is happening, boss is drawn as idle
		else {
			idle.draw(x, y);
		}
		
		// Reset action timer when past 400, infinite loop
		if (actionTimer > 400) {
			actionTimer = 0;
		}
	}
	
	/**
	 * Hit method is called when the boss is hit by any of the player's attacks.
	 * 
	 */
	@Override
	public void hit() {
		// Calls the parent hit method
		super.hit();
		// Sets grace period to on, doesn't allow boss to be attacked during this period 
		super.setGracePeriod(true);
		// Boss is hit set to true
		isHit = true;
		
		// Recalculates distances
		playerX = Player.getX();
		bossToPlayer = Math.abs(playerX - x);
		bossToBorder = Math.abs(x - BORDER);
		bossToBorder1 = Math.abs(x - BORDER1);
	}
	
	/**
	 * Draws/updates boss image
	 * 
	 */
	public void draw() {
		
		// If boss is not dead
		if (phase != 0) {
			// Draw healthbar if not player
			super.drawHealthBar(false);
			
			// If entity health is below 0, boss dies, death method called
			if (super.getHealth() <= 0) {
				death();
			}
			
			// If boss is not hit
			if (!isHit) {

				// Draw phase 1 of the boss
				if (phase == 1) {
					phase1();
				}
				
				// If boss is not attacking, update hitbox
				if (!attacking)
					super.updateHitbox(x, y , width, height);
				
				// Draw hitbox
				super.drawFlipHitbox();
			
			// If boss is hit and hit loop is 0
			} else if (isHit && hit.getLoop() == 0){
				// Draw hit animation
				hit.draw(x, y);
			// If hit animation already finished one loop
			} else if (hit.getLoop() > 0) {
				// Reset hit loop
				// Stop hit animation and draw idle
				hit.resetLoop();
				isHit = false;
				idle.draw(x, y);
			}
			
		// If boss is dead
		} else {
			
			// If first death animation loop is finished
			if (death.getLoop() > 0) {
				// Draw single image of boss dead
				death.drawSingle(x, y, 5);
				
			} else {
				// If boss loop is not finished, draw death animation
				death.draw(x, y);
			}
		}
	}
	
	/**
	 * Retrieeves the phase value for boss
	 * 
	 * @return phase Phase of boss
	 */
	public int getPhase() {
		return phase;
	}
	
}
