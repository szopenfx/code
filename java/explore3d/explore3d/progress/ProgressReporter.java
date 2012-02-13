package explore3d.progress;

/**
 * Interface for reporting log messages
 * @author J.J. Molenaar, J. Dooper
 */
public interface ProgressReporter
{
	/**
	 * Report message to user
	 * @param sender Sending object
	 * @param message Message to report
	 */
	public void message(Object sender, String message);
	
	/**
	 * Clear message container, if possible
	 */
	public void clear();
}
