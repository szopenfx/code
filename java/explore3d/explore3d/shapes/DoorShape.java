package explore3d.shapes;

import java.io.File;

import javax.media.j3d.Geometry;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import explore3d.Constants;
import explore3d.themes.Theme;

/**
 * Shape of a door
 * @author J.J. Molenaar, J. Dooper
 */
public class DoorShape extends GenericFileShape
{	
	/**
	 * Create shape of door for a particular file
	 * @param file File object for label and navigation
	 */
	public DoorShape(File file)
	{
		super(file);
		createShape();
	}
	
	/**
	 * Create the shape
	 */
	private void createShape()
	{
		Geometry g = createGeometry();
		setGeometry(g);
		setAppearance(Theme.getDoor(false));
		setCapability(Shape3D.ALLOW_APPEARANCE_WRITE); // for selection
	}

	/**
	 * Create the geometry for a door
	 * @return Door geometry
	 */
	private Geometry createGeometry()
	{
		float X = Constants.DOOR_WIDTH / 2;
		float Y = Constants.DOOR_HEIGHT / 2;
		float Z = Constants.FLAT;
		
		Point3f p_LDF = new Point3f(-X, -Y,  Z); // left down front
		Point3f p_LUF = new Point3f(-X,  Y,  Z); // left up front
		Point3f p_RDF = new Point3f( X, -Y,  Z); // right down front
		Point3f p_RUF = new Point3f( X,  Y,  Z); // right up front
		
		Point3f p_LDB = new Point3f(-X, -Y, -Z); // left down front
		Point3f p_LUB = new Point3f(-X,  Y, -Z); // left up front
		Point3f p_RDB = new Point3f( X, -Y, -Z); // right down front
		Point3f p_RUB = new Point3f( X,  Y, -Z); // right up front
		
		Point3f[] points = {
				// front
				p_LUF, p_LDF, p_RDF, p_RUF,
				// left
				p_LUB, p_LDB, p_LDF, p_LUF,
				// right
				p_RUF, p_RDF, p_RDB, p_RUB,
				// up
				p_LUF, p_RUF, p_RUB, p_LUB,
				// below
				p_LDF, p_RDF, p_RDB, p_LDB,
				// back
				p_LDB, p_LUB, p_RUB, p_RDB
		};
		
		QuadArray qa = new QuadArray(points.length,	QuadArray.COORDINATES);
		qa.setCoordinates(0, points);
		return qa;
	}

	/**
	 * Set selected state of door
	 * @param selected Obvious
	 */
	public void setSelected(boolean selected)
	{
		Constants.MAIN_FRAME.displayFile(selected ? this : null);
		setAppearance(Theme.getDoor(selected));
	}
}
