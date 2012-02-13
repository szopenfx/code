package life;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

public abstract class DoubleBuffer extends Panel
{
	private Graphics graphics;
	
	protected Image image;
	protected Dimension size;
	
	public DoubleBuffer()
	{
		super();
	}
	
	private void initialize()
	{
		size = getSize();
		
		if (graphics != null)
		{
			graphics.dispose();
			graphics = null;
		}
		
		if (image != null)
		{
			image.flush();
			image = null;
		}
		
		image = createImage(size.width, size.height);
		graphics = image.getGraphics();
	}

	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		Dimension sz = getSize();
		if (size == null || size.width != sz.width || size.height != sz.height || image == null || graphics == null)
			initialize();
		
		if (graphics != null)
		{
			graphics.clearRect(0, 0, size.width, size.height);
			paintBuffer(graphics);
			g.drawImage(image, 0, 0, this);
		}
	}
	
	public abstract void paintBuffer(Graphics g);
}
