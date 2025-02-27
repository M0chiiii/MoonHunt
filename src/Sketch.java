// Imports
// Processing
import processing.core.*;
// File IO
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
// ArrayList
import java.util.ArrayList;


/**
 * Sketch class used by PApplet processing to draw graphics
 */
public class Sketch extends PApplet {
	
	
	// Declaration of Variables and Constants
	// Screen size
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;
	
	// Menu background images
	private PImage menu;
	private PImage bg;
	
	// Setting images
	private PImage bg3, bg2, bg1, bg5 = createImage(WIDTH, HEIGHT, RGB);
	private PImage floor = createImage(1280, 187, RGB);
	
	// Decoration images
	private PImage tree = createImage(303, 387, RGB);
	private PImage rock = createImage(96, 63, RGB);
	private PImage door = createImage(102, 140, RGB);
	private PImage table = createImage(326, 126, RGB);
	private PImage mooncake = createImage(68, 60, RGB);
	private PImage changE = createImage(154, 154, RGB);
	
	// User UI
	private PImage fKey = createImage(41, 38, RGB);
	private PImage dialogueBox = createImage(656, 224, RGB);
	
	// Setting coordinate values
	private int groundX = 0;
	private int groundY = 533;
	private int decoY = 533 + 45;
	
	// Level status
	private boolean level1 = false;
	private boolean level2 = false;
	private boolean level3 = false;
	private int currentLevel = 0;
	private int currentRoom = 0;
	
	// Display screen status
	private boolean menuScreen = true;
	private boolean levelScreen = false;
	private boolean loginScreen = false;
	private boolean playingScreen = false;
	private boolean controlsScreen = false;
	
	// Login checks
	private boolean invalidUser = false;
	private boolean takenUser = false;
	private boolean guest = false;
	
	// User input
	private boolean typingUsername = false;
	private boolean typingPassword = false;
	private String userInput = "";
	private String username = "";
	private String password = "";
	
	// Dialogue 
	private boolean inDialogue = false;
	private int dialogueLine = 0;
	
	private String[] dialogue1 = 
			{
			"Chang E is up at the moon.",
			"I miss her.",
			"I'll bring her favourite desserts back."
			};
	
	private String[] dialogue2 = 
		{
		"It's already night.",
		"Chang E",
		"Here are your favourite foods.", 
		"Come home soon",
		"I'll be waiting..."
		};
	
	// Entities
	private Boss1 boss1;
	
	// Player
	private Player player;
	
	
	/**
	 * Used to determine display window settings/configurations
	 * 
	 */
    public void settings() {
    	
    	// Sets the screen size
    	size(WIDTH, HEIGHT);
    }

    
    /**
     * Setups/initializes images and entities/objects. 
     * 
     */
    public void setup() {
    	// Setting frame rate
    	frameRate(40);
    	// Setting text size
    	textSize(30);
    	
    	// Backgrounds
    	menu = loadImage("Images/Backgrounds/menu.png");
    	bg = loadImage("Images/Backgrounds/bg7.png");
    	bg3 = loadImage("Images/Backgrounds/bg3.png");
    	bg2 = loadImage("Images/Backgrounds/bg2.png");
    	bg1 = loadImage("Images/Backgrounds/bg1.png");
    	bg5 = loadImage("Images/Backgrounds/bg5.png");
    	
    	// Floor
    	floor = loadImage("Images/Tileset/floor1.png");
    	
    	// Decorations
    	door = loadImage("Images/Decorations/door.png");
    	tree = loadImage("Images/Decorations/tree.png");
    	rock = loadImage("Images/Decorations/rock.png");
    	mooncake = loadImage("Images/Decorations/mooncake.png");
    	table = loadImage("Images/Decorations/table.png");
    	changE = loadImage("Images/Entities/Chang e/chang e.png");
    	
    	// UI
    	dialogueBox = loadImage("Images/dialogue.png");
    	fKey = loadImage("Images/Keys/fkey.png");
    	
    	// Entities
    	// Calling boss1 and Player constructors
    	boss1 = new Boss1(this);
    	player = new Player(this, 0, 455, 75, 117); // x, y, width, height
    }


    /**
     * Draws all graphics to screen. This method constantly loops through images in order to run the program.
     * 
     */
    public void draw() {
    	
    	// If on the menu screen
    	if (menuScreen) {
    		// Set background and text settings
    		background(menu); // Sets background image to menu
    		textAlign(CENTER, CENTER); // Centers text
    		
    		// Play button
    		// Checks if mouse is hovering over button, changes transparency of button
    		if (mouseX > 520 && mouseX < 720 && mouseY > 380 && mouseY < 450) {
    			stroke(color(0, 0, 0, 50)); // rectangle border colour
    			fill(color(0, 0, 0, 100)); // changes drawing colour
        		rect(520, 380, 200, 70); // rectangle
        		fill(255, 255, 255);
        		text("Play", 520, 380, 200, 70); // Displays text within the coordinates of the rectangle
    		} else {
    			noStroke();
    			fill(color(0, 0, 0, 150));
        		rect(520, 380, 200, 70);
        		fill(255, 255, 255);
        		text("Play", 520, 380, 200, 70);
    		}
    		
    		// Controls
    		// Checks if mouse is hovering over button, changes transparency of button
    		if (mouseX > 520 && mouseX < 720 && mouseY > 480 && mouseY < 550) {
    			stroke(color(0, 0, 0, 50));
        		fill(color(0, 0, 0, 100));
        		rect(520, 480, 200, 70);
        		fill(255, 255, 255);
        		text("Controls", 520, 480, 200, 70);
    		} else {
    			noStroke();
        		fill(color(0, 0, 0, 150));
        		rect(520, 480, 200, 70);
        		fill(255, 255, 255);
        		text("Controls", 520, 480, 200, 70);
    		}
    		
    	// if user is on login screen
    	} else if (loginScreen) {
    		background(bg);
    		
    		// Draw login screen container
    		noStroke();
    		fill(0, 0, 0, 150);
    		rect(400, 150, 480, 425);
    		fill(255, 255, 255);
    		textAlign(CENTER);
    		text("User Login: ", 640, 180);
    		
    		// Changes colour to red
    		fill(255, 0, 0, 150);
    		
    		// If username is already taken, text displays
    		if (takenUser) {
    			text("Username is taken.", 640, 225);
    		} 
    		// If the login details are invalid, text displays
    		else if (invalidUser) {
    			text("Invalid login.", 640, 225);
    		}
    		
    		// Draw username box
    		fill(255, 255, 255, 200);
    		rect(520, 250, 240, 50);
    		fill(0, 0, 0, 50);
    		textAlign(CENTER, CENTER);
    		// If username is empty, displays "Username"
    		if (username == "") {
    			text("Username", 520, 250, 240, 50);
    		} else {
    			// Changes colour to black and displays username, what user typed
    			fill(0);
    			text(username, 520, 250, 240, 50);
    		}
    		
    		// Draw password box
    		fill(255, 255, 255, 200);
    		rect(520, 325, 240, 50);
    		fill(0, 0, 0, 50);
    		textAlign(CENTER, CENTER);
    		// If password is empty, displays "Password"
    		if (password == "") {
    			text("Password", 520, 325, 240, 50);
    		} else {
    			// Changes colour to black and displays what user typed for password
    			fill(0);
    			text(password, 520, 325, 240, 50);
    		}
    		
    		
    		// Draws the login button
    		// Checks if user is hovering over the button, if they are then changes transparency of button
    		if (mouseX > 520 && mouseX < 760 && mouseY > 400 && mouseY < 450) {
    			// If hovering
    			// Changes transparency so lighter
    			stroke(color(0, 0, 0, 50));
    			fill(0, 0, 0, 100);
        		rect(520, 400, 240, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Login", 520, 400, 240, 50);
    		} else {
    			// if not hovering
    			// Changes transparency so darker
    			noStroke();
    			fill(0, 0, 0, 150);
        		rect(520, 400, 240, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Login", 520, 400, 240, 50);
    		}
    		
    		// Create new account button
    		// Checks if user is hovering over the button, if they are then changes transparency of button
    		if (mouseX > 420 && mouseX < 640 && mouseY > 500 && mouseY < 550) {
    			// If hovering
    			// Changes transparency so lighter
    			stroke(color(0, 0, 0, 50));
    			fill(0, 0, 0, 100);
        		rect(420, 500, 220, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Create New Login", 420, 500, 220, 50);
    		} else {
    			// if not hovering
    			// Changes transparency so darker
    			noStroke();
    			fill(0, 0, 0, 150);
        		rect(420, 500, 220, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Create New Login", 420, 500, 220, 50);
    		}
    		
    		
    		// Continue as guest button
    		// Checks if user is hovering over the button, if they are then changes transparency of button
    		if (mouseX > 660 && mouseX < 880 && mouseY > 500 && mouseY < 550) {
    			// If hovering
    			// Changes transparency so lighter
    			stroke(color(0, 0, 0, 50));
    			fill(0, 0, 0, 100);
    			rect(660, 500, 220, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Guest", 660, 500, 220, 50);
    		} else {
    			// if not hovering
    			// Changes transparency so darker
    			noStroke();
    			fill(0, 0, 0, 150);
        		rect(660, 500, 220, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Guest", 660, 500, 220, 50);
    		}
    		
    		
    	// If user is on level selection screen
    	} else if (levelScreen) {
    		// Draw background image
    		background(bg);
    		
    		// Draws the level selection container
    		noStroke();
    		fill(0, 0, 0, 150);
    		rect(140, 80, 1000, 560);
    		fill(255, 255, 255);
    		textAlign(CENTER);
    		text("Level Selection", 640, 180);
    		
    		
    		// Level 1 button
    		// Checks if user is hovering over the button, if they are then changes transparency of button
    		if (mouseX > 202.5 && mouseX < 452.5 && mouseY > 200 && mouseY < 520) {
    			// If hovering
    			// Changes transparency so lighter
    			stroke(color(0, 0, 0, 50));
    			fill(0, 0, 0, 100);
        		rect(202.5f, 200, 250, 320);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Level 1", 202.5f, 200, 250, 320);
    		} else {
    			// if not hovering
    			// Changes transparency so darker
    			noStroke();
    			fill(0, 0, 0, 150);
        		rect(202.5f, 200, 250, 320);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Level 1", 202.5f, 200, 250, 320);
    		}
    		
    		
    		
    		// Level 2 button
    		// Checks if level1 is true to see if they beat level1 and unlocked level2
    		if (level1) {
    			
    			// Checks if user is hovering over the button, if they are then changes transparency of button
    			if (mouseX > 515 && mouseX < 765 && mouseY > 200 && mouseY < 520) {
    				// If hovering
        			// Changes transparency so lighter
    				stroke(color(0, 0, 0, 50));
    				fill(0, 0, 0, 100);
            		rect(515, 200, 250, 320);
            		fill(255, 255, 255);
            		textAlign(CENTER, CENTER);
            		text("Level 2", 515, 200, 250, 320);
        		} else {
        			// if not hovering
        			// Changes transparency so darker
        			noStroke();
        			fill(0, 0, 0, 150);
            		rect(515, 200, 250, 320);
            		fill(255, 255, 255);
            		textAlign(CENTER, CENTER);
            		text("Level 2", 515, 200, 250, 320);
        		}
    			
    		} else { // If level 1 not cleared yet, level 2 locked
    			// Level 2 button will be gray and unclickable
    			noStroke();
    			fill(160, 160, 160, 200);
        		rect(515, 200, 250, 320);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Locked", 515, 200, 250, 320);
    		}
    		
    		
    		// Level 3 button
    		// Checks if level2 is true to see if user beat level2 and unlocked level3
    		if (level2) {
    			
    			// Checks if user is hovering over the button, if they are then changes transparency of button
    			if (mouseX > 827.5 && mouseX < 1077.5 && mouseY > 200 && mouseY < 520) {
    				// If hovering
        			// Changes transparency so lighter
    				stroke(color(0, 0, 0, 50));
    				fill(0, 0, 0, 100);
            		rect(827.5f, 200, 250, 320);
            		fill(255, 255, 255);
            		textAlign(CENTER, CENTER);
            		text("Level 3", 827.5f, 200, 250, 320);
    			} else {
    				// if not hovering
        			// Changes transparency so darker
    				noStroke();
    				fill(0, 0, 0, 150);
            		rect(827.5f, 200, 250, 320);
            		fill(255, 255, 255);
            		textAlign(CENTER, CENTER);
            		text("Level 3", 827.5f, 200, 250, 320);
    			}
    			
    			
    		} else {// If level 2 not cleared yet, level 3 locked
    			// Level 3 button will be gray and unclickable
    			noStroke();
    			fill(160, 160, 160, 200);
        		rect(827.5f, 200, 250, 320);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Locked", 827.5f, 200, 250, 320);
    		}
    		
    		
    	// If user is on the playing screen
    	} else if (playingScreen) {
	    	
    		// Array for level setting images
	    	PImage levels[][] = {
	    			{bg3, rock},
	    			{bg2, tree},
	    			{bg1, tree},
	    			{bg5, }
	    			};
	    	
	    	// Loop through the images corresponding to the level
	    	for (int i = 0; i < levels[currentLevel-1].length; i++) {
	    		// Checks if not first element in array, first element is background image
	    		if (i != 0) {
	    			// If first room of the current level
	    			// Draws the decoration images
	    			if (currentRoom == 0)
	    				image(levels[currentLevel-1][i], 800, decoY - levels[currentLevel-1][i].height);
	    			// Otherwise draw the background image, if first element of array
	    		} else {
	    			background(levels[currentLevel-1][i]);
	    		}
	    	}
	    	
	    	// Draw ground
	    	image(floor, groundX, groundY);
	    	
	    	// Checks if player is on the ground, ground collision
	    	player.checkOnGround(groundX, groundY, floor.width, floor.height);
	    	// Draws the player
	    	player.draw();
	    	
	    	
	    	// If user in level 1 
	    	if (currentLevel == 1) {
	    		
	    		// Checks the player direction and draws the image depending on how the screen is reflected
	    		if (Player.getDirection() > 0) {
	    			image(table, 500, decoY - table.height);
	    		} else {
	    			image(table, -480-table.width, decoY - table.height);
	    		}
	    		
	    		// Checks if the player is not in a dialogue and if they are close to the table image
	    		// Draws the f key (interact) if they are within 50 pxs proximity
	    		if (Math.abs(500 - player.getX()) < 50 && !inDialogue) {
	    			image(fKey, Player.getX() + player.getWidth()/2, player.getY() - 30);
	    		}
	    		
	    		// If the user is on the last dialogue line
	    		if (dialogueLine == dialogue1.length) {
    				// Resets dialogue variables
	    			dialogueLine = 0;
    				inDialogue = false;
    				// Resets level and screen
    				currentLevel = 0;
    				playingScreen = false;
    				// level 1 is completed
    				level1 = true;
    				// Changes back to level screen
    				levelScreen = true;
    				// Moves player back to default location and saves the game data
    				Player.setX(0);
        			updateSave();
    			}
	    		
	    		// If the user is in a dialogue
	    		if (inDialogue) {
	    			
	    			// Draws dialogue box
	    			image(dialogueBox, 312, 40);
	    			textAlign(CENTER, CENTER);
	    			fill(0, 0, 0);
	    			// Draws dialogue text depending on dialogue line
	    			text(dialogue1[dialogueLine], 330, 90, 620, 125);
	    		}
	    		
	    	// If level 2
	    	} else if (currentLevel == 2) { 
	    		
	    		// Checks if player is in first room
	    		// Will draw a door 
	    		if (currentRoom == 0) {
		    		image(door, 1100, decoY - door.height);
		    		// If user is close to door, draws f key (interact)
		    		if ((1100 + door.width) - Player.getX() < 150) {
		    			image(fKey, Player.getX() + player.getWidth()/2, player.getY() - 30);
		    		}
		    	}
		    	
		    	// stores arrow of arrow images
	    		Arrow[] arrows = Player.getArrows();
		    	
	    		// If player is in the boss room
		    	if (currentLevel == 2 && currentRoom == 1) {
		    		// draw boss
		    		boss1.draw();
		    		// Loop through every arrow in array to check if they collide with the boss
		    		for (int i = 0; i < arrows.length; i++) {
		    			// If arrows collide with boss, set them to null to delete
		    			if (arrows[i] != null && arrows[i].getHit(boss1)) {
		    				Player.getArrows()[i] = null;
		    				// Boss hit method ran to deplete health
		    				boss1.hit();
		    			}
		    		}
		    		
		    		// Boss is dead, draw a mooncake 
		    		if (boss1.getPhase() == 0) {
		    			image(mooncake, -1100, decoY - mooncake.height);
		    			// IF player is within close proximity of mooncake, draw f key (interact)
		    			if ((1100 + mooncake.width) - Player.getX() < 150) {
		    				image(fKey, -Player.getX() - player.getWidth()/2, player.getY() - 30);
		    			}
		    		}
		    	}
	    		
	    		
		    	// If level 3
	    	} else if (currentLevel == 3) {
	    		
	    		// Draw table, location changes depending on player direction because of bug
	    		if (Player.getDirection() > 0) {
	    			image(table, 500, decoY - table.height);
	    		} else {
	    			image(table, -480-table.width, decoY - table.height);
	    		}
	    		
	    		// If player is not in a dialogue and close to the table
	    		// Draw f key (interact)
	    		if (Math.abs(500 - player.getX()) < 50 && !inDialogue) {
	    			image(fKey, Player.getX() + player.getWidth()/2, player.getY() - 30);
	    		}
	    		
	    		// If user dialogue is at the last line
	    		if (dialogueLine == dialogue2.length) {
	    			// Reset dialogue
    				dialogueLine = 0;
    				inDialogue = false;
    				// Complete level 3 and bring user back to level selection screen
    				currentLevel = 0;
    				playingScreen = false;
    				level3 = true;
    				levelScreen = true;
    				// Move player back to default location and update save file
    				Player.setX(0);
        			updateSave();
    			}
	    		
	    		// If user is in dialogue
	    		if (inDialogue) {
	    			
	    			// Draw images
	    			image(changE, 1000, 40);
	    			image(mooncake, 620, 430);
	    			
	    			// Draw dialogue box
	    			image(dialogueBox, 312, 40);
	    			textAlign(CENTER, CENTER);
	    			fill(0, 0, 0);
	    			
	    			// Draw text depending on dialogue line
	    			text(dialogue2[dialogueLine], 330, 90, 620, 125);	
	    		}	    		
	    	}
	    	
	    // If user is on controls screen
    	} else if (controlsScreen) {
    		
    		// Draw background
    		background(bg);
    		
    		// controls container
    		noStroke();
    		fill(0, 0, 0, 150);
    		rect(400, 150, 480, 425);
    		fill(255, 255, 255);
    		textAlign(CENTER);
    		text("Controls: ", 640, 180);
    		
    		// Checks if user is hovering over the button, if they are then changes transparency of button
    		if (mouseX > 520 && mouseX < 760 && mouseY > 525 && mouseY < 575) {
    			// If hovering
    			// Changes transparency so lighter
    			stroke(color(0, 0, 0, 50));
    			fill(0, 0, 0, 100);
        		rect(520, 525, 240, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Back", 520, 525, 240, 50);
    		} else {
    			// If not hovering
    			// Changes transparency so darker
    			noStroke();
    			fill(0, 0, 0, 150);
    			rect(520, 525, 240, 50);
        		fill(255, 255, 255);
        		textAlign(CENTER, CENTER);
        		text("Back", 520, 525, 240, 50);
    		}
    		
    		// Writes controls onto screen
    		textAlign(CENTER, CENTER);
    		text("A/D - move\nW - jump\nF - interact\nF/Enter - dialogue next\nJ - attack", 400, 150, 480, 350);
    	}
   	}
  
  
    /**
     * Detects when mouse is clicked
     * 
     */
    public void mouseClicked() {
    	
    	// If on menu screen
    	if (menuScreen) {
    		// Checks if player clicked play button
    		if (mouseX > 520 && mouseX < 720 && mouseY > 380 && mouseY < 450) {
    			loginScreen = true;
    			menuScreen = false;
    			// Checks if player clicked controls button
    		} else if (mouseX > 520 && mouseX < 720 && mouseY > 480 && mouseY < 550) {
    			controlsScreen = true;
    			menuScreen = false;
    		}
    		
    	// If on login screen
    	} else if (loginScreen) {
    		
    		// sets values so user isn't typing anything
    		typingUsername = false;
    		typingPassword = false;
    		
    		// checks if user clicked login button
    		if (mouseX > 520 && mouseX < 760 && mouseY > 400 && mouseY < 450) {
    			// if account is valid, changes screen
    			if (login()) {
    				loginScreen = false;
    				levelScreen = true;
    				// if account not valid, display error 
    			} else {
    				invalidUser = true;
    				takenUser = false;
    			}
    		
    		// Guest Play button
    		// Continues game as guest, data is not saved
    		} else if (mouseX > 660 && mouseX < 880 && mouseY > 500 && mouseY < 550) {
    			loginScreen = false;
    			levelScreen = true;
    			guest = true;
    		
    		// username box
    		// Allows user to type in box and stores input as username
    		} else if (mouseX > 520 && mouseX < 760 && mouseY > 250 && mouseY < 300) {
    			typingUsername = true;
    			typingPassword = false;
    			userInput = username;
    			
    		// password box
    		// Allows user to type in box and stores input as password
    		} else if (mouseX > 520 && mouseX < 760 && mouseY > 325 && mouseY < 375) {
    			typingPassword = true;
    			typingUsername = false;
    			userInput = password;
    			
    		// create account button
    		} else if (mouseX > 420 && mouseX < 640 && mouseY > 500 && mouseY < 550) {
    			// Checks if username is already taken
    			// Will display error if taken
    			if (checkTaken()) {
    				takenUser = true;
    				invalidUser = false;
    				// Changes to level selection if user is valid and stores user data
    			} else {
    				loginScreen = false;
        			levelScreen = true;
        			createUser();
    			}
    		}
    	
    	// If level screen
    	} else if (levelScreen) {
    		
    		// level 1 button
    		// Brings user to first level
    		if (mouseX > 202.5 && mouseX < 452.5 && mouseY > 200 && mouseY < 520) {
    			levelScreen = false;
    			currentLevel = 1;
    			playingScreen = true;
    			
    		// level 2 button
    		// Checks if user completed level 1 and brings them to level 2
    		} else if (mouseX > 515 && mouseX < 765 && mouseY > 200 && mouseY < 520 && level1) {
    			levelScreen = false;
    			currentLevel = 2;
    			playingScreen = true;
    			
    		// level 3 button
    		// Checks if user completed level 2 and brings them to level 1
    		} else if (mouseX > 827.5 && mouseX < 1077.5 && mouseY > 200 && mouseY < 520 && level2) {
    			levelScreen = false;
    			currentLevel = 3;
    			playingScreen = true;
    		}
    		
    	// if controls screen 
    	} else if (controlsScreen) {

    		// checks if back button is clicked and changes screen to menu
    		if (mouseX > 520 && mouseX < 760 && mouseY > 525 && mouseY < 575) {
    			controlsScreen = false;
    			menuScreen = true;
    		}
    		
    	}
    	// When mouse is clicked, display coodinates
    	System.out.println(mouseX + " " + mouseY);
   	}
    
    
    /**
     * Detects when key is released
     * 
     */
    public void keyReleased() {
    	// If key is A or D key is released, player stops moving
    	if (key == 'a' || key == 'A' || key == 'd' || key == 'D') {
    		player.move(0);
    	}
    }
    
    /**
     * Checks if a key is pressed
     * 
     */
    public void keyPressed() {
    	
    	// checks if in login screen and is typing either username or password
    	if (loginScreen && (typingUsername || typingPassword)) {
    		
    		// When user clicks enter, stops typing
    		if (key == ENTER) {
    			typingUsername = false;
    			typingPassword = false;
    		// If backspace, remove last character typed unless already blank
    		} else if (key == BACKSPACE) {
    			if (!userInput.equals(""))
    				userInput = userInput.substring(0, userInput.length() - 1);
    		// Add key pressed to input
    		} else {
    			userInput += key;
    		}
    		
    		// if user is typing username, stores userInput as username
    		if (typingUsername) {
    			username = userInput;
    		// if user typing password, stores userInput as password
    		} else if (typingPassword) {
    			password = userInput;
    		}
    		
    	// If on playing screen and not in dialogue
    	// When in dialogue player cannot move 
    	} else if (playingScreen && !inDialogue) {
    		
    		// Movement keys, call player move method with direction, -1 for left, 1 for right, 0 stop
    		if (key == 'a' || key == 'A') {
        		player.move(-1);
        	} else if (key == 'd' || key == 'D') {
        		player.move(1);
        	// Jump
        	} else if (key == 'w' || key == 'W') {
        		player.jump();
        	}
        	
    		// Attack
        	if (key == 'J' || key == 'j') {
        		player.attack();
        	}
        	
        	// Toggle entity hitbox
        	if (key == 'P' || key == 'p') {
        		player.toggleHitbox();
        		boss1.toggleHitbox();
        	}
        	
        	// Interact button
        	if (key == 'F' || key == 'f') {
        		
        		// If in level 1
        		if (currentLevel == 1) {
        			
        			// Checks if user interacted with table and starts dialogue 
        			if (Math.abs(500 - player.getX()) < 50) {
        				inDialogue = true;
        			}
        		// Checks if user interacted with table in level 3 and starts dialogue
        		} else if (currentLevel == 3) {
        			if (Math.abs(500 - player.getX()) < 50)
        				inDialogue = true;
        		
        		// If player interacts with a door, move player to next room
        		} else if ((1100 + door.width) - Player.getX() < 150 && currentRoom == 0) {
        			currentRoom++;
        			Player.setX(0);
        		// If player interacts with mooncake
        		} else if ((1100 + mooncake.width) - Player.getX() < 150 && currentRoom == 1 && boss1.getPhase() == 0) {
        			// Completes level 2
        			currentRoom = 0;
        			level2 = true;
        			currentLevel = 0;
        			// Changes screen displayed
        			playingScreen = false;
        			levelScreen = true;
        			// Resets player location and saves data
        			Player.setX(0);
        			updateSave();
        		}
        	}
    	
        	// If user in dialogue
        	// F key or Enter to go to next line in dialogue
    	} else if (inDialogue) {
    		if (key == ENTER || key == 'f' || key == 'F') {
    			dialogueLine++;
    		}
    	}
    }
    
    
    /**
     * Checks if username and password are correct and retrieves data.
     * 
     * @return Returns boolean depending on if user account is valid.
     */
    public boolean login() {
    	// Try catch statement for File IO
		try {
			// Scanner object to read file
			Scanner read = new Scanner(new File("accounts.txt"));
			
			// Loop through file
			while (read.hasNext()) {
				// Store read data
				String line = read.nextLine();
				String[] parts = line.split("-");

				// If data is the example data, skip
				if (parts[0].equals("Username") && parts[1].equals("Password")) {
					continue;
				}
				
				// If username and password match an account, retrieves data
				if (parts[0].equals(username) && parts[1].equals(password)) {
					// Close scanner
					read.close();
					username = parts[0];
					password = parts[1];
					level1 = Boolean.parseBoolean(parts[2]);
					level2 = Boolean.parseBoolean(parts[3]);
					level3 = Boolean.parseBoolean(parts[4]);
					// Returns true, account is valid
					return true;
				}
					
			}
			
			read.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		// Returns false, account is invalid
		return false;
	}
    
    
    /**
     * Checks if account is already taken
     * 
     * @return Returns boolean value depending on if account username is already taken.
     */
    public boolean checkTaken() {
    	// Try catch for File IO
    	try {
    		// Scanner object to read file
			Scanner read = new Scanner(new File("accounts.txt"));
			
			// Loop through file 
			while (read.hasNext()) {
				// Store data read
				String line = read.nextLine();
				String[] parts = line.split("-");

				// Checks if data is the same as the example, skip
				if (parts[0].equals("Username") && parts[1].equals("Password")) {
					continue;
				}
				
				// Checks if username matches a username in file
				if (parts[0].equals(username)) {
					read.close();
					// True, username already exists
					return true;
				}
			}
			
			// Close scanner object
			read.close();
		} catch (IOException e) {
			System.out.println(e);
		}
    	// False, username is not taken
		return false;
    }
    
    
    /**
     * Creates a new user in the flat file
     * 
     */
    public void createUser() {
    	// Try catch for File IO
    	try {
    		// FileWriter and PrintWriter objects to write to file
    		FileWriter write = new FileWriter("accounts.txt", true);
    		PrintWriter fw = new PrintWriter(write);
    		
    		// Stores information from current save to one line and writes line to file
    		String line = String.join("-", username, password, "" + level1, "" + level2, "" + level3);
    		fw.println(line);
    		
    		// Close PrintWriter stream
    		fw.close();
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
    
    
    /**
     * Updates database flat file with new save information
     * 
     */
    public void updateSave() {
    	
    	// If user is guest, data does not save
    	if (guest) {
    		return;
    	}
    	
    	// Try catch for File IO
    	try {
    		// Scanner object to read file
    		Scanner read = new Scanner(new File("accounts.txt"));
    		
    		// Arraylist to store all data
    		ArrayList<String> newData = new ArrayList<String>();
    		
    		// Loop through file
    		while (read.hasNext()) {
    			// Stores data read
    			String line = read.nextLine();
    			String[] parts = line.split("-");
    			
    			// Checks if username matches current user's username
    			// Will overwrite old data and add it to arraylist
    			if (parts[0].equals(username)) {
    				String newInfo = String.join("-", username, password, "" + level1, "" + level2, "" + level3);
    				newData.add(newInfo);
    			} else {
    				newData.add(line);
    			}
    		}
    		// Close scanner stream
    		read.close();
    		
    		// Creates new text file named temp
    		File oldFile = new File("accounts.txt");
    		File newFile = new File("temp.txt");
    		
    		// Creates new file, deletes old, and renames the new file to the old file
    		newFile.createNewFile();
    		oldFile.delete();
    		newFile.renameTo(oldFile);
    		
    		// FileWriter and PrintWriter streams
    		FileWriter fileW = new FileWriter("accounts.txt", true);
    		PrintWriter w = new PrintWriter(fileW);
    		
    		// Loop through arraylist and write all data into flatfile
    		for (int i = 0; i < newData.size(); i++) {
    			w.println(newData.get(i));
    		}
    		
    		// CLose writer stream
    		w.close();
    		
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
    
  
}



