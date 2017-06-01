import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.codingeek.serialization.SerializationUtil;

public class CreateMap implements MouseListener, KeyListener {

	private green g;
	private ground ground;
	private NonSolidBlocks nSB;
	private LooseObjects lObj;
	private DangerousBoxes dB;
	private AntiGravity ag;
	private Camera cam;
	private white white;
	private Belongings bel;
	private KeyTest key;
	private Sound sound;
	protected ArrayList<Object> CreateMap;
	private int isoPeriod = 2000, numberSavedBoxes, numberSavedNonSolidBoxes, numberSavedLooseBoxes,
			numberSavedDangerousBoxes, numberSavedMagnetPlates, numberSavedMagnetPlatesISO,
			numberSavedMagnetPlatesControlled;
	protected int gridX[], gridY[], mx, my, width, height, nChar, mChar, n, m, x, y, listHeight, listX, listY,
			objectIndex;
	protected Image[] objectImages = new Image[7];
	protected boolean editMap, hide, antigravityActivatedInitial, adding = false;
	protected long time;
	private PrintStream ps;
	private Scanner scanner;
	protected Image image;
	protected ImageIcon ii;
	protected Rectangle RmM, RM;
	protected Timer timer;

	public CreateMap(green g, ground ground, NonSolidBlocks nSB, LooseObjects lObj, DangerousBoxes dB, AntiGravity ag,
			Camera cam, white white, Belongings bel, KeyTest key, Sound sound) {
		this.g = g;
		this.ground = ground;
		this.nSB = nSB;
		this.lObj = lObj;
		this.dB = dB;
		this.ag = ag;
		this.cam = cam;
		this.white = white;
		this.bel = bel;
		this.key = key;
		this.sound = sound;
		init();
	}

	public CreateMap(Image image, int listX, int listY) {
		this.image = image;
		this.listX = listX;
		this.listY = listY;
	}

	public void update(long timePassed) {

		time += timePassed;
		if (time > 1000 && isoPeriod == 1000) {
			antigravityActivatedInitial = !antigravityActivatedInitial;
			time = time - 1000;
		}
		if (time > 2000 && isoPeriod == 2000) {
			antigravityActivatedInitial = !antigravityActivatedInitial;
			time = time - 2000;
		}
		if (time > 3000 && isoPeriod == 3000) {
			antigravityActivatedInitial = !antigravityActivatedInitial;
			time = time - 3000;
		}
		if (time > 4000 && isoPeriod == 4000) {
			antigravityActivatedInitial = !antigravityActivatedInitial;
			time = time - 4000;
		}

		mx = (int) MouseInfo.getPointerInfo().getLocation().getX() - (int) cam.getX();
		my = (int) MouseInfo.getPointerInfo().getLocation().getY() - (int) cam.getY();
		width = gridX[gridX.length - 1];
		height = gridY[gridY.length - 1];
		nChar = (int) Math.floor((white.getX() - width) / width) + 1;
		mChar = (int) Math.floor((white.getY() - height) / height) + 1;

		// Rectify grid marker
		n = nChar;
		m = mChar;

		if (n != 0)
			n = n / n;
		if (m != 0)
			m = m / m;

		x = (int) Math.floor((mx + n) / 50) * 50;
		y = (int) Math.floor((my + m) / 50) * 50;

	}

	private void init() {

		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addMouseListener(this);
		w.addKeyListener(this);

		gridX = new int[(int) Math.ceil(g.getWidth() / 50 + 2)];
		gridY = new int[(int) Math.ceil(g.getHeight() / 50 + 1)];

		for (int n = 0; n < Math.ceil(g.getWidth() / 50 + 2); n++)
			gridX[n] = 50 * n;
		for (int n = 0; n < Math.ceil(g.getHeight() / 50 + 1); n++)
			gridY[n] = 50 * n;

		CreateMap = new ArrayList<>();
		listHeight = 10;

		ii = new ImageIcon(this.getClass().getResource("MetalBox.png"));
		objectImages[0] = ii.getImage();
		objectImages[1] = lObj.Box;
		objectImages[2] = lObj.Box;
		ii = new ImageIcon(this.getClass().getResource("box666.png"));
		objectImages[3] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("MagnetPlate.png"));
		objectImages[4] = ii.getImage();
		objectImages[5] = ii.getImage();
		objectImages[6] = ii.getImage();

		for (int j = 0; j < objectImages.length; j++) {
			listX = (int) g.getWidth() - (int) g.getWidth() / 16 - 50 / 2;
			listY = listHeight;
			CreateMap.add(new CreateMap(objectImages[j], listX, listY));
			listHeight += 60;
		}
	}

	public void mouseClicked(MouseEvent e) {

		RmM = getBoundsMenu();
		RM = new Rectangle((int) e.getXOnScreen(), (int) e.getYOnScreen(), 1, 1);

		// Choice of object to add
		for (int i = 0; i < CreateMap.size(); i++) {
			CreateMap mapObj = (CreateMap) CreateMap.get(i);
			if (RM.intersects(mapObj.getBounds())) {
				objectIndex = i;
			}
		}

		// Remove objects to make room for new
		removeObjects();

		// Add objects on empty squares
		if (editMap && !key.RM.intersects(RmM) && e.getButton() != MouseEvent.BUTTON3) {

			addObjects();

			adding = true;
			timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					try {
						Thread.sleep(500);
						adding = false;
						timer.cancel();
						timer.purge();
					} catch (Exception ex) {
						System.out.println("Error: " + ex);
					}
				}
			}, 500);
		}
	}

	private void addObjects() {
		if( y < 0)
			y = y - 50;

		RmM = getBoundsMenu();

		if (editMap && !key.RM.intersects(RmM)) {
			if (objectIndex == 0) {
				ground groundObject = new ground(x, y);
				ground.addObject(groundObject);

				try {
					scanner = new Scanner(new File("Boxes\\numberSavedBoxes.txt"));
					numberSavedBoxes = scanner.nextInt() + 1;
					SerializationUtil.serialize(groundObject, "Boxes\\box" + (numberSavedBoxes - 1) + ".ser");
					ps = new PrintStream("Boxes\\numberSavedBoxes.txt");
					ps.println(String.valueOf(numberSavedBoxes));

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (objectIndex == 1) {
				NonSolidBlocks block = new NonSolidBlocks(nSB.NonSolidBlock, x, y);
				nSB.nonSolidBlocks.add(block);
				try {
					scanner = new Scanner(new File("NonSolidBoxes\\numberSavedNonSolidBoxes.txt"));
					numberSavedNonSolidBoxes = scanner.nextInt() + 1;
					SerializationUtil.serialize(block,
							"NonSolidBoxes\\NonSolidBox" + (numberSavedNonSolidBoxes - 1) + ".ser");
					ps = new PrintStream("NonSolidBoxes\\numberSavedNonSolidBoxes.txt");
					ps.println(String.valueOf(numberSavedNonSolidBoxes));

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (objectIndex == 2) {
				LooseObjects lBox = new LooseObjects(lObj.Box, x, y, 0, 0);
				lObj.LooseObjects.add(lBox);
				try {
					scanner = new Scanner(new File("LooseBoxes\\numberSavedLooseBoxes.txt"));
					numberSavedLooseBoxes = scanner.nextInt() + 1;
					SerializationUtil.serialize(lBox, "LooseBoxes\\looseBox" + (numberSavedLooseBoxes - 1) + ".ser");
					ps = new PrintStream("LooseBoxes\\numberSavedLooseBoxes.txt");
					ps.println(String.valueOf(numberSavedLooseBoxes));

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (objectIndex == 3) {
				DangerousBoxes dBobject = new DangerousBoxes(x, y);
				dB.addObject(dBobject);

				try {
					scanner = new Scanner(new File("DangerousBoxes\\numberSavedDangerousBoxes.txt"));
					numberSavedDangerousBoxes = scanner.nextInt() + 1;
					SerializationUtil.serialize(dBobject,
							"DangerousBoxes\\DangerousBox" + (numberSavedDangerousBoxes - 1) + ".ser");
					ps = new PrintStream("DangerousBoxes\\numberSavedDangerousBoxes.txt");
					ps.println(String.valueOf(numberSavedDangerousBoxes));

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (objectIndex == 4) {
				AntiGravity agObj = new AntiGravity(x, y, "Standard", 0, true);
				ag.magnetPlates.add(agObj);
				try {
					scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlates.txt"));
					numberSavedMagnetPlates = scanner.nextInt() + 1;
					SerializationUtil.serialize(agObj,
							"MagnetPlates\\magnetPlate" + (numberSavedMagnetPlates - 1) + ".ser");
					ps = new PrintStream("MagnetPlates\\numberSavedMagnetPlates.txt");
					ps.println(String.valueOf(numberSavedMagnetPlates));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (objectIndex == 5) {
				AntiGravity agObj = new AntiGravity(x, y, "ISO", isoPeriod, antigravityActivatedInitial);
				ag.magnetPlatesISO.add(agObj);
				for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
					AntiGravity magplate = (AntiGravity) ag.magnetPlatesISO.get(i);
					magplate.time = 0;
					time = 0;
				}
				try {
					scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlatesISO.txt"));
					numberSavedMagnetPlatesISO = scanner.nextInt() + 1;
					SerializationUtil.serialize(agObj,
							"MagnetPlates\\magnetPlateISO" + (numberSavedMagnetPlatesISO - 1) + ".ser");
					ps = new PrintStream("MagnetPlates\\numberSavedMagnetPlatesISO.txt");
					ps.println(String.valueOf(numberSavedMagnetPlatesISO));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (objectIndex == 6) {
				AntiGravity agObj = new AntiGravity(x, y, "Controlled", 0, false);
				ag.magnetPlatesControlled.add(agObj);
				AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled
						.get(ag.magnetPlatesControlled.size() - 1);
				magplate.antigravityActivated = false;
				try {
					scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlatesControlled.txt"));
					numberSavedMagnetPlatesControlled = scanner.nextInt() + 1;
					SerializationUtil.serialize(agObj,
							"MagnetPlates\\magnetPlateControlled" + (numberSavedMagnetPlatesControlled - 1) + ".ser");
					ps = new PrintStream("MagnetPlates\\numberSavedMagnetPlatesControlled.txt");
					ps.println(String.valueOf(numberSavedMagnetPlatesControlled));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private void removeObjects() {
		RM = new Rectangle((int) key.RM.x - (int) cam.getX(), (int) key.RM.y - (int) cam.getY(), 1, 1);

		// Remove objects to make room for new
		for (int i = 0; i < ground.boxes.size(); i++) {
			ground box = (ground) ground.boxes.get(i);
			if (editMap && RM.intersects(box.getBounds())) {
				ground.boxes.remove(i);
				try {
					scanner = new Scanner(new File("Boxes\\numberSavedBoxes.txt"));
					numberSavedBoxes = scanner.nextInt() - 1;
					ps = new PrintStream("Boxes\\numberSavedBoxes.txt");
					ps.println(String.valueOf(numberSavedBoxes));
					for (int j = i; j < ground.boxes.size(); j++) {
						ground boxj = (ground) ground.boxes.get(j);
						SerializationUtil.serialize(boxj, "Boxes\\box" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < nSB.nonSolidBlocks.size(); i++) {
			NonSolidBlocks block = (NonSolidBlocks) nSB.nonSolidBlocks.get(i);
			if (editMap && RM.intersects(block.getBounds())) {
				nSB.nonSolidBlocks.remove(i);
				try {
					scanner = new Scanner(new File("NonSolidBoxes\\numberSavedNonSolidBoxes.txt"));
					numberSavedNonSolidBoxes = scanner.nextInt() - 1;
					ps = new PrintStream("NonSolidBoxes\\numberSavedNonSolidBoxes.txt");
					ps.println(String.valueOf(numberSavedNonSolidBoxes));
					for (int j = i; j < nSB.nonSolidBlocks.size(); j++) {
						NonSolidBlocks blockj = (NonSolidBlocks) nSB.nonSolidBlocks.get(j);
						SerializationUtil.serialize(blockj, "NonSolidBoxes\\NonSolidBox" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < lObj.LooseObjects.size(); i++) {
			LooseObjects lBox = (LooseObjects) lObj.LooseObjects.get(i);
			if (editMap && RM.intersects(lBox.getBounds())) {
				lObj.LooseObjects.remove(i);
				try {
					scanner = new Scanner(new File("LooseBoxes\\numberSavedLooseBoxes.txt"));
					numberSavedLooseBoxes = scanner.nextInt() - 1;
					ps = new PrintStream("LooseBoxes\\numberSavedLooseBoxes.txt");
					ps.println(String.valueOf(numberSavedLooseBoxes));
					for (int j = i; j < lObj.LooseObjects.size(); j++) {
						LooseObjects lBoxj = (LooseObjects) lObj.LooseObjects.get(j);
						SerializationUtil.serialize(lBoxj, "LooseBoxes\\looseBox" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < dB.DangerousBoxes.size(); i++) {
			DangerousBoxes dBox = (DangerousBoxes) dB.DangerousBoxes.get(i);
			if (editMap && RM.intersects(dBox.getBounds())) {
				dB.DangerousBoxes.remove(i);
				try {
					scanner = new Scanner(new File("DangerousBoxes\\numberSavedDangerousBoxes.txt"));
					numberSavedDangerousBoxes = scanner.nextInt() - 1;
					ps = new PrintStream("DangerousBoxes\\numberSavedDangerousBoxes.txt");
					ps.println(String.valueOf(numberSavedDangerousBoxes));
					for (int j = i; j < dB.DangerousBoxes.size(); j++) {
						DangerousBoxes dBoxj = (DangerousBoxes) dB.DangerousBoxes.get(j);
						SerializationUtil.serialize(dBoxj, "DangerousBoxes\\DangerousBox" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < ag.magnetPlates.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlates.get(i);
			if (editMap && RM.intersects(magplate.getBounds())) {
				ag.magnetPlates.remove(i);
				try {
					scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlates.txt"));
					numberSavedMagnetPlates = scanner.nextInt() - 1;
					ps = new PrintStream("MagnetPlates\\numberSavedMagnetPlates.txt");
					ps.println(String.valueOf(numberSavedMagnetPlates));
					for (int j = i; j < ag.magnetPlates.size(); j++) {
						AntiGravity magplatej = (AntiGravity) ag.magnetPlates.get(j);
						SerializationUtil.serialize(magplatej, "MagnetPlates\\magnetPlate" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < ag.magnetPlatesISO.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesISO.get(i);
			if (editMap && RM.intersects(magplate.getBounds())) {
				ag.magnetPlatesISO.remove(i);
				try {
					scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlatesISO.txt"));
					numberSavedMagnetPlatesISO = scanner.nextInt() - 1;
					ps = new PrintStream("MagnetPlates\\numberSavedMagnetPlatesISO.txt");
					ps.println(String.valueOf(numberSavedMagnetPlatesISO));
					for (int j = i; j < ag.magnetPlatesISO.size(); j++) {
						AntiGravity magplatej = (AntiGravity) ag.magnetPlatesISO.get(j);
						SerializationUtil.serialize(magplatej, "MagnetPlates\\magnetPlateISO" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < ag.magnetPlatesControlled.size(); i++) {
			AntiGravity magplate = (AntiGravity) ag.magnetPlatesControlled.get(i);
			if (editMap && RM.intersects(magplate.getBounds())) {
				ag.magnetPlatesControlled.remove(i);
				try {
					scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlatesControlled.txt"));
					numberSavedMagnetPlatesControlled = scanner.nextInt() - 1;
					ps = new PrintStream("MagnetPlates\\numberSavedMagnetPlatesControlled.txt");
					ps.println(String.valueOf(numberSavedMagnetPlatesControlled));
					for (int j = i; j < ag.magnetPlatesControlled.size(); j++) {
						AntiGravity magplatej = (AntiGravity) ag.magnetPlatesControlled.get(j);
						SerializationUtil.serialize(magplatej, "MagnetPlates\\magnetPlateControlled" + (j) + ".ser");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void loadMap() {
		key.message = "Level 1";
		try {
			scanner = new Scanner(new File("Boxes\\numberSavedBoxes.txt"));
			numberSavedBoxes = scanner.nextInt();
			for (int i = 0; i < numberSavedBoxes; i++) {
				ground groundObject = (ground) SerializationUtil.deserialize("Boxes\\box" + i + ".ser");
				groundObject = new ground(groundObject.x, groundObject.y);
				ground.addObject(groundObject);
			}

			scanner = new Scanner(new File("NonSolidBoxes\\numberSavedNonSolidBoxes.txt"));
			numberSavedNonSolidBoxes = scanner.nextInt();
			for (int i = 0; i < numberSavedNonSolidBoxes; i++) {
				NonSolidBlocks blockObject = (NonSolidBlocks) SerializationUtil
						.deserialize("NonSolidBoxes\\NonSolidBox" + i + ".ser");
				blockObject = new NonSolidBlocks(nSB.NonSolidBlock, blockObject.x, blockObject.y);
				nSB.nonSolidBlocks.add(blockObject);
			}

			scanner = new Scanner(new File("LooseBoxes\\numberSavedLooseBoxes.txt"));
			numberSavedLooseBoxes = scanner.nextInt();
			for (int i = 0; i < numberSavedLooseBoxes; i++) {
				LooseObjects lBox = (LooseObjects) SerializationUtil.deserialize("LooseBoxes\\looseBox" + i + ".ser");
				lBox = new LooseObjects(lObj.Box, lBox.x, lBox.y, lBox.dx, lBox.dy);
				lObj.LooseObjects.add(lBox);
			}

			scanner = new Scanner(new File("DangerousBoxes\\numberSavedDangerousBoxes.txt"));
			numberSavedDangerousBoxes = scanner.nextInt();
			for (int i = 0; i < numberSavedDangerousBoxes; i++) {
				DangerousBoxes dBoxObject = (DangerousBoxes) SerializationUtil
						.deserialize("DangerousBoxes\\DangerousBox" + i + ".ser");
				dBoxObject = new DangerousBoxes(dBoxObject.x, dBoxObject.y);
				dB.addObject(dBoxObject);
			}

			scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlates.txt"));
			numberSavedMagnetPlates = scanner.nextInt();
			for (int i = 0; i < numberSavedMagnetPlates; i++) {
				AntiGravity magplate = (AntiGravity) SerializationUtil
						.deserialize("MagnetPlates\\magnetPlate" + i + ".ser");
				magplate = new AntiGravity(magplate.x, magplate.y, "Standard", 0, true);
				ag.magnetPlates.add(magplate);
			}

			scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlatesISO.txt"));
			numberSavedMagnetPlatesISO = scanner.nextInt();
			for (int i = 0; i < numberSavedMagnetPlatesISO; i++) {
				AntiGravity magplate = (AntiGravity) SerializationUtil
						.deserialize("MagnetPlates\\magnetPlateISO" + i + ".ser");
				magplate = new AntiGravity(magplate.x, magplate.y, "ISO", magplate.isoPeriod,
						magplate.antigravityActivatedInitial);
				ag.magnetPlatesISO.add(magplate);
				magplate.time = 0;

			}
			time = 0;

			scanner = new Scanner(new File("MagnetPlates\\numberSavedMagnetPlatesControlled.txt"));
			numberSavedMagnetPlatesControlled = scanner.nextInt();
			for (int i = 0; i < numberSavedMagnetPlatesControlled; i++) {
				AntiGravity magplate = (AntiGravity) SerializationUtil
						.deserialize("MagnetPlates\\magnetPlateControlled" + i + ".ser");
				magplate = new AntiGravity(magplate.x, magplate.y, "Controlled", 0, false);
				ag.magnetPlatesControlled.add(magplate);
				magplate.antigravityActivated = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();

		RmM = getBoundsMenu();

		if (KeyCode == KeyEvent.VK_E) {
			hide = g.hide;
			editMap = !editMap;
			key.keyCommands = false;
			bel.showItems = false;
			if (editMap)
				g.hideMouse(false);
			else
				g.hideMouse(hide);
		}

		if (KeyCode == KeyEvent.VK_R) {
			sound.changeSong("EntranceValkommen.wav");
		}

		if (editMap && KeyCode == KeyEvent.VK_ALT) {
			removeObjects();
			addObjects();
		}
		if (editMap && KeyCode == KeyEvent.VK_SHIFT) {
			removeObjects();
		}

		if (editMap && KeyCode == KeyEvent.VK_1) {
			isoPeriod = 1000;
		}
		if (editMap && KeyCode == KeyEvent.VK_2) {
			isoPeriod = 2000;
		}
		if (editMap && KeyCode == KeyEvent.VK_3) {
			isoPeriod = 3000;
		}
		if (editMap && KeyCode == KeyEvent.VK_4) {
			isoPeriod = 4000;
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

	public Rectangle getBounds() {
		return new Rectangle(listX, listY, 50, 50);
	}

	public Rectangle getBoundsMenu() {
		return new Rectangle((int) g.getWidth() - (int) g.getWidth() / 8, (int) 0, (int) g.getWidth() / 8,
				g.getHeight());

	}
}
