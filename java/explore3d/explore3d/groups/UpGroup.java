package explore3d.groups;

import java.io.File;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import explore3d.Constants;
import explore3d.DirectoryList;
import explore3d.shapes.DoorShape;
import explore3d.shapes.PanelShape;

/**
 * Create panel with door to parent directory
 * @author J.J. Molenaar, J.Dooper
 */
public class UpGroup extends TransformGroup
{
	/**
	 * Construct panel with door to parent directory
	 */
	public UpGroup()
	{
		createPanel();
		createDoor();
		createLabel();
	}

	/**
	 * Create panel object
	 */
	private void createPanel() 
	{
		addChild(
				PanelShape.createFrontPanel(
						DirGroup.calculateWidth() / 2, 
						Constants.ROOM_HEIGHT / 2)
		);
	}

	/**
	 * Create door object
	 */
	private void createDoor() 
	{
		if(DirectoryList.instance.getCurrentDirectory() != null)
		{
			float z = -2 * Constants.OFFSET;
			
			File parent = DirectoryList.instance.getCurrentDirectory().getParentFile();

			Transform3D t3d = new Transform3D();
			t3d.setTranslation(new Vector3f(0, 0, z));
			
			TransformGroup tg = new TransformGroup(t3d);
			tg.addChild(new DoorShape(parent));
			
			addChild(tg);
		}
	}

	/**
	 * Create label object
	 */
	private void createLabel() 
	{
		// TODO Put label here!
	}
}
