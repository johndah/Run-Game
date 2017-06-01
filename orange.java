import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class orange {

	
	protected ArrayList<Object> loopedAnimation, gettingDownAnimation;
	private ArrayList<Object> skyScene;
	public int sceneIndex1;
	public int sceneIndex2;
	public int skyIndex;
	public double frameSpeed = 1;
	protected long movieTimeLA;
	protected long movieTimeGD;
	protected long skyMovieTime;
	protected long totalTimeLA;
	protected long totalTimeGD;
	private long skyTotalTime;
	public String animation = null;
	protected Image Character0;
	private int frame;
	private int skyFrame;
	public int frameInc;
	public int skyFrameInc = 0;
	great great = new great();
	KeyTest key = new KeyTest();

	public orange() {
		loopedAnimation = new ArrayList<>();
		gettingDownAnimation = new ArrayList<>();
		skyScene = new ArrayList<>();
		Character0 = new ImageIcon(this.getClass().getResource("Character\\Character0.png")).getImage();
		totalTimeLA = 0;
		totalTimeGD = 0;
		skyTotalTime = 0;
		//start1();
	}

	// Adds scenes in loopAnimation
	public synchronized void loopedAnimation(Image i, long t) {
		totalTimeLA += t;
		loopedAnimation.add(new OneScene(i, totalTimeLA));

	}

	// Adds scenes in gettingDownAnimation
	public synchronized void gettingDownAnimation(Image i, long t) {
		totalTimeGD += t;
		gettingDownAnimation.add(new OneScene(i, totalTimeGD));
	}

	// Adds scenes in skyAnimation
	public synchronized void skyAnimation(Image i, long t) {
		skyTotalTime += t;
		skyScene.add(new SkyScene(i, skyTotalTime));

	}

	public synchronized void start1() {
		movieTimeLA = 0;
		sceneIndex1 = 0;
	}

	public synchronized void start2() {
		movieTimeGD = 0;
		frame = 0;
	}

	public synchronized void startSky() {
		skyMovieTime = 0;
		skyFrame = 0;
	}

	/*public synchronized void update(long timePassed) {

		// Loop Animation
		if (animation == "loopedAnimation") {
			sceneIndex2 = 0;

			if (loopedAnimation.size() > 1) {

				movieTimeLA += frameSpeed * timePassed;
				if (movieTimeLA >= totalTimeLA)
					start1();
			}
			while (movieTimeLA > getScene(sceneIndex1).endTime) {
				sceneIndex1++;
			}

		}

		// Paused Animation
		if (animation == "gettingDownAnimation") {

			if (gettingDownAnimation.size() > 1) {
				movieTimeGD += timePassed;

				if (movieTimeGD >= totalTimeGD) {
					start2();
				}

				while (movieTimeGD > getScene( animationType,  x) .endTime) {

					if (!(sceneIndex2 >= (gettingDownAnimation.size() - 1) && frameInc > 0)
							&& !(sceneIndex2 < 2 && frameInc < 0))
						sceneIndex2 += frameInc;

					frame++;
				}

			}

		}

	}*/

	public synchronized void skyUpdate(long timePassed) {
		
		// Sky animation
		if (skyScene.size() > 1) {
			skyMovieTime += timePassed;

			if (skyMovieTime >= skyTotalTime) {
				startSky();
			}

			while (skyMovieTime > getSkyScene(skyFrame).endTime) {

				if (!(skyIndex >= (skyScene.size() - 1) && skyFrameInc > 0)
						&& !(skyIndex < 2 && skyFrameInc < 0))
					skyIndex += skyFrameInc;

				skyFrame++;
			}

		}
	}

	// get Character Image
	public synchronized Image getImage(String animationType, int x)  {
		return getScene(animationType, x).pic;
		
		/*if (loopedAnimation.size() == 0) {
			return null;
		} else if (animation == "loopedAnimation") {
			return getScene(animationType, x).pic;
		} else if (animation == "gettingDownAnimation") {
			return getScene(animationType, x).pic;
		} else {
			return Character0;
		}*/
	}

	// get Character Scene
	protected OneScene getScene(String animationType, int x) {
		if (animationType == "loopedAnimation")
			return (OneScene) loopedAnimation.get(x);
		else if (animationType == "gettingDownAnimation")
			return (OneScene) gettingDownAnimation.get(x);
		else
			return null;

	}

	// Character
	protected class OneScene {
		Image pic;
		long endTime;

		public OneScene(Image pic, long endTime) {
			this.pic = pic;
			this.endTime = endTime;
		}
	}

	// get Sky Image
	public synchronized Image getSkyImage() {
		
		if (skyScene.size() == 0) {
			return null;
		} else {
			return getSkyScene(skyIndex).pic;
		}

	}

	// get Sky Scene
	protected SkyScene getSkyScene(int x) {
		if (skyScene.size() == 0) {
			return null;
		} else {
			return (SkyScene) skyScene.get(x);
		}
	}

	// Sky
	private class SkyScene {
		Image pic;
		long endTime;

		public SkyScene(Image pic, long endTime) {
			this.pic = pic;
			this.endTime = endTime;
		}
	}
}
