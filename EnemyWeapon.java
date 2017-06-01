
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class EnemyWeapon implements MouseListener, KeyListener {

	private green g;
	private Camera cam;
	private AnimationWeapon animationW;
	private Enemy enemy;
	private white white;
	protected BufferedImage Weapon, rotatedImage;
	protected int thetaHitIndex, index, weaponIndex;
	protected float x, y, mX, mY;
	protected float delta, theta, timeHit;
	protected int centX, centY;
	protected double thetaHit;
	protected ArrayList<Double> theataHitArray;
	protected ArrayList<Object> EnemyWeapon;
	protected BufferedImage i1, i2, i3 = null;
	protected String animationType = null;
	protected float johnny = 1;

	public void init() {
		loadImages();
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		w.addMouseListener(this);
	}

	public EnemyWeapon(green g, AnimationWeapon animationW, Enemy enemy, white white) {
		this.g = g;
		this.animationW = animationW;
		this.enemy = enemy;
		this.white = white;

	}

	public EnemyWeapon(BufferedImage Weapon, int x, int y, int index, int weaponIndex) {
		this.Weapon = Weapon;
		this.x = x;
		this.y = y;
		this.index = index;
		this.weaponIndex = weaponIndex;
	}

	// Change position
	public void update(long timePassed) {
		for (int i = 0; i < EnemyWeapon.size(); i++) {
			EnemyWeapon eWeapon = (EnemyWeapon) EnemyWeapon.get(i);
			Enemy person = (Enemy) enemy.Enemies.get(i);

			if (eWeapon.Weapon != null) {
				Rectangle RE = enemy.getBounds(person);
				Rectangle RW = white.getBounds();

				int centX = (int) RE.getCenterX();
				int centY = (int) RE.y + (int) RE.height / 3;
				eWeapon.x = (float) (centX + Math.abs(centX - RW.getCenterX()) / (centX - RW.getCenterX()) * 6);
				eWeapon.y = (float) (centY - i1.getHeight());

			}
		}
	}

	private void loadImages() {
		try {

			// File gunPNG = new File("Gun.png");
			// i1 = ImageIO.read(gunPNG);

			i1 = ImageIO.read(this.getClass().getResource("Gun.png"));
			i2 = ImageIO.read(this.getClass().getResource("Sword.png"));
			i3 = ImageIO.read(this.getClass().getResource("Sword.png"));

			EnemyWeapon = new ArrayList<>();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected float theta(EnemyWeapon eWeapon) {
		Rectangle RW = white.getBounds();
		Enemy person = (Enemy) enemy.Enemies.get(eWeapon.index);
		Rectangle RE = enemy.getBounds(person);

		int centX = (int) RE.getCenterX();
		int centY = (int) RE.y + (int) RE.height / 3;

		if (centX - RW.getCenterX() != 0) {
			theta = (float) (Math.PI - Math.abs(centX - RW.getCenterX()) / (centX - RW.getCenterX())
					* (Math.atan2((centX - RW.getCenterX()), -(centY - RW.getY() - RW.getHeight() / 10))));
		} else if (centY - RW.getY() - eWeapon.Weapon.getHeight() >= 0)
			theta = (float) 0;
		else
			theta = (float) Math.PI;
		return theta;
	}

	protected Image getImage(EnemyWeapon eWeapon) {
		if (eWeapon.Weapon != null) {
			AffineTransform tx = new AffineTransform();
			tx.rotate(theta(eWeapon), eWeapon.Weapon.getWidth() / 2, eWeapon.Weapon.getHeight());

			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			if (eWeapon.Weapon.getWidth() > 0) {
				if (!(eWeapon.Weapon.getWidth() > 0))
					System.out.println("Weapon  width is " + eWeapon.Weapon.getWidth());

				rotatedImage = op.filter(eWeapon.Weapon, null);
			}

			return rotatedImage;
		} else
			return null;
	}

	public int getWidth(EnemyWeapon eWeapon) {
		if (eWeapon.Weapon != null) {
			return getImage(eWeapon).getWidth(null);
		} else
			return 0;
	}

	public int getHeight(EnemyWeapon eWeapon) {
		if (eWeapon.Weapon != null) {
			return getImage(eWeapon).getHeight(null);
		} else
			return 0;
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

	public void setY(float y) {
		this.y = y;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		/* if (key == KeyEvent.VK_1) {
			weaponIndex = 1;
			Weapon = i1;
		}

		if (key == KeyEvent.VK_2) {
			weaponIndex = 2;
			Weapon = i2;
		}

		if (key == KeyEvent.VK_3) {
			weaponIndex = 3;
			Weapon = i3;
		}
*/
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (weaponIndex == 3) {
			// timeHit = theta();
			thetaHitIndex = 1;
			animationType = "hitAnimation";
		}

		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public Rectangle getBounds(EnemyWeapon eWeapon) {
		if (mX - getX() >= 0)
			return new Rectangle((int) getX(), (int) getY(), (int) getWidth(eWeapon), (int) getHeight(eWeapon));
		else
			return new Rectangle((int) getX() - getWidth(eWeapon), (int) getY(), (int) getWidth(eWeapon),
					(int) getHeight(eWeapon));

	}

}
