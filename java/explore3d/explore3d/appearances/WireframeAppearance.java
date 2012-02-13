package explore3d.appearances;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;

import explore3d.Constants;

/**
 * Wireframe appearance
 * @author J.J. Molenaar, J.Dooper
 */
public class WireframeAppearance extends Appearance
{
	private static WireframeAppearance _normal = createNormal();
	private static WireframeAppearance _selected = createSelected();
	
	/**
	 * Private constructor
	 */
	private WireframeAppearance()
	{
		// set polygon attributes to wireframe mode
		PolygonAttributes pa = new PolygonAttributes();
		pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		
		setPolygonAttributes(pa);		
	}

	/**
	 * Create normal, non-selected wireframe appearance
	 * @return WireframeAppearance instance
	 */
	private static WireframeAppearance createNormal()
	{
		// set color of wires
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(Constants.WIREFRAME_NORMAL_COLOR);
		
		// create instance and set color
		WireframeAppearance wa = new WireframeAppearance();
		wa.setColoringAttributes(ca);
		
		return wa;
	}
	
	/**
	 * Create selected version of wireframe appearance
	 * @return Selected wireframe appearance
	 */
	private static WireframeAppearance createSelected()
	{
		// set color of wires
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(Constants.WIREFRAME_SELECTED_COLOR);

		// create instance and set color
		WireframeAppearance wa = new WireframeAppearance();
		wa.setColoringAttributes(ca);
		
		return wa;
	}
	
	/**
	 * Create normal, non-selected wireframe appearance
	 * @return WireframeAppearance instance
	 */
	public static WireframeAppearance getNormal()
	{
		return _normal;
	}
	
	/**
	 * Create selected version of wireframe appearance
	 * @return Selected wireframe appearance
	 */
	public static WireframeAppearance getSelected()
	{
		return _selected;
	}
}
