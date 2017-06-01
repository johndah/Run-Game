import java.awt.Rectangle;
import java.awt.Window;

public class Camera {

	private float x, y;
	private white white;
	private green g;
	protected int x0;
	protected int y0;

	public Camera(white white, green g) {
		this.white = white;
		this.g = g;
		x0 = this.g.getWidth() - 450;
		y0 = 300;

		x = x0 - white.getX();
		y = y0 - white.getY();
	}

	public void update(long timePassed) {
		x = x0 - white.getX();
		y = y0 - white.getY();

	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) white.getX() - x0, (int) white.getY() - y0, (int) g.getWidth(), (int) g.getHeight());

	}

}
