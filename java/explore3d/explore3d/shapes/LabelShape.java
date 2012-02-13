package explore3d.shapes;

import java.awt.Font;

import com.sun.j3d.utils.geometry.Text2D;

import explore3d.Constants;

/**
 * Shape of a generic label
 * @author J.J. Molenaar, J. Dooper
 */
public class LabelShape extends Text2D 
{
	/**
	 * Constructor
	 * @param label Label to display
	 */
	public LabelShape(String label)
	{
		super(label, Constants.WIREFRAME_NORMAL_COLOR, "Arial", 14, Font.BOLD);
	}
}
