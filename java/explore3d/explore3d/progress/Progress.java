package explore3d.progress;

import java.util.Vector;

/**
 * Class for reporting log messages to user
 * @author J.J. Molenaar, J. Dooper
 */
public class Progress
{
	/**
	 * Vector of senders - parallel with _messages
	 */
	private static Vector _senders = new Vector();
	
	/**
	 * Vector of messages - parallel with _senders
	 */
	private static Vector _messages = new Vector();
	
	/**
	 * Objects that report messages
	 */
	private static Vector _reporters = new Vector();
	
	/**
	 * Report message to user via all registered reporters
	 * @param sender Sending object
	 * @param message Message to report
	 */
	public static void report(Object sender, String message)
	{
		say(sender, message);
		_senders.add(sender);
		_messages.add(message);
	}
	
	/**
	 * The actual act of calling the reporting object
	 * @param sender Sending object
	 * @param message Message to report
	 */
	private static void say(Object sender, String message)
	{
		for(int i = 0; i < _reporters.size(); i++)
			((ProgressReporter) _reporters.get(i)).message(sender, message);
	}
	
	/**
	 * Add reporter to collection
	 * @param reporter Reporter to add
	 */
	public static void addReporter(ProgressReporter reporter)
	{
		if(! _reporters.contains(reporter))
		{
			_reporters.add(reporter);
			resetReporter(reporter);
		}
	}
	
	/**
	 * Remove reporter from collection
	 * @param reporter Reporter to remove
	 */
	public static void removeReporter(ProgressReporter reporter)
	{
		if(_reporters.contains(reporter))
			_reporters.remove(reporter);
	}
	
	/**
	 * Fill reporter object with all available messages after clearing
	 * @param reporter Reporter to reset
	 */
	private static void resetReporter(ProgressReporter reporter)
	{
		reporter.clear();
		for(int i = 0; i < _senders.size(); i++)
			say(
					_senders.get(i), 
					(String) _messages.get(i)
			);

	}
}
