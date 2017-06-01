import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class LooseObjects implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5798736302183053639L;
	transient private green g;
	transient private white white;
	transient protected ArrayList<Object> LooseObjects;
	protected float x, y, dx, dy;
	transient protected BufferedImage image, Box, rotatedImage;
	public long t = 1;
	protected float theta = 0;;
	protected boolean inContact = false;
	private AffineTransform tx;
	private AffineTransformOp op;
	protected boolean grab, space;

	public void update(long timePassed) {

		for (int i = 0; i < LooseObjects.size(); i++) {
			LooseObjects lBox = (LooseObjects) LooseObjects.get(i);
			if (lBox.space && lBox.grab) {
				if (white.right) 
						lBox.setX(white.getX() + white.getBounds().width - 5);
					else
						lBox.setX(white.getX() - lBox.getWidth() + 5);

					lBox.dy = 0;
					lBox.t = 0;
					lBox.setY(white.getY() + white.getHeight() / 3 - lBox.getHeight());
				
			} else if (lBox.grab) {
				if (lBox.x - white.x > 0)
					lBox.setX(white.getX() + white.getBounds().width - 5);
				else
					lBox.setX(white.getX() - lBox.getWidth() + 5);

				lBox.dy = 0;
				lBox.t = 0;
				lBox.setY(white.getY() + white.getHeight() / 3 - lBox.getHeight());
			}

			// lBox.theta += 0.06;
			lBox.x += lBox.dx * timePassed;
			lBox.y += white.gravity * lBox.t + lBox.dy;
			if (Math.abs(lBox.dy) > 0.1)
				lBox.dy += white.gravity;
			lBox.t++;

		}

	}

	public LooseObjects(BufferedImage image, float x, float y, float dx, float dy) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.grab = false;
	}

	public LooseObjects(green g, white white) {
		this.g = g;
		this.white = white;
		init();
	}

	public void init() {
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		LooseObjects = new ArrayList<>();
		loadImages();

		// LooseObjects.add(new LooseObjects(Box, 50, g.getHeight() - 300, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 250, g.getHeight() - 80, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 250, g.getHeight() - 110, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 310, g.getHeight() - 140, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 310, g.getHeight() - 170, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 450, g.getHeight() - 140, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 450, g.getHeight() - 300, 0,
		// 0));
		// LooseObjects.add(new LooseObjects(Box, 450, g.getHeight() - 400, 0,
		// 0));

	}

	public void loadImages() {

		try {
			BufferedImage ii = ImageIO.read(this.getClass().getResource("Box.png"));
			// ImageIO.read(this.getClass().getResource("Sword.png"));

			Box = ii;

		} catch (IOException e) {
			e.printStackTrace();
		}
		// ImageIcon ii = new ImageIcon(this.getClass().getResource("Box.png"));
	}

	protected float theta() {
		return theta;
	}

	public Image getImage() {
		return image;
	}
	/*
	 * public Image getImage() { if (image != null) { tx = new
	 * AffineTransform(); // AffineTransform.getQuadrantRotateInstance(1);
	 * 
	 * tx.rotate(theta(), image.getWidth(null) / 2, image.getHeight(null)/ 2);
	 * 
	 * AffineTransformOp op = new AffineTransformOp(tx,
	 * AffineTransformOp.TYPE_BILINEAR); if (image.getWidth(null) > 0) { if
	 * (!(image.getWidth(null) > 0)) System.out.println("Weapon  width is " +
	 * image.getWidth(null)); rotatedImage = op.filter(image, null); }
	 * 
	 * return rotatedImage; } else return null; }
	 */

	// Get x position
	public float getX() {
		return x;
	}

	// Get y position
	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
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

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, (int) image.getWidth(null), image.getHeight(null));
	}

	public Rectangle getBoundsExtended() {
		return new Rectangle((int) x - 1, (int) y, (int) image.getWidth(null) + 2, image.getHeight(null));
	}
}
