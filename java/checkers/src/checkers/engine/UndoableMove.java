package checkers.engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import checkers.enums.Phase;
import checkers.enums.Player;

public class UndoableMove extends Move
{
	public int index;
	public UndoableMove previous;
	public Board board;
	public int clock;
	public Phase phase;
	
	public UndoableMove(Move move, UndoableMove prev, Board b, int clock_, Phase phase_)
	{
		super(move);
		previous = prev;
		index = (previous != null) ? previous.index + 1 : 0;
		board = new Board(b);
		clock = clock_;
		phase = phase_;
	}
	
	public UndoableMove getMove(int idx)
	{
		return index == idx 
				? this
				: previous != null
					? previous.getMove(idx)
					: null;
	}
	
	public String moveString()
	{
		return previous == null 
			? toString()
			: previous.moveString() + " " + toString();
	}
	
	public String phaseString()
	{
		return previous == null
			? phase.toString()
			: previous.phaseString() + " " + phase.toString();
	}
	
	public String clockString()
	{
		return previous == null
			? "" + clock
			: previous.clockString() + " " + clock;
	}
	
	public void save(String filename) throws IOException
	{
		//String newline = System.getProperty("line.seperator");
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		
		out.printf("player1 %s %s%s", Player.PLAYER1.phase, Player.PLAYER1.name);
		out.printf("player2 %s %s%S", Player.PLAYER2.phase, Player.PLAYER2.name);
		out.printf("moves %s", moveString());
		out.printf("phases %s", phaseString());
		out.printf("clocks %s", clockString());

	}
}
