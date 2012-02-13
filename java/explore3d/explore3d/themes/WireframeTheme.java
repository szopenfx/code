package explore3d.themes;

import javax.media.j3d.Appearance;

import explore3d.appearances.WireframeAppearance;

/**
 * Wireframe theme
 * @author J.J. Molenaar, J. Dooper
 */
public class WireframeTheme extends ThemeCache
{
	/**
	 * Create ceiling appearance
	 * @return Appearance object
	 */
	protected Appearance createCeiling()
	{
		return WireframeAppearance.getNormal();
	}

	/**
	 * Create floor appearance
	 * @return Appearance object
	 */
	protected Appearance createFloor()
	{
		return WireframeAppearance.getNormal();
	}

	/**
	 * Create side appearance
	 * @return Appearance object
	 */
	protected Appearance createSide()
	{
		return WireframeAppearance.getNormal();
	}

	/**
	 * Create front appearance
	 * @return Appearance object
	 */
	protected Appearance createFront()
	{
		return WireframeAppearance.getNormal();
	}

	/**
	 * Create door appearance
	 * @param selected Selected or normal
	 * @return Appearance object
	 */
	protected Appearance createDoor(boolean selected)
	{
		return selected
			? WireframeAppearance.getSelected()
			: WireframeAppearance.getNormal();
	}

	/**
	 * Create window appearance
	 * @param selected Selected or normal
	 * @return Appearance object
	 */
	protected Appearance createWindow(boolean selected)
	{
		return selected 
			? WireframeAppearance.getSelected()
			: WireframeAppearance.getNormal();
	}
}
