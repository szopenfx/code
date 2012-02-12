package checkers.enums;

import java.awt.Color;

import checkers.engine.Board;
import checkers.engine.Move;

public enum Player
{ 
	PLAYER1(0xf0000000, new Direction[] { Direction.NE, Direction.NW, Direction.SE, Direction.SW }, Color.RED, "Player1")
	{
		public int mine(Board b) { return b.player1; }
		
		public int his(Board b) { return b.player2; }
		
		public boolean canMove(Direction d) { return d == Direction.NE || d == Direction.NW; }
		
		public void applyMove(Board b, Move m)
		{
			b.player1 = m.applyMine(b.player1);
			b.player2 = m.applyHis(b.player2);
			b.kings = m.applyKings(b.kings, crownrow);
		}
	},
	
	PLAYER2(0x0000000f, new Direction[] { Direction.SW, Direction.SE, Direction.NW, Direction.NE }, Color.WHITE, "Player2")
	{
		public int mine(Board b) { return b.player2; }
		
		public int his(Board b) { return b.player1; }
		
		public boolean canMove(Direction d) { return d == Direction.SE || d == Direction.SW; }
		
		public void applyMove(Board b, Move m)
		{
			b.player1 = m.applyHis(b.player1);
			b.player2 = m.applyMine(b.player2);
			b.kings = m.applyKings(b.kings, crownrow);;
		}
	};
	
	static {
		PLAYER1.other = PLAYER2;
		PLAYER2.other = PLAYER1;
	}
	
	public int crownrow;
	public Color color;
	public String name;
	public int clock;
	public int moves;
	public boolean cpu = false;
	public Player other;
	public Phase phase = Phase.PHASE1;
	public Direction[] directions;
	
	Player(int cr, Direction[] d, Color c, String n)
	{
		crownrow = cr;
		directions = d;
		color = c;
		name = n;
	}
	
	public abstract int mine(Board b);
	public abstract int his(Board b);
	public abstract boolean canMove(Direction d);
	public abstract void applyMove(Board b, Move m);
};
