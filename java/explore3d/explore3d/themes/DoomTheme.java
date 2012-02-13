package explore3d.themes;

import javax.media.j3d.Appearance;

import explore3d.appearances.TexturedAppearance;

/**
 * Textured theme with a Doom-style feel to it
 * @author J.J. Molenaar, J. Dooper
 */
public class DoomTheme extends ThemeCache 
{
	/**
	 * Create ceiling appearance
	 * @return Appearance object
	 */
	protected Appearance createCeiling() 
	{
		return TexturedAppearance.createNormal(
				TexturedAppearance.FLAT_PLANE, "doom-ceiling.jpg", 512, 512
		);
	}

	/**
	 * Create floor appearance
	 * @return Appearance object
	 */
	protected Appearance createFloor() 
	{
		return TexturedAppearance.createNormal(
				TexturedAppearance.FLAT_PLANE,  "doom-floor.jpg", 256, 256
		);
	}

	/**
	 * Create side appearance
	 * @return Appearance object
	 */
	protected Appearance createSide() 
	{
		return TexturedAppearance.createNormal(
				TexturedAppearance.SIDE_PLANE, "doom-side.JPG", 256, 256
		);
	}

	/**
	 * Create front appearance
	 * @return Appearance object
	 */
	protected Appearance createFront() 
	{
		return TexturedAppearance.createNormal(
				TexturedAppearance.FRONT_PLANE, "doom-side.JPG", 256, 256
		);
	}

	/**
	 * Create door appearance
	 * @param selected Selected or normal
	 * @return Appearance object
	 */
	protected Appearance createDoor(boolean selected) 
	{
		String name = "doom-door.jpg";
		int plane = TexturedAppearance.FRONT_PLANE;
		int x = 64, y = 64;

		return selected
				? (Appearance) TexturedAppearance.createSelected(plane, name, x, y)
				: (Appearance) TexturedAppearance.createNormal(plane, name, x, y);
	}

	/**
	 * Create window appearance
	 * @param selected Selected or normal
	 * @return Appearance object
	 */
	protected Appearance createWindow(boolean selected) 
	{
		String name = "doom-window.jpg";
		int plane = TexturedAppearance.SIDE_PLANE;
		int x = 256, y = 128;

		return selected
				? (Appearance) TexturedAppearance.createSelected(plane, name, x, y)
				: (Appearance) TexturedAppearance.createNormal(plane, name, x, y);
	}
}
