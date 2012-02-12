package explore3d.groups;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import explore3d.Constants;
import explore3d.DirectoryList;
import explore3d.shapes.DoorShape;
import explore3d.shapes.PanelShape;

/**
 * Transform group that puts DoorShapes, PanelShapes and LabelGroups together so that they
 * form a wall with labeled doors for directories.
 * @author J.J. Molenaar, J. Dooper
 */
public class DirGroup extends TransformGroup
{
	/**
	 * Construct DirGroup object; it calculates its own size and position
	 */
	public DirGroup()
	{
		createPanel();
		createDoors();
		createDirNames();
	}
	
	/**
	 * Calculate width of entire panel
	 * @return Width of panel in floats
	 */
	public static float calculateWidth()
	{
		int n = DirectoryList.instance.dirCount();
		float width = (n+1) * Constants.SPACE + n * Constants.DOOR_WIDTH;
		if(width < 3.0f) 
			width = 3.0f;
		return width;
	}
	
	/**
	 * Create a front panel for background
	 */
	private void createPanel()
	{
		addChild(PanelShape.createFrontPanel(
				calculateWidth() / 2, 
				Constants.ROOM_HEIGHT / 2));
	}

	/**
	 * Create door objects for each directory
	 */
	private void createDoors()
	{
		float d = calculateWidth();
		int n = DirectoryList.instance.dirCount();
		for(int i = 0; i < n; i++)
		{
			float x = -d/2 
					+ (i * Constants.DOOR_WIDTH) 
					+ (i+1) * Constants.SPACE
					+ Constants.DOOR_WIDTH/2;

			Transform3D t3d = new Transform3D();
			t3d.setTranslation(new Vector3f(x, 0, Constants.OFFSET));
			
			TransformGroup tg = new TransformGroup(t3d);
			tg.addChild(new DoorShape(DirectoryList.instance.getDir(i)));
			
			addChild(tg);
		}
	}

	/**
	 * Create label objects for each directory
	 */
	private void createDirNames()
	{
		float d = calculateWidth();
		int n = DirectoryList.instance.dirCount();
		for(int i = 0; i < n; i++)
		{
			Transform3D t3d = new Transform3D();
			
			float x = -d/2 + i * Constants.DOOR_WIDTH + (i+1) * Constants.SPACE;
			t3d.setTranslation(new Vector3f(x, 0, 2 * Constants.OFFSET));
			
			String name = DirectoryList.instance.getDir(i).getName();
			if(name.equals(""))
				name = DirectoryList.instance.getDir(i).toString();
			
			TransformGroup tg = new TransformGroup(t3d);
			tg.addChild(new LabelGroup(name));
			
			addChild(tg);
		}
	}
}
