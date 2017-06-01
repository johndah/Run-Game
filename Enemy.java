import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class Enemy implements KeyListener {

	private green g;
	private Animation animation;
	private white white;
	private EnemyWeapon enemyW;
	private EnemyAmmo eAmmo;
	protected float x, y;
	protected float dx = 0;
	protected float dy = 0;
	private double gravity = 7E-1;
	protected float jumpSpeed = -12f;
	protected float deltaX, deltaY;
	private float walkingSpeed;
	protected int enemySize = 0;
	private double MY = 1;
	protected ArrayList<Object> Enemies;
	public boolean accelerate = true;
	protected double acc = 5e-9;
	protected Image Character;
	public boolean active, right = true;
	public long t = 1;
	protected Image images;
	protected Image charWalk[] = new Image[23];
	protected Image charDown[] = new Image[23];
	protected long time = 0;
	public double movieTimeLA, movieTimeGD;
	public int sceneIndexLA, sceneIndexGD, life = 100;
	protected String activity, animationType = null;
	protected String[] activities = { null, "talkToChar", "attackChar" };
	protected ArrayList<String> dialog = new ArrayList<>();
	private float velocityX, frameSpeed;
	protected int frameIncrement, enemyIndex, sentenceIndex;
	private int frameTimeWalk = 30;
	private int frameTimeDown = 10;
	protected boolean swordHit, init, reload = true;
	private Random rand;
	private float theta;
	protected int who = 1;

	public void init(EnemyWeapon enemyW, EnemyAmmo eAmmo) {
		this.enemyW = enemyW;
		this.eAmmo = eAmmo;
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		loadImages();
		addPersons();
	}

	public Enemy(float x, float y, int sceneIndexLA, float walkingSpeed, float frameSpeed, String activity,
			ArrayList<String> dialog) {
		this.x = x;
		this.y = y;
		this.sceneIndexLA = sceneIndexLA;
		this.walkingSpeed = walkingSpeed;
		this.frameSpeed = frameSpeed;
		this.swordHit = false;
		this.activity = activity;
		this.enemyIndex = enemySize;
		this.active = false;
		this.dialog = dialog;
		this.sentenceIndex = -1;
	}

	private void addPersons() {
		rand = new Random();
		int randomIndex = rand.nextInt(animation.loopedAnimation.size());
		float randomSpeed = rand.nextInt(50);
		randomSpeed = (float) (randomSpeed / 1000 + 0.05);
		dialog.add("Hey what's up?");
		dialog.add("So you know what's happening?");
		dialog.add("No time to explain..");
		dialog.add(" ");
		Enemies.add(new Enemy((float) -1800, (float) 120, randomIndex, randomSpeed, (float) (10 * randomSpeed + 0.2),
				"talkToChar", dialog));
		enemySize++;

		// The enemy deserves a weapon ^^
		Enemy person = (Enemy) Enemies.get(Enemies.size() - 1);
		Rectangle RE = getBounds(person);
		int centX = (int) RE.getCenterX();
		int centY = (int) RE.y + (int) RE.height / 3;

		if (person.activity == "attackChar")
			enemyW.EnemyWeapon.add(new EnemyWeapon(enemyW.i1, (int) 0, (int) 0, Enemies.size() - 1, 1));
		else
			enemyW.EnemyWeapon.add(new EnemyWeapon(null, (int) 0, (int) 0, Enemies.size() - 1, -1));

	}

	public Enemy(Animation animation, green g, white white) {
		this.animation = animation;
		this.g = g;
		this.white = white;

		sceneIndexLA = 0;
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				try {

					t.cancel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 500);
	}

	// Change position
	public void update(long timePassed) {

		if (who > 3) {

			who = -1;
		} else if (who > 0)
			who++;

		EnemyWeapon eWeapon;
		for (int i = 0; i < enemySize; i++) {
			Enemy person = (Enemy) Enemies.get(i);
			eWeapon = (EnemyWeapon) enemyW.EnemyWeapon.get(i);

			// Eliminating enemies
			if (person.life <= 0) {
				Enemies.remove(i);
				enemyW.EnemyWeapon.remove(i);
				for (int j = i; j < enemyW.EnemyWeapon.size(); j++) {
					eWeapon = (EnemyWeapon) enemyW.EnemyWeapon.get(j);
					eWeapon.index--;
				}
				enemySize--;
			}

			person.x += person.dx * timePassed;
			person.y += gravity * person.t + person.dy;

			if (Math.abs(dy) > 0.1)
				person.dy += gravity;

			person.t++;

			if (person.activity == "attackChar" && person.active)
				attackChar(i);
			else if (person.activity == "talkToChar" && person.active)
				talkToChar(person);
		}

		// Update animation
		animationUpdate(timePassed);
	}

	public synchronized void animationUpdate(long timePassed) {
		for (int i = 0; i < enemySize; i++) {
			Enemy person = (Enemy) Enemies.get(i);

			if (person.animationType == "loopedAnimation" && animation.loopedAnimation.size() > 1) {

				person.movieTimeLA += person.frameSpeed * timePassed;

				if (person.movieTimeLA >= animation.totalTimeLA) {
					person.movieTimeLA = 0;
					person.sceneIndexLA = 0;

				}
				while (person.movieTimeLA > animation.getScene("loopedAnimation", person.sceneIndexLA).endTime) {
					person.sceneIndexLA++;
				}
			}

			if (person.animationType == "gettingDownAnimation" && animation.gettingDownAnimation.size() > 1) {

				person.movieTimeGD += timePassed;

				if (person.movieTimeGD >= animation.totalTimeGD) {
					person.movieTimeGD = 0;

					if (person.frameIncrement > 0)
						person.sceneIndexGD = animation.gettingDownAnimation.size() - 1;
					else if (!(person.sceneIndexGD == animation.gettingDownAnimation.size() - 1))
						person.animationType = null;

					person.frameIncrement = 0;
				}

				if (person.frameIncrement > 0) {
					while ((person.movieTimeGD >= animation.getScene("gettingDownAnimation",
							person.sceneIndexGD).endTime)) {

						person.sceneIndexGD++;

					}
				} else if (person.frameIncrement < 0) {

					while ((person.movieTimeGD >= animation.totalTimeGD
							- animation.getScene("gettingDownAnimation", person.sceneIndexGD).endTime)) {

						if (person.sceneIndexGD > 0)
							person.sceneIndexGD--;
						else
							break;

					}

				}
			}
		}
	}

	private void loadImages() {

		for (int i = 0; i < 23; i++) {

			String imageLocation = "Character\\Character" + i + ".png";
			charWalk[i] = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

			animation.loopAnimation(charWalk[i], frameTimeWalk);

		}

		for (int i = 1; i < 24; i++) {
			String imageLocation = "Character\\CharDown\\CharDown" + i + ".png";
			charDown[i - 1] = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

			animation.gettingDownAnimation(charDown[i - 1], frameTimeDown);
		}

		Enemies = new ArrayList<>();

	}

	private void talkToChar(Enemy person) {

		person.deltaX = person.getX() - white.getX() - white.getWidth() / 2;
		person.deltaY = person.getY() - white.getY() - white.getHeight() / 2;
		if (person.animationType != "gettingDownAnimation") {
			if (person.deltaX > 5 * (1 + 200 * person.walkingSpeed)) {
				person.animationType = "loopedAnimation";
				person.right = false;
				accelerate(-person.walkingSpeed, person);

			} else if (person.deltaX < -5 * (1 + 200 * person.walkingSpeed)) {
				person.animationType = "loopedAnimation";
				person.right = true;
				accelerate(person.walkingSpeed, person);
			} else {
				person.animationType = null;
				accelerate(0, person);
			}
		}
	}

	public void attackChar(int i) {
		Enemy person;

		if (i < Enemies.size())
			person = (Enemy) Enemies.get(i);
		else
			person = (Enemy) Enemies.get(i - 1);

		person.deltaX = person.getX() - white.getX() - white.getWidth() / 2;
		person.deltaY = person.getY() - white.getY() - white.getHeight() / 2;

		if (person.animationType != "gettingDownAnimation") {
			if (person.deltaX > person.walkingSpeed * 3000) {
				person.animationType = "loopedAnimation";
				person.right = false;
				accelerate(-person.walkingSpeed, person);

			} else if (person.deltaX < -person.walkingSpeed * 2000) {
				person.animationType = "loopedAnimation";
				person.right = true;
				accelerate(person.walkingSpeed, person);
			} else {
				person.animationType = null;
				accelerate(0, person);
				EnemyWeapon eWeapon = (EnemyWeapon) enemyW.EnemyWeapon.get(i);
				if (eWeapon.weaponIndex == 1) {
					Rectangle RE = getBounds(person);
					Timer t = new Timer();

					if (person.reload) {
						int centX = (int) RE.getCenterX();
						int centY = (int) RE.y + (int) RE.height / 3;

						eAmmo.bulletSize++;
						eAmmo.theta = enemyW.theta(eWeapon) + (float) Math.PI / 7 * rand.nextFloat()
								- (float) Math.PI / 14;
						eAmmo.Bullets.add(new EnemyAmmo((BufferedImage) eAmmo.getImage(eAmmo.theta),
								(float) (centX - Math.abs(person.deltaX) / person.deltaX
										* (enemyW.getHeight(eWeapon) - enemyW.getWidth(eWeapon) / 2)
										* Math.sin(eAmmo.theta)),
								(float) (centY - enemyW.getHeight(eWeapon) * Math.cos(eAmmo.theta)
										- eAmmo.Bullet.getHeight(null)),
								(float) -(eAmmo.V0 * Math.abs(person.deltaX) / person.deltaX * Math.sin(eAmmo.theta)),
								(float) (eAmmo.V0 * -Math.cos(eAmmo.theta)), (float) person.deltaX));
						person.reload = false;
						t.schedule(new TimerTask() {
							public void run() {
								try {
									person.reload = true;
									t.cancel();
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						}, rand.nextInt(1500) + 200);
					}

				}
			}
		}
	}

	// }

	public void enemyShoot() {

	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_G) {

			for (int i = 0; i < enemySize; i++) {
				Enemy person = (Enemy) Enemies.get(i);
				person.frameIncrement = 1;
				person.animationType = "gettingDownAnimation";
				accelerate(0, person);

			}

		}

		if (key == KeyEvent.VK_ENTER) {
			for (int i = 0; i < enemySize; i++) {
				Enemy person = (Enemy) Enemies.get(i);

				if (person.sentenceIndex >= 0 && person.sentenceIndex < person.dialog.size() - 1)
					person.sentenceIndex++;
			}

		}

		if (key == KeyEvent.VK_O) {
			rand = new Random();
			float randomPositionX = (float) rand.nextInt(2000) - 1000;
			int randomIndex = rand.nextInt(animation.loopedAnimation.size());
			float randomSpeed = rand.nextInt(65);
			randomSpeed = (float) (randomSpeed / 1000 + 0.05);
			int randActivityIndex = rand.nextInt(3);
			dialog = new ArrayList<>();
			dialog.add(" ");
			Enemies.add(new Enemy((float) randomPositionX, (float) 120, randomIndex, randomSpeed,
					(float) (10 * randomSpeed + 0.2), activities[randActivityIndex], dialog));
			enemySize++;

			// The enemy deserves a weapon ^^
			Enemy person = (Enemy) Enemies.get(Enemies.size() - 1);
			Rectangle RE = getBounds(person);
			int centX = (int) RE.getCenterX();
			int centY = (int) RE.y + (int) RE.height / 3;

			x = (centX - enemyW.i1.getWidth() / 2);
			y = (centY - enemyW.i1.getHeight());

			if (person.activity == "attackChar") {
				enemyW.EnemyWeapon.add(new EnemyWeapon(enemyW.i1, (int) x, (int) y, Enemies.size() - 1, 1));
			} else
				enemyW.EnemyWeapon.add(new EnemyWeapon(null, (int) x, (int) y, Enemies.size() - 1, -1));

			e.consume();

		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_G) {
			for (int i = 0; i < enemySize; i++) {
				Enemy person = (Enemy) Enemies.get(i);
				person.movieTimeGD = 0;
				person.frameIncrement = -1;
				person.animationType = "gettingDownAnimation";
				accelerate(0, person);
			}
		}
	}

	public void accelerate(float dx, Enemy person) {
		if (!accelerate) {

			person.setVelocityX(dx);

		} else {

			double deltaV = dx - person.dx;
			acc = MY * acc;
			time = 0;

			if (deltaV < 0) {
				acc = -Math.abs(acc);

				do {
					velocityX = person.getVelocityX();
					person.setVelocityX((float) (acc * time) + velocityX);
					time++;

				} while ((dx) < velocityX);

			} else if (deltaV > 0) {

				acc = Math.abs(acc);

				do {
					velocityX = person.getVelocityX();
					person.setVelocityX((float) (acc * time) + velocityX);
					time++;
				} while ((dx) > velocityX);

			}
		}
	}

	public void keyTyped(KeyEvent e) {
		e.consume();
	}

	// Get x position
	public float getX() {
		return x;
	}

	// Get y position
	public float getY() {
		return y;
	}

	// get sprite width
	public int getWidth(Enemy person) {
		return getImage(person).getWidth(null);
	}

	// get sprite height
	public int getHeight(Enemy person) {
		return getImage(person).getHeight(null);
	}

	public float getVelocityX() {
		return dx;
	}

	public float getVelocityY() {
		return dy;
	}

	// set sprite x position
	public void setX(float x) {
		this.x = x;
	}

	// set sprite y position
	public void setY(float y) {
		this.y = y;
	}

	public void setVelocityX(float dx) {
		this.dx = dx;
	}

	public void setVelocityY(float dy) {
		this.dy = dy;
	}

	public Image getImage(Enemy person) {

		if (person.animationType == "loopedAnimation")
			return animation.getImage(person.animationType, person.sceneIndexLA);
		else if (person.animationType == "gettingDownAnimation")
			return animation.getImage(person.animationType, person.sceneIndexGD);
		else {
			return charWalk[0];
		}
	}

	public Rectangle getBounds(Enemy person) {
		if (person.right) {
			return new Rectangle((int) person.getX(), (int) person.getY(), getWidth(person), getHeight(person));
		} else {
			return new Rectangle((int) person.getX() - getWidth(person), (int) person.getY(), getWidth(person),
					getHeight(person));
		}

	}

}