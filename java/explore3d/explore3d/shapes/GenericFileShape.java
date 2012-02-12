package explore3d.shapes;

import java.io.File;

import javax.media.j3d.Shape3D;

/**
 * Generic properties of a file shape - file name, selectability
 * @author J.J. Molenaar, J. Dooper
 */
public abstract class GenericFileShape extends Shape3D
{
	/**
	 * File object that belongs to the shape
	 */
	protected File _file;
	
	/**
	 * Store File object
	 * @param file File object that this shape represents
	 */
	public GenericFileShape(File file)
	{
		_file = file;
	}
	
	/**
	 * Retrieve File object
	 * @return File object
	 */
	public File getFile()
	{
		return _file;
	}
	
	/**
	 * Set selected to true or false - must be abstract because only inheriting classes know which
	 * appearance to use.
	 * @param selected Selected state
	 */
	public abstract void setSelected(boolean selected);
}
