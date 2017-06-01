import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Ammo implements MouseListener, KeyListener {

	private white white;
	private green g;
	private Weapon weapon;
	private Camera cam;
	protected BufferedImage Bullet, images;
	protected ArrayList<Object> Bullets;
	protected float x, y, dx, dy, V0x, V0y;
	protected float V0 = 1.1f;
	protected int bulletSize = 0;
	protected int centX, centY;
	protected Random rand = new Random();
	protected float deltaX, deltaY, theta;
	BufferedImage i = null;

	public void init() {
		loadImages();
		Window w = g.getFullScreenWindow();

		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		w.addMouseListener(this);

	}

	public Ammo(white white, green g, Weapon weapon, Camera cam) {
		this.white = white;
		this.g = g;
		this.weapon = weapon;
		this.cam = cam;
	}

	public Ammo(BufferedImage images, float x, float y, float dx, float dy, float deltaX) {
		this.images = images;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.deltaX = deltaX;
	}

	// Change position
	public synchronized void update(long timePassed) {
		bulletSize = Bullets.size();
		for (int i = 0; i < Bullets.size(); i++) {
			Ammo bullet = (Ammo) Bullets.get(i);

			bullet.x += bullet.dx * timePassed;
			bullet.y += bullet.dy * timePassed;

		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public float getDeltaX() {
		return deltaX;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	protected Image getImage(float theta) {

		AffineTransform tx = new AffineTransform();

		tx.rotate(theta, Bullet.getWidth(null) / 2, Bullet.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		return op.filter((BufferedImage) Bullet, null);

	}

	/*
	 * protected int getWidth() { return getImage().getWidth(null); }
	 * 
	 * protected int getHeight() { return getImage().getHeight(null); }
	 */
	private void loadImages() {

		try {
			i = ImageIO.read(this.getClass().getResource("bullet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bullet = i;

		Bullets = new ArrayList<>();

	}

	public void mousePressed(MouseEvent e) {
		if (weapon.Weapon != null) {

			if (weapon.WeaponType == "Gun") {
				Rectangle RW = white.getBounds();

				int centX = (int) RW.getCenterX();
				int centY = (int) RW.y + (int) RW.height / 3;
				deltaX = (float) (e.getX() - cam.getX() - RW.getCenterX());
				deltaY = (float) (e.getY() - cam.getY() - (RW.y + RW.height / 3));

				bulletSize++;
				theta = weapon.theta();// + (float) Math.PI / 7 *
										// rand.nextFloat() - (float)
										// Math.PI / 14;
				Bullets.add(
						new Ammo((BufferedImage) getImage(theta),
								(float) (centX + Math.abs(deltaX) / deltaX
										* (weapon.Weapon.getHeight() - weapon.Weapon.getWidth() / 2)
										* Math.sin(weapon.theta())),
								(float) (centY - weapon.Weapon.getHeight() * Math.cos(weapon.theta())
										- Bullet.getHeight(null)),
								(float) (V0 * Math.abs(deltaX) / deltaX * Math.sin(theta)),
								(float) (V0 * -Math.cos(theta)), (float) deltaX));

			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_F) {
			if (weapon.weaponIndex == 1) {
				Rectangle RW = white.getBounds();

				int centX = (int) RW.getCenterX();
				int centY = (int) RW.y + (int) RW.height / 3;
				deltaX = (float) (MouseInfo.getPointerInfo().getLocation().getX() - cam.getX() - RW.getCenterX());
				deltaY = (float) (MouseInfo.getPointerInfo().getLocation().getY() - cam.getY()
						- (RW.y + RW.height / 3));

				bulletSize++;
				if (weapon.Weapon != null) {
					theta = weapon.theta();// + (float) Math.PI / 7 *
											// rand.nextFloat() - (float)
											// Math.PI / 14;
					Bullets.add(new Ammo((BufferedImage) getImage(theta),
							(float) (centX + Math.abs(deltaX) / deltaX
									* (weapon.Weapon.getHeight() - weapon.Weapon.getWidth() / 2)
									* Math.sin(weapon.theta())),
							(float) (centY - weapon.Weapon.getHeight() * Math.cos(weapon.theta())
									- Bullet.getHeight(null)),
							(float) (V0 * Math.abs(deltaX) / deltaX * Math.sin(theta)), (float) (V0 * -Math.cos(theta)),
							(float) deltaX));
				}
			}
		}

		if (key == KeyEvent.VK_2) {

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	public Rectangle getBounds() {
		if (getDeltaX() >= 0) {
			return new Rectangle((int) getX(), (int) getY(), images.getWidth(null), images.getHeight(null));
		} else {
			return new Rectangle((int) getX() - images.getWidth(null), (int) getY(), images.getWidth(null),
					images.getHeight(null));
		}
	}
}
