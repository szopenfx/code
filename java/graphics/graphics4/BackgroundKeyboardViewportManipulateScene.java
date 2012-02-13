package graphics4;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;

import graphics.Scene;

public class BackgroundKeyboardViewportManipulateScene extends Scene
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
	 * Voeg aan opdracht 5 een achtergrond toe met een bepaalde kleur.
	 * 
	 * <code><pre>
	 * bg ---- tg -- cube1 [cube] 
	 *    |--- tg -- cube2 [cube]
	 *    |--- background -- bb
	 *    '--- knb(tgvp)
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
		
		Background background = new Background(0.2f, 0.4f, 0.6f);
		background.setApplicationBounds(bb);

		TransformGroup tgvp = simpleUniverse.getViewingPlatform().getViewPlatformTransform();

		KeyNavigatorBehavior knb = new KeyNavigatorBehavior(tgvp);
		knb.setSchedulingBounds(bb);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(cube1);
		bg.addChild(cube2);
		bg.addChild(background);
		bg.addChild(knb);
		bg.compile();
		return bg;
	}
}
