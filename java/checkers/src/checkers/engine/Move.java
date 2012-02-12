package checkers.engine;

import java.util.Vector;

public class Move extends Vector<Integer> /*implements Comparable<Move>*/
{
	public Integer value;
	
	public Move(int... coords)
	{
		for (int coord : coords)
			add(coord);
	}
	
	public Move(Move m)
	{
		for (int coord : m)
			add(coord);
	}

	public Move clone()
	{
		Move newmove = new Move();
		for (int c : this)
			newmove.add(c);
		return newmove;
	}
	
	public boolean equals(Object other)
	{
		if (other.getClass() != Move.class)
			return false;
		
		Move m = (Move) other;

		if (m.size() != size())
			return false;
		
		for (int i = 0; i < size(); i++)
			if (m.get(i) != get(i))
				return false;

		return true;
	}
	
	/*public int compareTo(Move other)
	{
		return other.value != null 
			&& value != null
			? other.value.compareTo(value)
			: 0;
	}*/
	
	public String toString()
	{
		if (size() == 2)
		{
			return String.format("%d-%d", get(0) + 1, get(1) + 1);
		}
		else
		{
			String result = "";
			for (int i = 0; i < size(); i += 2)
			{
				result += get(i) + 1;
				if (i < (size() - 1))
					result += "x";
			}
			return result;
		}
	}

	public int applyMine(int mine)
	{
		int b = 1 << get(0);
		if ((mine & b) != 0)
		{
			mine ^= b;
			mine ^= 1 << lastElement();
		}
		return mine;
	}

	public int applyHis(int his)
	{
		if (size() > 2)
			for (int i = 1; i < size(); i += 2)
			{
				int b = 1 << get(i);
				if ((his & b) != 0)
					his ^= 1 << get(i);
			}
		return his;
	}
	
	public int applyKings(int kings, int crownrow)
	{
		kings = applyMine(applyHis(kings));

		int b = 1 << lastElement();
		if ((crownrow & b) != 0)
			return kings | b;

		return kings;
	}
}
