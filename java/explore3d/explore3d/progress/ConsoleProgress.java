package explore3d.progress;

/**
 * Class for reporting progress messages onto the console
 * @author J.J. Molenaar, J. Dooper
 */
public class ConsoleProgress implements ProgressReporter
{
	/**
	 * Report message
	 * @param sender Sending object (for class name)
	 * @param message Message to display
	 */
	public void message(Object sender, String message)
	{
		if(sender != null)
			System.out.println(sender.getClass().getSimpleName() + ": " + message);
		else
			System.out.println("Explore3D: " + message);
	}

	/**
	 * Clear console - not sure if escape codes work from Java
	 */
	public void clear()
	{
		//System.out.println("\018[2J");
	}
}
