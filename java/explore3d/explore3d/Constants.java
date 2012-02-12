package explore3d;

import java.awt.Component;

import javax.vecmath.Color3f;

import explore3d.frames.MainFrameInterface;

/**
 * Class that holds constants for other classes
 * @author J.J. Molenaar, J. Dooper
 */
public class Constants 
{
	/**
	 * Width of a door
	 */
	public static final float DOOR_WIDTH = 0.5f;
	
	/**
	 * Height of a door
	 */
	public static final float DOOR_HEIGHT = 0.8f;
	
	/**
	 * Width of a window
	 */
	public static final float WINDOW_WIDTH = 0.5f;
	
	/**
	 * Height of a window
	 */
	public static final float WINDOW_HEIGHT = 0.5f;
	
	/**
	 * Thickness of flat objects (like walls)
	 */
	public static final float FLAT = 0.01f;
	
	/**
	 * Offset for generic file shape so that they don't intersect with the walls
	 */
	public static final float OFFSET = FLAT + 0.001f;
	/**
	 * Space between two windows or two doors
	 */
	public static final float SPACE = 0.1f;
	
	/**
	 * Height of the room
	 */
	public static final float ROOM_HEIGHT = 1.0f;
	
	/**
	 * Colour of unselected wireframe objects
	 */
	public static final Color3f WIREFRAME_NORMAL_COLOR = new Color3f(1.0f, 1.0f, 1.0f);
	
	/**
	 * Colour of selected wireframe objects
	 */
	public static final Color3f WIREFRAME_SELECTED_COLOR = new Color3f(0.0f, 1.0f, 0.0f);

	/**
	 * Evil global variable that is changed by KeyNavigator to influence MouseLooker
	 */
	private static boolean ALLOW_ROTATE = false; 

	/**
	 * Set rotation permission to true or false
	 * @param allow Allow rotation
	 */
	public static void allowRotateSet(boolean allow)
	{
		ALLOW_ROTATE = allow;
		if(MAIN_FRAME != null)
			MAIN_FRAME.setRotate(allow);
	}
	
	/**
	 * Toggle status of rotation permission
	 */
	public static void allowRotateToggle()
	{
		allowRotateSet(!ALLOW_ROTATE);
	}
	
	/**
	 * Return status of rotation permission
	 * @return
	 */
	public static boolean allowRotate()
	{
		return ALLOW_ROTATE;
	}
	
	/**
	 * Evil global variable that acts as image observer
	 */
	public static Component IMAGE_OBSERVER = null;
	
	/**
	 * Evil global variable that points to main frame
	 */
	public static MainFrameInterface MAIN_FRAME = null;
}
