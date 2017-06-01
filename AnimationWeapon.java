import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class AnimationWeapon {

	protected ArrayList<Object> hitAnimation, gettingDownAnimation, bloodAnimation, splitterAnimation;
	protected long totalTimeHA, totalTimeGD, totalTimeB, totalTimeS;
	protected Image Character0 = new ImageIcon(this.getClass().getResource("Character\\Character0.png")).getImage();
	public int frameInc;
	public int skyFrameInc = 0;
	great great = new great();
	KeyTest key = new KeyTest();

	public AnimationWeapon() {
		hitAnimation = new ArrayList<>();
		gettingDownAnimation = new ArrayList<>();
		bloodAnimation = new ArrayList<>();
		splitterAnimation = new ArrayList<>();
		totalTimeHA = 0;
		totalTimeGD = 0;
		totalTimeB = 0;
		totalTimeS = 0;
	}

	// Adds scenes in loopAnimation
	public synchronized void hitAnimation(Image i, long t) {
		totalTimeHA += t;
		hitAnimation.add(new OneScene(i, totalTimeHA));
	}

	public synchronized void gettingDownAnimation(Image i, long t) {
		totalTimeGD += t;
		gettingDownAnimation.add(new OneScene(i, totalTimeGD));
	}

	public synchronized void bloodAnimation(Image i, long t) {
		totalTimeB += t;
		bloodAnimation.add(new OneScene(i, totalTimeB));
	}

	public synchronized void splitterAnimation(Image i, long t) {
		totalTimeS += t;
		splitterAnimation.add(new OneScene(i, totalTimeS));
	}

	// get Character Image
	public synchronized Image getImage(String animationType, int sceneIndex) {
		return getScene(animationType, sceneIndex).pic;

	}

	// get Character Scene
	protected OneScene getScene(String animationType, int x) {
		if (animationType == "hitAnimation")
			return (OneScene) hitAnimation.get(x);
		else if (animationType == "gettingDownAnimation")
			return (OneScene) gettingDownAnimation.get(x);
		else if (animationType == "bloodAnimation")
			return (OneScene) bloodAnimation.get(x);
		else if (animationType == "splitterAnimation")
			return (OneScene) splitterAnimation.get(x);
		else
			return null;

	}

	protected class OneScene {
		Image pic;
		long endTime;

		public OneScene(Image pic, long endTime) {
			this.pic = pic;
			this.endTime = endTime;
		}
	}

}
