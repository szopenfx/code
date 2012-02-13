package graphics4;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.ColorCube;

import graphics.Scene;

public class ExclusiveZoomRotateCubesScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create translated ColorCube.
	 * @param v Translation vector
	 * @return TransformGroup
	 */
	protected TransformGroup createTranslatedColorCube(Vector3f v)
	{
		ColorCube cc = new ColorCube(0.3f);
		
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(v); 
		
		TransformGroup tg = new TransformGroup(t3d);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(cc);
		return tg;
	}
	
	/**
	 * Pas opdracht 2 zo aan dat op de eerste kubus met behulp van de muis 
	 * kan worden in- en uitgezoomd en dat de tweede kubus met behulp van 
	 * de muis geroteerd kan worden.
	 * 
	 * <code><pre>
	 * bg ---- tg -- cube1 [cube] -- mz(cube1) -- bb
	 *    '--- tg -- cube2 [cube] -- mr(cube2) -- bb
	 *    
	 * [cube]
	 * tg(t3d) -- cc
	 * </pre></code>
	 * 
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		TransformGroup cube1 = createTranslatedColorCube(new Vector3f(-0.5f, 0.0f, 0.0f));
		TransformGroup cube2 = createTranslatedColorCube(new Vector3f( 0.5f, 0.0f, 0.0f));
		
		BoundingBox bb = new BoundingBox();
		
		MouseZoom mz = new MouseZoom(cube1);
		mz.setSchedulingBounds(bb);

		MouseRotate mr = new MouseRotate(cube2);
		mr.setSchedulingBounds(bb);
		
		cube1.addChild(mz);
		cube2.addChild(mr);
				
		BranchGroup bg = new BranchGroup();
		bg.addChild(cube1);
		bg.addChild(cube2);
		bg.compile();
		return bg;
	}
}
