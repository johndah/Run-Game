import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class AntiGravity implements Serializable {

	private static final long serialVersionUID = -8804523412550393883L;
	transient private green g;
	private white white;
	transient private Image Box;
	public int x, y;
	public ArrayList<Object> magnetPlates, magnetPlatesISO, magnetPlatesControlled, boxarray;
	public int isoPeriod, width, height;
	protected String type;
	protected long time = 0;
	protected boolean antigravityActivated, antigravityActivatedInitial, moveable = true, above = false, below = false,
			left = false, right = false, inContact = false;

	public void init() {
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		magnetPlates = new ArrayList<Object>();
		magnetPlatesISO = new ArrayList<Object>();
		magnetPlatesControlled = new ArrayList<Object>();

	}

	public AntiGravity(green g, white white) {
		this.g = g;
		this.white = white;
	}

	public AntiGravity(int x, int y, String type, int isoPeriod, Boolean antigravityActivatedInitial) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("MagnetPlate.png"));
		Box = ii.getImage();
		width = Box.getWidth(null);
		height = Box.getHeight(null);
		this.x = x;
		this.y = y;
		this.type = type;
		this.isoPeriod = isoPeriod;
		this.antigravityActivatedInitial = antigravityActivatedInitial;
		this.antigravityActivated = antigravityActivatedInitial;

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

	// set sprite x position
	public void setX(int x) {
		this.x = x;
	}

	// set sprite y position
	public void setY(int y) {
		this.y = y;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());

	}

	public Rectangle field() {
		return new Rectangle((int) getX(), (int) getY() - 10000, width, 10000);

	}

	public Rectangle extendedField() {
		return new Rectangle((int) getX() - 5, (int) getY() - 50, width + 10, 50);

	}
}
