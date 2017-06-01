
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class DangerousBoxes implements Serializable {

	transient private green g = new green();
	public great great = new great();
	transient protected Image Box;
	public float x, y, nx;
	private static final long serialVersionUID = -55857686305273843L;
	public ArrayList<Object> DangerousBoxes, boxarray;
	public int width, height;
	public long t = 1;
	public long time = 0;
	public float dx = 0;
	public float dy = 0;
	protected Boolean dangerous = true, above = false, left = false, right = false, below = false;


	public void init() {
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		DangerousBoxes = new ArrayList<Object>();		
	}

	
	public void addObject(DangerousBoxes object) {
		DangerousBoxes.add(object);
	}

	public void removeObject(DangerousBoxes object) {
		DangerousBoxes.remove(object);
	}

	// Change position
	public void update(long timePassed) {
	}

	public DangerousBoxes() {

	}

	public DangerousBoxes(float x, float y) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("box666.png"));
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
