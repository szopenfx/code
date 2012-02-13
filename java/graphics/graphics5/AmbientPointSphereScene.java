package graphics5;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Sphere;

import graphics.Scene;

/**
 * Voeg aan opdracht 1 drie ‘point’-lichtbronnen toe, met elk een andere kleur, 
 * die de primitieve van opdracht 1 van buiten belichten. Laat  elke ‘point’-lichtbron 
 * om een andere as van het assenstelsel roteren.
 * @author J.J. Molenaar
 */
public class AmbientPointSphereScene extends Scene
{
	private static final long serialVersionUID = 1L;
	private static final int ROTATE_X = 1;
	private static final int ROTATE_Y = 2;
	private static final int ROTATE_Z = 3;

	private BoundingBox bb;
	
	/**
	 * Create a transform object with a rotation of HALF_PI over an axis
	 * @param rotation ROTATE_X, ROTATE_Y or ROTATE_Z
	 * @return Transform3D object
	 */
	protected Transform3D createRotationTransform(int rotation)
	{
		Transform3D t3d = new Transform3D();
		
		switch(rotation)
		{
			case ROTATE_X:
				t3d.rotX(HALF_PI);
				break;
			case ROTATE_Z:
				t3d.rotZ(HALF_PI);
				break;
		}
		
		return t3d;
	}
	
	/**
	 * Create PointLight node
	 * 
	 * <code><pre>
	 * tgrot --- pl -- bb
	 *       '-- rot(a, tg, t3drot, 0, TWO_PI) -- bb
	 * </pre></code>
	 * 
	 * @param color Color of light
	 * @param position Position of light
	 * @return PointLight
	 */
	protected TransformGroup createPointLight
			(int rotation, Color3f color, Point3f position)
	{
		PointLight light = new PointLight();
		light.setColor(color);
		light.setPosition(position);
		light.setInfluencingBounds(bb);
		
		Transform3D t3drot = createRotationTransform(rotation);

		TransformGroup tgrot = new TransformGroup();
		tgrot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Alpha a = new Alpha(-1, 3000);
		
		RotationInterpolator rot = new RotationInterpolator	(a, tgrot, t3drot, 0.0f, TWO_PI);
		rot.setSchedulingBounds(bb);
		rot.setTransformAxis(t3drot);
		
		tgrot.addChild(light);
		tgrot.addChild(rot);
		return tgrot;
	}
	
	/**
	 * <code><pre>
	 * bg --- s -- app -- m
	 *    |-- ambient -- bb
	 *    |-- light1 [pointlight]
	 *    |-- light2 [pointlight]
	 *    |-- light3 [pointlight]
	 *    '-- mr(bg)
	 * </pre></code>
	 * @see {createPointLight}
	 */
	protected BranchGroup createScene()
	{
		bb = new BoundingBox();
		
		// create ambient and point lights
		AmbientLight ambient = new AmbientLight();
		ambient.setInfluencingBounds(bb);
		
		TransformGroup point1 = createPointLight(
				ROTATE_X,
				new Color3f(1.0f, 0.0f, 0.0f), 
				new Point3f(1.0f, 0.0f, 0.0f));
		TransformGroup point2 = createPointLight(
				ROTATE_Y,
				new Color3f(0.0f, 1.0f, 0.0f), 
				new Point3f(0.0f, 0.0f, -1.0f));
		TransformGroup point3 = createPointLight(
				ROTATE_Z,
				new Color3f(0.0f, 0.0f, 1.0f), 
				new Point3f(0.0f, -1.0f, 0.0f));
		
		// create appearance
		Material m = new Material();
		m.setShininess(1.0f);
		
		Appearance app = new Appearance();
		app.setMaterial(m);
		
		// create object
		//Box b = new Box(0.3f, 0.3f, 0.3f, Box.GENERATE_NORMALS, app);
		Sphere s = new Sphere(0.3f, Sphere.GENERATE_NORMALS, 36, app);

		// create transform group for mouse rotator
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		// create keyboard navigator
		TransformGroup tgvp = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
		KeyNavigatorBehavior knb = new KeyNavigatorBehavior(tgvp);
		knb.setSchedulingBounds(bb);
		
		// create mouse rotator
		MouseRotate mr = new MouseRotate(tg);
		mr.setSchedulingBounds(bb);
		
		// populate transform group
		tg.addChild(s);
		tg.addChild(mr);
		
		// create branch group
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.addChild(knb);
		bg.addChild(ambient);
		bg.addChild(point1);
		bg.addChild(point2);
		bg.addChild(point3);
		bg.compile();
		return bg;
	}
}
