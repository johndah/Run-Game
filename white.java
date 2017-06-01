import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;

public class white implements KeyListener {

	private green gr;
	private LooseObjects lObj;
	private Background bg;
	protected orange a;
	private Checkpoints cp;
	private AntiGravity ag;
	private Dying die;
	private collisionDetector cd;

	protected float x, y, x0 = 100;
	protected float dx0 = 0, dx = 0, dy0 = 0, dy = 0;
	protected double g = 9.82E-1, gravity = g;
	protected float jumpSpeed0 = -18f, jumpSpeed = jumpSpeed0;// -17.4f;
	protected float walkingSpeed = 0.17f;// 0.15f;
	private double MY = 1;
	public boolean accelerate = true;
	protected double acc = 5e-9;
	protected int frameIncrement;
	protected String animationType = null;
	public boolean groundContact, arrowPressed, ctrlPressed, right = true;
	public float t = 1;
	private float frameSpeed = 1;
	public double movieTimeLA, movieTimeGD;
	protected int sceneIndexLA, sceneIndexGD;
	protected Image charWalk[] = new Image[23];
	protected Image charDown[] = new Image[23];
	protected long time = 0;
	protected boolean hurted;
	public int life = 100;
	public Rectangle RWplus;
	protected boolean leftKey, rightKey, shiftTabPressed;
	float yMessure = 500;

	public void init(LooseObjects lObj, AntiGravity ag, Background bg, Dying die, Checkpoints cp,
			collisionDetector cd) {
		Window w = gr.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		loadImages();
		this.lObj = lObj;
		this.ag = ag;
		this.bg = bg;
		this.die = die;
		this.cp = cp;
		this.cd = cd;

		x = x0;
		y = 500;
	}

	public white(orange a, green gr) {
		this.a = a;
		this.gr = gr;
	}

	// Change position
	public void update(long timePassed) {
	//	timePassed = 5;
	/*	if (Math.abs(dy) > 0) {
			System.out.println("y: " + y);
			System.out.println("dy: " + dy);
			System.out.println("t: " + t);
			System.out.println(" ");
			if (y < yMessure)
				yMessure = y;
		}
	 */
		x += dx * timePassed;
		y += dy;//dy*timePassed * 6.2e-2;

		if (gravity > 0)
			dy = (float) (gravity * t  + dy0);

		t += timePassed * 4.4e-2;
		//t += timePassed * 4.4e-2;
		animationUpdate(timePassed);
		keyUpdate();
	//	cd.individualsLimits();
	}

	public synchronized void animationUpdate(long timePassed) {

		if (animationType == "loopedAnimation" && a.loopedAnimation.size() > 1) {

			movieTimeLA += frameSpeed * timePassed;

			if (movieTimeLA >= a.totalTimeLA) {
				movieTimeLA = 0;
				sceneIndexLA = 0;

			}
			while (movieTimeLA > a.getScene("loopedAnimation", sceneIndexLA).endTime) {
				sceneIndexLA++;
			}
		}

		if (animationType == "gettingDownAnimation" && a.gettingDownAnimation.size() > 1) {

			movieTimeGD += timePassed;

			if (movieTimeGD >= a.totalTimeGD) {
				movieTimeGD = 0;

				if (frameIncrement > 0)
					sceneIndexGD = a.gettingDownAnimation.size() - 1;
				else if (!(sceneIndexGD == a.gettingDownAnimation.size() - 1))
					animationType = null;

				frameIncrement = 0;
			}

			if (frameIncrement > 0) {
				while ((movieTimeGD >= a.getScene("gettingDownAnimation", sceneIndexGD).endTime)) {

					sceneIndexGD++;

				}
			} else if (frameIncrement < 0) {

				while ((movieTimeGD >= a.totalTimeGD - a.getScene("gettingDownAnimation", sceneIndexGD).endTime)) {

					if (sceneIndexGD > 0)
						sceneIndexGD--;
					else
						break;

				}

			}
		}
	}

	private void loadImages() {

		a = new orange();

		for (int i = 0; i < 23; i++) {

			String imageLocation = "Character\\Character" + i + ".png";
			charWalk[i] = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

			a.loopedAnimation(charWalk[i], 30);

		}

		for (int i = 1; i < 24; i++) {
			String imageLocation = "Character\\CharDown\\CharDown" + i + ".png";
			charDown[i - 1] = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

			a.gettingDownAnimation(charDown[i - 1], 10);
		}

	}

	private void keyUpdate() {
		if (leftKey) {
			right = false;
			animationType = "loopedAnimation";
			arrowPressed = true;
			for (int j = 0; j < lObj.LooseObjects.size(); j++) {
				LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
				if (lBox.grab && !lBox.space)
					lBox.space = true;
			}
			if (gravity > 0 || groundContact && gravity == 0 && ag.moveable)
				accelerate(-walkingSpeed);

		} else if (rightKey) {
			right = true;
			animationType = "loopedAnimation";
			arrowPressed = true;
			for (int j = 0; j < lObj.LooseObjects.size(); j++) {
				LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
				if (lBox.grab && !lBox.space)
					lBox.space = true;
			}
			if (gravity > 0 || groundContact && gravity == 0 && ag.moveable)
				accelerate(walkingSpeed);
		} else {
			a.start1();
			animationType = null;
			if ((gravity > 0 || groundContact && gravity == 0))
				accelerate(0);
			arrowPressed = false;
		}

	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_CONTROL) {
			ctrlPressed = true;
		}

		if (key == KeyEvent.VK_T) {
			// setX(9205);
			// setY(420);
			setX(7600);
			setY(500 - 60);
			for (int j = 0; j < bg.backgrounds.size(); j++) {
				Background background = (Background) bg.backgrounds.get(j);
				background.setX((float) background.cx * getX());
			}
		}

		if (key == KeyEvent.VK_Y) {
			setX(12818);
			setY(-2300);
			for (int j = 0; j < bg.backgrounds.size(); j++) {
				Background background = (Background) bg.backgrounds.get(j);
				background.setX((float) background.cx * getX());
			}
		}
		if (key == KeyEvent.VK_U) {
			setX(25500);
			setY(420);
			for (int j = 0; j < bg.backgrounds.size(); j++) {
				Background background = (Background) bg.backgrounds.get(j);
				background.setX((float) background.cx * getX());
			}
		}

		if (key == KeyEvent.VK_BACK_SPACE && cp.checkPointTopIndex > 0) {
			Checkpoints place = (Checkpoints) cp.Checkpoints.get(cp.checkPointTopIndex - 1);
			setX(place.x);
			setY(place.y);
			for (int j = 0; j < bg.backgrounds.size(); j++) {
				Background background = (Background) bg.backgrounds.get(j);
				background.setX((float) background.cx * getX());
			}
		}

		if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A && !(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S))) {
			leftKey = true;
			/*
			 * right = false; animationType = "loopedAnimation"; arrowPressed =
			 * true; for (int j = 0; j < lObj.LooseObjects.size(); j++) {
			 * LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
			 * Rectangle RlO = lBox.getBounds(); if (lBox.grab && !lBox.space)
			 * lBox.space = true; } if (gravity > 0 || groundContact && gravity
			 * == 0 && ag.moveable) accelerate(-walkingSpeed);
			 * 
			 */
			e.consume();
		}

		if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D && !(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S))) {
			/*
			 * right = true; animationType = "loopedAnimation"; arrowPressed =
			 * true; for (int j = 0; j < lObj.LooseObjects.size(); j++) {
			 * LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
			 * Rectangle RlO = lBox.getBounds(); if (lBox.grab && !lBox.space)
			 * lBox.space = true; } if (gravity > 0 || groundContact && gravity
			 * == 0 && ag.moveable) accelerate(walkingSpeed); e.consume();
			 */
			rightKey = true;
		}

		if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && groundContact &! cd.fallWallContact) {
			setY(y - 2);
			dy0 = jumpSpeed;
			e.consume();
		}

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			frameIncrement = 1;
			animationType = "gettingDownAnimation";
			accelerate(0);
			e.consume();
		}

		if ((key == KeyEvent.VK_L)) {

			for (int i = 0; i < lObj.LooseObjects.size(); i++) {
				LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(i);
				Rectangle RlO = lBox.getBounds();
				RWplus = new Rectangle((int) getX() - 5, (int) getY() - 5, getWidth() + 10, getHeight() + 10);

				if (RWplus.intersects(RlO)) {
					lBox.grab = true;

					if (RlO.getCenterX() > getBounds().getCenterX())
						lBox.setX(getX() + getWidth() - 5);
					else
						lBox.setX(getX() - lBox.getWidth() + 5);

					lBox.dy = 0;
					lBox.t = 0;
					lBox.setY(getY() + getHeight() / 3 - lBox.getHeight());

				}
			}
		}

		if (key == KeyEvent.VK_PLUS) {
			// if (MY < 1E6)
			// MY = 1.3 * MY;
			// else
			accelerate = false;
			e.consume();
		}

		if (key == KeyEvent.VK_MINUS) {
			if (MY > 1) {
				accelerate = true;
				// MY = 0.7 * MY;
			}
			e.consume();

		}

		if (key == KeyEvent.VK_TAB) {
			jumpSpeed = jumpSpeed0 * 3;
			walkingSpeed = 0.6f; // 0.15
			shiftTabPressed = true;
		}
		if (key == KeyEvent.VK_SHIFT) {
			yMessure = 500;
			jumpSpeed = jumpSpeed0 * 2;
			walkingSpeed = 0.6f; // 0.15
			shiftTabPressed = true;
		}

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_CONTROL) {
			gravity = g;
			ag.moveable = true;
			// t = 0;
			ctrlPressed = false;
			for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
				AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
				magplate.antigravityActivated = false;
				magplate.inContact = false;
			}
		}

		if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_TAB) {
			jumpSpeed = jumpSpeed0;// -17.4f;
			walkingSpeed = 0.15f; // 0.15
			shiftTabPressed = false;
		}

		if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)) {
			rightKey = false;
			/*
			 * a.start1(); animationType = null; if ((gravity > 0 ||
			 * groundContact && gravity == 0)) accelerate(0); arrowPressed =
			 * false;
			 */
		}
		if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)) {
			leftKey = false;
			/*
			 * a.start1(); animationType = null; if ((gravity > 0 ||
			 * groundContact && gravity == 0)) accelerate(0); arrowPressed =
			 * false;
			 */
		}

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			movieTimeGD = 0;
			frameIncrement = -1;
			animationType = "gettingDownAnimation";
			accelerate(0);

		}

		if ((key == KeyEvent.VK_L)) {
			for (int i = 0; i < lObj.LooseObjects.size(); i++) {
				LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(i);
				lBox.grab = false;
			}
		}

		if (key == KeyEvent.VK_UP) {

		}

	}

	public void accelerate(float dx) {
		if (!accelerate) {
			setVelocityX(dx);
		} else {
			double deltaV = dx - this.dx;
			acc = MY * acc;
			time = 0;

			if (deltaV < 0) {
				acc = -Math.abs(acc);

				do {
					setVelocityX((float) (acc * time) + getVelocityX());
					time++;

				} while ((dx) < getVelocityX());

			} else if (deltaV > 0) {

				acc = Math.abs(acc);

				do {
					setVelocityX((float) (acc * time) + getVelocityX());
					time++;
				} while ((dx) > getVelocityX());

			}
		}
	}

	public void start1() {
		a.start1();
	}

	public void nullAnimation() {
		while (a.sceneIndex2 != 1) {
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
	public int getWidth() {
		return getImage().getWidth(null);
	}

	// get sprite height
	public int getHeight() {
		return getImage().getHeight(null);
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

	public Image getImage() {

		if (animationType == "loopedAnimation")
			return a.getImage(animationType, sceneIndexLA);
		else if (animationType == "gettingDownAnimation")
			return a.getImage(animationType, sceneIndexGD);
		else {
			return charWalk[0];
		}
	}

	public Rectangle getBounds() {
		if (right) {
			if (animationType == "gettingDownAnimation")
				return new Rectangle((int) getX(), (int) getY(), charDown[charDown.length - 1].getWidth(null),
						getHeight());
			else
				return new Rectangle((int) getX(), (int) getY(), a.Character0.getWidth(null),
						a.Character0.getHeight(null));
		} else {
			if (animationType == "gettingDownAnimation")
				return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());
			else
				return new Rectangle((int) getX(), (int) getY(), a.Character0.getWidth(null),
						a.Character0.getHeight(null));
		}
	}

	public Rectangle characterSurrounding() {
		return new Rectangle((int) getX() - gr.getWidth(), (int) getY() - gr.getHeight(), 2 * gr.getWidth(),
				2 * gr.getHeight());
	}

}