package explore3d.groups;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import explore3d.shapes.LabelShape;

/**
 * Group of LabelShape objects
 * @author J.J. Molenaar, J. Dooper
 */
public class LabelGroup extends TransformGroup
{
	/**
	 * Create LabelGroup for a particular string
	 * @param name Name to display
	 */
	public LabelGroup(String name) 
	{
		createLabels(name);
	}

	/**
	 * Create label objects
	 * @param name Name to create
	 */
	private void createLabels(String name) 
	{
		final int CUTOFF = 16;
		int height = 0;
		while(name.length() > 0)
		{
			String first;
			if(name.length() > CUTOFF)
			{
				first = name.substring(0, CUTOFF);
				name = name.substring(CUTOFF);
			}
			else
			{
				first = name;
				name = "";
			}
			
			Transform3D t3d = new Transform3D();
			t3d.setTranslation(new Vector3f(0.0f, -0.05f * height++, 0.0f));
			
			TransformGroup tg = new TransformGroup(t3d);
			tg.addChild(new LabelShape(first));
			
			addChild(tg);
		}
	}
}
