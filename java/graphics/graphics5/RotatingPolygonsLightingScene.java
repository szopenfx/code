package graphics5;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import graphics.Scene;

/**
 * Creëer een vlak bestaande uit vier of meer polygonen met behulp van de 
 * TriangleStripArray en laat deze roteren rond de x-as. Zorg ervoor dat tevens 
 * de normalen van het vlak worden gespecificeerd en geef het vlak een uiterlijk 
 * door een kleur per vertex te specificeren. Voeg een ‘ambient’- en een 
 * ‘directional’-lichtbron toe en probeer verschillende kleuren en richtingen uit 
 * voor de ‘directional’-lichtbron.
 */
public class RotatingPolygonsLightingScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create a plane of four triangles
	 * @return Shape3D
	 */
	protected Shape3D createShape()
	{
		float x = 0.5f;
		float y = 0.5f;

		int[] stripcount = { 6 };
		
		// define points
		Point3f[] points =
		{
				new Point3f(  -x,  y, 0.0f),
				new Point3f(  -x, -y, 0.0f),
				new Point3f(0.0f,  y, 0.0f),
				new Point3f(0.0f, -y, 0.0f),
				new Point3f(   x,  y, 0.0f),
				new Point3f(   x, -y, 0.0f)
		};
		
		Color3f[] colors =
		{
				new Color3f(1.0f, 1.0f, 1.0f),
				new Color3f(1.0f, 1.0f, 1.0f),
				new Color3f(1.0f, 1.0f, 1.0f),
				new Color3f(1.0f, 1.0f, 1.0f),
				new Color3f(1.0f, 1.0f, 1.0f),
				new Color3f(1.0f, 1.0f, 1.0f)
		};

		Vector3f[] normals =
		{
				new Vector3f(0.0f, 0.0f, -1.0f),
				new Vector3f(0.0f, 0.0f, -1.0f),
				new Vector3f(0.0f, 0.0f, -1.0f),
				new Vector3f(0.0f, 0.0f, -1.0f),
				new Vector3f(0.0f, 0.0f, -1.0f),
				new Vector3f(0.0f, 0.0f, -1.0f)
		};
		
		// create geometry
		TriangleStripArray tsa = new TriangleStripArray(
				stripcount[0],
				TriangleStripArray.COORDINATES 
				| TriangleStripArray.NORMALS
				| TriangleStripArray.COLOR_3
				| TriangleStripArray.ALLOW_NORMAL_READ
				| TriangleStripArray.ALLOW_COLOR_WRITE,
				stripcount);
		tsa.setCoordinates(0, points);
		tsa.setColors(0, colors);
		tsa.setNormals(0, normals);
		
		// generate normal vectors ... wtf? doing something wrong here
		/*
		GeometryInfo gi = new GeometryInfo(tsa);
		gi.setStripCounts(stripcount);
		
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);
		*/
		
		// create appearance
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCapability(
				PolygonAttributes.ALLOW_NORMAL_FLIP_READ
				| PolygonAttributes.ALLOW_NORMAL_FLIP_WRITE);
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setBackFaceNormalFlip(true);
		
		Material m = new Material();
		m.setShininess(1.0f);
		
		Appearance a = new Appearance();
		a.setPolygonAttributes(pa);
		a.setMaterial(m);
		
		// create shape
		Shape3D s3d = new Shape3D();
		s3d.setGeometry(tsa);		
		s3d.setAppearance(a);
		return s3d;
	}
	
	/**
	 * <code><pre>
	 * bg -- tg ---- plane
	 *    |     '--- rot(a, tg)
	 *    |-- al
	 *    |-- dl1
	 *    '-- dl2
	 * </pre></code>
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		// create plane
		Shape3D plane = createShape();
		//Box plane = new Box(0.3f, 0.3f, 0.3f, null);
		
		// create rotation transform group
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		// create rotation
		Alpha a = new Alpha(-1, 3000);
	
		BoundingBox bb = new BoundingBox();
		
		RotationInterpolator rot = new RotationInterpolator(a, tg);
		rot.setSchedulingBounds(bb);
		
		// add children to transform group		
		tg.addChild(plane);
		tg.addChild(rot);
		
		// create ambient light
		AmbientLight al = new AmbientLight();
		al.setInfluencingBounds(bb);
		
		// create two directional lights
		DirectionalLight dl1 = new DirectionalLight(
				new Color3f(0.0f, 1.0f, 0.0f),
				new Vector3f(1.0f, 0.0f, 0.0f));
		dl1.setInfluencingBounds(bb);
		
		DirectionalLight dl2 = new DirectionalLight(
				new Color3f(0.0f, 0.0f, 1.0f),
				new Vector3f(-1.0f, 0.0f, 0.0f));
		dl2.setInfluencingBounds(bb);
				
		// add children to branch group
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.addChild(al);
		bg.addChild(dl1);
		bg.addChild(dl2);
		bg.compile();
		return bg;
	}
}
