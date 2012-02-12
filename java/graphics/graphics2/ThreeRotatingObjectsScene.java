package graphics2;

import graphics.Scene;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Laat het eerste object gecreëerd in opdracht 1 rond de y-as, het tweede rond de 
 * x-as en het derde rond de z-as roteren.
 * @author J.J. Molenaar
 */
public class ThreeRotatingObjectsScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * <pre><code>
	 *  bg ---- tg1(tr1) -- tg1rot -- box1 ---- a1
	 *     |                               |--- pa1
	 *     |                               '--- rot1(alpha1, tg1rot)
	 *     |--- tg2(tr2) -- tg2rot -- sphere2 ---- a2
	 *     |                                  |--- pa2
	 *     |                                  '--- rot2(alpha2, tg2rot, tr2rot, 0, 2pi)
	 *     '--- tg3(tr3) -- tg3rot -- box3 ---- a3
	 *                                     '--- rot3(alpha3, tg3rot, tr3rot, 0, 2pi)
	 *  </code></pre>
	 *  
	 *  Note : switching the tgN with tgNrot objects causes the objects to rotate around
	 *  the origin of the scene
	 *  
	 * @return A branchgroup scene
	 */
	protected BranchGroup createScene()
	{
		// box 1
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(new Vector3d(-1, 0, 0));

		TransformGroup tg1rot = new TransformGroup();
		tg1rot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha alpha1 = new Alpha(-1, 3000);

		Transform3D tr1rot = new Transform3D();
		tr1rot.rotX(HALF_PI);
		
		RotationInterpolator rot1 = new RotationInterpolator
			(alpha1, tg1rot, tr1rot, 0.0f, TWO_PI);
		rot1.setSchedulingBounds(new BoundingBox());

		PolygonAttributes pa1 = new PolygonAttributes();
		pa1.setPolygonMode(PolygonAttributes.POLYGON_LINE);

		Appearance a1 = new Appearance();
		a1.setColoringAttributes(new ColoringAttributes(1.0f, 0.0f, 0.0f, ColoringAttributes.NICEST));
		a1.setPolygonAttributes(pa1);

		Box box1 = new Box(0.3f, 0.3f, 0.3f, a1);
		box1.addChild(rot1);
		
		TransformGroup tg1 = new TransformGroup(tr1);
		tg1.addChild(tg1rot);

		tg1rot.addChild(box1);
		
		// box 2
		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(new Vector3d(0, 0, 0));

		TransformGroup tg2rot = new TransformGroup();
		tg2rot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha alpha2 = new Alpha(-1, 3000);

		Transform3D tr2rot = new Transform3D();
		tr2rot.rotY(HALF_PI);
		
		RotationInterpolator rot2 = new RotationInterpolator
			(alpha2, tg2rot, tr2rot, 0.0f, TWO_PI);
		rot2.setSchedulingBounds(new BoundingBox());

		PolygonAttributes pa2 = new PolygonAttributes();
		pa2.setPolygonMode(PolygonAttributes.POLYGON_POINT);

		Appearance a2 = new Appearance();
		a2.setColoringAttributes(new ColoringAttributes(0.0f, 1.0f, 0.0f, ColoringAttributes.NICEST));
		a2.setPolygonAttributes(pa2);

		Sphere sphere2 = new Sphere(0.3f, a2);
		sphere2.addChild(rot2);
		
		TransformGroup tg2 = new TransformGroup(tr2);
		tg2.addChild(tg2rot);

		tg2rot.addChild(sphere2);

		// box 3
		Transform3D tr3 = new Transform3D();
		tr3.setTranslation(new Vector3d(1, 0, 0));

		TransformGroup tg3rot = new TransformGroup();
		tg3rot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha alpha3 = new Alpha(-1, 3000);

		Transform3D tr3rot = new Transform3D();
		tr3rot.rotZ(HALF_PI);
		
		RotationInterpolator rot3 = new RotationInterpolator
			(alpha3, tg3rot, tr3rot, 0.0f, TWO_PI);
		rot3.setSchedulingBounds(new BoundingBox());

		PolygonAttributes pa3 = new PolygonAttributes();
		pa3.setPolygonMode(PolygonAttributes.POLYGON_LINE);

		Appearance a3 = new Appearance();
		a3.setColoringAttributes(new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.NICEST));
		a3.setPolygonAttributes(pa3);

		Box box3 = new Box(0.3f, 0.3f, 0.3f, a3);
		box3.addChild(rot3);
		
		TransformGroup tg3 = new TransformGroup(tr3);
		tg3.addChild(tg3rot);

		tg3rot.addChild(box3);
		
		// create branch group
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg1);
		bg.addChild(tg2);
		bg.addChild(tg3);
		bg.compile();
		return bg;
	}
}
