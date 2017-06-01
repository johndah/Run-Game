import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class green implements KeyListener{
	
	private GraphicsDevice vc;
	JFrame f;
	Cursor hiddenCursor;
	Boolean hide = true;
	
	public green(){
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
		
	}
	
	public void init(){
		Window w = getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
	}
	
	public DisplayMode[] getCompatiDisplayModes(){
		return vc.getDisplayModes();
	}
	
	public DisplayMode findFirstCompatibleMode(DisplayMode modes[]){
		DisplayMode goodModes[] = vc.getDisplayModes();
		for(int x = 0;x < modes.length; x++){
			for(int y =0; y<goodModes.length; y++){
				if(displayModesMatch(modes[x], goodModes[y])){
					return modes[x];
				}
			}
		}
		return null;
	}
	
	//get current DM
	public DisplayMode getCurrentDisplayMode(){
		return vc.getDisplayMode();
	}
	
	public boolean displayModesMatch(DisplayMode m1, DisplayMode m2){
		if(m1.getWidth() != m2.getWidth() || m1.getHeight() != m2.getHeight())
			return false;
		
		if(m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m1.getBitDepth() != m2.getBitDepth() )
			return false;
		
		if(m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m1.getRefreshRate() != m2.getRefreshRate())
			return false;
		
		return true;
	}
	
	public void setFullScreen(DisplayMode dm){
		f = new JFrame();
		f.setUndecorated(true);
		f.setIgnoreRepaint(true);
		f.setResizable(true);
		vc.setFullScreenWindow(f);
		
		if(dm != null && vc.isDisplayChangeSupported()){
			try{
				vc.setDisplayMode(dm);
			}catch(Exception ex){}
		}
		f.createBufferStrategy(2);
		
		/// Hiding cursor 
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// get the smallest valid cursor size
		Dimension dim = toolkit.getBestCursorSize(1, 1);
		// create a new image of that size with an alpha channel
		BufferedImage cursorImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		// get a Graphics2D object to draw to the image
		Graphics2D g2d = cursorImg.createGraphics();
		// set the background 'color' with 0 alpha and clear the image
		g2d.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		g2d.clearRect(0, 0, dim.width, dim.height);
		// dispose the Graphics2D object
		g2d.dispose();
		// now create your cursor using that transparent image
		 hiddenCursor = toolkit.createCustomCursor(cursorImg, new Point(0,0), "hiddenCursor");
		 hideMouse(hide);
	}
	
	public void hideMouse(boolean hide) {
	    if(hide) {
	    	f.setCursor(hiddenCursor);
	    } else {
	    	f.setCursor(Cursor.getDefaultCursor());
	    }
	}
	
	public Graphics2D getGraphics(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy b = w.getBufferStrategy();
			return (Graphics2D) b.getDrawGraphics();
		}else{
			return null;
		}
	}
	
	public void update(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy b = w.getBufferStrategy();
			if(!b.contentsLost()){
				b.show();
			}
		}
	}
	
	public Window getFullScreenWindow(){
		return vc.getFullScreenWindow();
	}
	
	public int getWidth(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getWidth();
		}else{
			return 0;
		}
	}
	
	public int getHeight(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getHeight();
		}else{
			return 0;
		}
	}
	
	public void restoreScreen(){
		Window w = vc.getFullScreenWindow();
		if(w != null)
			w.dispose();
		
		vc.setFullScreenWindow(null);
	}
	
	public BufferedImage creatCompatibleImage(int w, int h, int t){
		Window win = vc.getFullScreenWindow();
		if(win != null){
			GraphicsConfiguration gc = win.getGraphicsConfiguration();
			return gc.createCompatibleImage(w, h, t);
		}
		return null;
	
	}

	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
	
		if (KeyCode == KeyEvent.VK_H) {
			hide = !hide;
			hideMouse(hide);
		}


		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}