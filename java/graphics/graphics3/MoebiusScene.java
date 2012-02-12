package graphics3;

import graphics.Scene;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

public class MoebiusScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Calculate X coordinate for Möbius strip chunk
	 * @param u Radial (0..TWO_PI)
	 * @param v Height on strip (-1..1)
	 * @return X coordinate
	 */
	protected float calcX(float u, float v)
	{
		float cos_half_u = ((float) Math.cos(u / 2));
		float cos_u = ((float) Math.cos(u));
		return (1 + v / 2 * cos_half_u) * cos_u;
	}

	/**
	 * Calculate Y coordinate for Möbius strip chunk
	 * @param u Radial (0..TWO_PI)
	 * @param v Height on strip (-1..1)
	 * @return Y coordinate
	 */
	protected float calcY(float u, float v)
	{
		float sin_half_u = ((float) Math.sin(u / 2)); 
		return v / 2 * sin_half_u;
	}
	
	/**
	 * Calculate Z coordinate for Möbius strip chunk
	 * @param u Radial (0..TWO_PI)
	 * @param v Height on strip (-1..1)
	 * @return Z coordinate
	 */
	protected float calcZ(float u, float v)
	{
		float cos_half_u = ((float) Math.cos(u / 2));
		float sin_u = ((float) Math.sin(u));
		return (1 + v / 2 * cos_half_u) * sin_u;
	}
	
	/**
	 * Create colored Möbius strip
	 * @see <a href="http://en.wikipedia.org/wiki/Mobius_strip">Wikipedia: Mobius Strip</a> 
	 * @return Shape3D
	 */
	protected Shape3D createMoebius()
	{
		int i;
		
		// should be multiple of 3 for coloring
		//final int chunks = 1440;
		final int chunks = 90;
		
		// top coordinates
		float[] xt = new float[chunks];
		float[] yt = new float[chunks];
		float[] zt = new float[chunks];
		
		// bottom coordinates
		float[] xb = new float[chunks];
		float[] yb = new float[chunks];
		float[] zb = new float[chunks];
		
		// points for quads
		Point3f[] points = new Point3f[chunks * 4];

		// colors for quads
		Color3f[] colors = new Color3f[chunks * 4];
		
		// calculate the points of all the chunks
		for(int u = 0; u < chunks; u++)
		{
			float v = u / ((float) chunks) * TWO_PI;
			xt[u] = calcX(v, 1.0f);
			yt[u] = calcY(v, 1.0f);
			zt[u] = calcZ(v, 1.0f);
			xb[u] = calcX(v, -1.0f);
			yb[u] = calcY(v, -1.0f);
			zb[u] = calcZ(v, -1.0f);
			//System.out.println("" + u + " : " + xt[u] + " " + yt[u] + " " + zt[u] + " : " + xb[u] + " " + yb[u] + " " + zb[u]);
		}
		
		// generate the Point3f objects for QuadArray
		i = 0;
		for(int c = 0; c < chunks - 1; c++)
		{
			points[i++] = new Point3f(xt[c], yt[c], zt[c]);
			points[i++] = new Point3f(xb[c], yb[c], zb[c]);
			points[i++] = new Point3f(xb[c+1], yb[c+1], zb[c+1]);
			points[i++] = new Point3f(xt[c+1], yt[c+1], zt[c+1]);
		}
		
		// make the last quad 
		// ...... this shouldn't really work: top/bottom/top/bottom is weird
		int c = chunks - 1;
		points[i++] = new Point3f(xt[0], yt[0], zt[0]);
		points[i++] = new Point3f(xb[c], yb[c], zb[c]);
		points[i++] = new Point3f(xt[c], yt[c], zt[c]);
		points[i++] = new Point3f(xb[0], yb[0], zb[0]);
		
		// generate the colors
		i = 0;
		float delta = 1.0f / (chunks / 3);
		float red = 0.0f;
		float green = 1.0f;
		float blue = 0.0f;
		
		// -green, +red
		for(c = 0; c < chunks / 3; c++)
		{
			green -= delta;
			red += delta;
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
		}
		
		// -red, +blue
		for(c = chunks / 3; c < chunks * 2 / 3; c++)
		{
			red -= delta;
			blue += delta;
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
		}
		
		// -blue, +green
		for(c = chunks * 2 / 3; c < chunks; c++)
		{
			blue -= delta;
			green += delta;
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
			colors[i++] = new Color3f(red, green, blue);
		}
		
		// generate the shape
		QuadArray qa = new QuadArray(
				4 * chunks, 
				QuadArray.COORDINATES | QuadArray.COLOR_3);
		qa.setCoordinates(0, points);
		qa.setColors(0, colors);
		
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		
		Appearance app = new Appearance();
		app.setPolygonAttributes(pa);
		
		Shape3D s3d = new Shape3D();
		s3d.setGeometry(qa);
		s3d.setAppearance(app);
		
		return s3d;
	}
	
	/**
	 * <code><pre>
	 * bg -- tg(t3d) -- tgrot ---- s3d
	 *                        '--- rot(a, tg)
	 * </pre></code>
	 * 
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		Shape3D s3d = createMoebius();
		
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(0, 0, -1));
		
		TransformGroup tg = new TransformGroup(t3d);
		
		TransformGroup tgrot = new TransformGroup();
		tgrot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha a = new Alpha(-1, 5 * 3000);
		
		RotationInterpolator rot = new RotationInterpolator(a, tgrot);
		rot.setSchedulingBounds(new BoundingBox());
		
		tgrot.addChild(s3d);
		tgrot.addChild(rot);
		
		tg.addChild(tgrot);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.compile();
		return bg;
	}
}
