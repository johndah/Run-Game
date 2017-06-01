import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Effects {

	private green g;
	private Weapon weapon;
	private AnimationWeapon animationW;
	protected float movieTimeB, movieTimeS, movieTime;
	protected long totalTime;
	protected int frameSpeed, sceneIndex;
	protected Image bloodImage, splitterImage;
	protected ArrayList<Object> Effects, Blood, Splitter;
	protected float x, y, dx, dy, V0x, V0y;
	protected float V0 = 1.1f;
	protected int bulletSize = 0;
	protected float deltaX, deltaY, theta;

	public void init() {
		loadImages();
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);

	}

	public Effects(AnimationWeapon animationW, green g, Weapon weapon) {
		this.animationW = animationW;
		this.g = g;
		this.weapon = weapon;
		Effects = new ArrayList<>();

	}

	public Effects(String animationType, float x, float y, float movieTime, int frameSpeed) {
		/*if (animationType == "bloodAnimation")
			this.Effects = Blood;
		else if (animationType == "splitterAnimation")
			this.Effects = Splitter;
		*/
		
		this.x = x;
		this.y = y;
		this.movieTime = movieTime;
		this.frameSpeed = frameSpeed;
		this.sceneIndex = 1;
	}

	// Change position
	public synchronized void update(long timePassed) {

		for (int i = 0; i < Effects.size(); i++) {
			Effects anEffect = (Effects) Effects.get(i);

			if (animationW.bloodAnimation.size() > 1) {

				anEffect.movieTime += anEffect.frameSpeed * timePassed;
				if (anEffect.movieTime >= animationW.totalTimeB) {
					Effects.remove(i);
				} else {

					while (anEffect.movieTime >= animationW.getScene("bloodAnimation", anEffect.sceneIndex).endTime) {
						anEffect.sceneIndex++;
					}
				}
			}

		}
	}

	private void loadImages() {
		Blood = new ArrayList<>();
		Splitter = new ArrayList<>();
		totalTime = 0;
		
		for (int i = 0; i < 5; i++) {
			String imageLocation = "Blood\\Blood" + (i + 1) + ".png";

			bloodImage = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();
			
			totalTime = totalTime + 40;
			Blood.add(new OneScene(bloodImage, totalTime));
			
			animationW.bloodAnimation(bloodImage, 40);
		}

		for (int i = 0; i < 4; i++) {
			String imageLocation = "Splitter\\Splitter" + (i + 1) + ".png";

			splitterImage = new ImageIcon(this.getClass().getResource(imageLocation)).getImage();

			animationW.splitterAnimation(splitterImage, 40);
		}
	}

	protected class OneScene {
		Image pic;
		long endTime;

		public OneScene(Image pic, long endTime) {
			this.pic = pic;
			this.endTime = endTime;
		}
	}

	protected Image getImage(Effects anEffect) {
		return animationW.getImage("bloodAnimation", anEffect.sceneIndex);
		//return (Image) anEffect.Effects.get(anEffect.sceneIndex);
	}

	protected int getWidth(Effects anEffect) {
		return getImage(anEffect).getWidth(null);
	}

	protected int getHeight(Effects anEffect) {
		return getImage(anEffect).getHeight(null);
	}

	public Rectangle getBounds(Effects anEffect) {

		return new Rectangle((int) anEffect.x, (int) anEffect.y, getWidth(anEffect), getHeight(anEffect));

	}

}
