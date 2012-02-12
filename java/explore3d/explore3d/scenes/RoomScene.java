package explore3d.scenes;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import explore3d.behaviors.KeyNavigator;
import explore3d.behaviors.MouseLooker;
import explore3d.groups.DirGroup;
import explore3d.groups.FileGroup;
import explore3d.groups.UpGroup;
import explore3d.progress.Progress;
import explore3d.shapes.PanelShape;

public class RoomScene extends Scene
{
	/**
	 * Unnecessary field to shut up warning from Eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Dimensions of room
	 */
	private float W, D, H;

	/**
	 * Create room scene: A branchgroup with a transformgroup that holds the various groups and panels.
	 * Also adds behavior objects
	 * <code><pre>
	 * bg ---- tg ---- s3d [PanelShapes]
	 *    '--- knb(tgvp)
	 * </pre></code>
	 */
	protected void createScene()
	{
		_root = new BranchGroup();
		_root.setCapability(BranchGroup.ALLOW_DETACH);

		TransformGroup tg = new TransformGroup();

		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		addShapes(tg);
		addBehaviors(tg, _root);

		_root.addChild(tg);
		_root.compile();
	}
	
	/**
	 * Create sides of room
	 * @return TransformGroup containing all shapese
	 */
	private void addShapes(TransformGroup target)
	{
		W = DirGroup.calculateWidth() / 2;
		D = FileGroup.calculateDepth() / 2;
		H = 0.5f;
		
		TransformGroup result = new TransformGroup();

		result.addChild(createPanel( W, 0, 0, FileGroup.createRightPanel()));
		result.addChild(createPanel(-W, 0, 0, FileGroup.createLeftPanel()));
		result.addChild(createPanel(0, 0, -D, new DirGroup()));
		result.addChild(createPanel(0, 0, D, new UpGroup()));
		result.addChild(createPanel(0, -H, 0, PanelShape.createFloorPanel(W, D)));
		result.addChild(createPanel(0,  H, 0, PanelShape.createCeilingPanel(W, D)));
	
		target.addChild(result);
	}

	/**
	 * Add behaviors to transform group
	 * @param target
	 */
	private void addBehaviors(TransformGroup target, BranchGroup root)
	{
		// get view transform group
		TransformGroup view = _simpleUniverse.getViewingPlatform().getViewPlatformTransform();
		
		// explore3d.behaviors.KeyNavigator
		KeyNavigator keynavigator = new KeyNavigator(view, this);
		keynavigator.setSchedulingBounds(new BoundingBox()); 
		// TODO change scheduling bounds to something that does work

		// explore3d.behaviors.MouseLooker
		MouseLooker mouselooker = new MouseLooker(view, this);
		mouselooker.setSchedulingBounds(new BoundingBox());
		
		// add behaviors to target transform group
		target.addChild(keynavigator);
		target.addChild(mouselooker);
	}
	
	/**
	 * Utility method for use by createShapes
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param s TransformGroup or Shape3D (Nodes)
	 * @return TransformGroup that contains a panel
	 */
	private TransformGroup createPanel(float x, float y, float z, Node s)
	{
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3f(x, y, z));
		TransformGroup tg = new TransformGroup(t3d);
		tg.addChild(s);
		return tg;		
	}
	
	/**
	 * Load new scene into memory
	 * @param viewport Reset viewport location or keep it the way it is
	 */
	public void refreshScene(boolean viewport)
	{
		// remove branchgraph
		_simpleUniverse.getLocale().removeBranchGraph(_root);
		_root = null; // deader! deader!
		
		// collect garbage
		System.gc();
		
		// create new scene
		createScene();
		
		// add scene to universe
		if(viewport)
			_simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		_simpleUniverse.addBranchGraph(_root);
		
		Progress.report(this, "New 3D scene loaded");
	}
}
