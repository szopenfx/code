package life;

import java.util.concurrent.Semaphore;

public class Life implements Runnable
{
	public LifeWorld world;
	public MainFrame main;
	public LifePanel gfx;
	
	private Thread runner;
	public Semaphore itersem = new Semaphore(0);

	public volatile int nappytime = 0;

	public Life()
	{
		main = new MainFrame(this);
		gfx = main.lifepanel;
		world = new LifeWorld.Circular(gfx.getWidth() / gfx.sz - 1, gfx.getHeight() / gfx.sz - 1);
		main.setVisible(true);
	}
	
	public static void main(String[] arg)
	{
		new Life();
	}
	
	public void start()
	{
		itersem = new Semaphore(0);
		if (runner == null)
		{
			runner = new Thread(this);
			runner.start();
		}
	}
	
	public void stop()
	{
		if (runner != null)
			runner.interrupt();
	}
	
	public void run()
	{
		double ctime = 0, ptime = 0, stime = 0, time = 0;
		double nextupd = 0;
		double count = 0;
		try
		{
			do 
			{
				double t0 = System.nanoTime() / 1e6;
				world.iterate();
				
				double t1 = System.nanoTime() / 1e6;
				gfx.repaint();
				itersem.acquire();
				
				double t2 = System.nanoTime() / 1e6;
				if (nappytime > 0) 
					Thread.sleep(nappytime * 100);
				
				double t3 = System.nanoTime() / 1e6;
				count = 1;
				ctime = t1 - t0;
				ptime = t2 - t1;
				stime = t3 - t2;
				time = t3 - t0;
				
				if (t3 > nextupd)
				{
					gfx.setStatus(String.format(
							"REKEN %.1f   TEKEN %.1f   SLAAP %.1f   FPS %.2f   %d",
							ctime / time * 100,
							ptime / time * 100,
							stime / time * 100,
							count / time * 1000,
							nappytime
					));
					nextupd = t3 + 500;
				}
			}
			while (! runner.isInterrupted());
		}
		catch (InterruptedException e)
		{
		}
		finally
		{
			runner = null;
		}
	}
}
