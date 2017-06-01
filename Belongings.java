import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Belongings implements KeyListener, MouseListener {

	private green g;
	private ground ground;
	private Camera cam;
	private KeyTest key;
	protected ArrayList<Object> Belongings, objects;
	protected Image image, PieceOfPaper, displayingItemImage;
	protected String imageLocation, item;
	protected int heigths;
	protected float x, y, dx, dy;
	protected boolean showItems = false, reading = false;
	protected int mX, mY;
	private Rectangle RI, RM;

	public void update(long timePassed) {
		for (int i = 0; i < Belongings.size(); i++) {
			Belongings item = (Belongings) Belongings.get(i);

			heigths = 30;
			for (int j = 0; j < i; j++) {
				Belongings items = (Belongings) Belongings.get(j);
				heigths += items.image.getHeight(null) + 30;
			}

			item.x = (int) this.g.getWidth() - (int) this.g.getWidth() / 16 - item.image.getWidth(null) / 2;
			item.y = heigths;

		}
	}

	public Belongings(green g, ground ground, Camera cam, KeyTest key) {
		this.g = g;
		this.ground = ground;
		this.cam = cam;
		this.key = key;
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addMouseListener(this);
		w.addKeyListener(this);
	}

	public Belongings(Image image, String item, Image displayingItemImage) {
		this.image = image;
		this.x = 0;
		this.y = -500;
		this.item = item;
		this.displayingItemImage = displayingItemImage;
	}

	public void init() {
		loadImages();
		Belongings = new ArrayList<>();

		// Belongings.add(new Belongings(PieceOfPaper1, 300, 200));
	}

	private void loadImages() {
		// imageLocation = "PieceOfPaper2.png";
		// PieceOfPaper = new
		// ImageIcon(this.getClass().getResource(imageLocation)).getImage();
		// PieceOfPaper1 = new
		// ImageIcon(this.getClass().getResource(imageLocation1)).getImage();

		// imageLocation = "Note1.png";
		// Notes.add(new
		// ImageIcon(this.getClass().getResource(imageLocation)).getImage());

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();

		if (KeyCode == KeyEvent.VK_I) {
			key.keyCommands = false;
			showItems = !showItems;
		}

		if (KeyCode == KeyEvent.VK_ENTER) {
			for (int i = 0; i < Belongings.size(); i++) {
				Belongings item = (Belongings) Belongings.get(i);
				item.reading = false;
			}
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
		for (int i = 0; i < Belongings.size(); i++) {
			Belongings item = (Belongings) Belongings.get(i);
			RI = item.getBounds();
			if (key.RM.intersects(RI)) {
				for (int j = 0; j < Belongings.size(); j++) {
					Belongings item2 = (Belongings) Belongings.get(i);
					item2.reading = false;
				}
				item.reading = true;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, image.getWidth(null), image.getHeight(null));
	}

	public Rectangle getBoundsMenu() {
		return new Rectangle((int) this.g.getWidth() - (int) this.g.getWidth() / 8, (int) 0,
				(int) this.g.getWidth() / 8, this.g.getHeight());
	}

}
