import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

public class Dying implements KeyListener, MouseListener {

	private white white;
	private green gr;
	private Checkpoints cp;
	private Background bg;
	protected boolean died = false, diedOnce = false;
	protected int dieVelocity = 40;
	protected Timer timer;
	protected boolean waiting = false;

	public Dying(white white, green gr, Checkpoints cp, Background bg) {
		this.white = white;
		this.gr = gr;
		this.cp = cp;
		this.bg = bg;
		init();
	}

	private void init() {
		Window w = gr.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		w.addMouseListener(this);

	}

	public void dying() {

		if (!white.shiftTabPressed && !waiting) {
			waiting = true;
			died = true;
			restore();
			timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					died = false;
					restore();
					waiting = false;
					timer.cancel();
					timer.purge();
				}
			}, 2000);

		}
		diedOnce = true;

	}

	public void restore() {
		Checkpoints place = (Checkpoints) cp.Checkpoints.get(cp.checkPointTopIndex - 1);
		white.setX(place.x);
		white.setY(place.y);
		for (int j = 0; j < bg.backgrounds.size(); j++) {
			Background background = (Background) bg.backgrounds.get(j);
			background.setX((float) background.cx * white.getX());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();

		if (died && (KeyCode == KeyEvent.VK_ENTER || KeyCode == KeyEvent.VK_SPACE
				|| KeyCode == KeyEvent.VK_BACK_SPACE)) {
			timer.cancel();
			timer.purge();
			waiting = false;
			died = false;
			restore();

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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (died) {
			timer.cancel();
			timer.purge();
			waiting = false;
			died = false;
			restore();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
