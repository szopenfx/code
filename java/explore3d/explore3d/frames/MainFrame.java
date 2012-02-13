package explore3d.frames;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;

import explore3d.Constants;
import explore3d.Explore3D;
import explore3d.progress.Progress;
import explore3d.scenes.RoomScene;
import explore3d.scenes.Scene;
import explore3d.shapes.GenericFileShape;
import explore3d.shapes.WindowShape;
import explore3d.themes.Theme;
import explore3d.themes.ThemeCache;

/**
 * creates displaywindow with frames,panels and controls
 * on the frame the scene and panel is set. the panel contains controls.
 * * @author J.J. Molenaar, J. Dooper
 */

public class MainFrame implements MainFrameInterface
{
	private Frame _frame = new Frame();
	private Panel _panel = new Panel();
	private Panel _controls = new Panel();
	
	private BoxLayout _layout_frame = new BoxLayout(_frame, BoxLayout.Y_AXIS);
	private BoxLayout _layout_panel = new BoxLayout(_panel, BoxLayout.Y_AXIS);
	private BoxLayout _layout_controls = new BoxLayout(_controls, BoxLayout.X_AXIS);
	
	private Choice _cho_theme = new Choice();
	private Checkbox _cbx_rotate = new Checkbox("Select / (Rotate)");
	private Checkbox _cbx_log = new Checkbox("Log window");
	private Button _btn_quit = new Button();
	
	private Label _lbl_information = new Label();
	
	private Scene _scene = new RoomScene();
	//private Scene _scene = new ShapeTest(PanelShape.createFlatPanel(2.0f, 2.0f));
	
	private LogFrame _logframe = new LogFrame();

	/**
	 * constants are set for image_observer and main_frame
	 */
	
	public MainFrame()
	{
		Constants.MAIN_FRAME = this;
		Constants.IMAGE_OBSERVER = _scene;
		
		initializeForm();
	}
	
	/**
	 * init form:
	 * 
	 * layouts
	 * colors
	 * add panels to frame
	 * button quit properties
	 * label quit properties
	 * theme items
	 * theme event handler
	 * checkbox rotate properties
	 * checkbox log properties
	 * add label and controls panel to panel
	 * add controls to controls panel
	 * window close event handler
	 * frame properties
	 */
	
	private void initializeForm()
	{
		// set layouts
		_frame.setLayout(_layout_frame);
		_panel.setLayout(_layout_panel);
		_controls.setLayout(_layout_controls);

		// set colors
		_frame.setBackground(Color.BLACK);

		// add panels to frame
		_frame.add(_scene);
		_frame.add(_panel);
		
		// set button quit properties
		_btn_quit.setLabel("Quit");
		_btn_quit.setMaximumSize(new Dimension(100, 30));
		_btn_quit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				System.exit(0);
			}
		});
		
		// set label quit properties
		_lbl_information.setForeground(Color.WHITE);
		
		// set choice theme items
		_cho_theme.add("explore3d.themes.WireframeTheme");
		_cho_theme.add("explore3d.themes.MatrixTheme");
		_cho_theme.add("explore3d.themes.DoomTheme");
		
		// set choice theme event handler
		_cho_theme.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie)
			{
				try
				{
					String classname = (String) ie.getItem();
					Class themeclass = Class.forName(classname);
					Theme.setTheme((ThemeCache) themeclass.newInstance());
					Constants.MAIN_FRAME.refresh(false);
				}
				catch(ClassNotFoundException cnfex)
				{
					cnfex.printStackTrace();
				}
				catch(InstantiationException iex)
				{
					iex.printStackTrace();
				}
				catch(IllegalAccessException iaex)
				{
					iaex.printStackTrace();
				}
			}			
		});
		
		// set checkbox rotate properties
		_cbx_rotate.setForeground(Color.WHITE);
		_cbx_rotate.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				Constants.allowRotateToggle();
			}
		});
		
		// set checkbox log properties
		_cbx_log.setForeground(Color.WHITE);
		_cbx_log.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				Progress.addReporter(_logframe);
				_logframe.setVisible(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		
		// add label and controls panel to panel
		_panel.add(_lbl_information);
		_panel.add(_controls);
		
		// add controls to controls panel
		_controls.add(_cbx_rotate);
		_controls.add(_cbx_log);
		_controls.add(_cho_theme);
		_controls.add(_btn_quit);
		
		// set window close event handler
		_frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		// set frame properties
		_frame.pack();
		_frame.setBounds(0, 0, 800, 600);
		_frame.setTitle("Explore3D");
		_frame.setVisible(true);

		Progress.report(this, "AWT interface loaded");
	}
	
	/**
	 * sets label directory to frame
	 */
	
	public void displayDirectory(GenericFileShape shape)
	{
		File file = shape.getFile();
		String dir = file != null 
			? file.toString()
			: "Root";
		_frame.setTitle(dir + " - Explore3D");
	}
	
	/**
	 * sets label file to shape
	 */
	public void displayFile(GenericFileShape shape)
	{
		File file = shape != null ? shape.getFile() : null;
		if(file != null)
		{
			String path = file.getAbsolutePath();
			String paren = shape instanceof WindowShape
				? Explore3D.representByteSize(file.length())
				: "directory";
			_lbl_information.setText(path + " (" + paren + ")");
		}
		else
			_lbl_information.setText("");
	}
	
	/**
	 * checks if viewport is true to refreshscene
	 */
	
	public void refresh(boolean viewport)
	{
		((RoomScene) _scene).refreshScene(viewport);
	}
	
	/**
	 * checks if rotate is true to allow rotating
	 */

	public void setRotate(boolean rotate)
	{
		_cbx_rotate.setState(rotate);
		_cbx_rotate.setLabel(Constants.allowRotate()
				? "Rotate / (Select)"
				: "Select / (Rotate)"
		);
	}
	
	/**
	 * checks if logvisible is true to allow log window to be visible
	 */

	public void setLogVisible(boolean logvisible)
	{
		_cbx_log.setState(logvisible);
	}
}
