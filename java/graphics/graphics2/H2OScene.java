package graphics2;

import graphics.Scene;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Ontwerp een complexere vorm zoals een fles met behulp van geometrische primitieven. 
 * Gebruik daarbij drie of meer primitieven en zorg ervoor dat alle primitieven netjes
 * op elkaar aansluiten.
 * @author J.J. Molenaar
 */
public class H2OScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create translated and colored sphere
	 * @param radius Radius of sphere
	 * @param ca Colorint attributes of sphere
	 * @param trans Translation vector for sphere
	 * @return TransformGroup
	 */
	private TransformGroup translatedColoredSphere(float radius, ColoringAttributes ca, Vector3f trans)
	{
		Appearance a = new Appearance();
		Sphere s = new Sphere(radius, Sphere.GENERATE_NORMALS, 64, a);
		Transform3D t3d = new Transform3D();
				
		a.setColoringAttributes(ca);
		t3d.setTranslation(trans);
		
		TransformGroup tg = new TransformGroup(t3d);
		tg.addChild(s);
		
		return tg;
	}
	
	/**
	 * Create a translated colored cylinder
	 * @param radius
	 * @param height
	 * @param ca
	 * @param trans
	 * @return TransformGroup
	 */
	private TransformGroup translatedColoredCylinder(float radius, float height, ColoringAttributes ca, 
													 Vector3f trans)
	{
		Appearance a = new Appearance();
		Cylinder c = new Cylinder(radius, height);
		Transform3D t3d = new Transform3D();
		
		t3d.rotZ(Math.PI * 0.5f);
				
		a.setColoringAttributes(ca);
		c.setAppearance(a);
		
		t3d.setTranslation(trans);
		
		TransformGroup tg = new TransformGroup(t3d);
		tg.addChild(c);
		
		return tg;
	}
	
	/**
	 * Maak H2O molecuul.
	 * 
	 * <pre><code>
	 * bg ---- ml ---- hydro_1 [sphere]
	 *    |       |--- hydro_2 [sphere]
	 *    |       |--- oxygen [sphere]
	 *    |       |--- conn_1 [cylinder]
	 * 	  |       '--- conn_2 [cylinder]
	 *    '--- rotation(alpha, ml)
	 *    
	 *  [sphere]:
	 *  	tg(t3d) -- sphere -- appearance
	 *  
	 *  [cylinder]:
	 *  	tg(t3d) -- cylinder -- appearance
	 *  </pre></code>
	 *    
	 * @return BranchGroup dinges 
	 */
	protected BranchGroup createScene()
	{
		BranchGroup bg = new BranchGroup();
		TransformGroup ml = new TransformGroup();

		TransformGroup hydro_1 = translatedColoredSphere(
				0.2f, 
				new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.NICEST),
				new Vector3f(1.0f, 0.0f, 0.1f)
		);
		
		TransformGroup hydro_2 = translatedColoredSphere(
				0.2f, 
				new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.NICEST),
				new Vector3f(-1.0f, 0.0f, 0.1f)
		);
		
		TransformGroup oxygen = translatedColoredSphere(
				0.5f, 
				new ColoringAttributes(0.0f, 1.0f, 0.0f, ColoringAttributes.NICEST),
				new Vector3f(0.0f, 0.0f, 0.0f)
		);
		
		TransformGroup conn_1 = translatedColoredCylinder(
				0.01f,
				0.7f,
				new ColoringAttributes(1.0f, 0.0f, 0.0f, ColoringAttributes.NICEST),
				new Vector3f(0.8f, 0.0f, 0.0f)
		);
		
		TransformGroup conn_2 = translatedColoredCylinder(
				0.01f,
				0.7f,
				new ColoringAttributes(1.0f, 0.0f, 0.0f, ColoringAttributes.NICEST),
				new Vector3f(-0.8f, 0.0f, 0.0f)
		);
		
		ml.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Alpha alpha = new Alpha(-1, 3000);
		RotationInterpolator rotation = new RotationInterpolator(alpha, ml);
		rotation.setSchedulingBounds(new BoundingSphere());
		
		ml.addChild(hydro_1);
		ml.addChild(hydro_2);
		ml.addChild(oxygen);
		ml.addChild(conn_1);
		ml.addChild(conn_2);
		
		bg.addChild(ml);
		bg.addChild(rotation);
		
		bg.compile();
		return bg;
	}
}
