import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Objects {

	private green g;
	private ground ground;
	protected ArrayList<Object> Objects, objects;
	protected Image image, PieceOfPaper, displayingItemImage, Gun, Sword;
	protected String imageLocation, item;
	protected int x, y, dx, dy;

	public Objects(green g, ground ground) {
		this.g = g;
		this.ground = ground;
	}

	public Objects(Image image, int x, int y, String item, Image displayingItemImage) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.item = item;
		this.displayingItemImage = displayingItemImage;
	}

	public void init() {
		loadImages();
		Objects = new ArrayList<>();
		ArrayList<Coordinates> objectCoordinates = new ArrayList<Coordinates>();
		objectCoordinates.add(new Coordinates(2852, 482));
		objectCoordinates.add(new Coordinates(9542, g.getHeight()));
		objectCoordinates.add(new Coordinates(9540, -1969));
		objectCoordinates.add(new Coordinates(11948, -4267));
		objectCoordinates.add(new Coordinates(12050, 35));
		objectCoordinates.add(new Coordinates(16410, 481));
		objectCoordinates.add(new Coordinates(21850, g.getHeight()));
		objectCoordinates.add(new Coordinates(24800, -168));
		objectCoordinates.add(new Coordinates(-757, 232));

		for (int i = 0; i < objectCoordinates.size(); i++) {
			// imageLocation = "readingPaper" + i + ".png";

			if (i < 5)
				imageLocation = "Note" + (i + 1) + ".png";
			else
				imageLocation = "readingPaper0.png";
			
			displayingItemImage = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();
			Objects.add(new Objects(PieceOfPaper, objectCoordinates.get(i).getX(), objectCoordinates.get(i).getY(),
					"Paper", displayingItemImage));
		}

		//
		// Objects.add(new Objects(PieceOfPaper, 2852, 482, "Paper"));
		// Objects.add(new Objects(PieceOfPaper, -757, 232, "Paper"));
		// Objects.add(new Objects(PieceOfPaper, 9540, -1969, "Paper"));
		// Objects.add(new Objects(PieceOfPaper, 9542, g.getHeight() - 15,
		// "Paper"));
		// Objects.add(new Objects(PieceOfPaper, 11948, -4267, "Paper"));
		// Objects.add(new Objects(PieceOfPaper, 16410, 481, "Paper"));

		// Objects.add(new Objects(PieceOfPaper, 14965,-955, "Paper"));
		Objects.add(new Objects(Sword, 14906, -1535, "Control", null));
		// Objects.add(new Objects(Gun, 548, 240 , "Gun"));
	}

	private void loadImages() {
		imageLocation = "PieceOfPaper.png";
		PieceOfPaper = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();
		imageLocation = "Sword.png";
		Sword = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();
		imageLocation = "Gun.png";
		Gun = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, image.getWidth(null), image.getHeight(null));
	}

}
