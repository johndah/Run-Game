import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class KeyTest implements KeyListener, MouseListener {

	public String message = "Press K for key commands";
	public ArrayList<String> keyMessage = new ArrayList<>();
	// { { "Space - Create random enemy" },
	// { "L - Lift objects" }, { "I - Show/hide itemlist" }, { "C - Sunset/
	// -rise" }, { "M - Mute/unmute music" },
	// { "1,2/0 - Change weapon/no weapon" }, {"F - Hitman"}, { "G - Enemies
	// getting down" }, { "W,A,S,D/arrows - Jump, down, walk" } };
	protected green g;
	protected orange a;
	protected Camera cam;
	public boolean keyCommands, running = true;
	protected Rectangle RM;
	protected float mX, mY;

	public void init(Camera cam) {
		g = new green();
		this.cam = cam;
		Window w = g.getFullScreenWindow();
		w.setFont(new Font("Arial", Font.PLAIN, 20));
		w.setForeground(Color.CYAN);
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		w.addMouseListener(this);

	}

	public void update() {
		mX = (float) MouseInfo.getPointerInfo().getLocation().getX();
		mY = (float) MouseInfo.getPointerInfo().getLocation().getY();
		RM = new Rectangle((int) mX, (int) mY, 1, 1);
	}

	public void keyPressed(KeyEvent e) {

		int KeyCode = e.getKeyCode();

		if (KeyCode == KeyEvent.VK_ESCAPE || KeyCode == KeyEvent.VK_END) {
			running = false;
		}

		if (KeyCode == KeyEvent.VK_K) {

			keyCommands = !keyCommands;
		}

		e.consume();
	}

	// keyReleased
	public void keyReleased(KeyEvent e) {
		e.consume();
	}

	public void mouseClicked(MouseEvent e) {
		e.consume();
	}

	public void mousePressed(MouseEvent e) {
		e.consume();
	}

	public void mouseReleased(MouseEvent e) {
		e.consume();
	}

	public void mouseEntered(MouseEvent e) {

		e.consume();
	}

	public void mouseExited(MouseEvent e) {
		e.consume();
	}

	public void keyTyped(KeyEvent e) {
		e.consume();
	}
}
