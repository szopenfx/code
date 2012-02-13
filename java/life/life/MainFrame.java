package life;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends Frame
{
	private static final long serialVersionUID = 0;
	
	private Life life;
	
	public LifePanel lifepanel;
	public ControlPanel ctrlpanel;
	
	public MainFrame(Life L)
	{
		life = L;
		
		setLayout(null);
		setBackground(Color.GRAY);
		
		createLifePanel();
		createControlPanel();
		
		add(lifepanel);
		add(ctrlpanel);
		
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds());
		
		ctrlpanel.setBounds(0,  0, getWidth(), 35);
		lifepanel.setBounds(0, 35, getWidth(), getHeight() - 35);
		
		//setBounds(0, 0, 810, 665);
		setUndecorated(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0)
			{
				System.exit(0);
			}
		});
	}

	private void createControlPanel()
	{
		ctrlpanel = new ControlPanel(life);
	}

	private void createLifePanel()
	{
		lifepanel = new LifePanel(life, 5);
	}
}
