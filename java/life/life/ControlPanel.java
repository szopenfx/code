package life;

import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControlPanel extends Panel
{
	private static final long serialVersionUID = 0;
	
	private Life life;
	
	private void addButton(String title, MouseListener onClick)
	{
		Button b = new Button(title);
		b.addMouseListener(onClick);
		b.setForeground(Color.GRAY);
		b.setBackground(Color.BLACK);
		add(b);
	}
	
	public ControlPanel(Life L)
	{
		life = L;
		
		setBackground(Color.BLACK);
		
		addButton("start", new MouseAdapter() {
			public void mouseClicked(MouseEvent e) 
			{
				life.start();
			}
		});

		addButton("stop", new MouseAdapter() {
			public void mouseClicked(MouseEvent e) 
			{
				life.stop();
			}	
		});
		
		addButton("step", new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				life.world.iterate();
				life.gfx.repaint();
			}
		});
		
		addButton("clear", new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				life.world.clear();
				life.gfx.repaint();
			}
		});
		
		addButton("-", new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				life.nappytime += 1;
			}
		});

		addButton("+", new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				if (life.nappytime > 0)
					life.nappytime -= 1;
			}
		});
	}
}
