package checkers.engine;

import checkers.enums.Player;
import checkers.enums.State;

public class Board
{
	public int player1;
	public int player2;
	public int kings;
	
	public static final int RANK_0 = 0x00024000;
	public static final int RANK_1 = 0x00642600;
	public static final int RANK_2 = 0x07818170;
	public static final int RANK_3 = 0xf818181f;
	
	public Board()
	{
		set(0x00000fff, 0xfff00000, 0x00000000);
//		set(0x200800fD, 0x5C034000, 0x28000001);
//		set(0x00040008, 0xfc000504, 0x00000104);
//		set(0x0000008c, 0xfc810800, 0x00000800);
//		set(0x20042800, 0x00000408, 0x20042408);
	}
	
	public Board(int p1, int p2, int k)
	{
		set(p1, p2, k);
	}

	public Board(Board b)
	{
		set(b);
	}

	public void set(int p1, int p2, int k)
	{
		player1 = p1;
		player2 = p2;
		kings = k;
	}
	
	public void set(Board b)
	{
		set(b.player1, b.player2, b.kings);
	}
	
	public String toString()
	{
		String result = "";
		for (int i = 31; i >= 0; i--)
		{
			State state = getState(i);
			result += (i % 8) == 7 ? "  " : "";
			if (state != State.EMPTY)
				result += (getPlayer(i) == Player.PLAYER1 ? "1" : "2")
					   + (state == State.KING ? "k"	: "p");
			else
				result += "__";
			result += (i % 4) == 0 ? "\n" : "  ";
		}
		return result;
	}
	
	public Player getPlayer(int coord)
	{
		int b = 1 << coord;
		return ((player1 & b) == b) ? Player.PLAYER1 
			 : ((player2 & b) == b) ? Player.PLAYER2
			 : null;
	}
	
	public State getState(int coord)
	{
		int b = 1 << coord;
		return ((kings & b) != 0) ? State.KING
			 : ((player1 | player2) & b) != 0 ? State.PIECE
			 : State.EMPTY;
	}
}
