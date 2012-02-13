package explore3d.appearances;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
/**
 * makes objects transparent (when clicked in this program for example)
 * @author J.J. Molenaar, J.Dooper
 */


public class TransparentAppearance extends Appearance
{
	public TransparentAppearance()
	{
		// set alpha blending
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparency(0.6f);
		ta.setTransparencyMode(TransparencyAttributes.BLENDED);
		
		// set color
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(new Color3f(0.0f, 0.0f, 0.0f));
		
		setTransparencyAttributes(ta);
		setColoringAttributes(ca);
	}
}
