package life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;

public abstract class LifeWorld 
{
	public int W, H, sz;

	protected int cursx, cursy;
	
	public static final Color CELLCOLOR = new Color(0x000080);
	public static final Color CURSORCOLOR = Color.RED;
	
	protected int[][] world;
	protected int[][] calc;
	
	public LifeWorld(Rectangle display, int size)
	{
		sz = size;
		setWorldSize(display);
		
		world = new int[W][H];
		calc = new int[W][H];
	}
	
	public void clear()
	{
		for (int x = 0; x < W; x++)
		{
			Arrays.fill(world[x], 0);
		}
	}
	
	protected synchronized void flip()
	{
		int[][] temp = calc;
		
		calc = world;
		world = temp;
	}
	
	public abstract void iterate();
	public abstract void paint(Graphics g);
	public abstract void set(int x, int y, int v);
	public abstract void setCursor(int x, int y);
	public abstract void inputCoord(int x, int y, int dragmode);
	public abstract void setWorldSize(Rectangle display);
}
