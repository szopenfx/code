package explore3d.scenes;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import com.sun.j3d.utils.universe.SimpleUniverse;

import explore3d.progress.Progress;

/**
 * Class that puts a SimpleUniverse and a Canvas3D onto an applet
 * @author J.J. Molenaar
 */
public abstract class Scene extends Applet
{
	/**
	 * Utility field
	 */
	protected static final float PI = (float) Math.PI;
	
	/**
	 * Utility field
	 */
	protected static final float HALF_PI = 0.5f * ((float) Math.PI);
	
	/**
	 * Utility field
	 */
	protected static final float TWO_PI = 2.0f * ((float) Math.PI);

	/**
	 * Graphics configuration
	 */
	public GraphicsConfiguration _graphicsConfig;
	
	/**
	 * 3D canvas
	 */
	public Canvas3D _canvas3D;
	
	/**
	 * SimpleUniverse - this gives problems with boundingbox
	 */
	public SimpleUniverse _simpleUniverse;
	
	/**
	 * Is used by ShapeTest's rotator object
	 */
	public Object _data;
	
	/**
	 * Root branch group
	 */
	public BranchGroup _root;
	
	/**
	 * Create scene object
	 */
	public Scene()
	{
		Progress.report(this, "Creating 3D scene");
		createCanvas();
	}
	
	/**
	 * Create scene object with an Object object
	 * @param data Is in ShapeTest supposed to be a Shape3D object
	 */
	public Scene(Object data)
	{
		_data = data;
		createCanvas();
	}
	
	/**
	 * Create 3D canvas
	 */
	private void createCanvas()
	{
		Progress.report(this, "Creating 3D universe");

		// create canvas and universe
		_graphicsConfig = SimpleUniverse.getPreferredConfiguration();
		_canvas3D = new Canvas3D(_graphicsConfig);
		_simpleUniverse = new SimpleUniverse(_canvas3D);

		// create scene -- this should also set that bounding box!
		createScene();
		
		// move viewer to standard location
		_simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		_simpleUniverse.addBranchGraph(_root);

		// add canvas to applet
		setLayout(new BorderLayout());
		add("Center", _canvas3D);
	}
	
	/**
	 * Create scene; that is, add a branch group
	 */
	protected abstract void createScene();
}
