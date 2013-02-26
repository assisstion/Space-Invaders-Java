package com.github.assisstion.spaceInvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentSkipListSet;

import com.github.assisstion.spaceInvaders.EnemySquad.Direction;

/**
 * Engine class for rendering the game. This class extends Canvas and overrides
 * the paint() method so it can be directly used to paint objects.
 * 
 * @author Markus Feng
 * @author Michael Man
 */
public class Engine extends Canvas implements KeyListener {

	/*
	 * Serial version UID, recommended for every class that implements
	 * Serializable or any class that extends something that implements
	 * Serializable
	 */
	private static final long serialVersionUID = 21816248595432439L;
	private static final Font FONT_SMALL = new Font("Copperplate",
			Font.BOLD, 33);
	private static final Font FONT_LARGE = new Font("Times New Roman",
			Font.BOLD, 80);
	private static final Font FONT_HUGE = new Font("Times New Roman",
			Font.BOLD, 110);
	private static final Font FONT_MEDIUM = new Font("Copperplate",
			Font.BOLD, 50);
	private static final int[][] LEVELS = {{10,5}, {12,7},{15,8},{17,10},{17,10}};
	private static final Enemy.EnemyType[] LEVEL1DATA={Enemy.EnemyType.RED,Enemy.EnemyType.BLUE,Enemy.EnemyType.NORMAL,Enemy.EnemyType.NORMAL,Enemy.EnemyType.NORMAL};
	private static final Enemy.EnemyType[] LEVEL2DATA={Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.NORMAL,Enemy.EnemyType.NORMAL};
	private static final Enemy.EnemyType[] LEVEL3DATA={Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.NORMAL};
	private static final Enemy.EnemyType[] LEVEL4DATA={Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE,Enemy.EnemyType.BLUE};
	private static final Enemy.EnemyType[] LEVEL5DATA={Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED,Enemy.EnemyType.RED};
	/*
	 * Update code runs according to current state of the code Possible states:
	 * not_ready: not ready to start ready: ready to start but not started yet
	 * main: started
	 */
	private String state = "not_ready";
	private Graphics2D g;
	private String godmode = "";
	// Unused for now
	private boolean godmodeOn = false;
	private Player player1;
	private boolean bulletLeft = true;
	// true if right arrow key down
	private boolean rightOn = false;
	// true if left arrow key down
	private boolean leftOn = false;
	// true if space key down
	private boolean spaceOn = false;
	// Set containing all game objects
	private ConcurrentSkipListSet<Sprite> gameObjects = new ConcurrentSkipListSet<Sprite>();
	// set containing all enemies
	private ConcurrentSkipListSet<EnemySquad> enemySquads = new ConcurrentSkipListSet<EnemySquad>();
	// Set containing all bullets
	private ConcurrentSkipListSet<Bullet> bullets = new ConcurrentSkipListSet<Bullet>();
	// Set containing all bunkers
	private ConcurrentSkipListSet<Bunker> bunkers = new ConcurrentSkipListSet<Bunker>();
	//Current level
	public int currentLevel = 1;
	/*
	 * Creates a new Engine and sets up the background and dimensions
	 */
	public Engine() {
		addKeyListener(this);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(960, 740));
	}

	/*
	 * Method for updating this canvas DO NOT put direct code in here, please
	 * put methods accessing the code from here
	 */
	@Override
	public void paint(Graphics g) {
		try {
			requestFocus();
			if (state.equalsIgnoreCase("not_ready")) {
				return;
			} else if (state.equalsIgnoreCase("ready")) {
				startGame(g);
			} else if (state.equalsIgnoreCase("main")) {
				updateMain(g);
			} else if (state.equalsIgnoreCase("game_over")) {
				updateMain(g);
				MainCanvas.isOn = false;
			} else {
				// Throws an exception if none of the states match
				throw new IllegalStateException("Illegal engine state: "
						+ state);
			}
		} catch (GameException e) {
			// placeholder
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * Starts the game
	 */
	public void startGame(Graphics graphics) {
		g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		String message = new String("Press Enter To Start");
		g.setFont(FONT_LARGE);
		g.drawString(message,
				getWidth() / 2 - (g.getFontMetrics().stringWidth(message) / 2),
				350);

	}

	/*
	 * Main update method
	 */
	public void updateMain(Graphics graphics) {
		g = (Graphics2D) graphics;
		// Renders the game objects
		for (Sprite object : gameObjects) {
			Helper.renderSprite(g, object);
		}
		inputUpdate();
		bulletUpdate();
		playerUpdate();
		endUpdate();
		drawMenu(g);
		if (state.equals("game_over")){
			g.fillRect(0, 0, 960, 740);
			String gameOver = new String("Game Over!");
			String yourScore = new String("Final Score: " + player1.score);
			g.setColor(Color.RED);
			g.setFont(FONT_HUGE);
			g.drawString(gameOver,getWidth() / 2 - (g.getFontMetrics().stringWidth(gameOver) / 2), 370);
			g.setColor(Color.WHITE);
			g.setFont(FONT_LARGE);
			g.drawString(yourScore,getWidth() / 2 - (g.getFontMetrics().stringWidth(yourScore) / 2), 450);
		}
		
	}

	public void drawMenu(Graphics2D g) {
		
		//message = Score Display
		//message2=Health Display
		//message3=Life Display
		//message4=GOD MODE ON CONFIRMER
		//message5=Level Display
		
		g.setFont(FONT_SMALL);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 960, 70);
		g.setColor(Color.BLACK);
		String message = null;
		String message3=null;
		if (!godmodeOn){
		message = new String(player1.getName() + "'s Score: "
				+ player1.score);
		message3 = new String("Lives Left: " + player1.livesRemaining);
		} else {
			g.setColor(Color.RED);
			message = new String(player1.getName() + "'s Score: GOD MODE ON");
			message3 = new String("Lives Left: �");
			
		}
		g.drawString(message, 10, 25);
		g.drawString(message3,730, 25);
		
		
		if (godmodeOn){
			g.setColor(Color.RED);
			g.setFont(FONT_MEDIUM);
			String message4 = new String("GOD MODE ON");
			g.drawString(message4,getWidth() / 2 - (g.getFontMetrics().stringWidth(message4) / 2),
			570);
			g.setFont(FONT_SMALL);
		}
		
		
		String message2 = new String(player1.getName() + "'s Health: "
				+ player1.health + "/2000");

		Color tempColor = null;
		if (godmodeOn){
			tempColor = Color.red;
			message2 = (player1.getName() + "'s Health: INFINITE");
		}
		else if (player1.health > 1300) {
			tempColor = Color.green;
		} else if (player1.health > 600) {
			tempColor = Color.yellow;
		} else {
			tempColor = Color.red;
		}

		g.setColor(tempColor);

		g.drawString(message2, 10, 60);

		
		
		g.setColor(Color.BLACK);
		String message5 = new String("Level: " + currentLevel + "/5");
		g.drawString(message5,730,60);
	}

	// map input will be developed here later
	public void constructEnemyFormation(int lvlnum) {
		int enemyWidth = LEVELS[lvlnum-1][0];
		
		
		int x = 10;
		EnemySquad enemies = new EnemySquad();
		
		for (int i = 0; i < enemyWidth; i++) {
			Enemy.EnemyType[] EnemyData=null;
			if (lvlnum == 1){
				EnemyData=LEVEL1DATA;
			} else if (lvlnum==2){
				EnemyData=LEVEL2DATA;
			} else if (lvlnum==3){
				EnemyData=LEVEL3DATA;
			} else if (lvlnum==4){
				EnemyData=LEVEL4DATA;
			} else if (lvlnum==5){
				EnemyData=LEVEL5DATA;
			} else {
				System.out.println("LEVEL NUMBER ERROR");
			}
			enemies.direction = EnemySquad.Direction.RIGHT;
			
			Enemy enemy1 = new Enemy(EnemyData[0], x, 80);
			Enemy enemy2 = new Enemy(EnemyData[1], x, 120);
			Enemy enemy3 = new Enemy(EnemyData[2], x, 160);
			Enemy enemy4 = new Enemy(EnemyData[3], x, 200);
			Enemy enemy5 = new Enemy(EnemyData[4], x, 240);
			
			gameObjects.add(enemy1);
			gameObjects.add(enemy2);
			gameObjects.add(enemy3);
			gameObjects.add(enemy4);
			gameObjects.add(enemy5);
			enemies.add(enemy1);
			enemies.add(enemy2);
			enemies.add(enemy3);
			enemies.add(enemy4);
			enemies.add(enemy5);
			
			if (lvlnum>=2){
			Enemy enemy6 = new Enemy(EnemyData[5], x, 280);
			Enemy enemy7 = new Enemy(EnemyData[6], x, 320);
			gameObjects.add(enemy6);
			enemies.add(enemy6);
			gameObjects.add(enemy7);
			enemies.add(enemy7);
			} if (lvlnum>=3){
			Enemy enemy8 = new Enemy(EnemyData[7], x, 360);
			gameObjects.add(enemy8);
			enemies.add(enemy8);
			} if (lvlnum>=4){
				Enemy enemy9 = new Enemy(EnemyData[8], x, 400);
				gameObjects.add(enemy9);
				enemies.add(enemy9);
				Enemy enemy10 = new Enemy(EnemyData[9], x, 440);
				gameObjects.add(enemy10);
				enemies.add(enemy10);
			}
			
			
			enemySquads.add(enemies);
			x += 50;
		}
	}

	// For cleaning up stuff
	public void endUpdate() {
		for (EnemySquad enemies : enemySquads) {
			if (enemies.isEmpty()) {
				enemySquads.remove(enemies);
			} 
			if (enemySquads.isEmpty()){
				System.out.println("Level Complete!");
				nextLevel();
			}
		}
	}

	
	public void nextLevel(){
		//STUFF TO DO HERE: display info to player.
		if (currentLevel==5){
			GameWon();
		} else {
		currentLevel+=1;
		constructEnemyFormation(currentLevel);
		MovementClock.MovementSpeed=1750;
		player1.livesRemaining+=1;
		}
	}
	
	public void GameWon(){
		System.out.println("You've Won the Game!");
	}
	public void inputUpdate() {
		// Bullet creation code
		if (spaceOn) {
			if (player1.firingCooldown <= 0) {
				int tempx = player1.x + 4;
				if (!bulletLeft) {
					tempx = player1.x + 28;
					bulletLeft = true;
				} else {
					bulletLeft = false;
				}
				Bullet b = new Bullet(Bullet.BulletType.PLAYER, tempx,
						player1.y);
				bullets.add(b);
				gameObjects.add(b);
			
				player1.firingCooldown = 50;
			}
		}

	}

	public void playerUpdate() {
		// Changes the player location depending on the current direction
		if (player1.currentDirection.equals(Player.Direction.LEFT)) {
			player1.x -= 4;
			Helper.updateHitbox(player1);
		} else if (player1.currentDirection.equals(Player.Direction.RIGHT)) {
			player1.x += 4;
			Helper.updateHitbox(player1);
		}
		if (player1.firingCooldown > 0) {
			if (godmodeOn){
				player1.firingCooldown-=10;
			} else{
			player1.firingCooldown--;
			}
		}
		if (player1.x < 0) {
			player1.x = 0;
		} else if (player1.x > MainCanvas.frame.getWidth()
				- player1.getImage().getWidth()) {
			player1.x = MainCanvas.frame.getWidth()
					- player1.getImage().getWidth();
		}
	}

	public void bulletUpdate() {
		for (EnemySquad es : enemySquads) {
			for (Enemy e : es) {
				if (e.shootingCounter == 0) {
					Bullet b = new Bullet(Bullet.BulletType.NORMAL, e.x, e.y);
					if (e.enemytype.equals(Enemy.EnemyType.RED)) {
						b = new Bullet(Bullet.BulletType.RED, e.x, e.y);
					} else if (e.enemytype.equals(Enemy.EnemyType.BLUE)) {
						b = new Bullet(Bullet.BulletType.BLUE, e.x, e.y);
					}
					if (e.hitBox.overLaps(player1.hitBox)){
						player1.livesRemaining=0;
					}
					bullets.add(b);
					gameObjects.add(b);
					e.shootingCounter = MainCanvas.rand
							.nextInt(e.shootingCooldownMax
									- e.shootingCooldownMin)
							+ e.shootingCooldownMin;
				} else {
					e.shootingCounter--;
				}
			}
		}

		for (Bullet b : bullets) {

			if (b.direction.equals(Bullet.BulletDirection.UP)) {
				b.y -= b.movementSpeed;
				Helper.updateHitbox(b);
			} else if (b.direction.equals(Bullet.BulletDirection.DOWN)) {
				b.y += b.movementSpeed;
				Helper.updateHitbox(b);
			}
			for (Bunker k : bunkers) {
				if (b.hitBox.overLaps(k.hitBox)) {
					gameObjects.remove(b);
					
					k.health -= b.damage;
					
					
					if (godmodeOn && b.movementSpeed==8){
						k.health=0;
					}
					bullets.remove(b);
				}
				if (k.health <= 0) {
					gameObjects.remove(k);
					bunkers.remove(k);
				} else if ((k.health % 100) == 0
						&& k.lastImageUpdate > k.health) {
					int x = k.getBunkerNum() + 10;
					k.setImage(x);
					k.lastImageUpdate = k.health;
				}
			}

			if (b.hitBox.overLaps(player1.hitBox)) {
				// CHANGE THIS LATER FOR VARYING BULLET DAMAGE
				if (b.direction.equals(Bullet.BulletDirection.DOWN)
						&& !godmodeOn) {
					player1.health -= b.damage;
					bullets.remove(b);
					gameObjects.remove(b);
					if (player1.health <= 0) {
						player1.livesRemaining-=1;
						player1.x=432;
						player1.y=680;
						player1.health=2000;
						
					}
					if (player1.livesRemaining == 0){
						System.out.println("You're Dead!");
						player1.x = MainCanvas.frame.getWidth();
						player1.y = MainCanvas.frame.getHeight();
						Helper.updateHitbox(player1);
						state = "game_over";
					}
				}
			}
			for (EnemySquad enemies : enemySquads) {
				for (Enemy e : enemies) {
					if (b.hitBox.overLaps(e.hitBox)) {
						if (b.direction.equals(Bullet.BulletDirection.UP)) {
							gameObjects.remove(b);
							bullets.remove(b);
							e.health -= b.damage;
							if (e.health <= 0 || godmodeOn) {
								enemies.remove(e);
								gameObjects.remove(e);
								player1.score += e.scoreReward;
								System.out.println("Enemy Killed");
							}
						}
					if (e.y>960){
						gameObjects.remove(enemies);
						state="game_over";
					}
					}
				}
			}
			if (b.y < 0 - b.getImage().getHeight()
					|| b.x < 0 - b.getImage().getWidth()
					|| b.x > MainCanvas.frame.getWidth()
					|| b.y > MainCanvas.frame.getHeight()) {
				bullets.remove(b);
				gameObjects.remove(b);
			}
		}
	}

	/*
	 * Called by the main method when the game wants to start
	 */
	public void start() {
		state = "ready";
	}

	public void startGame() {
		// Starts the game
		System.out.println("It's starting!");
		// Creates a new player
		new Thread(new MovementClock()).start();
		player1 = new Player("Bob");
		gameObjects.add(player1);
		// loads map plan here
		constructEnemyFormation(1);
		// Constructs the bunker formations
		constructBunkerFormation(64, 600);
		constructBunkerFormation(252, 600);
		constructBunkerFormation(440, 600);
		constructBunkerFormation(628, 600);
		constructBunkerFormation(816, 600);
		state = "main";

	}

	// Constructs a bunker formation with the top left corner at (x, y)
	public void constructBunkerFormation(int x, int y) {
		int bunkerSize = Bunker.BUNKER_SIZE;
		constructBunker(4, x + bunkerSize * 3, y + bunkerSize);
		constructBunker(0, x, y + bunkerSize * 3);
		constructBunker(0, x, y + bunkerSize * 2);
		constructBunker(0, x, y + bunkerSize);
		constructBunker(1, x, y);
		constructBunker(0, x + bunkerSize, y);
		constructBunker(0, x + bunkerSize * 2, y);
		constructBunker(0, x + bunkerSize * 3, y);
		constructBunker(2, x + bunkerSize * 4, y);
		constructBunker(0, x + bunkerSize * 4, y + bunkerSize);
		constructBunker(0, x + bunkerSize * 4, y + bunkerSize * 2);
		constructBunker(0, x + bunkerSize * 4, y + bunkerSize * 3);
		constructBunker(3, x + bunkerSize, y + bunkerSize);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (state.equalsIgnoreCase("ready")) {
				startGame();
			}
			if (godmode.equals("god")) {
				godmode();
				godmode = "";
				System.out.println("God Mode is starting!");
			}
			else {
				godmode = "";
			}
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// tells the update loop to allow bullet firing
			if (state.equals("main")) {
				spaceOn = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// sets the direction to Right
			if (state.equals("main")) {
				rightOn = true;
				player1.currentDirection = Player.Direction.RIGHT;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// sets the direction to Left
			if (state.equals("main")) {
				leftOn = true;
				player1.currentDirection = Player.Direction.LEFT;
			}
		}

		else if (e.getKeyCode() == KeyEvent.VK_G) {
			if (godmode.equals("")) {
				godmode = "g";
			}
			else {
				godmode = "";
			}
		}

		else if (e.getKeyCode() == KeyEvent.VK_O) {
			if (godmode.equals("g")) {
				godmode = "go";
			}
			else {
				godmode = "";
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			if (godmode.equals("go")) {
				godmode = "god";
				// Added God Mode
			} 
			else {
				godmode = "";
			}

		} else {
			godmode = "";
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// sets the direction to None
			if (state.equals("main")) {
				rightOn = false;
				if (player1.currentDirection == Player.Direction.RIGHT) {
					player1.currentDirection = Player.Direction.NONE;
					// Sees whether leftarrow is still being pressed.
					if (leftOn) {
						// If it is, change direction back to left
						player1.currentDirection = Player.Direction.LEFT;
					}
				}
			}

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// sets the direction to None
			if (state.equals("main")) {
				leftOn = false;
				if (player1.currentDirection == Player.Direction.LEFT) {
					player1.currentDirection = Player.Direction.NONE;
					// Sees whether rightarrow is still being pressed.
					if (rightOn) {
						player1.currentDirection = Player.Direction.RIGHT;
					}
					player1.currentDirection = Player.Direction.NONE;
					// Sees whether rightarrow is still being pressed.
					if (rightOn) {
						// If it is, change direction back to Right
						player1.currentDirection = Player.Direction.RIGHT;
					}
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// tells the update loop to stop bullet firing
			if (state.equals("main")) {
				spaceOn = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	private void godmode() {
		godmodeOn = true;
		// Unimplemented
		// God Mode to be initiated here
	}

	private void constructBunker(int bunkerType, int x, int y) {
		Bunker a = new Bunker(bunkerType, x, y);
		bunkers.add(a);
		gameObjects.add(a);
	}

	public void moveEnemies() {
		for (EnemySquad enemies : enemySquads) {
			if (enemies.direction.equals(Direction.DOWN)) {
				enemies.direction = enemies.pendingDirection;
			}
			for (Enemy e : enemies) {
				if (e.x + 50 >= MainCanvas.frame.getWidth()
						&& enemies.direction.equals(EnemySquad.Direction.RIGHT)) {
					enemies.direction = EnemySquad.Direction.DOWN;
					MovementClock.MovementSpeed = (int) (MovementClock.MovementSpeed * (4.0 / 5.0));
					enemies.pendingDirection = EnemySquad.Direction.LEFT;
				} else if (e.x - 50 <= 0
						&& enemies.direction.equals(EnemySquad.Direction.LEFT)) {
					enemies.direction = EnemySquad.Direction.DOWN;
					MovementClock.MovementSpeed = (int) (MovementClock.MovementSpeed * (4.0 / 5.0));
					enemies.pendingDirection = EnemySquad.Direction.RIGHT;
				}
			}
			for (Enemy e : enemies) {
				if (enemies.direction.equals(EnemySquad.Direction.RIGHT)) {
					e.x += 25;
				} else if (enemies.direction.equals(EnemySquad.Direction.LEFT)) {
					e.x -= 25;
				} else if (enemies.direction.equals(EnemySquad.Direction.DOWN)) {
					e.y += 25;
				}
				Helper.updateHitbox(e);
			}
		}
	}
}
