package explore3d.shapes;

import java.io.File;

import javax.media.j3d.Geometry;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import explore3d.Constants;
import explore3d.themes.Theme;

/**
 * Shape for a normal file (a window)
 * @author J.J. Molenaar, J. Dooper
 */
public class WindowShape extends GenericFileShape
{
	/**
	 * Construct file shape for a particular file
	 * @param file File for label and execution
	 */
	public WindowShape(File file)
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
		setAppearance(Theme.getWindow(false));
		setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}

	/**
	 * Create the geometry for a window
	 * @return Window geometry
	 */
	private Geometry createGeometry()
	{
		float X = Constants.FLAT;
		float Y = Constants.WINDOW_HEIGHT / 2;
		float Z = Constants.WINDOW_WIDTH / 2;
		
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
				p_LDF, p_RDF, p_RUF, p_LUF,
				// left
				p_LUB, p_LDB, p_LDF, p_LUF,
				// right
				p_RUF, p_RDF, p_RDB, p_RUB,
				// up
				p_LUF, p_RUF, p_RUB, p_LUB,
				// below
				p_LDF, p_RDF, p_RDB, p_LDB,
				// back
				p_RUB, p_RDB, p_LDB, p_LUB
		};
		
		QuadArray qa = new QuadArray(points.length,	QuadArray.COORDINATES);
		qa.setCoordinates(0, points);
		return qa;
	}

	/**
	 * Set selected status of shape (affects appearance)
	 * @param selected Selected or normal
	 */
	public void setSelected(boolean selected)
	{
		Constants.MAIN_FRAME.displayFile(selected ? this : null);
		setAppearance(Theme.getWindow(selected));
	}
}
