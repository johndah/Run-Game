import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Animation {

	protected ArrayList<Object> loopedAnimation, gettingDownAnimation;
	protected long totalTimeLA, totalTimeGD;
	protected Image Character0 = new ImageIcon(this.getClass().getResource(
			"Character\\Character0.png")).getImage();
	public int frameInc;
	public int skyFrameInc = 0;
	great great = new great();
	KeyTest key = new KeyTest();

	public Animation() {
		loopedAnimation = new ArrayList<>();
		gettingDownAnimation = new ArrayList<>();
		totalTimeLA = 0;
		totalTimeGD = 0;

	}

	// Adds scenes in loopAnimation
	public synchronized void loopAnimation(Image i, long t) {
		totalTimeLA += t;
		loopedAnimation.add(new OneScene(i, totalTimeLA));

	}

	public synchronized void gettingDownAnimation(Image i, long t) {
		totalTimeGD += t;
		gettingDownAnimation.add(new OneScene(i, totalTimeGD));

	}

	// get Character Image
	public synchronized Image getImage(String animationType, int sceneIndex) {
			return getScene(animationType, sceneIndex).pic;

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

}
