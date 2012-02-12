package graphics2;

import graphics.Scene;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Creëer een virtueel object met behulp van één van de geometrische primitieven zoals een bol of een 
 * cilinder. Probeer hierbij verschillende afmetingen van de objecten uit.
 * 
 * @author J.J. Molenaar
 */
public class CylinderScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * <code><pre>
	 * bg -- tg(t3d) -- c -- a
	 * </pre></code>
	 * 
	 * @return BranchGroup object
	 */
	protected BranchGroup createScene()
	{
		BranchGroup bg = new BranchGroup();

		Appearance a = new Appearance();
		Cylinder c = new Cylinder(0.5f, 0.5f);

		a.setColoringAttributes(
				new ColoringAttributes(1.0f, 0.5f, 0.0f, ColoringAttributes.SHADE_GOURAUD)
		);
		c.setAppearance(a);
		
		Transform3D t3d = new Transform3D();
		t3d.rotX(Math.PI * 0.25f);
		t3d.transform(new Vector3f(0.0f, 0.0f, -2.0f));
		
		TransformGroup tg = new TransformGroup(t3d);

		bg.addChild(tg);
		tg.addChild(c);
		bg.compile();
		
		return bg;
	}

}
