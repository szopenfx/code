package explore3d.shapes;

import javax.media.j3d.Geometry;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import explore3d.themes.Theme;

/**
 * Class that creates a panel
 * @author J.J. Molenaar, J. Dooper
 */
public class PanelShape extends Shape3D
{	
	/**
	 * X "radius" of panel
	 */
	private float X;
	
	/**
	 * Y "radius" of panel
	 */
	private float Y;
	
	/**
	 * Z "radius" of panel
	 */
	private float Z;
	
	/**
	 * Size of a flat surface
	 */
	private static final float FLAT = 0.0001f;
	
	/**
	 * Create panel with certain sizes
	 * @param _X X size
	 * @param _Y Y size
	 * @param _Z Z size
	 */
	private PanelShape(float _X, float _Y, float _Z)
	{
		X = _X;
		Y = _Y;
		Z = _Z;

		setGeometry(createGeometry());
	}
	
	/**
	 * Create a panel on the side
	 * @param height Height of panel
	 * @param depth Depth of panel
	 * @return PanelShape instance
	 */
	public static PanelShape createSidePanel(float height, float depth)
	{
		PanelShape ps = new PanelShape(PanelShape.FLAT, height, depth);
		ps.setAppearance(Theme.getSide());
		return ps;
	}
	
	/**
	 * Create floor panel
	 * @param width Width of panel
	 * @param depth Height of panel
	 * @return PanelShape instance
	 */
	public static PanelShape createFloorPanel(float width, float depth)
	{
		PanelShape ps = new PanelShape(width, PanelShape.FLAT, depth);
		ps.setAppearance(Theme.getFloor());
		return ps;
	}
	
	/**
	 * Create ceiling panel
	 * @param width Width of panel
	 * @param depth Height of panel
	 * @return PanelShape instance
	 */
	public static PanelShape createCeilingPanel(float width, float depth)
	{
		PanelShape ps = new PanelShape(width, PanelShape.FLAT, depth);
		ps.setAppearance(Theme.getCeiling());
		return ps;
	}

	/**
	 * Create front or back panel
	 * @param width Width of panel
	 * @param height Height of panel
	 * @return PanelShape instance
	 */
	public static PanelShape createFrontPanel(float width, float height)
	{
		PanelShape ps = new PanelShape(width, height, PanelShape.FLAT);
		ps.setAppearance(Theme.getFront());
		return ps;
	}

	/**
	 * Creates the geometry for a door
	 * @return Door geometry
	 */
	private Geometry createGeometry()
	{		
		Point3f ldf = new Point3f(-X, -Y,  Z); // left down front
		Point3f luf = new Point3f(-X,  Y,  Z); // left up front
		Point3f rdf = new Point3f( X, -Y,  Z); // right down front
		Point3f ruf = new Point3f( X,  Y,  Z); // right up front
		
		Point3f ldb = new Point3f(-X, -Y, -Z); // left down front
		Point3f lub = new Point3f(-X,  Y, -Z); // left up front
		Point3f rdb = new Point3f( X, -Y, -Z); // right down front
		Point3f rub = new Point3f( X,  Y, -Z); // right up front
		
		Point3f[] points = {
				luf, ldf, rdf, ruf,
				ldf, ldb, rdb, rdf,
				ldb, lub, rub, rdb,
				lub, luf, ruf, rub,
				lub, ldb, ldf, luf, 
				ruf, rdf, rdb, rub			
				
		};
		
		QuadArray qa = new QuadArray(points.length,	QuadArray.COORDINATES);
		qa.setCoordinates(0, points);
		return qa;
	}
	
}
