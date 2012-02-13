package checkers.engine;

import java.util.HashMap;
import java.util.Vector;

import checkers.enums.Direction;
import checkers.enums.Player;

public class MoveList extends Vector<Move>
{
	protected static class IntegerMap extends HashMap<Integer, Integer> {}
	
	public MoveList(Player player, Board board)
	{
		initialize(player, board);
	}
	
	public MoveList(Player player, int m, int h, int k)
	{
		initialize(player, m, h, k);
	}
	
	public MoveList()
	{
	}
	
	public void initialize(Player player, Board board)
	{
		initialize(player, player.mine(board), player.his(board), board.kings);
	}
	
	private void initialize(Player player, int mine, int his, int kings) 
	{
		clear();
		
		// find captures
		findCaptures(player, null, mine, his, kings);

		// find moves
		if(isEmpty())
			findMoves(player, mine, his, kings);
		/* leave this line here to play english draughts * /
		else 
		{
			int max = 0;
			
			// find longest capture
			for (Move move : this)
				if (move.size() > max)
					max = move.size();
			
			// delete shorter captures
			for (int i = size() - 1; i >= 0; i--)
				if (get(i).size() < max)
					remove(i);
		}
		/* shorter captures than the longest were not allowed in our school competition, 
		 * which is a slight deviation from english draughts rules */
	}

	protected void findMoves(Player player, int mine, int his, int kings)
	{
		for (Direction dir : player.directions)
		{
			// use pieces or kings depending on player
			int source = (player.canMove(dir) ? mine : mine & kings) & dir.moves; 

			// calculate destination squares
			int dest = (((source & 0x0f0f0f0f) << dir.move_shl_e) >>> dir.move_shr_e)
					 | (((source & 0xf0f0f0f0) << dir.move_shl_o) >>> dir.move_shr_o);
			
			// find origins of empty destination squares
			int orig = (((dest & ~(mine | his) & 0xf0f0f0f0) >>> dir.move_shl_e) << dir.move_shr_e)
					 | (((dest & ~(mine | his) & 0x0f0f0f0f) >>> dir.move_shl_o) << dir.move_shr_o);
			
			// create move object for every origin square
			if (orig != 0)
				for (int coord = 0; coord < 32; coord++)
					if ((orig & (1 << coord)) != 0)
					{
						add(new Move(coord, coord 
								+ (coord % 8 < 4 ? dir.move_shl_e : dir.move_shl_o) 
								- (coord % 8 < 4 ? dir.move_shr_e : dir.move_shr_o)
						));
					}
		}
	}

	protected MoveList findCaptures(Player player, Integer from, int mine, int his, int kings)
	{
		// initialize this movelist or return a movelist from certain square
		MoveList result = from == null 
						? this
						: new MoveList();
		
		for (Direction dir : player.directions)
		{
			// use only pieces or kings depending on player
			int source = player.canMove(dir) 
					? (from == null ? mine : (1 << from))
					: (from == null ? mine : (1 << from)) & kings;
			
			// find jump destinations and origins of empty destination squares
			int jumpdest = ((source & dir.jumps) << dir.jump_shl) >>> dir.jump_shr;
			int jumporig = ((jumpdest & ~(mine | his)) >>> dir.jump_shl) << dir.jump_shr;
			
			// find moves to enemy-occupied squares and their origins
			int captdest = (((source & dir.moves & 0x0f0f0f0f) << dir.move_shl_e) >>> dir.move_shr_e)
						 | (((source & dir.moves & 0xf0f0f0f0) << dir.move_shl_o) >>> dir.move_shr_o);
			
			int captorig = (((captdest & his & 0xf0f0f0f0) >>> dir.move_shl_e) << dir.move_shr_e)
						 | (((captdest & his & 0x0f0f0f0f) >>> dir.move_shl_o) << dir.move_shr_o);
			
			int canjump = jumporig & captorig;
			
			// create move object(s) for every jump origin square
			if (canjump != 0)
				for (int coord = 0; coord < 32; coord++)
					if ((canjump & (1 << coord)) != 0)
					{
						// calculate capture and destination squares
						int captsquare = coord % 8 < 4
									   ? coord + dir.move_shl_e - dir.move_shr_e
									   : coord + dir.move_shl_o - dir.move_shr_o;
						int destsquare = coord + dir.jump_shl - dir.jump_shr;
				
						// find rest of jump, if jump didn't end on crowning row
						Move move = new Move(coord, captsquare, destsquare);
						MoveList rest = ((1 << destsquare) & player.crownrow & ~kings) == 0
									  | ((1 << coord) & kings) != 0
									  ? findCaptures(player, destsquare, move.applyMine(mine), move.applyHis(his), move.applyKings(kings, player.crownrow))
									  : null;
						
						// create move object for every way to finish the capture from the origin square
						if (rest != null && rest.size() > 0)
							for (Move moverest : rest)
							{
								Move newmove = move.clone();
								moverest.remove(0);
								newmove.addAll(moverest);
								result.add(newmove);
							}
						// no way to extend the capture, just add the capture
						else
							result.add(move);
					}
		}
		
		return result;
	}
	
	/*public static void main(String[] args)
	{
		Board b = new Board();
		MoveList ml_1 = new MoveList(Player.PLAYER1, b);
		MoveList ml_2 = new MoveList(Player.PLAYER2, b);
		System.out.println(ml_1);
		System.out.println(ml_2);
	}*/
}
