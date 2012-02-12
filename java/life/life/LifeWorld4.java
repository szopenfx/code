package life;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class LifeWorld4 extends LifeWorld
{
	public LifeWorld4(Rectangle display, int size)
	{
		super(display, size);
	}
	
	public void setWorldSize(Rectangle display)
	{
		W = display.width / sz;
		H = display.height / sz;
	}
	
	public void paint(Graphics g)
	{
		g.setColor(CURSORCOLOR);
		g.fillRect(cursx * sz, cursy * sz, sz, sz);
		
		g.setColor(CELLCOLOR);
		
		for (int x = 0; x < W; x++)
		{
			for (int y = 0; y < H; y++)
			{
				if (world[x][y] == 1)
				{
					g.fillRect(x * sz, y * sz, sz, sz);
				}
			}
		}
	}
	
	public void set(int x, int y, int v)
	{
		world[x / sz][y / sz] = v;
	}
	
	public void setCursor(int x, int y)
	{
		cursx = x / sz;
		cursy = y / sz;
	}

	public synchronized void inputCoord(int x, int y, int dragmode)
	{
		world[x / sz][y / sz] = dragmode;
	}
	
	public static class Bounded extends LifeWorld4
	{
		public Bounded(Rectangle display, int sz)
		{
			super(display, sz);
		}
		
		public void iterate()
		{
			for (int x = 0; x < W; x++)
			{
			    for (int y = 0; y < H; y++)
			    {
			    	boolean NL = x > 0,
			    	        NR = x < (W-1),
			    	        NT = y > 0,
			    	        NB = y < (H-1);
			    	
		  	    	int v = 0;
			    	    	
		   	    	if (NT) v += world[x][y-1];
			    	if (NB) v += world[x][y+1];
			    	if (NL) v += world[x-1][y];
			    	if (NR) v += world[x+1][y];
		
			    	if (NT && NL) v += world[x-1][y-1];
		    		if (NT && NR) v += world[x+1][y-1];
			    	if (NB && NL) v += world[x-1][y+1];
			    	if (NB && NR) v += world[x+1][y+1];
			    	
			    	/* sides 4, neighbours 8, survival 23, birth 3 */
					calc[x][y] = world[x][y] == 1
					   		   ? (v == 2 || v == 3 ? 1 : 0)
					   		   : (v == 3           ? 1 : 0);
			    }
			}
			
			super.flip();
		}
	}

	public static class Circular extends LifeWorld4
	{
		public Circular(Rectangle display, int sz)
		{
			super(display, sz);
		}
		
		public void iterate()
		{
			for (int x = 0; x < W; x++)
			{
			    for (int y = 0; y < H; y++)
			    {
			    	boolean L = x == 0,
			    	        R = x == (W-1),
			    	        T = y == 0,
			    	        B = y == (H-1);
			    	
		  	    	int v = 0;
		  	    	
		  	    	v += world[x][T ? H-1 : y-1];
		  	    	v += world[x][B ? 0   : y+1];
		  	    	v += world[L ? W-1 : x-1][y];
		  	    	v += world[R ? 0   : x+1][y];
		  	    	
		  	    	v += world[L ? W-1 : x-1][T ? H-1 : y-1];
		  	    	v += world[R ? 0   : x+1][T ? H-1 : y-1];
		  	    	v += world[L ? W-1 : x-1][B ? 0   : y+1];
		  	    	v += world[R ? 0   : x+1][B ? 0   : y+1];
		  	    	
			    	/* sides 4, neighbours 8, survival 23, birth 3 */
		  	    	calc[x][y] = world[x][y] == 1
					   		   ? (v == 2 || v == 3 ? 1 : 0)
					   		   : (v == 3           ? 1 : 0);

			    }
			}
			
			super.flip();
		}
	}
}
