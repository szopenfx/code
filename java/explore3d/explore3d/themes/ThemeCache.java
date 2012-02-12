package explore3d.themes;

import javax.media.j3d.Appearance;

import explore3d.progress.Progress;

/**
 * Class for caching a theme (singleton-ish)
 * @author J.J. Molenaar, J. Dooper
 */
public abstract class ThemeCache 
{
	protected Appearance _ceiling;
	protected Appearance _floor;
	protected Appearance _side;
	protected Appearance _front;
	
	protected Appearance _door_normal;
	protected Appearance _door_selected;
	
	protected Appearance _window_normal;
	protected Appearance _window_selected;

	protected abstract Appearance createCeiling(); 
	protected abstract Appearance createFloor(); 
	protected abstract Appearance createSide(); 
	protected abstract Appearance createFront(); 
	protected abstract Appearance createDoor(boolean selected);
	protected abstract Appearance createWindow(boolean selected); 

	/**
	 * Constructor
	 */
	public ThemeCache()
	{
		Progress.report(this, "Loading theme");
		createAppearances();
	}
	
	/**
	 * Create appearance objects
	 */
	private void createAppearances()
	{
		_ceiling = createCeiling();
		_floor = createFloor();
		_side = createSide();
		_front = createFront();
		
		_door_normal = createDoor(false);
		_door_selected = createDoor(true);
		
		_window_normal = createWindow(false);
		_window_selected = createWindow(true);
	}
	
	/**
	 * Return ceiling appearance
	 * @return Appearance object
	 */

	public Appearance getCeiling()
	{
		return _ceiling;
	}

	/**
	 * Create floor appearance
	 * @return Appearance object
	 */
	public Appearance getFloor()
	{
		return _floor;
	}

	/**
	 * Create side appearance
	 * @return Appearance object
	 */
	public Appearance getSide()
	{
		return _side;
	}

	/**
	 * Create front appearance
	 * @return Appearance object
	 */
	public Appearance getFront()
	{
		return _front;
	}

	/**
	 * Create door appearance
	 * @param selected Selected or normal
	 * @return Appearance object
	 */
	public Appearance getDoor(boolean selected)
	{
		return selected ? _door_selected : _door_normal;
	}

	/**
	 * Create window appearance
	 * @param selected Selected or normal
	 * @return Appearance object
	 */
	public Appearance getWindow(boolean selected)
	{
		return selected ? _window_selected : _window_normal;
	}
}
