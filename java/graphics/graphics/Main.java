package graphics;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Class for starting Java3D scenes
 * @author J.J. Molenaar
 */
public class Main
{
	private Frame frame = new Frame();
	private GridLayout grid = new GridLayout(0, 1);
	private Vector labels = new Vector();
	
	private static final Color FOREGROUND = new Color(128, 128, 128);
	private static final Color HOVERING = new Color(0, 0, 0);
	private static final Color BACKGROUND = new Color(255, 255, 255);

	private Object[] scenes = { 
			"1.5", graphics1.TranslatedCubeScene.class,
			"2.1.1 a", graphics2.CylinderScene.class, 
			"2.1.1 b", graphics2.RotatingCylinderScene.class, 
			"2.1.3 & 2.1.4", graphics2.H2OScene.class, 
			"2.2.1 & 2.2.2", graphics2.ThreeObjectsScene.class, 
			"2.2.3", graphics2.ThreeRotatingObjectsScene.class, 
			"2.3.1", graphics3.QuadArrayCubeScene.class, 
			"2.3.2", graphics3.ColoredQuadArrayCubeScene.class, 
			"2.3.3", graphics3.TriangleStripArrayCubeScene.class, 
			"2.3.4", graphics3.PencilScene.class, 
			"2.3.5", graphics3.MoebiusScene.class,
			"3.1", graphics4.ColorCubeRotateScene.class,
			"3.2", graphics4.ColorCubesRotateTranslateZoomScene.class,
			"3.3", graphics4.ExclusiveZoomRotateCubesScene.class,
			"3.4", graphics4.ViewportManipulateScene.class,
			"3.5", graphics4.KeyboardViewportManipulateScene.class,
			"3.6", graphics4.BackgroundKeyboardViewportManipulateScene.class,
			"4.1", graphics5.AmbientCubeScene.class,
			"4.2", graphics5.AmbientPointSphereScene.class,
			"4.3", graphics5.RotatingPolygonsLightingScene.class,
			"4.4", graphics5.TranslatedVisualObjectScene.class,
			"4.5", graphics5.LightedTranslatedVisualObjectScene.class
	};

	/**
	 * Constructor for Main class
	 */
	public Main()
	{
		initFrame();
	}

	/**
	 * Initialize the frame
	 */
	private void initFrame()
	{
		frame.setLayout(grid);

		// for every title/class pair 
		for(int i = 0; i < scenes.length; i += 2)
		{
			// get name and class objects
			String name_ = (String) scenes[i];
			Class class_ = (Class) scenes[i+1];
			String caption = name_ + "   -   " + class_.getSimpleName();
			
			// create new label
			Label label = new Label(caption);
			label.setBackground(BACKGROUND);
			label.setForeground(FOREGROUND);
			
			// event : label clicked, create scene
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					int i = labels.indexOf(e.getSource());
					Class c = (Class) scenes[i * 2 + 1];
					createScene(c);
				}
				
				public void mouseEntered(MouseEvent e)
				{
					((Label) e.getSource()).setForeground(HOVERING);
				}
				
				public void mouseExited(MouseEvent e)
				{
					((Label) e.getSource()).setForeground(FOREGROUND);
				}
			});

			// add label to frame
			frame.add(label);
			labels.add(label);
		}

		// event : application closing
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		frame.setTitle("Java3D");
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Create a Frame with a Scene applet in it
	 * @param c The class of the Scene to create
	 */
	private void createScene(Class c)
	{
		frame.setCursor(Cursor.WAIT_CURSOR);		
		try
		{
			Scene scene = (Scene) c.newInstance();
			Frame f = new Frame();

			f.setLocation(frame.getWidth(), 0);
			f.setSize(256, 256);
			f.add(scene);
			f.setVisible(true);
			f.setTitle(c.getName());
			
			f.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e)
				{
					((Frame) e.getComponent()).dispose();
				}
			});
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		frame.setCursor(Cursor.DEFAULT_CURSOR);
	}
	
	/**
	 * Create a frame that displays all known scene classes
	 * @param args Ignored
	 */
	public static void main(String[] args)
	{
		new Main();
	}
}
