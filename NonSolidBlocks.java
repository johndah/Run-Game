import java.awt.Image;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class NonSolidBlocks implements Serializable{

	private static final long serialVersionUID = 5768885841286547104L;
	transient protected Image NonSolidBlock;
	protected int x;
	protected int y;
	protected int dx;
	protected int dy;
	protected ArrayList<Object> nonSolidBlocks;
	transient private Image image;

	public NonSolidBlocks() {
		init();
	}

	public NonSolidBlocks(Image image, int x, int y) {
		this.setImage(image);
		this.setX(x);
		this.setY(y);
	}

	protected void init() {
		nonSolidBlocks = new ArrayList<Object>();
		loadImages();

	}

	protected void loadImages() {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("Box.png"));
		NonSolidBlock = ii.getImage();
	}

	public Rectangle getBounds() {
		return new Rectangle((int)getX(),(int) getY(), getImage().getWidth(null), getImage().getHeight(null));
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
