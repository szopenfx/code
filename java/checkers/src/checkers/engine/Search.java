package checkers.engine;

import java.util.Timer;
import java.util.TimerTask;

import checkers.Checkers;
import checkers.enums.Direction;
import checkers.enums.Player;

// TODO : evaluatie mobiliteit
// TODO : evaluatie centraalheid

public class Search extends Thread
{
	private static final int ODDSQUARE = 0xf0f0f0f0;
	private static final int EVENSQUARE = 0x0f0f0f0f;
	protected Checkers checkers;
	protected Timer timer;
	protected boolean stop = false;
	
	public Search(Checkers c)
	{
		super("Search");
		checkers = c;
	}
	
	public void cancel()
	{
		timer.cancel();
		stop = true;
	}

	public void run()
	{
		Player player = checkers.player;
		Board board = checkers.board;

		// calculate time to search
		int time = 1000 * (player.clock - 1) / player.moves;
		System.out.println("searching for " + time + " ms");
		
		// start timer that will stop search thread
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run()
			{	
				stop = true; 
			}
		}, time);
		
		// find a good move to play
		checkers.doMove(
				search(player, player.mine(board), player.his(board), board.kings)
		);
		
		// can this hurt? don't know if necessary
		System.gc();		
	}
	
	private Move search(Player player, int mine, int his, int kings)
	{
		int depth = 0;
		int max = -1000;
		Move best = null;
		
		// find possible moves
		MoveList moves = new MoveList(player, mine, his, kings);
		
		// avoid thinking :->
		if (moves.size() == 1)
			best = moves.firstElement();
		else
			// try to look a little further ahead as long as there is time
			while (! stop)
			{
				depth++;
				checkers.mainframe.statuspanel.setDepth(depth, max, best != null ? best.toString() : null);

				// try each possible move - there's going to be some reordering as well
				for (int i = 0; i < moves.size(); i++)
				{
					Move move = moves.get(i);
					
					if (stop) 
						break;
					
					// find value of move
					move.value = -alphaBeta(
							depth, 
							-1000,
							-max,
							player.other, 
							move.applyHis(his), 
							move.applyMine(mine), 
							move.applyKings(kings, player.crownrow)
					);
					
					// remember best move so far
					if (move.value > max)
					{
						moves.remove(i);
						moves.insertElementAt(move, 0);
						
						max = move.value;
						best = move;
					}
					
				}

				System.out.printf("search moves %2d %s %d\n", depth, moves, max);
			}
		
		checkers.mainframe.statuspanel.setDepth(depth, max, best != null ? best.toString() : null);
		System.out.printf("search depth=%d evaluation=%d\n", depth, max);
		
		return best;
	}
	
	private int alphaBeta(int depth, int alpha, int beta, Player player, int mine, int his, int kings)
	{
		if (depth == 0)
			// search for just a little longer if the situation looks too dynamic
			if (isUnstable(player, mine, his, kings))
				//depth += 1;
				return alphaBeta(depth, -2, alpha, beta, player, mine, his, kings);
		
			// return static evaluation of position
			else
				return evaluate(mine, his, kings);
		
		// try all possible moves
		MoveList moves = new MoveList(player, mine, his, kings);
		
		//System.out.printf("%2d %2d %2d %s\n", depth, alpha, beta, moves);
		
		if (moves.size() == 0)
			return evaluate(mine, his, kings);
		
		for (Move move : moves)
		{
			// find value of move
			int value = -alphaBeta(
					depth - 1,
					-beta,
					-alpha,
					player.other,
					move.applyHis(his),
					move.applyMine(mine),
					move.applyKings(kings, player.crownrow)
			);

			// save value of best move so far
			if (value > alpha)
			{
				alpha = value;
				
				// a better alternative was possible in previous move for the other player
				if (alpha >= beta) 
					return alpha;
			}

			// stop if time is up
			if (stop)
				return alpha;
		}
		
		return alpha;
	}
	
	private int alphaBeta(int depth, int maxdepth, int alpha, int beta, Player player, int mine, int his, int kings)
	{
		if (depth <= maxdepth)
			return evaluate(mine, his, kings);
		
		// try all possible moves
		MoveList moves = new MoveList(player, mine, his, kings);
		
		//System.out.printf("%2d %2d %2d %2d %s\n", depth, maxdepth, alpha, beta, moves);
		
		if (moves.size() == 0)
			return evaluate(mine, his, kings);
		
		for (Move move : moves)
		{
			// find value of move
			int value = -alphaBeta(
					depth - 1,
					maxdepth,
					-beta,
					-alpha,
					player.other,
					move.applyHis(his),
					move.applyMine(mine),
					move.applyKings(kings, player.crownrow)
			);

			// save value of best move so far
			if (value > alpha)
			{
				alpha = value;
				
				// a better alternative was possible in previous move for the other player
				if (alpha >= beta) 
					return alpha;
			}

			// stop if time is up
			if (stop)
				return alpha;
		}
		
		return alpha;
	}

	private int evaluate(int mine, int his, int kings)
	{
		/* (simple and fast) */
		return Integer.bitCount(mine & ~kings) + 5 * Integer.bitCount(mine & kings)
			 - Integer.bitCount(his  & ~kings) - 5 * Integer.bitCount(his  & kings);

		/* (complex, slow and wrong) * /
		int mm = mine & ~kings, mk = mine & kings;
		int hm = his & ~kings, hk = his & kings;
		
		final int rank0 = 0x00024000;
		final int rank1 = 0x00642600;
		final int rank2 = 0x07818170;
		final int rank3 = 0xf818181f;
		
		return 4 * (Integer.bitCount(mm & rank0) + 3 * Integer.bitCount(mk & rank0))
			 + 3 * (Integer.bitCount(mm & rank1) + 3 * Integer.bitCount(mk & rank1))
			 + 2 * (Integer.bitCount(mm & rank2) + 3 * Integer.bitCount(mk & rank2))
			 + 1 * (Integer.bitCount(mm & rank3) + 3 * Integer.bitCount(mk & rank3))

			 - 4 * (Integer.bitCount(hm & rank0) + 3 * Integer.bitCount(hk & rank0))
			 - 3 * (Integer.bitCount(hm & rank1) + 3 * Integer.bitCount(hk & rank1))
			 - 2 * (Integer.bitCount(hm & rank2) + 3 * Integer.bitCount(hk & rank2))
			 - 1 * (Integer.bitCount(hm & rank3) + 3 * Integer.bitCount(hk & rank3));
			  
		/**/
	}
	
	private boolean isUnstable(Player player, int mine, int his, int kings)
	{
		for (Direction dir : player.directions)
		{
			// only use kings if direction requires it
			int source = player.canMove(dir) ? mine : mine & kings;

			// find origins of empty destination squares of jumps
			int jumpdest = ((source & dir.jumps) << dir.jump_shl) >>> dir.jump_shr;
			int jumporig = ((jumpdest & ~(mine | his)) >>> dir.jump_shl) << dir.jump_shr;
			
			// find origins of squares on which a capture is possible
			int captdest = (((source & dir.moves & EVENSQUARE) << dir.move_shl_e) >>> dir.move_shr_e)
						 | (((source & dir.moves & ODDSQUARE) << dir.move_shl_o) >>> dir.move_shr_o);

			int captorig = (((captdest & his & ODDSQUARE) >>> dir.move_shl_e) << dir.move_shr_e)
			 			 | (((captdest & his & EVENSQUARE) >>> dir.move_shl_o) << dir.move_shr_o);

			// can jump and capture from at least one square, position not stable
			if ((jumporig & captorig) != 0)
				return true;
		}
		
		// position stable
		return false;			
	}

	/*private int miniMax(int depth, Player p, int m, int h, int k)
	{
		if (depth == 0)
			return evaluate(p, m, h, k);
		
		MoveList moves = new MoveList(p, m, h, k);

		int max = Integer.MIN_VALUE;
		for (int i = 0; i < moves.size(); i++)
		{
			Move move = moves.get(i);
			int v = -miniMax(
					depth - 1,
					p.other,
					move.applyHis(h),
					move.applyMine(m),
					move.applyKings(k, p.crownrow)
			);
			if (v > max)
				max = v;
		}
		
		return max;
	}*/
}
