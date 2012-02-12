package explore3d.groups;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import explore3d.Constants;
import explore3d.DirectoryList;
import explore3d.shapes.WindowShape;
import explore3d.shapes.PanelShape;

/**
 * Creates a left or right file wall using FileShape, LabelGroup and PanelShape objects.
 * @author J.J. Molenaar, J. Dooper
 */
public class FileGroup extends TransformGroup
{
	private int _side;
	private int _filecount;
	private int _start;
	private int _end;
	private static final int LEFT_SIDE  = 43345;
	private static final int RIGHT_SIDE = 89274;

	/**
	 * Create file group for right or left side
	 * @param side One of LEFT_SIDE or RIGHT_SIDE
	 */
	private FileGroup(int side)
	{
		_side = side;
		calculateStartEnd();
		createPanel();
		createFiles();
		createFileNames();
	}
	
	/**
	 * Create panel on left side
	 * @return FileGroup instance for left side
	 */
	public static FileGroup createLeftPanel()
	{
		return new FileGroup(LEFT_SIDE);
	}
	
	/**
	 * Create panel on right side
	 * @return FileGroup instance for right side
	 */
	public static FileGroup createRightPanel()
	{
		return new FileGroup(RIGHT_SIDE);
	}
	
	/**
	 * Calculate the start and end indices for the left or right file panel
	 */
	private void calculateStartEnd()
	{
		_filecount = DirectoryList.instance.fileCount();
		if(_side == LEFT_SIDE)
		{
			_start = 0;
			_end = _filecount / 2;
		}
		else // _side == RIGHT_SIDE
		{
			_start = _filecount / 2;
			_end = _filecount;
		}
	}

	/**
	 * Calculate length of entire file panel
	 * @return meters
	 */
	public static float calculateDepth()
	{
		int n = (DirectoryList.instance.fileCount() + 1) / 2;
		float depth = (n+1) * Constants.SPACE + n * Constants.WINDOW_WIDTH;
		if(depth < 5.5f) 
			depth = 5.5f;
		return depth;
	}

	/**
	 * Create panel object
	 */
	private void createPanel()
	{
		addChild(PanelShape.createSidePanel(0.5f, calculateDepth()/2));
	}

	/**
	 * Create window objects for each file
	 */
	private void createFiles()
	{
		float d = calculateDepth();
		for(int i = _start; i < _end; i++)
		{
			float x, z;
			if(_side == RIGHT_SIDE)
			{
				x = -Constants.OFFSET;
				z = -d/2 
					+ (i - _start + 1) * Constants.SPACE 
					+ (i - _start) * Constants.WINDOW_WIDTH
					+ Constants.WINDOW_WIDTH/2;
			}
			else // _side == LEFT_SIDE
			{
				x = Constants.OFFSET;
				z = d/2 
					- (i+1) * Constants.SPACE 
					- i * Constants.WINDOW_WIDTH
					- Constants.WINDOW_WIDTH/2;
			}

			Transform3D t3d = new Transform3D();
			t3d.setTranslation(new Vector3f(x, 0.15f, z));
			TransformGroup tg = new TransformGroup(t3d);
			
			tg.addChild(new WindowShape(DirectoryList.instance.getFile(i)));
			addChild(tg);
		}
	}

	/**
	 * Create label objects for each file
	 */
	private void createFileNames() 
	{
		float d = calculateDepth();
		for(int i = _start; i < _end; i++)
		{
			float x, z;
			if(_side == RIGHT_SIDE)
			{
				x = -2 * Constants.OFFSET;
				z = -d/2 
					+ (i - _start + 1) * Constants.SPACE 
					+ (i - _start) * Constants.WINDOW_WIDTH;
			}
			else // _side == LEFT_SIDE
			{
				x = 2 * Constants.OFFSET;
				z = d/2 
					- (i+1) * Constants.SPACE 
					- i * Constants.WINDOW_WIDTH;
			}

			Transform3D trans = new Transform3D();
			trans.setTranslation(new Vector3f(x, 0.15f, z));
			
			Transform3D rot = new Transform3D();

			if(_side == RIGHT_SIDE) rot.rotY((float) Math.PI / -2);
			if(_side == LEFT_SIDE)  rot.rotY((float) Math.PI / 2);
			
			trans.mul(rot);
			
			TransformGroup tg = new TransformGroup(trans);
			
			String name = DirectoryList.instance.getFile(i).getName();
			
			tg.addChild(new LabelGroup(name));
			addChild(tg);
		}
	}
}
