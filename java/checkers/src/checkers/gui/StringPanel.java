package checkers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

public class StringPanel extends DoubleBuffer
{
	protected Observer observer;
	protected Vector<String> strings = new Vector<String>();
	
	public int item = -1;
	protected int top = 0;
	protected int lineheight;
	protected int linecount;
	
	protected Rectangle button = new Rectangle();
	protected String buttontext;
	
	protected static final Color SELECTEDTEXT = Color.GREEN;
	protected static final Color NORMALTEXT = Color.WHITE;
	protected static final Color BUTTONCOLOR = Color.GRAY;
	protected static final Color BUTTONTEXT = Color.WHITE;
	
	public StringPanel(String buttonText, Observer o)
	{
		observer = o;
		buttontext = buttonText;
	
		initEvents();
	}
	
	public static interface Observer
	{
		public void buttonClicked(int item);
		public void itemClicked(int item);
		public void itemCanceled();
	}

	private void initEvents()
	{
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					item = -1;
					observer.itemCanceled();
				}
				else
				{
					int x = e.getX(), y = e.getY();
					
					if (button.contains(x, y))
						observer.buttonClicked(item);
					
					if (y > 0 && y < (lineheight * linecount))
					{
						item = top + y / lineheight; 
						if (item < strings.size())
							observer.itemClicked(item);
						else
							observer.itemCanceled();
					}
					else
						observer.itemCanceled();
				}
				repaint();
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				int r = e.getWheelRotation();
				
				if (r == -1 && top > 0)
					top--;
				
				if (r == +1 && top < (strings.size() - linecount + 1))
					top++; 
				
				repaint();
			}
		});
	}

	public void addItem(String text)
	{
		strings.add(text);
		
		if (linecount > 0)
			if (strings.size() > (top + linecount - 1))
				top = strings.size() - linecount + 1;

		item = -1;

		repaint();
	}

	public void removeItem()
	{
		strings.remove(strings.size() - 1);
		repaint();
	}
	
	public void clear(boolean repaint)
	{
		item = -1;
		top = 0;
		strings.clear();
		if (repaint)
			repaint();
	}
	
	public void paintBuffer(Graphics g)
	{
		Dimension size = getSize();
		
		g.setFont(new Font("Courier", Font.PLAIN, 12));
		
		lineheight = g.getFontMetrics().getHeight();
		linecount = size.height / lineheight - 1;
		
		for (int i = 0; i < linecount - 1; i++)
			if ((top + i) < strings.size())
			{
				String s = strings.get(top + i);
				g.setColor(item == (top + i) ? SELECTEDTEXT : NORMALTEXT);
				g.drawString(s, 0, (i + 1) * lineheight);
			}
		
		button.setBounds(2 * size.width / 3, linecount * lineheight, size.width / 3 - 1, lineheight);
		
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		int lineheight2 = g.getFontMetrics().getHeight();
		
		g.setColor(BUTTONCOLOR);
		g.drawRect(button.x, button.y, button.width, button.height);
		
		g.setColor(BUTTONTEXT);
		g.drawString(buttontext, 
				button.x + (button.width / 2) - (g.getFontMetrics().stringWidth(buttontext) / 2), 
				button.y + lineheight2 - 2);
	}
}
