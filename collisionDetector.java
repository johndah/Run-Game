import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.codingeek.serialization.SerializationUtil;

public class collisionDetector {
	private green g;
	private KeyTest key;
	private ground ground;
	private LooseObjects lObj;
	private DangerousBoxes dB;
	private Camera cam;
	private white white;
	private AnimationWeapon animationW;
	private Weapon weapon;
	private Effects effects;
	private Enemy enemy;
	private Ammo ammo;
	private EnemyAmmo eAmmo;
	private Background bg;
	private Objects obj;
	private Belongings bel;
	private CreateMap cM;
	private AntiGravity ag;
	private Sound sound;
	private orange a;
	private Checkpoints cp;
	private Dying die;

	private Random rand = new Random();
	private PrintStream ps;
	private Scanner scanner;
	double width, height, width2, height2, intersecCx, intersecCy, boxCx, boxCy, boxCx2, boxCy2;
	protected int seaLevel, y0;
	protected String message;
	protected Rectangle RA, ReA, RW, RG, RO, RlO, TpT, RenterGField, RFallingBox, RpressL, RinitKeys, Rcareful,
			RctrlField, Rkeys, RaG, RaGExt, RcP, RItem, Rstuck;
	protected Boolean charAtSolidWall = false, showIndicator = false, fallWallContact = false, wallContact = false, fallingBox = true,
			keyMessage = true, enterGField = true, catchedCtrlItem = false, ctrlGranted = true;

	public collisionDetector(green g, KeyTest key, Camera cam, Enemy enemy, ground ground, LooseObjects lObj,
			DangerousBoxes dB, white white, Background bg, Ammo ammo, EnemyAmmo eAmmo, AnimationWeapon animationW,
			Weapon weapon, Effects effects, Belongings bel, Objects obj, CreateMap cM, AntiGravity ag, Sound sound,
			orange a, Checkpoints cp, Dying die) {

		this.g = g;
		this.key = key;
		this.cam = cam;
		this.enemy = enemy;
		this.ground = ground;
		this.lObj = lObj;
		this.dB = dB;
		this.white = white;
		this.bg = bg;
		this.ammo = ammo;
		this.eAmmo = eAmmo;
		this.weapon = weapon;
		this.animationW = animationW;
		this.effects = effects;
		this.bel = bel;
		this.obj = obj;
		this.cM = cM;
		this.ag = ag;
		this.sound = sound;
		this.a = a;
		this.cp = cp;
		this.die = die;
		init();
	}

	private void init() {
		message = null;
		RFallingBox = new Rectangle(1551, 150, 250, 50);
		RItem = new Rectangle(2850, 250, 50, 200);
		Rstuck = new Rectangle(2900, 300, 100, 200);
		RinitKeys = new Rectangle(0, g.getHeight() - 200, 850, 200);
		Rcareful = new Rectangle(1450, -500, 100, 200);
		RpressL = new Rectangle(2100, g.getHeight() - 200, 150, 200);
		Rkeys = new Rectangle(3000, g.getHeight() - 200, 500, 200);
		RctrlField = new Rectangle(14800, -1250, 100, 300);

	}

	public void update(long timePassed) {
		for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
			AntiGravity magField = (AntiGravity) ag.magnetPlatesISO.get(i);
			magField.time += timePassed;
			if (magField.time > magField.isoPeriod) {
				magField.antigravityActivated = !magField.antigravityActivated;
				magField.time = magField.time - magField.isoPeriod;
			}
		}
	}

	public void checkCollision() {
		showIndicator = false;
		white.groundContact = false;
		fallWallContact = false;
		seaLevel = cM.gridY[cM.gridY.length - 1] + 50;
		outsideCam();
		individualsLimits();
		collisionsBoxes();
		collisionsLooseBoxes();
		collisionDangerousBoxes();
		collisionMagplates();
		collisionEnemies();
		collisionCharacter();
		collisionCheckpoints();
		collisionLooseObjects();
		takingObjects();
		personTrigger();
		dialogTrigger();
		messageTrigger();
		objectTriggers();
		gravityTrigger();
		RctrlFieldTrigger();
		switchGravity();

		for (int i = 0; i < bg.backgrounds.size(); i++) {
			Background background = (Background) bg.backgrounds.get(i);
			background.setY((float) background.cy * white.getY() + background.pos[i][1]);
		}

	}

	private void collisionDangerousBoxes() {
		RW = white.getBounds();

		for (int j = 0; j < dB.DangerousBoxes.size(); j++) {
			DangerousBoxes box = (DangerousBoxes) dB.DangerousBoxes.get(j);
			RG = box.getBounds();

			// Bullet collision
			for (int i = 0; i < ammo.Bullets.size(); i++) {
				Ammo bullet = (Ammo) ammo.Bullets.get(i);
				RA = bullet.getBounds();

				if (RA.intersects(RG)) {
					ammo.Bullets.remove(i);
					ammo.bulletSize--;
				}
			}

			for (int i = 0; i < eAmmo.Bullets.size(); i++) {
				EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);
				ReA = bullet.getBounds();

				if (ReA.intersects(RG)) {
					eAmmo.Bullets.remove(i);
					eAmmo.bulletSize--;
				}
			}

			// Character collision
			if (RW.intersects(RG)) {

				if (box.dangerous || Math.abs(white.dx) >= die.dieVelocity)
					die.dying();

			}

			// Enemy collision
			for (int k = 0; k < enemy.enemySize; k++) {
				Enemy person = (Enemy) enemy.Enemies.get(k);
				Rectangle RE = enemy.getBounds(person);
				if (RE.intersects(RG)) {

					intersecCx = RE.intersection(RG).getCenterX(); // Mittpunktskoordinater
																	// för
																	// skärningsrektangel
					intersecCy = RE.intersection(RG).getCenterY();
					boxCx = RG.getCenterX();
					boxCy = RG.getCenterY();

					if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
						// if (box.dangerous || Math.abs(white.dx) >=
						// die.dieVelocity)
						// die.dying();

					}
				}
			}

		}

	}

	private void collisionCheckpoints() {
		RW = white.getBounds();
		for (int i = 0; i < cp.Checkpoints.size(); i++) {
			Checkpoints place = (Checkpoints) cp.Checkpoints.get(i);
			RcP = place.getBounds();
			if (RW.intersects(RcP) && i + 1 > cp.checkPointTopIndex) {
				cp.checkPointIndex = i;
				try {
					scanner = new Scanner(new File("checkPointTopIndex.txt"));
					cp.checkPointTopIndex = scanner.nextInt() + 1;
					ps = new PrintStream("checkPointTopIndex.txt");
					ps.println(String.valueOf(cp.checkPointTopIndex));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void switchGravity() {
		RW = white.getBounds();

		ag.moveable = true;
		white.gravity = white.g;

		// Declare stepping on magnetPlates
		for (int i = 0; i < ag.magnetPlates.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlates.get(i);
			RaG = new Rectangle((int) magplate.x, (int) magplate.y - 3, (int) magplate.width, 4);
			magplate.above = false;
			if (RW.intersects(RaG)) {
				magplate.above = true;
			}
		}
		for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesISO.get(i);
			RaG = new Rectangle((int) magplate.x, (int) magplate.y - 3, (int) magplate.width, 4);
			magplate.above = false;
			if (RW.intersects(RaG)) {
				magplate.above = true;
			}
		}
		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			RaG = new Rectangle((int) magplate.x, (int) magplate.y - 3, (int) magplate.width, 4);
			magplate.above = false;
			if (RW.intersects(RaG)) {
				magplate.above = true;
			}
		}

		// Remove gravity if within anti gravity field
		for (int i = 0; i < ag.magnetPlates.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlates.get(i);
			RaG = magplate.field();

			if (RW.intersects(RaG) && magplate.antigravityActivated) {

				ag.moveable = false;
				white.groundContact = false;
				white.gravity = 0;
				white.t = 0.1f;
				white.dy0 = white.dy;
				bg.gravity = 0;
				bg.t = 0;
				bg.dy0 = bg.dy;

				// if (white.dy == 0 && Math.abs(white.dx) < 1e-3
				// || Math.abs(white.y + white.getHeight() - magplate.y) == 0) {

				if (magplate.above) {
					white.groundContact = true;
					ag.moveable = true;
					white.y = magplate.y - white.getHeight();
					for (int j = 0; j < bg.backgrounds.size(); j++) {
						Background background = (Background) bg.backgrounds.get(j);
						background.setY((float) background.cy * white.getY() + background.pos[j][1]);
					}
				}
			}

		}
		for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesISO.get(i);
			RaG = magplate.field();

			if (RW.intersects(RaG) && magplate.antigravityActivated) {
				ag.moveable = false;
				white.groundContact = false;
				white.gravity = 0;
				white.t = 0.1f;
				white.dy0 = white.dy;
				bg.gravity = 0;
				bg.t = 0;
				bg.dy0 = bg.dy;

				// if (white.dy == 0 && Math.abs(white.dx) < 1e-3
				// || Math.abs(white.y + white.getHeight() - magplate.y) == 0) {
				if (magplate.above) {
					// if (magplate.antigravityActivated) {
					white.groundContact = true;
					ag.moveable = true;
					white.y = magplate.y - white.getHeight();
					for (int j = 0; j < bg.backgrounds.size(); j++) {
						Background background = (Background) bg.backgrounds.get(j);
						background.setY((float) background.cy * white.getY() + background.pos[j][1]);
					}
					// }

				}
			}
		}

		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			RaG = magplate.field();
			if (RW.intersects(RaG) && white.ctrlPressed && ctrlGranted) {
				magplate.antigravityActivated = true;
				magplate.inContact = true;
			} else {
				magplate.antigravityActivated = false;
				magplate.inContact = false;
			}
		}
		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			RaG = magplate.field();
			for (int j = 0; j < ag.magnetPlatesControlled.size(); j++) {
				AntiGravity magplate1 = (AntiGravity) ag.magnetPlatesControlled.get(j);
				RaGExt = magplate1.extendedField();
				if (RaG.intersects(RaGExt) && white.ctrlPressed && (magplate.inContact || magplate1.inContact)
						&& ctrlGranted) {
					magplate.antigravityActivated = true;
					magplate.inContact = true;
					magplate1.antigravityActivated = true;
					magplate1.inContact = true;
				}
			}
		}

		for (int i = ag.magnetPlatesControlled.size() - 1; i >= 0; i--) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			RaG = magplate.field();
			for (int j = 0; j < ag.magnetPlatesControlled.size(); j++) {
				AntiGravity magplate1 = (AntiGravity) ag.magnetPlatesControlled.get(j);
				RaGExt = magplate1.extendedField();
				if (RaG.intersects(RaGExt) && white.ctrlPressed && (magplate.inContact || magplate1.inContact)
						&& ctrlGranted) {
					magplate.antigravityActivated = true;
					magplate.inContact = true;
					magplate1.antigravityActivated = true;
					magplate1.inContact = true;
				}
			}
		}
		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			RaG = magplate.field();
			if (RW.intersects(RaG) && !ctrlGranted) {
				message = "No control";
			}
			if (RW.intersects(RaG) && magplate.antigravityActivated && ctrlGranted) {
				ag.moveable = false;
				white.groundContact = false;
				white.gravity = 0;
				white.t = 0.1f;
				white.dy0 = white.dy;
				bg.gravity = 0;
				bg.t = 0;
				bg.dy0 = bg.dy;

				// if (white.dy == 0 && Math.abs(white.dx) < 1e-3
				// || Math.abs(white.y + white.getHeight() - magplate.y) == 0) {

				if (magplate.above) {
					white.groundContact = true;
					ag.moveable = true;
					white.y = magplate.y - white.getHeight();
					for (int j = 0; j < bg.backgrounds.size(); j++) {
						Background background = (Background) bg.backgrounds.get(j);
						background.setY((float) background.cy * white.getY() + background.pos[j][1]);
					}
				}
			}

		}
	}

	private void gravityTrigger() {
		if (enterGField && ag.magnetPlates.size() > 0) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlates.get(0);
			RenterGField = magplate.field();
			RW = white.getBounds();
			if (RW.intersects(RenterGField)) {
				sound.changeSong("EntranceValkommen.wav");
				a.skyFrameInc = 1;
				bg.day = false;
				enterGField = false;
			}
		}
	}

	private void collisionMagplates() {
		RW = white.getBounds();
		for (int i = 0; i < ag.magnetPlates.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlates.get(i);
			RaG = magplate.getBounds();
			// Character collision
			if (RW.intersects(RaG)) {

				width = magplate.getImage().getWidth(null);
				height = magplate.getImage().getHeight(null);

				intersecCx = RW.intersection(RaG).getCenterX(); // Mittpunktskoordinater
				// för
				// skärningsrektangel
				intersecCy = RW.intersection(RaG).getCenterY();
				boxCx = RaG.getCenterX();
				boxCy = RaG.getCenterY();

				if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
					if (Math.abs(white.dx) >= die.dieVelocity)
						die.dying();

					charAtSolidWall = true;
					if (boxCx - intersecCx > 0) { // Left of box
						magplate.left = true;
						white.groundContact = false;
						white.setX(magplate.getX() - white.getBounds().width);
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setX((float) background.cx * white.getX());
							if (j == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}
					} else {
						// Right of box
						magplate.right = true;
						white.groundContact = false;
						white.setX(magplate.getX() + (int) magplate.width);
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setX((float) background.cx * white.getX());
							if (j == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}
						bg.dx = 0;
					}
				} else {
					if (Math.abs(white.dy) >= die.dieVelocity)
						die.dying();

					if (boxCy - intersecCy > 0) { // Above box

						if (Math.abs(boxCx - intersecCx) < magplate.width / 2 - 3)
							magplate.above = true;
						white.groundContact = true;
						white.setY(magplate.getY() - white.getBounds().height + 1);
						white.dy0 = 0;
						white.dy = 0;
						white.t = 0.1f;
						if (white.dx != 0 && white.dy != 0 && !white.arrowPressed) {
							white.start1();
							white.animationType = null;
							white.accelerate(0);
							bg.accelerate(0);
						}
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setY((float) background.cy * white.getY() + background.pos[j][1]);
						}
						bg.dy0 = 0;
						bg.t = 0;

					} else {
						// Below box
						if (Math.abs(boxCx - intersecCx) < magplate.width / 2 - 3)
							magplate.below = true;
						if (magplate.below) {
							white.setY(magplate.getY() + magplate.getImage().getHeight(null) + 2);
							white.dy = 0f;// .1f;
							white.dy0 = 0f;
							white.t = 0.1f;
							for (int j = 0; j < bg.backgrounds.size(); j++) {
								Background background = (Background) bg.backgrounds.get(j);
								background.setY((float) background.cy * white.getY() + background.pos[j][1]);
								bg.dy0 = (float) background.cy * white.dy;
							}
							bg.t = 0;
						}
					}
				}
			}

		}
		RW = white.getBounds();
		for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesISO.get(i);
			RaG = magplate.getBounds();

			// Character collision
			if (RW.intersects(RaG)) {
				width = magplate.getImage().getWidth(null);
				height = magplate.getImage().getHeight(null);

				intersecCx = RW.intersection(RaG).getCenterX(); // Mittpunktskoordinater
				// för
				// skärningsrektangel
				intersecCy = RW.intersection(RaG).getCenterY();
				boxCx = RaG.getCenterX();
				boxCy = RaG.getCenterY();

				if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
					if (Math.abs(white.dx) >= die.dieVelocity)
						die.dying();

					charAtSolidWall = true;
					if (boxCx - intersecCx > 0) { // Left of box
						magplate.left = true;
						white.groundContact = false;
						white.setX(magplate.getX() - white.getBounds().width);
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setX((float) background.cx * white.getX());
							if (j == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}

					} else {
						// Right of box
						magplate.right = true;
						white.groundContact = false;
						white.setX(magplate.getX() + (int) magplate.width);
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setX((float) background.cx * white.getX());
							if (j == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}
						bg.dx = 0;
					}

				} else {
					if (Math.abs(white.dy) >= die.dieVelocity)
						die.dying();

					if (boxCy - intersecCy > 0) { // Above box

						if (Math.abs(boxCx - intersecCx) < magplate.width / 2 - 3)
							magplate.above = true;
						white.groundContact = true;
						white.setY(magplate.getY() - white.getBounds().height + 1);
						white.dy0 = 0;
						white.dy = 0;
						white.t = 0.1f;
						if (white.dx != 0 && white.dy != 0 && !white.arrowPressed) {
							white.start1();
							white.animationType = null;
							white.accelerate(0);
							bg.accelerate(0);
						}
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setY((float) background.cy * white.getY() + background.pos[j][1]);
						}
						bg.dy0 = 0;
						bg.t = 0;

					} else {
						// Below box
						if (Math.abs(boxCx - intersecCx) < magplate.width / 2 - 3)
							magplate.below = true;
						if (magplate.below) {
							white.setY(magplate.getY() + magplate.getImage().getHeight(null) + 2);
							white.dy = 0f;// .1f;
							white.dy0 = 0f;
							white.t = 0.1f;
							for (int j = 0; j < bg.backgrounds.size(); j++) {
								Background background = (Background) bg.backgrounds.get(j);
								background.setY((float) background.cy * white.getY() + background.pos[j][1]);
								bg.dy0 = (float) background.cy * white.dy;
							}
							bg.t = 0;
						}
					}
				}
			}
		}
		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			RaG = magplate.getBounds();

			// Character collision
			if (RW.intersects(RaG)) {
				width = magplate.getImage().getWidth(null);
				height = magplate.getImage().getHeight(null);

				intersecCx = RW.intersection(RaG).getCenterX(); // Mittpunktskoordinater
				// för
				// skärningsrektangel
				intersecCy = RW.intersection(RaG).getCenterY();
				boxCx = RaG.getCenterX();
				boxCy = RaG.getCenterY();

				if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
					if (Math.abs(white.dx) >= die.dieVelocity)
						die.dying();

					charAtSolidWall = true;
					if (boxCx - intersecCx > 0) { // Left of box
						magplate.left = true;
						white.groundContact = false;
						white.setX(magplate.getX() - white.getBounds().width);
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setX((float) background.cx * white.getX());
							if (j == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}

					} else {
						// Right of box
						magplate.right = true;
						white.groundContact = false;
						white.setX(magplate.getX() + (int) magplate.width);
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setX((float) background.cx * white.getX());
							if (j == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}
						bg.dx = 0;
					}

				} else {
					if (Math.abs(white.dy) >= die.dieVelocity)
						die.dying();

					if (boxCy - intersecCy > 0) { // Above box

						if (Math.abs(boxCx - intersecCx) < magplate.width / 2 - 3)
							magplate.above = true;
						white.groundContact = true;
						white.setY(magplate.getY() - white.getBounds().height + 1);
						white.dy0 = 0;
						white.dy = 0;
						white.t = 0.1f;
						if (white.dx != 0 && white.dy != 0 && !white.arrowPressed) {
							white.start1();
							white.animationType = null;
							white.accelerate(0);
							bg.accelerate(0);
						}
						for (int j = 0; j < bg.backgrounds.size(); j++) {
							Background background = (Background) bg.backgrounds.get(j);
							background.setY((float) background.cy * white.getY() + background.pos[j][1]);
						}
						bg.dy0 = 0;
						bg.t = 0;

					} else {
						// Below box
						if (Math.abs(boxCx - intersecCx) < magplate.width / 2 - 3)
							magplate.below = true;
						if (magplate.below) {
							white.setY(magplate.getY() + magplate.getImage().getHeight(null) + 2);
							white.dy = 0f;// .1f;
							white.dy0 = 0f;
							white.t = 0.1f;
							for (int j = 0; j < bg.backgrounds.size(); j++) {
								Background background = (Background) bg.backgrounds.get(j);
								background.setY((float) background.cy * white.getY() + background.pos[j][1]);
								bg.dy0 = (float) background.cy * white.dy;
							}
							bg.t = 0;
						}
					}
				}
			}
		}
	}

	private void messageTrigger() {
		RW = white.getBounds();
		if (RW.intersects(RinitKeys) && !die.diedOnce) {
			message = "Walk with arrows or WASD";
		} else if (RW.intersects(RinitKeys) && die.diedOnce) {
			message = "Respawned at latest check point";
		} else if (RW.intersects(Rcareful)) {
			message = "Whoo easy... be careful!";
		} else if (RW.intersects(RpressL)) {
			message = "Press L to lift non-fixed objects";
		} else if (RW.intersects(RItem)) {
			if (bel.showItems)
				message = "Press H to hide/show mouse cursor";
			else
				message = "Press I to show/hide taken items and click them to read";
		} else if (RW.intersects(Rstuck)) {
			message = "If stuck, press BACK SPACE to respawn at latest checkpoint";
		} else if (RW.intersects(Rkeys)) {
			message = "Press K to show/hide key commands";
			if (keyMessage) {
				keyMessage = false;
				key.keyMessage.add("K - Show/hide key commands");
				key.keyMessage.add("M - Mute/unmute music");
				key.keyMessage.add("W,A,S,D/arrows - Jump, down, walk");
				key.keyMessage.add("L - Lift non-fixed objects");
				key.keyMessage.add("I - show/hide items");
				key.keyMessage.add("H - show/hide mouse cursor");
				key.keyMessage.add("BACKSPACE - respawn");

			}
		} else
			message = null;
	}

	private void objectTriggers() {
		RW = white.getBounds();
		Rectangle RFallingBox = new Rectangle(1551, 150, 250, 50);
		if (RW.intersects(RFallingBox) && fallingBox) {
			lObj.LooseObjects.add(new LooseObjects(lObj.Box, RW.x - 5, 300, 0, -2f));
			fallingBox = false;
		}
	}

	private void dialogTrigger() {
		RW = white.getBounds();
		for (int k = 0; k < enemy.enemySize; k++) {
			Enemy person = (Enemy) enemy.Enemies.get(k);

			int width = 400;
			int height = 600;
			TpT = new Rectangle((int) enemy.getBounds(person).getCenterX() - width / 2,
					(int) enemy.getBounds(person).getY() - height / 2, width, height);

			if (RW.intersects(TpT)) {
				if (person.sentenceIndex < 0)
					person.sentenceIndex++;
			} else
				person.sentenceIndex = -1;
		}
	}

	private void personTrigger() {
		RW = white.getBounds();
		for (int k = 0; k < enemy.enemySize; k++) {
			Enemy person = (Enemy) enemy.Enemies.get(k);

			int width = 600;
			TpT = new Rectangle((int) enemy.getBounds(person).getCenterX() - width / 2,
					(int) enemy.getBounds(person).getY(), width, 50);

			if (RW.intersects(TpT)) {
				person.active = true;
			}
		}

	}

	private void takingObjects() {
		RW = white.getBounds();

		for (int i = 0; i < obj.Objects.size(); i++) {
			Objects thing = (Objects) obj.Objects.get(i);
			RO = thing.getBounds();

			if (RW.intersects(RO)) {
				obj.Objects.remove(i);
				bel.Belongings.add(new Belongings(thing.image, thing.item, thing.displayingItemImage));
			}
		}
	}

	protected void outsideCam() {
		Rectangle RC = cam.getBounds();

		for (int i = 0; i < ammo.Bullets.size(); i++) {
			Ammo bullet = (Ammo) ammo.Bullets.get(i);
			RA = bullet.getBounds();

			if (!RA.intersects(RC)) {
				ammo.Bullets.remove(i);
				ammo.bulletSize--;
			}

		}
		for (int i = 0; i < eAmmo.Bullets.size(); i++) {
			EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);
			ReA = bullet.getBounds();

			if (!ReA.intersects(RC)) {
				eAmmo.Bullets.remove(i);
				eAmmo.bulletSize--;
			}

		}

	}

	private void RctrlFieldTrigger() {

		if (RW.intersects(RctrlField)) {
			for (int i = 0; i < bel.Belongings.size(); i++) {
				Belongings thing = (Belongings) bel.Belongings.get(i);
				if (thing.item == "Control") {
					message = "Hold Ctrl for activating anti gravity field";
				}
			}
			if (!catchedCtrlItem) {
				for (int i = 0; i < bel.Belongings.size(); i++) {
					Belongings thing = (Belongings) bel.Belongings.get(i);
					if (thing.item == "Control") {
						ctrlGranted = true;
						catchedCtrlItem = true;
						key.keyMessage.add("Ctrl - activating anti gravity field");
						break;
					}
				}
			}
		}
	}

	private void collisionCharacter() {

		for (int i = 0; i < eAmmo.Bullets.size(); i++) {
			EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);
			RW = white.getBounds();
			ReA = bullet.getBounds();

			if (ReA.intersects(RW)) {
				white.accelerate(0);
				bg.accelerate(0);
				white.hurted = true;
				effects.Effects.add(new Effects("bloodAnimation", (float) ReA.intersection(RW).getCenterX(),
						(float) ReA.intersection(RW).getCenterY(), 0, 1));
				white.life -= 11;
				eAmmo.Bullets.remove(i);
				eAmmo.bulletSize--;

				white.frameIncrement = 1;
				white.animationType = "gettingDownAnimation";
				white.accelerate(0);

				Timer hurtTimer = new Timer();
				hurtTimer.schedule(new TimerTask() {

					public void run() {
						try {
							white.movieTimeGD = 0;
							white.frameIncrement = -1;
							white.animationType = "gettingDownAnimation";
							white.accelerate(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
						hurtTimer.cancel();
						white.hurted = false;
					}

				}, rand.nextInt(100) + 10);

			}

		}

	}

	public synchronized void collisionEnemies() {
		for (int k = 0; k < enemy.enemySize; k++) {
			Enemy person = (Enemy) enemy.Enemies.get(k);
			Random randShot = new Random();
			Rectangle RE = enemy.getBounds(person);
			Rectangle RW = weapon.getBounds();

			if (!RW.intersects(RE) && weapon.weaponIndex == 2)
				person.swordHit = false;

			if (weapon.weaponIndex == 2 && RW.intersects(RE) && !person.swordHit) {
				person.swordHit = true;
				if (weapon.weaponIndex == 2) {
					effects.Effects.add(new Effects("bloodAnimation", (float) RW.intersection(RE).getCenterX(),
							(float) RW.intersection(RE).getCenterY(), 0, 1));
					// effects = new Effects((float)
					// RW.intersection(RE).getCenterX(), (float)
					// RW.intersection(RE).getCenterY(), 0, 1);
					person.life -= 10;

					person.frameIncrement = 1;
					person.animationType = "gettingDownAnimation";
					enemy.accelerate(0, person);

					if (person.life > 70) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {
								try {

									person.movieTimeGD = 0;
									person.frameIncrement = -1;
									person.animationType = "gettingDownAnimation";
									enemy.accelerate(0, person);
								} catch (Exception e) {
									e.printStackTrace();
								}
								hurtTimer.cancel();
							}

						}, randShot.nextInt(100) + 10);
					} else if (person.life > 40) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {
								try {
									person.movieTimeGD = 0;
									person.frameIncrement = -1;
									person.animationType = "gettingDownAnimation";
									enemy.accelerate(0, person);
								} catch (Exception e) {
									e.printStackTrace();
								}
								hurtTimer.cancel();
							}

						}, randShot.nextInt(200) + 50);
					} else if (person.life > 12) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {
								try {
									person.movieTimeGD = 0;
									person.frameIncrement = -1;
									person.animationType = "gettingDownAnimation";
									enemy.accelerate(0, person);
								} catch (Exception e) {
									e.printStackTrace();
								}
								hurtTimer.cancel();
							}

						}, randShot.nextInt(600) + 300);
					}

				}
			}

			for (int i = 0; i < ammo.Bullets.size(); i++) {
				Ammo bullet = (Ammo) ammo.Bullets.get(i);
				RA = bullet.getBounds();

				if (RA.intersects(RE)) {
					effects.Effects.add(new Effects("bloodAnimation", (float) RA.intersection(RE).getCenterX(),
							(float) RA.intersection(RE).getCenterY(), 0, 1));
					person.life -= 26;
					ammo.Bullets.remove(i);
					ammo.bulletSize--;

					person.frameIncrement = 1;
					person.animationType = "gettingDownAnimation";
					enemy.accelerate(0, person);

					if (person.life > 70) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {
								try {

									person.movieTimeGD = 0;
									person.frameIncrement = -1;
									person.animationType = "gettingDownAnimation";
									enemy.accelerate(0, person);
								} catch (Exception e) {
									e.printStackTrace();
								}
								hurtTimer.cancel();
							}

						}, randShot.nextInt(100) + 10);
					} else if (person.life > 40) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {
								try {
									person.movieTimeGD = 0;
									person.frameIncrement = -1;
									person.animationType = "gettingDownAnimation";
									enemy.accelerate(0, person);
								} catch (Exception e) {
									e.printStackTrace();
								}
								hurtTimer.cancel();
							}

						}, randShot.nextInt(600) + 300);
					}

				}

			}

			for (int i = 0; i < eAmmo.Bullets.size(); i++) {
				EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);
				ReA = bullet.getBounds();

				if (ReA.intersects(RE)) {
					effects.Effects.add(new Effects("bloodAnimation", (float) ReA.intersection(RE).getCenterX(),
							(float) ReA.intersection(RE).getCenterY(), 0, 1));
					person.life -= 26;
					eAmmo.Bullets.remove(i);
					eAmmo.bulletSize--;

					person.frameIncrement = 1;
					person.animationType = "gettingDownAnimation";
					enemy.accelerate(0, person);

					if (person.life > 70) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {
								try {

									person.movieTimeGD = 0;
									person.frameIncrement = -1;
									person.animationType = "gettingDownAnimation";
									enemy.accelerate(0, person);
								} catch (Exception e) {
									e.printStackTrace();
								}
								hurtTimer.cancel();
							}

						}, randShot.nextInt(100) + 10);
					} else if (person.life > 40) {
						Timer hurtTimer = new Timer();
						hurtTimer.schedule(new TimerTask() {

							public void run() {

							}

						}, randShot.nextInt(600) + 300);
					}

				}

			}

		}
	}

	public void collisionsBoxes() {
		Rectangle RW = white.getBounds();

		for (int j = 0; j < ground.boxes.size(); j++) {
			ground box = (ground) ground.boxes.get(j);
			RG = box.getBounds();

			// Bullet collision
			for (int i = 0; i < ammo.Bullets.size(); i++) {
				Ammo bullet = (Ammo) ammo.Bullets.get(i);
				RA = bullet.getBounds();

				if (RA.intersects(RG)) {
					ammo.Bullets.remove(i);
					ammo.bulletSize--;
				}
			}

			for (int i = 0; i < eAmmo.Bullets.size(); i++) {
				EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);
				ReA = bullet.getBounds();

				if (ReA.intersects(RG)) {
					eAmmo.Bullets.remove(i);
					eAmmo.bulletSize--;
				}
			}
			box.above = false;
			box.left = false;
			box.right = false;
			box.below = false;

			// Character collision
			if (RW.intersects(RG)) {

				width = box.getImage().getWidth(null);
				height = box.getImage().getHeight(null);

				intersecCx = RW.intersection(RG).getCenterX(); // Mittpunktskoordinater
																// för
																// skärningsrektangel
				intersecCy = RW.intersection(RG).getCenterY();
				boxCx = RG.getCenterX();
				boxCy = RG.getCenterY();

				if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
					if (Math.abs(white.dx) >= die.dieVelocity)
						die.dying();
					fallWallContact = true;
					charAtSolidWall = true;
					if (boxCx - intersecCx > 0) { // Left of box
						box.left = true;
						// white.dy0 = 0;
						white.groundContact = false;
						white.setX(box.getX() - white.getBounds().width);
						for (int i = 0; i < bg.backgrounds.size(); i++) {
							Background background = (Background) bg.backgrounds.get(i);
							// rectifyBackground = (float) background.getX()
							// - (float) background.cx * (float) white.getX();
							// background.setX((float) background.cx *
							// white.getX() + rectifyBackground);
							background.setX((float) background.cx * white.getX());
							if (i == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);
						}

					} else {
						// Right of box
						box.right = true;
						// white.dy0 = 0;
						white.groundContact = false;
						white.setX(box.getX() + box.getImage().getWidth(null));
						for (int i = 0; i < bg.backgrounds.size(); i++) {
							Background background = (Background) bg.backgrounds.get(i);
							// rectifyBackground = (float) background.getX()
							// - (float) background.cx * (float) white.getX();
							// background.setX((float) background.cx *
							// white.getX() + rectifyBackground);
							background.setX((float) background.cx * white.getX());
							if (i == 5)
								background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);

						}
						bg.dx = 0;

					}

				} else {// if (!(box.left || box.right)){

					wallContact = false;
					for (int k = 0; k < ground.boxes.size(); k++) {
						ground box1 = (ground) ground.boxes.get(k);
						if ((box1.left || box1.right) && !box1.above) {
							wallContact = true;
						}
					}

					if (Math.abs(white.dy) >= die.dieVelocity)
						die.dying();

					if (boxCy - intersecCy > 0) { // Above box
						if (Math.abs(boxCx - intersecCx) < box.width / 2 - 3)
							box.above = true;
						white.groundContact = true;
						// if (box.above) {
						white.setY(box.getY() - white.getBounds().height + 1);
						white.dy0 = 0;
						white.dy = 0;
						white.t = 0.1f;
						if (white.dx != 0 && white.dy != 0 && !white.arrowPressed) {
							white.start1();
							white.animationType = null;
							white.accelerate(0);
							bg.accelerate(0);
						}
						for (int i = 0; i < bg.backgrounds.size(); i++) {
							Background background = (Background) bg.backgrounds.get(i);

							background.setY((float) background.cy * white.getY() + background.pos[i][1]);
						}
						bg.dy0 = 0;
						bg.t = 0;
						// }
					} else {
						// Below box
						if (Math.abs(boxCx - intersecCx) < box.width / 2 - 3)
							box.below = true;
						if (box.below) {
							white.setY(box.getY() + box.getImage().getHeight(null) + 2);
							white.dy = 0f;// .1f;
							white.dy0 = 0f;
							white.t = 0.1f;
							for (int i = 0; i < bg.backgrounds.size(); i++) {
								Background background = (Background) bg.backgrounds.get(i);

								background.setY((float) background.cy * white.getY() + background.pos[i][1]);
								bg.dy0 = (float) background.cy * white.dy;
							}
							bg.t = 0;
						}
					}

				}

			}
			// Enemy collision
			for (int k = 0; k < enemy.enemySize; k++) {
				Enemy person = (Enemy) enemy.Enemies.get(k);
				Rectangle RE = enemy.getBounds(person);
				if (RE.intersects(RG)) {

					intersecCx = RE.intersection(RG).getCenterX(); // Mittpunktskoordinater
																	// för
																	// skärningsrektangel
					intersecCy = RE.intersection(RG).getCenterY();
					boxCx = RG.getCenterX();
					boxCy = RG.getCenterY();

					if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
						if (boxCx - intersecCx > 0) { // Left of box
							person.setX(box.getX() - enemy.getBounds(person).width);

							// Random jump

							if (rand.nextInt(50) + person.dy == 0) {
								person.setY(person.getY() - 2);
								person.setVelocityY(enemy.jumpSpeed);
							}

						} else {
							// Right of box
							person.setX(box.getX() + box.getImage().getWidth(null) + enemy.getWidth(person));

							// Random jump
							if (rand.nextInt(50) + person.dy == 0) {
								person.setY(person.getY() - 2);
								person.setVelocityY(enemy.jumpSpeed);
							}
						}

					} else {
						if (boxCy - intersecCy > 0) { // Above box
							person.setY(box.getY() - enemy.getBounds(person).height + 1);
							person.dy = 0;
							person.t = 0;

						} else {
							// Below box
							person.setY(box.getY() + box.getImage().getHeight(null) + 2);
							person.dy = 0.1f;
							person.t = 0;

						}

					}

				}
			}
		}

	}

	public void collisionsLooseBoxes() {
		Rectangle RW = white.getBounds();

		for (int j = 0; j < lObj.LooseObjects.size(); j++) {
			LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
			lBox.inContact = false;
		}

		for (int j = 0; j < lObj.LooseObjects.size(); j++) {
			LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
			Rectangle RlO = lBox.getBounds();
			Rectangle RlOEx = lBox.getBoundsExtended();

			width = lBox.getImage().getWidth(null);
			height = lBox.getImage().getHeight(null);
			boxCx = RlO.getCenterX();
			boxCy = RlO.getCenterY();

			for (int i = 0; i < ground.boxes.size(); i++) {
				ground box = (ground) ground.boxes.get(i);
				RG = box.getBounds();

				if (RlO.intersects(RG)) {
					width2 = RG.width;
					height2 = RG.height;

					boxCx2 = RG.getCenterX();
					boxCy2 = RG.getCenterY();
					intersecCx = RlO.intersection(RG).getCenterX();
					intersecCy = RlO.intersection(RG).getCenterY();

					if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
						if (boxCx - intersecCx > 0) { // Right ground
							lBox.setX(box.getX() + lBox.getWidth());
						} else { // Left ground
							lBox.setX(box.getX() - lBox.getWidth());
						}

					} else {
						if (boxCy - intersecCy > 0) { // Below box2
							if (Math.abs(white.dy) > 0)
								lBox.grab = false;

							if (RlO.intersects(RW)) {
								lBox.y = white.getY() - (float) height;
								lBox.setX(box.getX() - lBox.getWidth());
								// white.setY(lBox.y + (float)height + 2);
								// white.dy = 0.1f;
								// white.t = 0.1f;
							} else {
								lBox.setY(box.getY() + RlO.getBounds().height + 2);
								lBox.dy = 0;
								lBox.t = 0;
							}

						} else {
							// Above Box2
							// if (Math.abs(white.dy) > 0)
							// lBox.grab = false;

							lBox.setY(box.getY() - lBox.getHeight() + 1);
							lBox.dy = 0.1f;
							lBox.t = 0;

						}

					}

				}

			}

			for (int k = 0; k < ground.boxes.size(); k++) {
				ground boxEx = (ground) ground.boxes.get(k);
				Rectangle RGEx = boxEx.getBoundsExtended();
				boxCx = RlO.getCenterX();
				boxCy = RlO.getCenterY();
				intersecCx = RlO.intersection(RGEx).getCenterX();
				intersecCy = RlO.intersection(RGEx).getCenterY();

				if (RlO.intersects(RGEx) && Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
					lBox.inContact = true;
					lBox.space = false;
					if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
						if (boxCx - intersecCx > 0) // Right of box2
							lBox.setX(boxEx.getX() + lBox.getWidth());
						else // Left of box2
							lBox.setX(boxEx.getX() - lBox.getWidth());
					}
					if (lBox.grab) {
						if (white.right)
							lBox.setX(white.getX() - lBox.getWidth() + 5);
						else
							lBox.setX(white.getX() + white.getBounds().width - 5);
					}
				}
			}

			// LooseBox collision
			for (int i = lObj.LooseObjects.size() - 1; i >= 0; i--) {
				LooseObjects lBox2 = (LooseObjects) lObj.LooseObjects.get(i);
				Rectangle RlO2Ex = lBox2.getBoundsExtended();

				boxCx2 = RlO2Ex.getCenterX();
				boxCy2 = RlO2Ex.getCenterY();
				intersecCx = RlO.intersection(RlO2Ex).getCenterX();
				intersecCy = RlO.intersection(RlO2Ex).getCenterY();

				if (RlOEx.intersects(RlO2Ex) && Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy) && i != j) {
					if (lBox2.inContact || lBox.inContact) {
						lBox.inContact = true;
						lBox2.inContact = true;
					}
				}
			}
		}

		for (int j = 0; j < lObj.LooseObjects.size(); j++) {
			LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(j);
			Rectangle RlO = lBox.getBounds();
			Rectangle RlOEx = lBox.getBoundsExtended();

			// Bullet collision
			for (int i = 0; i < ammo.Bullets.size(); i++) {
				Ammo bullet = (Ammo) ammo.Bullets.get(i);
				RA = bullet.getBounds();

				if (RA.intersects(RlO)) {
					ammo.Bullets.remove(i);
					ammo.bulletSize--;
				}
			}

			width = lBox.getImage().getWidth(null);
			height = lBox.getImage().getHeight(null);
			boxCx = RlO.getCenterX();
			boxCy = RlO.getCenterY();

			for (int i = 0; i < lObj.LooseObjects.size(); i++) {
				LooseObjects lBox2 = (LooseObjects) lObj.LooseObjects.get(i);
				Rectangle RlO2Ex = lBox2.getBoundsExtended();

				boxCx2 = RlO2Ex.getCenterX();
				boxCy2 = RlO2Ex.getCenterY();
				intersecCx = RlO.intersection(RlO2Ex).getCenterX();
				intersecCy = RlO.intersection(RlO2Ex).getCenterY();

				if (RlOEx.intersects(RlO2Ex) && Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy) && i != j) {
					if (lBox2.inContact || lBox.inContact) {
						lBox.inContact = true;
						lBox2.inContact = true;
					}
				}
			}
			// LooseBox collision
			for (int i = 0; i < lObj.LooseObjects.size(); i++) {
				LooseObjects lBox2 = (LooseObjects) lObj.LooseObjects.get(i);
				Rectangle RlO2 = lBox2.getBounds();

				if (RlO.intersects(RlO2) && i != j) {

					width2 = lBox2.getImage().getWidth(null);
					height2 = lBox2.getImage().getHeight(null);

					boxCx2 = RlO2.getCenterX();
					boxCy2 = RlO2.getCenterY();
					intersecCx = RlO.intersection(RlO2).getCenterX();
					intersecCy = RlO.intersection(RlO2).getCenterY();

					if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
						if (boxCx - intersecCx > 0) { // Right of box2
							if (!lBox.inContact)
								lBox.setX(lBox2.getX() + lBox.getWidth());

						} else {
							// Left of box2
							if (!lBox.inContact)
								lBox.setX(lBox2.getX() - lBox.getWidth());
						}

					} else {
						if (boxCy - intersecCy > 0) { // Below box2
							if (Math.abs(white.dy) > 0)
								lBox.grab = false;
							lBox.setY(lBox2.getY() + RlO.getBounds().height + 2);
							lBox.dy = 0;
							// lBox.t = 0;

						} else {
							// Above Box2
							// if (Math.abs(white.dy) > 0)
							// lBox.grab = false;
							lBox.setY(lBox2.getY() - lBox.getHeight() + 1);
							lBox.dy = 0.1f;
							lBox.t = 0;

						}

					}

				}
			}

			// Enemy collision with loose boxes
			for (int k = 0; k < enemy.enemySize; k++) {
				Enemy person = (Enemy) enemy.Enemies.get(k);
				Rectangle RE = enemy.getBounds(person);
				if (RE.intersects(RlO)) {

					intersecCx = RE.intersection(RlO).getCenterX(); // Mittpunktskoordinater
																	// för
																	// skärningsrektangel
					intersecCy = RE.intersection(RlO).getCenterY();
					boxCx = RlO.getCenterX();
					boxCy = RlO.getCenterY();

					if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
						if (boxCx - intersecCx > 0) { // Left of box
							person.setX(lBox.getX() - enemy.getWidth(person));

							// Random jump

							if (rand.nextInt(50) + person.dy == 0) {
								person.setY(person.getY() - 2);
								person.setVelocityY(enemy.jumpSpeed);
							}

						} else {
							// Right of box
							person.setX(lBox.getX() + lBox.getImage().getWidth(null) + enemy.getWidth(person));

							// Random jump
							if (rand.nextInt(50) + person.dy == 0) {
								person.setY(person.getY() - 2);
								person.setVelocityY(enemy.jumpSpeed);
							}
						}

					} else {
						if (boxCy - intersecCy > 0) { // Above box
							person.setY(lBox.getY() - enemy.getBounds(person).height);
							person.dy = 0;
							person.t = 0;

						} else {
							// Below box
							person.setY(lBox.getY() + lBox.getImage().getHeight(null));
							person.dy = 0.1f;
							person.t = 0;

						}

					}

				}
			}

			for (int i = 0; i < eAmmo.Bullets.size(); i++) {
				EnemyAmmo bullet = (EnemyAmmo) eAmmo.Bullets.get(i);
				ReA = bullet.getBounds();

				if (ReA.intersects(RlO)) {
					eAmmo.Bullets.remove(i);
					eAmmo.bulletSize--;
				}
			}

			// Character collision
			if (RW.intersects(RlO) && !lBox.grab) {

				if (lBox.inContact && Math.sqrt(Math.pow(white.dx, 2) + Math.pow(white.dy, 2)) >= die.dieVelocity)
					die.dying();

				width = lBox.getImage().getWidth(null);
				height = lBox.getImage().getHeight(null);

				intersecCx = RW.intersection(RlO).getCenterX(); // Mittpunktskoordinater
																// för
																// skärningsrektangel
				intersecCy = RW.intersection(RlO).getCenterY();
				boxCx = RlO.getCenterX();
				boxCy = RlO.getCenterY();

				if (Math.abs(boxCx - intersecCx) > Math.abs(boxCy - intersecCy)) {
					if (boxCx - intersecCx > 0) { // Left of looseBox
						if (!lBox.inContact)
							lBox.setX(white.getX() + white.getBounds().width);
						else {
							charAtSolidWall = true;
							white.setX(lBox.x - RW.width);
							white.dx = 0;
							white.groundContact = false;
							for (int k = 0; k < bg.backgrounds.size(); k++) {
								Background background = (Background) bg.backgrounds.get(k);
								// rectifyBackground = (float) background.getX()
								// - (float) background.cx * (float)
								// white.getX();
								background.setX((float) background.cx * white.getX());
								if (k == 5)
									background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);

							}
							bg.dx = 0;
						}

					} else {
						// Right of looseBox
						if (!lBox.inContact)
							lBox.setX(white.getX() - lBox.getImage().getWidth(null));
						else {
							charAtSolidWall = true;
							white.setX(lBox.x + (float) width);
							white.dx = 0;
							white.groundContact = false;
							for (int k = 0; k < bg.backgrounds.size(); k++) {
								Background background = (Background) bg.backgrounds.get(k);
								// rectifyBackground = (float) background.getX()
								// - (float) background.cx * (float)
								// white.getX();
								background.setX((float) background.cx * white.getX());
								if (k == 5)
									background.x += (float) (5e-3 * bg.cloudVelocity * bg.accumulatedTime);

							}
							bg.dx = 0;
						}

					}

				} else {
					if (boxCy - intersecCy > 0) { // Above box
						white.groundContact = true;
						white.setY(lBox.getY() - white.getBounds().height + 1);
						white.dy0 = 0;
						white.t = 0.1f;
						if (white.dx != 0 && white.dy != 0 && !white.arrowPressed) {
							white.start1();
							white.animationType = null;
							white.accelerate(0);
							bg.accelerate(0);
						}

						for (int i = 0; i < bg.backgrounds.size(); i++) {
							Background background = (Background) bg.backgrounds.get(i);
							background.setY((float) background.cy * white.getY() + background.pos[i][1]);
						}
						bg.dy0 = 0;
						bg.t = 0;

					} else {
						// Above char
						lBox.setY(white.getY() - lBox.getHeight());
						lBox.dy = 0.1f;
						lBox.t = 0;

					}

				}

			}
		}

	}

	private void collisionLooseObjects() {
		for (int i = 0; i < lObj.LooseObjects.size(); i++) {
			LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(i);
			RlO = lBox.getBounds();

			if (lBox.y + lBox.getHeight() >= seaLevel) {
				lBox.y = seaLevel - RlO.height;
				lBox.dy = 0;
				lBox.t = 0;
			}

		}

	}

	public void individualsLimits() {
		seaLevel = cM.gridY[cM.gridY.length - 1] + 50;

		float y = white.getY() + white.getBounds().height;
		/*
		 * // if (white.dy > 10 && y >= seaLevel - 20 && y < seaLevel-5) {
		 * showIndicator = true; if (Math.abs(white.dy) >= die.dieVelocity)
		 * die.dying(); white.dy0 = 0;
		 * 
		 * if (white.dy > 10) white.dy = -(y - seaLevel); white.t = 0.1f; } else
		 * showIndicator = false;
		 */
		if (y > seaLevel - 1) {

			if (Math.abs(white.dy) >= die.dieVelocity)
				die.dying();

			white.groundContact = true;
			white.setY(seaLevel - white.getBounds().height);
			white.dy0 = 0;
			white.dy = 0;
			white.t = 0.1f;
			if (white.dx != 0 && white.dy != 0 && !white.arrowPressed) {
				white.start1();
				white.animationType = null;
				white.accelerate(0);
				bg.accelerate(0);
			}
			for (int i = 0; i < bg.backgrounds.size(); i++) {
				Background background = (Background) bg.backgrounds.get(i);
				background.setY((float) background.cy * white.getY() + background.pos[i][1]);
			}
			bg.dy0 = 0;
			bg.t = 0;
						

		}

		for (int i = 0; i < enemy.enemySize; i++) {
			Enemy person = (Enemy) enemy.Enemies.get(i);
			if (person.getY() + enemy.getBounds(person).height >= seaLevel) {
				person.setY(seaLevel - enemy.getBounds(person).height);
				person.setVelocityY(0);
				person.t = 0;
			}
		}
	}
}
