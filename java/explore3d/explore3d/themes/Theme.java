package explore3d.themes;

import javax.media.j3d.Appearance;

/**
 * Singleton-ish theme class 
 * @author J.J. Molenaar, J. Dooper
 */
public class Theme 
{
	/**
	 * Object that holds instances of Appearance objects
	 */
	private static ThemeCache _cache;
	
	/**
	 * Should not instantiate this class
	 */
	private Theme()
	{
	}
	
	/**
	 * Set theme handler
	 * @param theme Theme instance
	 */
	public static void setTheme(ThemeCache theme)
	{
		_cache = theme;
	}
	
	/**
	 * Get appearance for ceiling
	 * @return Appearance
	 */
	public static Appearance getCeiling()
	{
		return _cache.getCeiling();
	}

	/**
	 * Get appearance for floor
	 * @return Appearance
	 */
	public static Appearance getFloor()
	{
		return _cache.getFloor();
	}

	/**
	 * Get appearance for side
	 * @return Appearance
	 */
	public static Appearance getSide()
	{
		return _cache.getSide();
	}

	/**
	 * Get appearance for front
	 * @return Appearance
	 */
	public static Appearance getFront()
	{
		return _cache.getFront();
	}

	/**
	 * Get appearance for door
	 * @return Appearance
	 */
	public static Appearance getDoor(boolean selected)
	{
		return _cache.getDoor(selected);
	}

	/**
	 * Get appearance for window
	 * @return Appearance
	 */
	public static Appearance getWindow(boolean selected)
	{
		return _cache.getWindow(selected);
	}
}