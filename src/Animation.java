// Imports
import processing.core.*;

/**
 * Object for drawing entity animations and looping through them
 * 
 * @author Tim
 * @version 1.0
 * @since 2024-01-23
 */
public class Animation {
	
	// Declaration of variables and constants
	// PApplet for drawing graphics to screen
	private PApplet app;
	
	// Images
	private PImage[] images;
	private String imagePath;
	
	// Frames, frame counts, frame delays
	private int count;
	private int frameCount, frame = 0;
	private int delay;
	// Frame loops
	private int numOfLoops = 0;
	
	// Image size
	private int baseHeight;
	private int currentWidth;
	private int currentHeight;
	
	
	/**
	 * Default constructor of Animation method.
	 * 
	 * @param app Sketch object
	 * @param imagePath Image file directory
	 * @param count Number of animation frames
	 * @param delay Number of times a frame is displayed
	 * @param BASE_HEIGHT Height of largest image
	 */
	public Animation(PApplet app, String imagePath, int count, int delay, int BASE_HEIGHT) {
		this.app = app;
		this.count = count;
		this.delay = delay;
		this.imagePath = imagePath;
		this.baseHeight = BASE_HEIGHT;
		// Initialize images array with count of images
		images = new PImage[count];
		
		// Loop through images array and store image depending on iteration
		for (int i = 0; i < count; i++) {
			images[i] = app.loadImage(imagePath + i + ".png");
		}
	}
	
	
	/**
	 * Overloaded constructor of animation.
	 * Takes in a countStart and countEnd instead of count to only loop through specific frames of an animation
	 * 
	 * @param app Sketch object
	 * @param imagePath Image file directory
	 * @param countStart Start index of animation frames
	 * @param countEnd End index of animation frames
	 * @param delay Number of times a frame is displayed
	 * @param BASE_HEIGHT Height of largest image
	 */
	public Animation(PApplet app, String imagePath, int countStart, int countEnd, int delay, int BASE_HEIGHT) {
		this.app = app;
		this.count = countEnd - countStart;
		this.delay = delay;
		this.imagePath = imagePath;
		this.baseHeight = BASE_HEIGHT;
		// Initialize images array with count of images
		images = new PImage[count];
		
		// Loop through images array and store image depending on iteration
		for (int i = 0; i < count; i++) {
			images[i] = app.loadImage(imagePath + (i+countStart) + ".png");
		}
	}
	
	
	/**
	 * Method to draws/update animations
	 * 
	 * @param x X cooordinate of enetity.
	 * @param y Y coordinate of entity.
	 * @param direction Direction entity is facing
	 */
	public void draw(int x, int y, int direction) {
		
		// Sprite's cut out wrong, need to set every sprite draw to same level
		y = baseHeight - images[frame].height + y;
		currentWidth = images[frame].width;
		currentHeight = images[frame].height;
		
		// Increase frame count
		frameCount++;
		
		// Checks if one loop of an animation is finished
		if (frameCount == count*delay) {
			// Resets loop and frame to display
			numOfLoops++;
			frameCount = 0;
			frame = 0;
		// If frameCoutn is divisible by delay, frame changes to next frame
		} else if (frameCount % delay == 0) {
			frame = frameCount / delay;
		}
		
		// Change directions 
		// Scale reflects images off the y-axis so need to redraw images in new coordinates
		if (direction < 0) {
			app.scale(-1, 1);
			app.image(images[frame], -x-images[frame].width, y);
		} else if (direction > 0) {
			app.scale(1, 1);
			app.image(images[frame], x, y);
		} 
	}
	
	
	/**
	 * Overloadded method to draw animatiosn without direction. Used if entity is not player.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void draw(int x, int y) {
		// Sprite's cut out wrong, need to set every sprite draw to same level
		y = baseHeight - images[frame].height + y;
		currentWidth = images[frame].width;
		currentHeight = images[frame].height;
		// Increase frame count
		frameCount++;
		
		// Checks if one loop of an animation is finished
		if (frameCount == count*delay) {
			// Resets loop and frame to display
			numOfLoops++;
			frameCount = 0;
			frame = 0;
			// If frameCoutn is divisible by delay, frame changes to next frame
		} else if (frameCount % delay == 0) {
			frame = frameCount / delay;
		}
		
		// Draws image depending on player direction
		app.scale(-1 * Player.getDirection(), 1);
		app.image(images[frame], -x-images[frame].width, y);
		
	}
	
	/**
	 * Draws a single constant frame instead of looping through.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate 
	 * @param frame Index of frame to draw
	 */
	public void drawSingle(int x, int y, int frame) {
		
		// Sprite's cut out wrong, need to set every sprite draw to same level
		y = baseHeight - images[frame].height + y;
		currentWidth = images[frame].width;
		currentHeight = images[frame].height;
		
		// Draws image depending on player direction
		app.scale(-1 * Player.getDirection(), 1);
		app.image(images[frame], -x-images[frame].width, y);
	}
	
	
	/**
	 * Retrieves the current width of the object
	 * 
	 * @return int The current width
	 */
	public int getWidth() {
	    return currentWidth; // Return the value of currentWidth
	}

	/**
	 * Retrieves the current height of the object
	 * 
	 * @return int current height
	 */
	public int getHeight() {
	    return currentHeight; // Return the value of currentHeight
	}

	/**
	 * Retrieves current frame index
	 * 
	 * @return int The index of the current frame
	 */
	public int getFrame() {
	    return frame; // Return the current frame index
	}

	/**
	 * Retrieves number of completed loops
	 * 
	 * @return int number of loops that have been completed
	 */
	public int getLoop() {
	    return numOfLoops; // Return the number of completed loops
	}

	/**
	 * Resets the loop count to zero
	 */
	public void resetLoop() {
	    numOfLoops = 0; // Reset the loop counter to zero
	}
	
	
}
