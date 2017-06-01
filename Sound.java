import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements KeyListener {

	private green g;
	private KeyTest key;
	protected static boolean mute = true;
	protected static boolean running = true;
	protected boolean done = false;
	protected File file;
	protected Timer soundTimer;
	protected boolean playClip = !false;
	protected boolean entrance = !true;

	public Sound(green g, KeyTest key) {
		this.g = g;
		this.key = key;
		Window w = g.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
	}

	public void playSound(String fileName) {
		System.out.println("Prepering fileshit");
		soundTimer = new Timer();
		soundTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				while (running) {
					try {
						file = new File(this.getClass().getResource(fileName).getFile());
						playClip(file);
						file = new File(this.getClass().getResource("Valkommen.wav").getFile());
						while (running) {
						playClip(file);
						}

					} catch (IOException | UnsupportedAudioFileException | LineUnavailableException
							| InterruptedException e) {
						e.printStackTrace();
					}
				}
				soundTimer.cancel();
			}

		}, 0, 4000);

	}

	public void changeSong(String filename) {
		running = !running;
		Timer soundTimer = new Timer();
		soundTimer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
			
					running = !running;
					playSound(filename);

				soundTimer.cancel();
			}

		}, 400, 4000);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();

		if (KeyCode == KeyEvent.VK_ESCAPE || KeyCode == KeyEvent.VK_END) {
			running = false;

		}

		if (KeyCode == KeyEvent.VK_M) {
			mute = !mute;
			if (mute)
				key.message = "Muted";
			else
				key.message = " ";
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

	protected static void playClip(File clipFile)
			throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

		class AudioListener implements LineListener {
			protected boolean done = false;

			public synchronized void update(LineEvent event) {
				javax.sound.sampled.LineEvent.Type eventType = event.getType();

				if ((eventType == eventType.STOP || eventType == eventType.CLOSE) || done) {
					done = true;
					notifyAll();

				}
			}

			public synchronized void waitUntilDone(Clip clip, AudioInputStream audioInputStream)
					throws InterruptedException {
				while (!done && running) {
					wait();
				}
			}

		}

		AudioListener listener = new AudioListener();
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);

		try {

			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			try {

				clip.start();

				final Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);

					@Override
					public void run() {
						muteControl.setValue(mute);
						if (!running) {
							try {
								clip.close();
								audioInputStream.close();
								Thread.currentThread().interrupt();
								timer.cancel();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}, 0, 200);

				listener.waitUntilDone(clip, audioInputStream);

			} finally {
				clip.close();
			}
		} finally {
			audioInputStream.close();
		}
	}

}