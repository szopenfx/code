package life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class LifePanel extends DoubleBuffer
{
	private static final long serialVersionUID = 0;
	
	public int sz;
	private int v = 0;
	private Life life;

	private String status = "";
	
	public LifePanel(Life L, int size)
	{
		sz = size;
		life = L;

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				v = e.getButton() == MouseEvent.BUTTON1 ? 1
				  : e.getButton() == MouseEvent.BUTTON3 ? 0 : 0;
				life.world.set(e.getX() / sz, e.getY() / sz, v);
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e)
			{
				life.world.set(e.getX() / sz, e.getY() / sz, v);
				repaint();
			}
		});
	}
	
	public void paintBuffer(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (int x = 0; x < life.world.W; x++)
		for (int y = 0; y < life.world.H; y++)
		{
			g.setColor(life.world.world[x][y] == 0 
					   ? Color.BLACK 
					   : Color.GREEN);
			g.fillRect(x * sz, y * sz, sz, sz);
		}

		g.setColor(Color.GRAY);
		g.drawString(status, 50, 0 + 10);
		
		life.itersem.release();
	}

	public void setStatus(String s)
	{
		status = s;
	}
}
