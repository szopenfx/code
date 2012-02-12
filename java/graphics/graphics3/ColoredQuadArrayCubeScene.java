package graphics3;

import graphics.Scene;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class ColoredQuadArrayCubeScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create a Shape3D that represents a cube [hopefully]
	 * and has some color, too
	 * 
	 * <pre><code>
	 *       E--H
	 *      /  /|
	 * H-> D--C G
	 *     |  |/
	 *     A--B
	 * </code></pre>
	 * 
	 * @return QuadArray
	 */
	private Shape3D createCube()
	{
		final float x = 0.3f;
		final float y = 0.3f;
		final float z = 0.3f;

		Point3f A = new Point3f(-x, -y,  z);
		Point3f B = new Point3f( x, -y,  z);
		Point3f C = new Point3f( x,  y,  z);
		Point3f D = new Point3f(-x,  y,  z);
		Point3f E = new Point3f(-x,  y, -z);
		Point3f F = new Point3f(-x, -y, -z);
		Point3f G = new Point3f( x, -y, -z);
		Point3f H = new Point3f( x,  y, -z);

		Point3f[] points = 
		{ 
				A, B, C, D,
				A, F, G, B,
				A, D, E, F,
				B, G, H, C,
				G, F, E, H,
				D, C, H, E
		};
		
		Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
		Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
		Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
		Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);
		
		Color3f[] colors = 
		{
				red, red, red, red,
				green, green, green, green,
				blue, blue, blue, blue,
				red, red, red, red,
				yellow, yellow, yellow, yellow,
				blue, blue, blue, blue,
		};

		QuadArray qa = new QuadArray(24, QuadArray.COORDINATES | QuadArray.COLOR_3);
		qa.setCoordinates(0, points);
		qa.setColors(0, colors);
	
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);		
		return s3d;
	}
	
	/**
	 * Maak kubus met behulp van QuadArray 
	 * 
	 * <pre><code>
	 * bg -- tg ---- s3d 
	 *          '--- rot(tg, a)
	 * </code></pre>
	 * 
	 * @return Branchgroup object
	 */
	protected BranchGroup createScene()
	{
		Shape3D s3d = createCube();

		Alpha a = new Alpha(-1, 3000);
		
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Transform3D t3d = new Transform3D();
		t3d.rotZ(HALF_PI);
		
		RotationInterpolator rot = new RotationInterpolator(a, tg, t3d, 0, TWO_PI);
		rot.setSchedulingBounds(new BoundingBox());
		
		tg.addChild(s3d);
		tg.addChild(rot);

		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.compile();
		return bg;
	}
}