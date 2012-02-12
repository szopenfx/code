package life;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

public abstract class LifeWorld3 extends LifeWorld
{
	private Polygon[][] poly = new Polygon[W][H];
		
	public LifeWorld3(Rectangle display, int size)
	{
		super(display, size);
	
		int hsz = sz / 2;
		
		for (int x = 0; x < W / 2; x++)
		{
			for (int y = 0; y < H; y++)
			{
				int ax, bx, cx, dx;
				int ay, by, cy, dy;
				
				if (y % 2 == 0)
				{
					// A---B
					//  \ / \
					//   C---D 
					ax = x * sz;			bx = ax + sz;		cx = ax + hsz;		dx = ax + sz + hsz;
					ay = y * sz;			by = ay;			cy = ay + sz;		dy = ay + sz;
				}
				else
				{
					//   C---D
					//  / \ /
					// A---B
					ax = x * sz;	 		bx = ax + sz;		cx = ax + hsz;		dx = ax + sz + hsz;
					ay = y * sz + sz;		by = ay;			cy = ay - sz;		dy = ay - sz;
				}
				
				poly[2 * x    ][y] = new Polygon(new int[] { ax, bx, cx },
										 		 new int[] { ay, by, cy },
										 		 3);				
				
				poly[2 * x + 1][y] = new Polygon(new int[] { bx, cx, dx },
												 new int[] { by, cy, dy },
												 3);				
			}
		}
	}
	
	public void setWorldSize(Rectangle display)
	{
		W = display.width / sz * 2 - 2;
		H = display.height / sz;
	}

	public void paint(Graphics g)
	{
		g.setColor(CURSORCOLOR);
		g.fillPolygon(poly[cursx][cursy]);
		
		g.setColor(CELLCOLOR);
		
		for (int x = 0; x < W; x++)
		{
			for (int y = 0; y < H; y++)
			{
				if (world[x][y] == 1)
				{
					g.fillPolygon(poly[x][y]);
				}
			}
		}
	}
	
	public void set(int x, int y, int v)
	{
		int yc = y / sz;
		
		if (yc < H)
		{
			for (int xc = 0; xc < W; xc++)
			{
				if (poly[xc][yc].contains(x, y))
				{
					world[xc][yc] = v;
				}
			}
		}
	}
	
	public void setCursor(int x, int y)
	{
		int yc = y / sz;
		
		if (yc < H)
		{
			for (int xc = 0; xc < W; xc++)
			{
				if (poly[xc][yc].contains(x, y))
				{
					cursx = xc;
					cursy = yc;
				}
			}
		}
	}
	
	public synchronized void inputCoord(int x, int y, int dragmode)
	{
		int yc = y / sz;
		
		if (yc < H)
		{
			for (int xc = 0; xc < W; xc++)
			{
				if (poly[xc][yc].contains(x, y))
				{
					world[xc][yc] = dragmode;
				}
			}
		}
	}

	//public abstract void iterate();
	
	// o---o---o---o---o
	//  \ / \ / \ / \ / \
	//   o---o---o---o---o
	//  / \ / \ / \ / \ /
	// o---o---o---o---o
	//  \ / \ / \ / \ / \
	//   o---o---o---o---o
	//  / \ / \ / \ / \ /
	// o---o---o---o---o
	
	public static class Bounded extends LifeWorld3
	{
		public Bounded(Rectangle display, int sz)
		{
			super(display, sz);
		}

		public void iterate()
		{
			for (int y = 0; y < H; y++)
			{
				for (int x = 0; x < W; x++)
				{
					boolean down = (y % 2) == (x % 2);
					boolean NT = y > 0,
							NB = y < (H-1),
							NL = x > 0,
							NR = x < (W-1);

					int v;
					
					if (down)
					{
						v = (NT ? world[x][y-1] : 0)
						  + (NL ? world[x-1][y] : 0)
						  + (NR ? world[x+1][y] : 0);
					}
					else
					{
						v = (NB ? world[x][y+1] : 0)
						  + (NL ? world[x-1][y] : 0)
						  + (NR ? world[x+1][y] : 0);
					}

					/* sides 3, neighbours 3, survival 3, birth 0 */
					calc[x][y] = world[x][y] == 1
							   ? (v == 3 || v == 3 ? 1 : 0)
							   : (v == 0           ? 1 : 0);
				}
			}
			super.flip();
		}
	}

	public static class Circular extends LifeWorld3
	{
		public Circular(Rectangle display, int sz)
		{
			super(display, sz);
		}

		public void iterate()
		{
			for (int y = 0; y < H; y++)
			{
				for (int x = 0; x < W; x++)
				{
					boolean down = (y % 2) == (x % 2);
					boolean NT = y > 0,
							NB = y < (H-1),
							NL = x > 0,
							NR = x < (W-1);

					int v;
					
					if (down)
					{
						v = (NT ? world[x][y-1] : world[x][H-1])
						  + (NL ? world[x-1][y] : world[W-1][y])
						  + (NR ? world[x+1][y] : world[0][y]);
					}
					else
					{
						v = (NB ? world[x][y+1] : world[x][0])
						  + (NL ? world[x-1][y] : world[W-1][y])
						  + (NR ? world[x+1][y] : world[0][y]);
					}

					/* sides 3, neighbours 3, survival 3, birth 0 */
					calc[x][y] = world[x][y] == 1
							   ? (v == 3 || v == 3 ? 1 : 0)
							   : (v == 0           ? 1 : 0);
				}
			}
			super.flip();
		}
	}
}
