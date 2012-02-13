package graphics4;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.ColorCube;

import graphics.Scene;

public class ColorCubeRotateScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creëer een kubus met behulp van de ColorCube klasse 
	 * en laat deze roteren met behulp van de muis.
	 * 
	 * <code><pre>
	 * bg -- tg ---- cc
	 *          '--- mr(tg)
	 * </pre></code>
	 *          
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		ColorCube cc = new ColorCube(0.3f);
		
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		MouseRotate mr = new MouseRotate(tg);
		mr.setSchedulingBounds(new BoundingBox());
		
		tg.addChild(cc);
		tg.addChild(mr);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.compile();
		return bg;
	}
}
