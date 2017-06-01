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

public class Weapon implements MouseListener, KeyListener {

	private white white;
	private green g;
	private Camera cam;
	private AnimationWeapon animationW;
	private Belongings bel;
	private Object obj;
	private KeyTest key;
	protected BufferedImage Weapon, rotatedImage;
	protected int thetaHitIndex, weaponIndex = 1;
	protected float x, y, mX, mY;
	protected float delta, theta, timeHit;
	protected double thetaHit;
	protected ArrayList<Double> theataHitArray;
	protected BufferedImage i1, i2, i3 = null;
	protected String WeaponType, animationType = null;
	protected float johnny = 1;
	private AffineTransform tx;
	private AffineTransformOp op;
	private boolean addWeapon;
	private Rectangle RW, RM;

	public void init() {
		loadImages();
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		w.addMouseListener(this);
	}

	public Weapon(white white, green g, Camera cam, AnimationWeapon animationW, Belongings bel, Object obj,
			KeyTest key) {
		this.white = white;
		this.g = g;
		this.cam = cam;
		this.animationW = animationW;
		this.bel = bel;
		this.obj = obj;
		this.key = key;
	}

	// Change position
	public void update(long timePassed) {
		RW = white.getBounds();

		if (Weapon != null) {
			x = (float) (RW.getCenterX()
					- Math.abs(mX - RW.getCenterX()) / (mX - RW.getCenterX()) * Weapon.getWidth() / 2);
			y = (float) (RW.y + RW.height / 3 - Weapon.getHeight());
		}
		if (animationType == "hitAnimation") {
			if (timeHit > 2) {
				timeHit = 0;
				if (thetaHitIndex >= theataHitArray.size()) {
					animationType = null;
				} else {
					thetaHit = (double) theta() - theataHitArray.get(thetaHitIndex);
					thetaHitIndex++;
				}
			}
			timeHit += timePassed;
		}

	}

	private void loadImages() {
		try {

			// File gunPNG = new File("Gun.png");
			// i1 = ImageIO.read(gunPNG);

			i1 = ImageIO.read(this.getClass().getResource("Gun.png"));
			i2 = ImageIO.read(this.getClass().getResource("Sword.png"));
			i3 = null;// =
						// ImageIO.read(this.getClass().getResource("Sword.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		Weapon = null;

		theataHitArray = new ArrayList<>();
		for (double i = 0; i < Math.PI / 15; i += Math.PI / 32) {
			theataHitArray.add(i);
		}
		for (double i = Math.PI / 15; i > -Math.PI / 3; i -= Math.PI / 16) {
			theataHitArray.add(i);
		}
		for (double i = -Math.PI / 3; i < 0; i += Math.PI / 16) {
			theataHitArray.add(i);
		}
	}

	protected float theta() {
		RW = white.getBounds();
		mX = (float) MouseInfo.getPointerInfo().getLocation().getX() - cam.getX();
		mY = (float) MouseInfo.getPointerInfo().getLocation().getY() - cam.getY();

		if (mX - RW.getCenterX() != 0) {
			theta = (float) (Math.PI - Math.abs(mX - RW.getCenterX()) / (mX - RW.getCenterX())
					* (Math.atan2((mX - RW.getCenterX()), (mY - RW.getY() - Weapon.getHeight(null)))));
		} else if (mY - RW.getY() - getHeight() >= 0)
			theta = (float) 0;
		else
			theta = (float) Math.PI;

		return theta;
	}

	protected Image getImage() {
		if (Weapon != null) {
			tx = new AffineTransform();
			// AffineTransform.getQuadrantRotateInstance(1);

			if (animationType == "hitAnimation") {

				tx.rotate(Math.abs(thetaHit), Weapon.getWidth(null) / 2, Weapon.getHeight(null));
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

				rotatedImage = op.filter(Weapon, null);
			} else {
				tx.rotate(theta(), Weapon.getWidth(null) / 2, Weapon.getHeight(null));

				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				if (Weapon.getWidth(null) > 0) {
					if (!(Weapon.getWidth(null) > 0))
						System.out.println("Weapon  width is " + Weapon.getWidth(null));
					rotatedImage = op.filter(Weapon, null);
				}
			}

			return rotatedImage;
		} else
			return null;
	}

	public int getWidth() {
		if (Weapon != null) {
			if (getImage().getWidth(null) <= 0) {
				return Weapon.getWidth(null);
			} else
				return getImage().getWidth(null);

		} else
			return 0;
	}

	public int getHeight() {
		if (Weapon != null) {
			if (getImage().getHeight(null) <= 0) {
				return Weapon.getHeight(null);
			} else
				return getImage().getHeight(null);

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

		if (key == KeyEvent.VK_1) {

			for (int i = 0; i < bel.Belongings.size(); i++) {
				Belongings item = (Belongings) bel.Belongings.get(i);
				if (item.item == "Gun") {
					bel.Belongings.remove(i);
					if (WeaponType != null)
						bel.Belongings.add(new Belongings(Weapon, WeaponType, null));
					weaponIndex = 1;
					Weapon = i1;
					WeaponType = "Gun";
				}
			}
		}

		if (key == KeyEvent.VK_2) {

			for (int i = 0; i < bel.Belongings.size(); i++) {
				Belongings item = (Belongings) bel.Belongings.get(i);
				if (item.item == "Sword") {
					bel.Belongings.remove(i);
					if (WeaponType != null)
						bel.Belongings.add(new Belongings(Weapon, WeaponType, null));
					weaponIndex = 2;
					Weapon = i2;
					WeaponType = "Sword";
				}
			}
		}

		if (key == KeyEvent.VK_0) {

			addWeapon = true;

			for (int i = 0; i < bel.Belongings.size(); i++) {
				Belongings item = (Belongings) bel.Belongings.get(i);
				if (item.item == WeaponType)
					addWeapon = false;
			}
			if (addWeapon && Weapon != null)
				bel.Belongings.add(new Belongings(Weapon, WeaponType, null));

			WeaponType = null;
			weaponIndex = 0;
			Weapon = null;
		}
	}

	public void mouseClicked(MouseEvent e) {

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

		mX = (float) MouseInfo.getPointerInfo().getLocation().getX();
		mY = (float) MouseInfo.getPointerInfo().getLocation().getY();
		RM = new Rectangle((int) mX, (int) mY, 1, 1);

		for (int i = 0; i < bel.Belongings.size(); i++) {
			Belongings item = (Belongings) bel.Belongings.get(i);

			if (RM.intersects(item.getBounds())) {
				if (item.item == "Gun") {
					bel.Belongings.remove(i);
					if (WeaponType != null)
						bel.Belongings.add(new Belongings(Weapon, WeaponType, null));
					weaponIndex = 1;
					Weapon = i1;
					WeaponType = "Gun";
					break;
				} else if (item.item == "Sword") {
					bel.Belongings.remove(i);
					if (WeaponType != null)
						bel.Belongings.add(new Belongings(Weapon, WeaponType, null));
					weaponIndex = 2;
					Weapon = i2;
					WeaponType = "Sword";
					break;
				}
			}

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

	public Rectangle getBounds() {
		if (mX - getX() >= 0)
			return new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
		else
			return new Rectangle((int) getX() - getWidth(), (int) getY(), (int) getWidth(), (int) getHeight());

	}

}
