import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class great extends JFrame {

	private static final long serialVersionUID = 7435837299270928013L;

	public static void main(String[] args) {

		great great = new great();
		great.run();
	}

	private Sound sound;
	private orange a;
	private Animation animation;
	private AnimationWeapon animationW;
	private green g;
	private ground ground;
	private NonSolidBlocks nSB;
	private LooseObjects lObj;
	private Camera cam;
	private collisionDetector cd;
	private Background bg;
	private Belongings bel;
	private Objects obj;
	private KeyTest key;
	private white white;
	private AntiGravity ag, magplate;
	private Enemy enemy;
	private Weapon weapon;
	private EnemyWeapon enemyW;
	private Effects effects;
	private Ammo ammo;
	private EnemyAmmo eAmmo;
	private CreateMap cM;
	private Checkpoints cp;
	private Dying die;
	private DangerousBoxes dB;

	public Image Box;
	private double n, m;
	private int width, height;
	private Rectangle RI, ReM, RmM, RcS;
	public Image Sky[] = new Image[41];
	private static DisplayMode modes[] = {

			new DisplayMode(800, 550, 32, 0), new DisplayMode(800, 550, 24, 0), new DisplayMode(800, 550, 16, 0),
			new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 24, 0), new DisplayMode(640, 480, 16, 0)

	};

	public void init() {

		a = new orange();
		key = new KeyTest();
		ground = new ground();
		nSB = new NonSolidBlocks();
		dB = new DangerousBoxes();
		white = new white(a, g);
		animation = new Animation();
		animationW = new AnimationWeapon();
		ag = new AntiGravity(g, white);
		enemy = new Enemy(animation, g, white);
		cam = new Camera(white, g);
		bel = new Belongings(g, ground, cam, key);
		obj = new Objects(g, ground);
		weapon = new Weapon(white, g, cam, animationW, bel, obj, key);
		enemyW = new EnemyWeapon(g, animationW, enemy, white);
		ammo = new Ammo(white, g, weapon, cam);
		eAmmo = new EnemyAmmo(white, g, enemyW, cam);
		lObj = new LooseObjects(g, white);
		sound = new Sound(g, key);
		cM = new CreateMap(g, ground, nSB, lObj, dB, ag, cam, white, bel, key, sound);
		bg = new Background(a, white, ag);
		cp = new Checkpoints(g, ground, white, bg);
		effects = new Effects(animationW, g, weapon);
		die = new Dying(white, g, cp, bg);
		cd = new collisionDetector(g, key, cam, enemy, ground, lObj, dB, white, bg, ammo, eAmmo, animationW, weapon,
				effects, bel, obj, cM, ag, sound, a, cp, die);

		white.init(lObj, ag, bg, die, cp, cd);
		g.init();
		ag.init();
		weapon.init();
		enemyW.init();
		ammo.init();
		eAmmo.init();
		enemy.init(enemyW, eAmmo);
		ground.init();
		dB.init();
		key.init(cam);
		effects.init();
		bel.init();
		obj.init();
		bg.init();
		cp.init();

	}

	// //// RUN method //////
	public void run() {
		g = new green();
		try {
			DisplayMode dm = g.findFirstCompatibleMode(modes);
			g.setFullScreen(dm);
			init();
			sound.playSound("Valkommen.wav");
			cM.loadMap();
			movieLoop();
		} finally {
			g.restoreScreen();
		}
	}

	public void movieLoop() {

		long startingTime = System.currentTimeMillis();
		long cumTime = startingTime;
		long loopTime = 0;
		while (key.running) {
			System.out.println(loopTime);
			long timePassed = System.currentTimeMillis() - cumTime;
			cumTime += timePassed;
			if (loopTime > 20) {
				update(loopTime);
				Graphics g = this.g.getGraphics();
				cd.checkCollision();
				draw(g, loopTime);
				g.dispose();
				this.g.update();
				loopTime = 0;
			} else
				loopTime += timePassed;

			try {
				// if(cM.adding)
				// Thread.sleep(20);
				// else
				Thread.sleep(2);
			} catch (Exception ex) {
				System.out.println("Error: " + ex);
			}
		}
	}

	public synchronized void draw(Graphics g, long timePassed) {

		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(Color.WHITE);

		// Draw sky
		g2D.drawImage(bg.getSkyImage(), -1, -1, this.g.getWidth() + 5, this.g.getHeight() + 1, null);

		// Begin of camera
		g2D.translate((int) cam.getX(), (int) cam.getY());

		// g2D.setColor(Color.DARK_GRAY);

		// Draw background
		for (int i = bg.backgrounds.size() - 1; i > -1; i--) {

			Background background = (Background) bg.backgrounds.get(i);

			n = Math.floor((white.getX() - background.getX()) / (background.getWidth()));
			m = Math.floor((white.getY() - background.getY()) / background.getHeight());

			for (int j = -1; j < 2; j++) {

				for (int k = -1; k < 2; k++) {

					if (i == 1 && (m + k) < 0) {

						g2D.drawImage(background.getImage(),
								(int) background.getX() + (int) n * (int) background.getWidth()
										+ j * (int) background.getWidth(),
								(int) background.getY() + (int) (m + k) * (int) background.getHeight(), this);
					} else {
						Image image = background.getImage();
						if (i == 5 && !bg.day) {
							image = background.image2;

						}

						g2D.drawImage(image, (int) background.getX() + (int) n * (int) background.getWidth()
								+ j * (int) background.getWidth(), (int) background.getY(), this);

					}
				}

				// nFloor = Math.floor((white.getX() - background.getX()) /
				// bg.CleanFloor.getWidth(null));

			}
		}
		// for (int j = -1; j < 2; j++) {
		// g2D.drawRect((int) white.getX(), 500, bg.CleanFloor.getWidth(null),
		// bg.CleanFloor.getHeight(null));
		// g2D.drawImage(bg.CleanFloor, (int) white.getX() +
		// j*bg.CleanFloor.getWidth(null), 363, this); // +

		// }

		// Draw Checkpoints
		for (int i = 0; i < cp.Checkpoints.size(); i++) {
			Checkpoints place = (Checkpoints) cp.Checkpoints.get(i);
			g2D.drawImage(place.image, (int) place.x, (int) place.y, this);
		}

		// for (int j = -1; j < 2; j++) {
		// Background background = (Background) bg.backgrounds.get(0);
		//
		// g2D.drawImage(bg.CleanFloor,
		// (int) background.getX() + (int) n * (int) background.getWidth() + j *
		// (int) background.getWidth(),
		// (int) background.getY() + (int) background.getHeight(), this);
		//
		// g2D.drawLine(
		// (int) background.getX() + (int) n * (int) background.getWidth() + j *
		// (int) background.getWidth(),
		// (int) background.getY() + (int) background.getHeight(), (int) 50 +
		// (int) background.getX()
		// + (int) n * (int) background.getWidth() + j * (int)
		// background.getWidth(),
		// (int) background.getY() + (int) background.getHeight());
		// }

		// Draw non solid blocks
		for (int i = 0; i < nSB.nonSolidBlocks.size(); i++) {
			NonSolidBlocks block = (NonSolidBlocks) nSB.nonSolidBlocks.get(i);
			g2D.drawImage(block.getImage(), (int) block.getX(), (int) block.getY(), this);
		}

		g2D.setColor(Color.WHITE);
		g2D.setFont(g2D.getFont().deriveFont(10f));

		// Draw Character
		if (white.right) {
			g2D.drawImage(white.getImage(), (int) white.getX(), (int) white.getY(), this);

		} else {
			g2D.drawImage(white.getImage(), (int) white.getX() + white.getWidth(), (int) white.getY(),
					(int) white.getX(), (int) white.getY() + white.getBounds().height, 0, 0, white.getWidth(),
					white.getBounds().height, this);

		}
		for (int i = 0; i < ag.magnetPlates.size(); i++) {
			magplate = (AntiGravity) ag.magnetPlates.get(i);
			g2D.drawImage(magplate.getImage(), magplate.x, magplate.y, this);
			g2D.setColor(new Color(0f, 1f, .0f, .2f));
			g2D.fillRect(magplate.x, magplate.y - 10000, 50, 10000);
			g2D.setColor(Color.green);
			g2D.setStroke(new BasicStroke(2));
			if (magplate.above)
				g2D.drawLine((int) magplate.x, (int) magplate.y, (int) magplate.x + magplate.width, (int) magplate.y);
			g2D.setStroke(new BasicStroke(1));
		}
		for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
			magplate = (AntiGravity) ag.magnetPlatesISO.get(i);
			g2D.drawImage(magplate.getImage(), magplate.x, magplate.y, this);
			if (magplate.antigravityActivated)
				g2D.setColor(new Color(0f, 1f, .0f, .2f));
			else
				g2D.setColor(new Color(1f, 0f, .0f, .2f));
			g2D.fillRect(magplate.x, magplate.y - 10000, 50, 10000);
			if (magplate.antigravityActivated)
				g2D.setColor(Color.green);
			else
				g2D.setColor(Color.red);
			g2D.setStroke(new BasicStroke(2));
			if (magplate.above)
				g2D.drawLine((int) magplate.x, (int) magplate.y, (int) magplate.x + magplate.width, (int) magplate.y);
			g2D.setStroke(new BasicStroke(1));
		}

		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			g2D.drawImage(magplate.getImage(), magplate.x, magplate.y, this);
			if (magplate.antigravityActivated)
				g2D.setColor(new Color(0f, 1f, .0f, .2f));
			else
				g2D.setColor(new Color(1f, 0f, .0f, .2f));
			g2D.fillRect(magplate.x, magplate.y - 10000, 50, 10000);
			if (magplate.antigravityActivated)
				g2D.setColor(Color.green);
			else
				g2D.setColor(Color.red);
			g2D.setStroke(new BasicStroke(2));
			if (magplate.above)
				g2D.drawLine((int) magplate.x, (int) magplate.y, (int) magplate.x + magplate.width, (int) magplate.y);
			g2D.setStroke(new BasicStroke(1));
		}
		// Draw Enemy
		for (int i = 0; i < enemy.enemySize; i++) {
			Enemy person = (Enemy) enemy.Enemies.get(i);
			RcS = white.characterSurrounding();

			// Draw if within characterSurrounding
			if (RcS.intersects(enemy.getBounds(person))) {

				if (person.right) {
					g2D.drawImage(enemy.getImage(person), (int) person.getX(), (int) person.getY(), this);

				} else {
					g2D.drawImage(enemy.getImage(person), (int) person.getX(), (int) person.getY(),
							-enemy.getWidth(person), enemy.getHeight(person), this);
				}
				// g2D.drawRect((int) enemy.getBounds(person).getCenterX() - 600
				// /
				// 2, (int) enemy.getBounds(person).getY(),
				// 600, 50);

				if (person.sentenceIndex >= 0) {
					g2D.drawString(person.dialog.get(person.sentenceIndex), person.getX() + 30, person.getY() - 20);
					if (person.dialog.get(person.sentenceIndex) != " ")
						g2D.drawLine((int) person.getX() + 20, (int) person.getY(), (int) person.getX() + 50,
								(int) person.getY() - 15);
				}
			}
		}

		// g2D.drawLine((int) white.getBounds().getCenterX(), (int) weapon.y +
		// weapon.Weapon.getHeight(null),
		// (int) weapon.mX, (int) weapon.mY);

		g2D.setColor(Color.CYAN);

		/*
		 * if (weapon.animationType == "hitAnimation") { if (weapon.thetaHit >=
		 * 0) { g2D.drawImage(weapon.getImage(), (int) weapon.getX(), (int)
		 * weapon.getY(), this); } else { g2D.drawImage(weapon.getImage(), (int)
		 * weapon.getX(), (int) weapon.getY(), -weapon.getWidth(),
		 * weapon.getHeight(), this); } } else
		 */

		// Draw weapon
		if (weapon.Weapon != null) {

			if ((weapon.mX - weapon.getX()) >= 0) {
				if (weapon.animationType == "hitAnimation") {
					if (weapon.thetaHit >= 0) {
						g2D.drawImage(weapon.getImage(), (int) weapon.getX(), (int) weapon.getY(), this);
					}
				} else {
					g2D.drawImage(weapon.getImage(), (int) weapon.getX(), (int) weapon.getY(), this);

				}
			} else {
				if (weapon.animationType == "hitAnimation") {
					// if (weapon.thetaHit < 0) {
					g2D.drawImage(weapon.getImage(), (int) weapon.getX(), (int) weapon.getY(), -weapon.getWidth(),
							weapon.getHeight(), this);
					// }
				}
				g2D.drawImage(weapon.getImage(), (int) weapon.getX(), (int) weapon.getY(), -weapon.getWidth(),
						weapon.getHeight(), this);

			}
		}

		// Draw EnemyWeapon
		for (int i = 0; i < enemyW.EnemyWeapon.size(); i++) {
			EnemyWeapon eWeapon = (EnemyWeapon) enemyW.EnemyWeapon.get(i);
			Enemy person = (Enemy) enemy.Enemies.get(i);

			// Draw if within characterSurrounding
			if (RcS.intersects(enemy.getBounds(person))) {

				if (eWeapon.Weapon != null) {
					if ((enemy.getBounds(person).getCenterX() - white.getBounds().getCenterX()) < 0) {
						g2D.drawImage(enemyW.getImage(eWeapon), (int) eWeapon.getX(), (int) eWeapon.getY(), this);

					} else {

						g2D.drawImage(enemyW.getImage(eWeapon), (int) eWeapon.getX(), (int) eWeapon.getY(),
								-enemyW.getWidth(eWeapon), enemyW.getHeight(eWeapon), this);

						// g2D.drawRect((int)
						// eWeapon.getX()-enemyW.getWidth(eWeapon),
						// (int) eWeapon.getY(),
						// enemyW.getWidth(eWeapon), enemyW.getHeight(eWeapon));
						g2D.setColor(Color.GREEN);

						// g2D.drawRect((int)
						// enemy.getBounds(person).getCenterX()-1,
						// (int)enemy.getBounds(person).getCenterY()-1,
						// 2, 2);
					}
				}
			}
		}

		// Draw blood
		for (int effect = 0; effect < effects.Effects.size(); effect++) {
			Effects anEffect = (Effects) effects.Effects.get(effect);

			// Draw if within characterSurrounding
			if (RcS.intersects(effects.getBounds(anEffect))) {
				g2D.drawImage(effects.getImage(anEffect), (int) anEffect.x - effects.getBounds(anEffect).width / 2,
						(int) anEffect.y - effects.getBounds(anEffect).height / 2, this);
			}

		}

		// Draw bullet
		for (

		int i = 0; i < ammo.Bullets.size(); i++) {
			Ammo bullet = (Ammo) ammo.Bullets.get(i);

			if (bullet.deltaX >= 0) {

				g2D.drawImage(bullet.images, (int) bullet.getX(), (int) bullet.getY(), this);
			} else {

				g2D.drawImage(bullet.images, (int) bullet.getX(), (int) bullet.getY(), -bullet.images.getWidth(null),
						bullet.images.getHeight(null), this);
			}

		}

		// Draw enemy bullet
		for (int i = 0; i < eAmmo.Bullets.size(); i++) {
			EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);

			if (bullet.deltaX >= 0) {

				g2D.drawImage(bullet.images, (int) bullet.getX(), (int) bullet.getY(), this);
			} else {

				g2D.drawImage(bullet.images, (int) bullet.getX(), (int) bullet.getY(), -bullet.images.getWidth(null),
						bullet.images.getHeight(null), this);
			}

		}
		// Draw boxes
		for (int i = 0; i < ground.boxes.size(); i++) {
			ground box = (ground) ground.boxes.get(i);
			g2D.drawImage(box.getImage(), (int) box.getX(), (int) box.getY(), this);
		}

		// Draw DangerousBoxes
		for (int i = 0; i < dB.DangerousBoxes.size(); i++) {
			DangerousBoxes dBox = (DangerousBoxes) dB.DangerousBoxes.get(i);
			g2D.drawImage(dBox.getImage(), (int) dBox.getX(), (int) dBox.getY(), this);
			g2D.setColor(Color.red);
			g2D.setStroke(new BasicStroke(3));
			g2D.drawRect((int) dBox.getX() + 2, (int) dBox.getY() + 2, (int) dBox.getWidth() - 4,
					(int) dBox.getHeight() - 4);
			g2D.setStroke(new BasicStroke(1));
		}

		g2D.setColor(Color.CYAN);

		// Draw CreateMap objects
		if (cM.editMap) {
			width = cM.width;
			height = cM.height;
			n = cM.nChar;
			m = cM.mChar;

			for (int j = -1; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					for (int nx = 0; nx < Math.ceil(this.g.getWidth() / 50 + 2); nx++)
						g2D.drawLine((int) n * width + cM.gridX[nx] + j * width, (int) (m + k) * height,
								(int) n * width + cM.gridX[nx] + j * width,
								cM.gridY[cM.gridY.length - 1] + (int) (m + k) * height);
					for (int ny = 0; ny < Math.ceil(this.g.getHeight() / 50 + 1); ny++)
						g2D.drawLine((int) n * width + j * width, cM.gridY[ny] + (int) (m + k) * height,
								(int) n * width + width + j * width, cM.gridY[ny] + (int) (m + k) * height);
				}
			}

			g2D.setColor(Color.BLUE);

			// Rectify grid marker
			if (n != 0)
				n = n / n;
			if (m != 0)
				m = m / m;

			cM.x = (int) Math.floor((cM.mx + n) / 50) * 50;
			cM.y = (int) Math.floor((cM.my + m) / 50) * 50;

			RmM = cM.getBoundsMenu();
			if (!key.RM.intersects(RmM))
				g2D.drawRect(cM.x, cM.y, 50, 50);

		}

		// Draw looseObjects
		for (int i = 0; i < lObj.LooseObjects.size(); i++) {
			LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(i);

			if (Math.sin(lBox.theta) >= 0)
				g2D.drawImage(lBox.getImage(), (int) lBox.x, (int) lBox.y, this);
			else {
				// lBox.theta = -Math.abs(lBox.theta); //+ (float) Math.PI;
				g2D.drawImage(lBox.getImage(), (int) lBox.x, (int) lBox.y, lBox.getWidth(), lBox.getHeight(), this);
			}
			// if (lBox.inContact)
			// g2D.drawRect((int) lBox.getBoundsExtended().x, (int)
			// lBox.getBoundsExtended().y,
			// (int) lBox.getBoundsExtended().width, 50);

		}

		// Draw objects
		for (int i = 0; i < obj.Objects.size(); i++) {
			Objects thing = (Objects) obj.Objects.get(i);
			g2D.drawImage(thing.image, (int) thing.x, (int) thing.y, this);
		}

		// // Draw floor
		// Background background = (Background) bg.backgrounds.get(0);
		//
		// n = Math.floor((white.getX() - background.getX()) /
		// (background.getWidth()));
		// m = Math.floor((white.getY() - background.getY()) /
		// background.getHeight());
		//
		// for (int j = -1; j < 2; j++) {
		// for (int k = -1; k < 2; k++) {
		// // if (m != 0)
		// // m = m / m;
		//
		// g2D.drawImage(background.getImage(),
		// (int) background.getX() + (int) n * (int) background.getWidth()
		// + j * (int) background.getWidth(),
		// (int) background.getY() + (int) (m + k) * (int)
		// background.getHeight(), this);
		// } else {
		// g2D.drawImage(background.getImage(), (int) background.getX()
		// + (int) n * (int) background.getWidth() + j * (int)
		// background.getWidth(),
		// (int) background.getY(), this);
		// }
		// }
		// g2D.drawImage(bg.CleanFloor,
		// (int) background.getX() + (int) n * (int) background.getWidth()
		// + j * (int) background.getWidth(),
		// (int) background.getY() + (int) background.getHeight(), this);
		//
		// }

		g2D.setColor(Color.WHITE);
		g2D.setFont(g2D.getFont().deriveFont(15f));
		// key.message
		int mX = (int) MouseInfo.getPointerInfo().getLocation().getX();
		int mY = (int) MouseInfo.getPointerInfo().getLocation().getY();
		g2D.drawString(((int) (mX - cam.getX()) + ", " + (int) (mY - cam.getY())), 40 - cam.getX(), 40 - cam.getY());

		// End of camera
		g2D.translate((int) -cam.getX(), (int) -cam.getY());

		// Draw belongings
		if (bel.showItems) {
			g2D.setColor(new Color(.1f, .1f, .1f, .8f));
			g2D.fillRect(bel.getBoundsMenu().x, bel.getBoundsMenu().y, bel.getBoundsMenu().width,
					bel.getBoundsMenu().height);
			g2D.setColor(Color.green);
			for (int i = 0; i < bel.Belongings.size(); i++) {
				Belongings item = (Belongings) bel.Belongings.get(i);
				g2D.drawImage(item.image, (int) item.x, (int) item.y, this);

				RI = item.getBounds();
				if (key.RM.intersects(RI)) {
					g2D.drawRect((int) item.x, (int) item.y, (int) item.image.getWidth(null),
							(int) item.image.getHeight(null));
				}

				if (item.reading) {
					g2D.drawImage(item.displayingItemImage,
							(int) this.g.getWidth() / 2 - item.displayingItemImage.getWidth(null) / 2,
							(int) this.g.getHeight() / 12, this);
				}
			}

		}

		g2D.setColor(Color.WHITE);
		g2D.setFont(g2D.getFont().deriveFont(15f));
		// key.message
		// int mX = (int) MouseInfo.getPointerInfo().getLocation().getX();
		// int mY = (int) MouseInfo.getPointerInfo().getLocation().getY();
		// g2D.drawString(((int)(mX-cam.getX()) + ", " + (int)(mY-cam.getY())),
		// 40, 40);
		// g2D.drawRect((int)(mX-(int)cam.getX()), (int)(mY-cam.getY()), 3, 3);

		// Draw message for character
		g2D.setColor(Color.WHITE);
		g2D.setFont(g2D.getFont().deriveFont(10f));
		if (cd.message != null)
			g2D.drawString(cd.message, white.x0 + 120, this.g.getHeight() - 205);

		if (key.keyCommands) {
			bel.showItems = false;
			g2D.setColor(new Color(.1f, .1f, .1f, .8f));
			g2D.fillRect((int) this.g.getWidth() - 185, (int) 0, (int) this.g.getWidth(), this.g.getHeight());

			g2D.setColor(Color.WHITE);
			for (int i = 0; i < key.keyMessage.size(); i++) {
				g2D.drawString(key.keyMessage.get(i), this.g.getWidth() - 170, 30 * i + 30);
			}
		}

		// Draw map editing features
		if (cM.editMap) {
			g2D.setColor(new Color(.1f, .1f, .1f, .8f));
			g2D.fillRect((int) this.g.getWidth() - (int) this.g.getWidth() / 8, (int) 0, (int) this.g.getWidth() / 8,
					this.g.getHeight());

			height = 10;
			for (int j = 0; j < cM.CreateMap.size(); j++) {
				g2D.drawImage(cM.objectImages[j], (int) this.g.getWidth() - (int) this.g.getWidth() / 16 - 50 / 2,
						height, this);
				height += 60;

				for (int i = 0; i < cM.CreateMap.size(); i++) {
					CreateMap mapObj = (CreateMap) cM.CreateMap.get(i);
					ReM = mapObj.getBounds();
					// Highlight if mouse intersects object
					if (i == 3) {
						g2D.setColor(Color.red);
						g2D.setStroke(new BasicStroke(3));
						g2D.drawRect((int) ReM.x + 2, (int) ReM.y + 2, (int) ReM.width - 4, (int) ReM.height - 4);
						g2D.setStroke(new BasicStroke(1));
					}
					if (i == 4) {
						g2D.setColor(Color.green);
						g2D.drawRect((int) ReM.x, (int) ReM.y, (int) ReM.width, (int) ReM.height);
					}
					if (i == 5) {
						if (cM.antigravityActivatedInitial) {
							g2D.setColor(Color.green);
							g2D.drawRect((int) ReM.x, (int) ReM.y, (int) ReM.width, (int) ReM.height);
						} else {
							g2D.setColor(Color.red);
							g2D.drawRect((int) ReM.x, (int) ReM.y, (int) ReM.width, (int) ReM.height);
						}

					}
					if (i == 6) {
						if (white.ctrlPressed) {
							g2D.setColor(Color.green);
							g2D.drawRect((int) ReM.x, (int) ReM.y, (int) ReM.width, (int) ReM.height);
						} else {
							g2D.setColor(Color.red);
							g2D.drawRect((int) ReM.x, (int) ReM.y, (int) ReM.width, (int) ReM.height);
						}

					}
					g2D.setColor(Color.BLUE);
					if (key.RM.intersects(ReM)) {
						g2D.drawRect((int) ReM.x, (int) ReM.y, (int) ReM.width, (int) ReM.height);
					}
				}
			}
		}

		g.setColor(Color.green);
		// Draw indicator square
		//if (cd.fallWallContact)
			//g2D.drawRect(50, 50, 30, 30);

		// Draw dying scene
		if (die.died) {
			g2D.setColor(Color.BLACK);
			g2D.fillRect(0, 0, this.g.getWidth(), this.g.getHeight());
			g2D.setColor(Color.WHITE);
			g2D.setFont(g2D.getFont().deriveFont(15f));
			g2D.drawString("*Dead*", (int) this.g.getWidth() / 2 - 10, (int) this.g.getHeight() / 2 - 5);

		}

		// g2D.drawString("dy = " + (int) white.dy, 100, 50);
		// g2D.drawString("yMin = " + white.yMessure, 100, 70);
		// g2D.drawString("ground Contact = " + white.groundContact, 100, 90);
		// g2D.drawString("t = " + white.t, 100, 110);
		// g2D.drawString("timePassed = " + timePassed, 100, 130);

		g.dispose();
		g2D.dispose();

	}

	public void update(long timePassed) {

		cd.update(timePassed);
		white.update(timePassed);
		bg.update(timePassed);
		enemy.update(timePassed);
		ground.update(timePassed);
		lObj.update(timePassed);
		weapon.update(timePassed);
		enemyW.update(timePassed);
		ammo.update(timePassed);
		eAmmo.update(timePassed);
		cam.update(timePassed);
		effects.update(timePassed);
		bel.update(timePassed);
		cM.update(timePassed);
		cp.update(timePassed);
		key.update();

	}
}
