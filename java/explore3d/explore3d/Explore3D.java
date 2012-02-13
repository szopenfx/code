package explore3d;

import java.util.Map;

import javax.media.j3d.VirtualUniverse;

import explore3d.frames.MainFrame;
import explore3d.progress.ConsoleProgress;
import explore3d.progress.Progress;
import explore3d.themes.Theme;
import explore3d.themes.WireframeTheme;

/**
 * Main class that creates main frame.
 * @author J.J. Molenaar, J. Dooper
 */
public class Explore3D
{
	/**
	 * Return Java version
	 * @return Java version
	 */
	public static String getJavaVersion()
	{
		return "Java " + System.getProperty("java.version");
	}
	
	/**
	 * Return Java3D version
	 * @return Java3D version
	 */
	private static String getJava3DVersion()
	{
		Map m = VirtualUniverse.getProperties();
		return "Java3D " + m.get("j3d.version") + " " + m.get("j3d.renderer");
	}
	
	/**
	 * Return string representation of a file size
	 * @param size Size of file
	 * @return File size in nice representation
	 */
	public static String representByteSize(long size)
	{
		final String units[] = { "B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
		
		double number = size;
		int count = 0;
		
		while(number > 1024)
		{
			number /= 1024;
			count += 1;
		}
		
		String fmt = "%.1f %s";
		if(count == 0)
			fmt = "%.0f %s";
		
		if(count < units.length)	
			return String.format(fmt, new Object[] { 
				new Double(number),
				units[count]
			});
		else // :-)
			return String.format("%.1f x 2^%d0 bytes", new Object[] {
				new Double(number),
				new Integer(count)
			});
	}

	/**
	 * Start program
	 * @param args Arguments (ignored)
	 */
	public static void main(String args[])
	{
		// set reporter
		Progress.addReporter(new ConsoleProgress());
		
		// output some statistics
		Progress.report(null, getJavaVersion());
		Progress.report(null, getJava3DVersion());
		
		// set default theme
		Theme.setTheme(new WireframeTheme());
		
		// create main frame
		try
		{
			new MainFrame();
		}
		catch(RuntimeException re)
		{
			Progress.report(null, re.getMessage());
			re.printStackTrace();
		}
		catch(Exception e)
		{
			Progress.report(null, e.getMessage());
			e.printStackTrace();
		}
	}

	// TODO sort contents of directorylist!!!
	// TODO animation of click behavior
	// TODO set title of frame to current directory
	// TODO add map seen from above to frame
	// TODO convert text2D to text3D
	// TODO do something about huge directories!!
	// TODO move camera every frame and use keyboard behavior to manipulate velocity of move behavior
	// TODO add parent directory
}