package checkers;

import java.util.Timer;
import java.util.TimerTask;

public class Clock
{
	protected Timer timer;
	
	public Clock(Checkers checkers)
	{
		//checkers.timerTick();
		timer = new Timer("Clock");
		timer.schedule(new Task(checkers), 0, 1000);
	}
	
	public void finalize()
	{
		stop();
	}
	
	public void stop()
	{
		timer.cancel();
	}
	
	public static interface Observer
	{
		public void timerTick();
	}
	
	protected static class Task extends TimerTask
	{
		protected Observer observer;
		
		public Task(Checkers checkers)
		{
			observer = (Observer) checkers;
		}
		
		public void run()
		{
			observer.timerTick();
		}
	}
}
