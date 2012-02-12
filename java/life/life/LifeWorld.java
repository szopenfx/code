package life;

import java.util.Arrays;

public abstract class LifeWorld 
{
	public int W;
	public int H;
	
	public int[][] world;
	private int[][] calc;

	public LifeWorld(int width, int height)
	{
		W = width;
		H = height;
		
		world = new int[W][H];
		calc = new int[W][H];
	}
	
	public void clear()
	{
		for (int x = 0; x < W; x++)
			Arrays.fill(world[x], 0);
	}
	
	public void set(int x, int y, int v)
	{
		if (0 <= x && x < W  &&  0 <= y && y < H)
			world[x][y] = v;
	}

	private synchronized void flip()
	{
		int[][] temp = calc;
		
		calc = world;
		world = temp;
	}
	
	public abstract void iterate();
	
	public static class Bounded extends LifeWorld
	{
		public Bounded(int w, int h)
		{
			super(w, h);
		}
		
		public void iterate()
		{
			for (int x = 0; x < W; x++)
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
		    	
		    	if (world[x][y] == 1) 
		    		super.calc[x][y] = v == 2 || v == 3 ? 1 : 0;
		    	else
		    		super.calc[x][y] = v == 3 ? 1 : 0;
		    }
			super.flip();
		}
	}

	public static class Circular extends LifeWorld
	{
		public Circular(int w, int h)
		{
			super(w, h);
		}
		
		public void iterate()
		{
			for (int x = 0; x < W; x++)
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
	  	    	
		    	if (world[x][y] == 1) 
		    		super.calc[x][y] = v == 2 || v == 3 ? 1 : 0;
		    	else
		    		super.calc[x][y] = v == 3 ? 1 : 0;
		    }
			super.flip();
		}
	}
}
