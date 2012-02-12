package life;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Life extends Object
{
	private static int SIZE = 5;
	
	public LifePanel panel;
	public LifeFrame frame;
	public LifeWorld world;

	public boolean proceed = true;
	public boolean iterate = false;
	
	private String worldname = null;

	public int skip = 0;
	
	public static enum Worlds
	{	
		/* all i wanted was a pepsi... */
		WORLD3_FINITE("LifeWorld3.Bounded") { LifeWorld create(Rectangle display, int sz) { return new LifeWorld3.Bounded(display, sz);  } },
		WORLD3_WRAP("LifeWorld3.Circular") 	{ LifeWorld create(Rectangle display, int sz) { return new LifeWorld3.Circular(display, sz); } },
		WORLD4_FINITE("LifeWorld4.Bounded") { LifeWorld create(Rectangle display, int sz) { return new LifeWorld4.Bounded(display, sz);  } },
		WORLD4_WRAP("LifeWorld4.Circular") 	{ LifeWorld create(Rectangle display, int sz) { return new LifeWorld4.Circular(display, sz); } };
		
		String name;
		
		Worlds(String n) { name = n; }
		
		abstract LifeWorld create(Rectangle display, int sz);
		
		static LifeWorld create(String name, Rectangle display, int sz)
		{
			for (Worlds world : values())
			{
				if (world.name.equals(name))
				{
					return world.create(display, sz);
				}
			}
			throw new RuntimeException(String.format("Unknown class %s", name));
		}
	}
	
	public Life(DisplayMode m, String world, int size)
	{
		SIZE = size;
//		maxfps = m.getRefreshRate();
		worldname = world;
		frame = new LifeFrame(this, m);
	}
	
	public void createWorld(Rectangle displaysize)
	{
		world = Worlds.create(worldname, displaysize, SIZE);
		panel = new LifePanel(this, displaysize.width, displaysize.height);
	}

	private void run() throws InterruptedException
	{
		try
		{
			frame.setNewGraphicsMode();
			frame.initEvents();
			
			double nextupd = 0;
			
			while (proceed)
			{
				if (frame.strategy.contentsLost())
					frame.refreshBufferStrategy();

				int skipped = 0;
				
				while (proceed && !frame.strategy.contentsLost())
				{
					if (skip > 0)
						skipped += 1;
					
					if (skipped >= skip)
						skipped = 0;
					
					double t0 = System.nanoTime() / 1e6;
				
					// next iteration
					if (iterate && skipped == 0)
						world.iterate();
					
					double t1 = System.nanoTime() / 1e6;
					
//					world.processInput();
					Toolkit.getDefaultToolkit().sync();
					
					double t2 = System.nanoTime() / 1e6;
					
					// draw graphics
					Graphics g = frame.strategy.getDrawGraphics();
					panel.paintBuffer(g);
					frame.strategy.show();
				
					double t3 = System.nanoTime() / 1e6;
					
					// set statistics, which are mostly wrong (ctime is 'skipped' times less, and stime is more)
					// fps is basically useless as that is now the refresh rate of the monitor, this should become
					// iterations per second
					if (t3 > nextupd)
					{
						double ctime = t1 - t0;
						double stime = t2 - t1;
						double dtime = t3 - t2;
						double time = t3 - t0;
						double fps = 1 / time * 1000;  
						
						// ..o....o.. 		// is this really an oscillator i found? \o/ i wonder what it's called
						// oo.oooo.oo		// or if it's just a consequence of my now buggy code...
						// ..o....o..
						
						nextupd = t3 + 100;
						
						panel.statustext = String.format(
								"c %02.0f   d %02.0f   s %02.0f   fps %.1f   %d (%%d)   (%d,%d)",
								ctime / time * 100,
								dtime / time * 100,
								stime / time * 100,
								fps,
								skip,
//								sleep,
								world.cursx,
								world.cursy
						);
					}
				}
				
			}
		}
		finally
		{
			System.out.println("Quitting");
			frame.setOldGraphicsMode();
			frame.dispose();
		}
	}
	
//	private static void printThreads()
//	{
//		Thread[] threads = new Thread[Thread.activeCount()];
//		Thread.currentThread().getThreadGroup().enumerate(threads);
//		for (Thread t : threads)
//		{
//			System.out.printf("Thread: %s\n", t.getName());
//		}
//	}
	
	private static String getOption(String[] arg, String optname, String def)
	{
		boolean found = false;
		for (String a : arg)
			if (found)
				return a;
			else // return next parameter
				found = a.equals('-' + optname);
		return def;
	}
	
	private static int getOption(String[] arg, String optname, int def)
	{
		return Integer.parseInt(getOption(arg, optname, Integer.toString(def)));
	}
	
	private static DisplayMode getOptionDisplayMode(String[] arg, String optname)
	{
		String result = getOption(arg, optname, null);
		if (result != null)
		{
			Matcher regex = Pattern.compile("^(\\d+)x(\\d+)x(\\d+)x(\\d+)$").matcher(result);
			if (regex.matches())
				return new DisplayMode(
						Integer.parseInt(regex.group(1)),
						Integer.parseInt(regex.group(2)),
						Integer.parseInt(regex.group(3)),
						Integer.parseInt(regex.group(4)));
			else
				throw new IllegalArgumentException(String.format("bad option -%s: %s", optname, result));
		}
		return null;
	}

	public static void main(String[] a) throws Exception
	{
		Life life = new Life(getOptionDisplayMode(a, "mode"),
							 getOption(a, "world", "Life4.Circular"),
							 getOption(a, "size",  8));	
		life.run();
	}
}
