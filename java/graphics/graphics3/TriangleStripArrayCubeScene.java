package graphics3;

import graphics.Scene;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Point3f;

public class TriangleStripArrayCubeScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return Shape3D
	 */
	protected Shape3D createCube()
	{
		final float x = 0.3f;
		final float y = 0.3f;
		final float z = 0.3f;
		
		final int pointcount = 13;
		final int[] stripcount = { pointcount };
		
		Point3f[] points = 
		{
				new Point3f( x,  y, -z), // 0
				new Point3f(-x,  y, -z), // 1
				new Point3f( x,  y,  z), // 2
				new Point3f(-x,  y,  z), // 3
				new Point3f(-x, -y,  z), // 4
				new Point3f(-x,  y, -z), // 5
				new Point3f(-x, -y, -z), // 6
				new Point3f( x,  y, -z), // 7
				new Point3f( x, -y, -z), // 8
				new Point3f( x,  y,  z), // 9
				new Point3f( x, -y,  z), // 10
				new Point3f(-x, -y,  z), // 11
				new Point3f( x, -y, -z), // 12
				new Point3f(-x, -y, -z), // 13
		};
		
		TriangleStripArray tsa = new TriangleStripArray(
				3 * pointcount, 
				TriangleStripArray.COORDINATES, 
				stripcount);
		tsa.setCoordinates(0, points);
				
		Shape3D s3d = new Shape3D();
		s3d.setGeometry(tsa);
		
		return s3d;
	}
	
	/**
	 * <pre><code>
	 * bg -- tg ---- s3d
	 *          '--- rot(a, tg)
	 * </code></pre>
	 * 
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		Shape3D s3d = createCube();
		
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha a = new Alpha(-1, 3000);
						
		RotationInterpolator rot = new RotationInterpolator(a, tg);
		rot.setSchedulingBounds(new BoundingBox());
		
		tg.addChild(s3d);
		tg.addChild(rot);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.compile();
		return bg;
	}
}
