package explore3d.frames;

import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;

import explore3d.progress.ProgressReporter;
import explore3d.Constants;
/**
 * When selected a logframe appears that shows a messagelog of the actions and info 
 * of the explore3d program
 * * @author J.J. Molenaar, J. Dooper
 */
public class LogFrame implements ProgressReporter
{
	private Frame _frame = new Frame("Explore3D messages");
	private List _list = new List();
	private BoxLayout _layout = new BoxLayout(_frame, BoxLayout.X_AXIS);
	
	public LogFrame()
	{
		initializeComponents();
	}
/**
 * makes a 800 by 600 window with courier font
 *
 */
	private void initializeComponents()
	{
		_list.setFont(Font.decode("Courier"));
		
		_frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				_frame.setVisible(false);
				Constants.MAIN_FRAME.setLogVisible(false);
			}
		});
		
		_frame.setLayout(_layout);
		_frame.add(_list);
		_frame.pack();
		_frame.setBounds(800, 0, 400, 600);
	}
	
	/**
	 * if checkbox log is enabled v = true
	 * @param v
	 */
	
	public void setVisible(boolean v)
	{
		_frame.setVisible(v);
	}

	/**
	 * display messages in logwindow
	 */
	
	public void message(Object sender, String message)
	{
		String name = sender == null
			? "Explore3D"
			: sender.getClass().getSimpleName();
		_list.add(String.format("%-15s: %s", new Object[] { name, message }));
		_list.makeVisible(_list.getItemCount() - 1);
	}
	
	/**
	 * clear logwindow and close
	 */

	public void clear()
	{
		_list.removeAll();
	}
}
