import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ground implements Serializable {

	// private static final long serialVersionUID = 2497288463143948776L;
	transient private green g = new green();
	transient private orange a = new orange();
	public great great = new great();
	transient protected Image Box;
	public float x, y, nx;
	private static final long serialVersionUID = -55857686305273843L;
	public ArrayList<Object> boxes, boxarray;
	public int width, height;
	public long t = 1;
	public long time = 0;
	public float dx = 0;
	public float dy = 0;
	private double MY = 1;
	private double gravity = 5E-2;
	private float boxVelocityX;
	private float walkingSpeed = 0.2f;
	private boolean accelerate = false;
	protected Boolean above = false, left = false, right = false, below = false;

	public int[][] pos = {};
	/*
	 * 
	 * { 650, g.getHeight() - 200 }, { 230, g.getHeight() - 80 }, { 500,
	 * g.getHeight() - 110 }, { 310, g.getHeight() - 110 }, { 370, g.getHeight()
	 * - 140 }, { 450, g.getHeight() - 170 }, { 530, g.getHeight() - 200 }
	 * 
	 */

	public void init() {
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		boxes = new ArrayList();
		positions();
		for (int i = 0; i < pos.length; i++) {
			addObject(new ground(pos[i][0], pos[i][1]));
		}

	}

	private void positions() {

	}

	public void addObject(ground object) {
		boxes.add(object);
	}

	public void removeObject(ground object) {
		boxes.remove(object);
	}

	// Change position
	public void update(long timePassed) {
	}

	public ground() {

	}

	public ground(float x, float y) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("MetalBox.png"));
		Box = ii.getImage();
		width = Box.getWidth(null);
		height = Box.getHeight(null);

		this.x = x;
		this.y = y;
	}

	public Image getImage() {
		return Box;
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
		return Box.getWidth(null);
	}

	// get sprite height
	public int getHeight() {
		return Box.getHeight(null);
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

	public Rectangle getBounds() {
		return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());

	}

	public Rectangle getBoundsExtended() {
		return new Rectangle((int) getX() - 1, (int) getY(), getWidth() + 2, getHeight());

	}
}
