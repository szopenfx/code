package graphics4;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.picking.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.picking.behaviors.PickZoomBehavior;

import graphics.Scene;

public class ColorCubesRotateTranslateZoomScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create translated ColorCube.
	 * @param v Translation vector
	 * @return TransformGroup
	 */
	protected TransformGroup createTranslatedColorCube(Vector3f v)
	{
		ColorCube cc = new ColorCube(0.3f);
		
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(v);
		
		TransformGroup tg = new TransformGroup(t3d);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tg.addChild(cc);
		return tg;
	}
	
	/**
	 * Voeg aan opdracht 1 een tweede kubus toe die de eerste kubus niet raakt of 
	 * snijdt. Zorg ervoor dat beide kubussen met de muis kunnen worden geroteerd, 
	 * kunnen worden getransleerd en dat er op de kubussen kan worden in- en 
	 * uitgezoomd.
	 * 
	 * <code><pre>
	 * bg ---- [cube]
	 *    |--- [cube]
	 *    |--- prb(bg, canvas3D, bb)
	 *    |--- pzb(bg, canvas3D, bb)
	 *    '--- ptb(bg, canvas3D, bb)
	 *
	 * [cube]
	 * tg(t3d) -- cc
	 * </pre></code>
	 * 
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		TransformGroup cube1 = createTranslatedColorCube(new Vector3f(-0.5f, 0.0f, 0.0f));
		TransformGroup cube2 = createTranslatedColorCube(new Vector3f(0.5f, 0.0f, 0.0f));
		
		BranchGroup bg = new BranchGroup();

		BoundingBox bb = new BoundingBox();
		
		PickRotateBehavior prb = new PickRotateBehavior(bg, canvas3D, bb);
		PickZoomBehavior pzb = new PickZoomBehavior(bg, canvas3D, bb);
		PickTranslateBehavior ptb = new PickTranslateBehavior(bg, canvas3D, bb);
		
		bg.addChild(cube1);
		bg.addChild(cube2);
		bg.addChild(prb);
		bg.addChild(pzb);
		bg.addChild(ptb);
		bg.compile();
		return bg;
	}
}
