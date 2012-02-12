package life;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

@SuppressWarnings("serial")
public class LifeFrame extends Frame
{
	protected Life life;
	protected Keys keys;

	public GraphicsEnvironment environment;
	public GraphicsDevice device;
	public BufferStrategy strategy;
	public GraphicsConfiguration config;
	public DisplayMode mode;
	
	private int dragmode = 0;

	public DisplayMode olddm;
	private boolean eventsinitialized = false;
	
	public LifeFrame(Life L, DisplayMode m)
	{
		life = L;
		mode = m;
		
		initFrame();
	}
	
	public void initFrame()
	{
		environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
		config = device.getDefaultConfiguration();
		setBounds(config.getBounds());
	}
	
	private void debug(String s) { System.out.println(s); }
	
	public void setNewGraphicsMode()
	{
		olddm = device.getDisplayMode();

		debug("Going full-screen");
		if (! device.isFullScreenSupported())
		{
			debug("Fullscreen not supported, going ahead anyway :-O ");
		}

		setUndecorated(true);
		setIgnoreRepaint(true);
		device.setFullScreenWindow(this);

		// this WTF is because Frame.setCursor is deprecated and does not do what I need.
		// not that this thing does what I want (that would be NOT DISPLAYING ANYTHING. bitch API)
		((Component) this).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		if (mode != null)
		{
			device.setDisplayMode(mode);
		}
		
		debug("Creating buffer strategy");
		
		refreshBufferStrategy();
		
		debug("Creating world");
		
		life.createWorld(getBounds());
		
		debug("Done");
	}

	public void refreshBufferStrategy()
	{
		super.createBufferStrategy(2); // two buffers
		strategy = getBufferStrategy();
	}
	
	public void setOldGraphicsMode()
	{
		device.setDisplayMode(olddm);
		device.setFullScreenWindow(null);
		//System.exit(66); // crash it :p
	}
	
	private enum Keys
	// this should help create a menu thingie ... y.a.g.n.i.
	{
		MAIN {
			void keyPressed(Life life, KeyEvent e) 
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_S: // start, stop
						life.iterate = !life.iterate;
						break;
					
					case KeyEvent.VK_F: // display stats
						life.panel.drawstatus = !life.panel.drawstatus;
						break;
						
					case KeyEvent.VK_C: // clear world
						if (!life.iterate)
							life.world.clear();
						break;
						
					case KeyEvent.VK_SPACE: // do one iteration
						if (!life.iterate)
							life.world.iterate();
						break;
					
					case KeyEvent.VK_EQUALS: // + : go faster
						if (life.skip > 0)
							life.skip -= 1;
						break;
						
					case KeyEvent.VK_MINUS: // - : go slower
						if (life.skip < life.frame.mode.getRefreshRate())
							life.skip += 1;
						break;
				}
			};
		};
		
		abstract void keyPressed(Life life, KeyEvent e);
	}
	
	public void initEvents()
	{
		assert(!eventsinitialized);

		keys = Keys.MAIN;

		/*
		 * Event handler for Alt-F4
		 */
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				life.proceed = false;
			}
		});
		
		/*
		 * Event handler for mouse clicks
		 */
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				switch (e.getButton())
				{
					case MouseEvent.BUTTON1:
					case MouseEvent.BUTTON3:
					{
						int x = e.getX();
						int y = e.getY();
						
						dragmode = (e.getButton() == MouseEvent.BUTTON1) ? 1 : 0;

						life.world.setCursor(x, y);
						life.world.inputCoord(x, y, dragmode);
						break;
					}
				}				
			}
		});
		
		/*
		 * Event handlers for cursor drawing and dragging events 
		 */
		addMouseMotionListener(new MouseMotionListener() 
		{
			public void mouseMoved(MouseEvent e)
			{
				int x = e.getX();
				int y = e.getY();
				
				life.world.setCursor(x, y);
			}
			
			public void mouseDragged(MouseEvent e)
			{
				int x = e.getX();
				int y = e.getY();
				
				life.world.setCursor(x, y);
				life.world.inputCoord(x, y, dragmode);
			}
		});
		
		/*
		 * Hook up keyboard event listener, see Keys enumeration
		 */
		addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e)
			{
				if (keys != null)
					keys.keyPressed(life, e);
			}
		});
		
		eventsinitialized = true;
	}

}
