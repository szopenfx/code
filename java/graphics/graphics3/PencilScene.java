package graphics3;

import graphics.Scene;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleFanArray;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class PencilScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return Shape3D
	 */
	protected Shape3D createPencil()
	{
		final float by = -2.0f; // body y
		final float ty =  1.0f; // tip y
		final float ey =  2.5f; // end y

		float[] x = { 
				(float) Math.cos(0 / 3.0f * Math.PI),
				(float) Math.cos(1 / 3.0f * Math.PI),
				(float) Math.cos(2 / 3.0f * Math.PI),
				(float) Math.cos(3 / 3.0f * Math.PI),
				(float) Math.cos(4 / 3.0f * Math.PI),
				(float) Math.cos(5 / 3.0f * Math.PI) };

		float[] y = { 
				(float) Math.sin(0 / 3.0f * Math.PI),
				(float) Math.sin(1 / 3.0f * Math.PI),
				(float) Math.sin(2 / 3.0f * Math.PI),
				(float) Math.sin(3 / 3.0f * Math.PI),
				(float) Math.sin(4 / 3.0f * Math.PI),
				(float) Math.sin(5 / 3.0f * Math.PI) };

		int[] bottomCounts = { 8 };
		Point3f[] bottomPoints = { 
				new Point3f(0.0f, 0.0f, by),
				new Point3f(x[0], y[0], by),
				new Point3f(x[1], y[1], by),
				new Point3f(x[2], y[2], by),
				new Point3f(x[3], y[3], by),
				new Point3f(x[4], y[4], by),
				new Point3f(x[5], y[5], by),
				new Point3f(x[0], y[0], by) };

		int[] bodyCounts = { 14 };
		Point3f[] bodyPoints = { 
				new Point3f(x[0], y[0], by),
				new Point3f(x[0], y[0], ty),
				new Point3f(x[1], y[1], by),
				new Point3f(x[1], y[1], ty), 
				new Point3f(x[2], y[2], by),
				new Point3f(x[2], y[2], ty), 
				new Point3f(x[3], y[3], by),
				new Point3f(x[3], y[3], ty), 
				new Point3f(x[4], y[4], by),
				new Point3f(x[4], y[4], ty), 
				new Point3f(x[5], y[5], by),
				new Point3f(x[5], y[5], ty), 
				new Point3f(x[0], y[0], by),
				new Point3f(x[0], y[0], ty) };
		
		int[] tipCounts = { 8 };
		Point3f[] tipPoints = {
				new Point3f(0.0f, 0.0f, ey),
				new Point3f(x[0], y[0], ty), 
				new Point3f(x[1], y[1], ty),
				new Point3f(x[2], y[2], ty),
				new Point3f(x[3], y[3], ty),
				new Point3f(x[4], y[4], ty),
				new Point3f(x[5], y[5], ty),
				new Point3f(x[0], y[0], ty) };
		
		Color3f[] bottomColors = new Color3f[bottomCounts[0]]; 
		Color3f[] tipColors = new Color3f[tipCounts[0]]; 

		Color3f[] bodyColors = new Color3f[bodyCounts[0]];
		
		for(int i = 0; i < bottomColors.length; i++)
		{
			bottomColors[i] = new Color3f(0.5f, 0.25f, 0.0f);
			tipColors[i] = new Color3f(0.5f, 0.25f, 0.0f);
		}
		
		for(int i = 0; i < bodyColors.length; i++)
			bodyColors[i] = new Color3f(0.0f, 1.0f, 0.0f);

		// create bottom
		TriangleFanArray bottom = new TriangleFanArray(
				bottomCounts[0], 
				TriangleFanArray.COORDINATES | TriangleFanArray.COLOR_3, 
				bottomCounts);
		bottom.setCoordinates(0, bottomPoints);
		bottom.setColors(0, bottomColors);
		
		// create body
		TriangleStripArray body = new TriangleStripArray(
				bodyCounts[0], 
				TriangleStripArray.COORDINATES | TriangleStripArray.COLOR_3, 
				bodyCounts);
		body.setCoordinates(0, bodyPoints);
		body.setColors(0, bodyColors);
				
		// create tip
		TriangleFanArray tip = new TriangleFanArray(
				tipCounts[0], 
				TriangleFanArray.COORDINATES | TriangleFanArray.COLOR_3, 
				tipCounts);
		tip.setCoordinates(0, tipPoints);
		tip.setColors(0, tipColors);
		
		// create appearance
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		
		Appearance a = new Appearance();
		a.setPolygonAttributes(pa);
		
		// create shape
		Shape3D s3d = new Shape3D();
		s3d.setAppearance(a);
		s3d.addGeometry(bottom);
		s3d.addGeometry(body);
		s3d.addGeometry(tip);
		return s3d;
	}

	/**
	 * <pre><code>
	 * bg -- tg(t3d) -- tgrot ---- s3d 
	 *                        '--- rot(tg, a)
	 * </code></pre>
	 */
	protected BranchGroup createScene()
	{
		Shape3D s3d = createPencil();
		
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3f(0.0f, 0.0f, -5.0f));
		
		TransformGroup tg = new TransformGroup(t3d);
		
		TransformGroup tgrot = new TransformGroup();
		tgrot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha a = new Alpha(-1, 3000);
		
		RotationInterpolator rot = new RotationInterpolator(a, tgrot);
		rot.setSchedulingBounds(new BoundingBox());

		tg.addChild(tgrot);
		
		tgrot.addChild(s3d);
		tgrot.addChild(rot);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.compile();		
		return bg;
	}

}
