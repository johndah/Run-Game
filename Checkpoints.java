import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;

import com.codingeek.serialization.SerializationUtil;

public class Checkpoints {

	private green g;
	private ground ground;
	private white white;
	private Background bg;

	protected ArrayList<Object> Checkpoints;
	protected Image image, CPImageGreen, CPImageRed;
	protected String imageLocation;
	protected int x, y, checkPointIndex, checkPointTopIndex, index;
	protected boolean checkPointReached = false;
	private Scanner scanner;

	public Checkpoints(green g, ground ground, white white, Background bg) {
		this.g = g;
		this.ground = ground;
		this.white = white;
		this.bg = bg;
	}

	public Checkpoints(Image image, int x, int y, int i) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.index = i;
	}

	public void update(long timePassed){
		for (int i = 0; i < Checkpoints.size(); i++) {
			Checkpoints place = (Checkpoints) Checkpoints.get(i);
			if (place.index + 1 <= checkPointTopIndex)
				place.image = CPImageGreen;
		}
	}

	public void init() {
		loadImages();
		Checkpoints = new ArrayList<>();
		int[][] pos = { { 100, 500 - CPImageGreen.getHeight(null) },
				{ 2650, 500 - CPImageGreen.getHeight(null) },
				{ 7660, 500 - CPImageGreen.getHeight(null) },
				{ 9205, 500 - CPImageGreen.getHeight(null) },
				{ 11400, 500 - CPImageGreen.getHeight(null) }, { 12818, -2350 },
				{ 15200, 500 - CPImageGreen.getHeight(null) },
				{ 19000, 500 - CPImageGreen.getHeight(null) },
				{ 24560, 500 - CPImageGreen.getHeight(null) },
				{ 25700, 500 - CPImageGreen.getHeight(null) },
				{ 26350, 500 - CPImageGreen.getHeight(null) },
				{ 27100, 500 - CPImageGreen.getHeight(null) }};

		for (int i = 0; i < pos.length; i++){
			Checkpoints.add(new Checkpoints(CPImageRed, pos[i][0], pos[i][1], i));
		}

		try {
			scanner = new Scanner(new File("checkPointTopIndex.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		checkPointTopIndex = scanner.nextInt();
		checkPointIndex = checkPointTopIndex;

		Checkpoints place = (Checkpoints) Checkpoints.get(checkPointTopIndex - 1);
		white.setX(place.x);
		white.setY(place.y);
		for (int j = 0; j < bg.backgrounds.size(); j++) {
			Background background = (Background) bg.backgrounds.get(j);
			background.setX((float) background.cx * white.getX());
		}
	}

	private void loadImages() {
		imageLocation = "flaggr.png";
		CPImageGreen = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();
		imageLocation = "flagre.png";
		CPImageRed = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, image.getWidth(null), image.getHeight(null));
	}

}
