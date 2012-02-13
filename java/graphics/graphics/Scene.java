package graphics;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import com.sun.j3d.utils.universe.SimpleUniverse;

public abstract class Scene extends Applet
{
	protected static final float PI = (float) Math.PI;
	protected static final float HALF_PI = 0.5f * ((float) Math.PI);
	protected static final float TWO_PI = 2.0f * ((float) Math.PI);

	//public static String opdracht;
	
	protected GraphicsConfiguration graphicsConfig;
	protected Canvas3D canvas3D;
	protected SimpleUniverse simpleUniverse;
	
	public Scene()
	{
		graphicsConfig = SimpleUniverse.getPreferredConfiguration();
		canvas3D = new Canvas3D(graphicsConfig);
		simpleUniverse = new SimpleUniverse(canvas3D);
		
		BranchGroup scene = createScene();
		simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		simpleUniverse.addBranchGraph(scene);

		setLayout(new BorderLayout());
		add("Center", canvas3D);
	}
	
	protected abstract BranchGroup createScene();
}
