package life;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class LifePanel extends Object
{
	private Life life;
	
	private int width;
	private int height;

	public boolean drawstatus = false;
	public String statustext = "";
	
	public boolean drawhelp = false;
	public String[] helptext = new String[12];
	
	public LifePanel(Life L, int w, int h)
	{
		life = L;
		
		width = w;
		height = h;
	}
	
	public void paintBuffer(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		life.world.paint(g);
		
		if (drawstatus)
		{
			g.setColor(Color.GRAY);
			g.drawString(statustext, 0, 0 + 10);
		}
	}
}
