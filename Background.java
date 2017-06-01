import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Background implements KeyListener {

	protected orange a;
	private green gr = new green();
	private white white;
	private AntiGravity ag;
	protected boolean day = true;
	private Image Sky[] = new Image[41];
	protected Image BackgroundImages[] = new Image[6];
	protected Image CleanFloor;
	public ArrayList<Object> backgrounds;
	protected boolean accelerate = false;
	public float x, y;
	public float dx = 0;
	public float dy0, dy = 0;
	protected int cloudVelocity = 1;
	protected long accumulatedTime = 0;
	protected Image images, image2, City;
	public double cx0 = 6e-1;
	public double cy0 = 4e-1;
	protected double cx, cy;
	private double MY = 1;
	private float boxVelocityX;
	long time = 0;
	long t = 0;
	protected double g, gravity;
	public float pos[][] = new float[6][2];
	protected float[] heights = new float[pos.length];

	// Change position
	public void update(long timePassed) {

		for (int i = 0; i < backgrounds.size(); i++) {
			Background background = (Background) backgrounds.get(i);
			dx = white.dx;
			dy = white.dy;

			if (i != 5) {
				
				background.x = (float) background.cx * white.x;
				background.y = (float) background.cy * white.y;

			} else {
				if ((5e-3 * cloudVelocity * accumulatedTime) > background.getWidth() || cloudVelocity == 0) {
					cloudVelocity = -3 + (int) (Math.random() * ((3) + 1));
					accumulatedTime = 0;
				}
				accumulatedTime += timePassed;
				background.x = (float) background.cx * white.x;
				background.x += (float) (5e-3 * cloudVelocity * accumulatedTime);
				background.y = (float) background.cy * white.y;
			}
		}

		// if (gravity > 0)
		// dy = (float) gravity * t + dy0;
		// t++;

		a.skyUpdate(timePassed);

	}

	public void init() {

		Window w = gr.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		loadImages();
		backgrounds = new ArrayList<>();
		g = white.g;
		gravity = g;
		for (int i = 0; i < pos.length; i++) {
			backgrounds.add(new Background(getImage(), i));
		}

	}

	public Background(orange a, white white, AntiGravity ag) {

		this.a = a;
		this.white = white;
		this.ag = ag;

	}

	public Background(Image background, int i) {

		String imageLocation;

		if (i == 0)
			imageLocation = "CleanFloor.png";
		else if (i == 1)
			imageLocation = "Walls3.png";
		else if (i == 4)
			imageLocation = "mountainsBG2.png";
		else if (i == 5) {
			imageLocation = "cloud2.png";
			ImageIcon ii = new ImageIcon(this.getClass().getResource(imageLocation));
			image2 = ii.getImage();
			imageLocation = "cloud1.png";

		} else
			imageLocation = "City" + (i - 1) + ".png";

		ImageIcon ii = new ImageIcon(this.getClass().getResource(imageLocation));
		images = ii.getImage();

		this.cy = ((i - 1) + 0.1) / 3.25;
		this.cx = ((i - 1) + 0.1) / 3.25;

		this.x = (float) cx * 100;
		this.y = (float) 500 - images.getHeight(null) - 120 * (i - 1);

		if (i == 0) {
			this.cy = 0;
			this.cx = 1;
			this.x = (float) cx * 100;
			this.y = (float) 500;
		} else if (i == 4) {
			this.y = (float) 500 - images.getHeight(null) - 150 * i / 2;
		} else if (i == 5) {
			this.cy = 0.985;
			this.cx = 0.985;
		}
		pos[i][0] = x;
		pos[i][1] = y;
		this.t = 0;
	}

	private void loadImages() {

		for (int i = 1; i < 41; i++) {

			String imageLocation = "Sky\\Sky." + i + ".png";

			Sky[i] = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

			a.skyAnimation(Sky[i], 100);

		}

		for (int i = 0; i < pos.length; i++) {
			String imageLocation;
			if (i == 0)
				imageLocation = "CleanFloor.png";
			else if (i == 1)
				imageLocation = "Walls3.png";
			else if (i == 4)
				imageLocation = "mountainsBG2.png";
			else if (i == 5)
				imageLocation = "cloud1.png";
			else
				imageLocation = "City" + (i - 1) + ".png";

			ImageIcon ii = new ImageIcon(this.getClass().getResource(imageLocation));
			BackgroundImages[i] = ii.getImage();
			heights[i] = ii.getImage().getHeight(null);
		}

		ImageIcon ii = new ImageIcon(this.getClass().getResource("CleanFloor.png"));
		CleanFloor = ii.getImage();
	}

	public Image getSkyImage() {
		return a.getSkyImage();
	}

	// Keys
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_P) {
			gravity = 0;
			t = 0;
			dy0 = dy;
		}

		if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (white.gravity > 0 || dy == 0 && gravity == 0)) {
			if (white.gravity > 0 || white.groundContact && white.gravity == 0 && ag.moveable)
			//	accelerate((float) white.getVelocityX());
			e.consume();
		}

		if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (white.gravity > 0 || dy == 0 && gravity == 0)) {
			if (white.gravity > 0 || white.groundContact && white.gravity == 0 && ag.moveable)
			//	accelerate((float) white.getVelocityX());
			e.consume();
		}

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
		//	accelerate(0);
		//	dx = 0;
			e.consume();
		}

		if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy == 0) {
		//	setY(y - 2);
		//	dy0 = white.jumpSpeed;
			e.consume();
		}

		if (key == KeyEvent.VK_C) {

			if (day) {
				a.skyFrameInc = 1;
				day = false;
			} else {
				a.skyFrameInc = -1;
				day = true;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_P) {
			gravity = g;
			t = 0;
		}

		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A && (white.gravity > 0 || dy == 0 && white.gravity == 0)) {
			if (white.gravity > 0 || white.groundContact && white.gravity == 0)
				accelerate(0);
			e.consume();
		}

		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D && (white.gravity > 0 || dy == 0 && white.gravity == 0)) {
			if (white.gravity > 0 || white.groundContact && white.gravity == 0)
				accelerate(0);
			e.consume();
		}

	}

	public void accelerate(float dx) {

		if (!accelerate) {
			// for (int i = 0; i < backgrounds.size(); i++) {
			// Background background = (Background) backgrounds.get(i);
			this.dx = dx;
			// }
		} else {
			System.out.println("Background is fucking accelerating!! ");
			double deltaV = dx - this.dx;
			double acc = MY * white.acc;
			time = 0;

			if (deltaV < 0) {
				acc = -Math.abs(acc);

				do {
					for (int i = 0; i < backgrounds.size(); i++) {
						Background background = (Background) backgrounds.get(i);
						boxVelocityX = background.getVelocityX();
						background.setVelocityX((float) (acc * time) + boxVelocityX);
					}
					time++;

				} while ((dx) > boxVelocityX);

			} else if (deltaV > 0) {

				acc = Math.abs(acc);

				do {
					for (int i = 0; i < backgrounds.size(); i++) {
						Background background = (Background) backgrounds.get(i);
						boxVelocityX = background.getVelocityX();
						background.setVelocityX((float) (acc * time) + boxVelocityX);
					}
					time++;
				} while ((dx) < boxVelocityX);

			}
		}
	}

	// Get x position
	public float getX() {
		return x;
	}

	// Get y position
	public float getY() {
		return y;
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
		return images;
	}

	protected float getWidth() {
		return getImage().getWidth(null);
	}

	protected float getHeight() {
		return getImage().getHeight(null);
	}

	public void keyTyped(KeyEvent arg0) {

	}

}
