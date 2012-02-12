package checkers;

import checkers.engine.Board;
import checkers.engine.Move;
import checkers.engine.MoveList;
import checkers.engine.Search;
import checkers.engine.UndoableMove;
import checkers.enums.Phase;
import checkers.enums.Player;
import checkers.gui.GameStart;
import checkers.gui.MainFrame;

// TODO : undo fixen m.b.t. search thread
// TODO : loggen errors en excepties

public class Checkers implements Clock.Observer
{
	public static final String VERSION = "0.3β";
	public Board board = new Board();

	public Player player = Player.PLAYER1;
	public MoveList possible = new MoveList();
	public UndoableMove last = null;
	public UndoableMove lastdisp = null;

	public Clock clock;
	protected Search search;

	protected int moves2, clock2;
	protected boolean relaxed;

	public MainFrame mainframe; 
	
	public static Checkers instance;
	
	public Checkers(
			String name1, 
			String name2, 
			int time1, 
			int time2, 
			int moves1, 
			int moves2,
			boolean relaxed)
	{
		instance = this;
		
		Player.PLAYER1.name = name1;
		Player.PLAYER2.name = name2;
		
		Player.PLAYER1.cpu = name1.equals("Computer");
		Player.PLAYER2.cpu = name2.equals("Computer");
		
		Player.PLAYER1.clock = time1;
		Player.PLAYER2.clock = time1;
		
		Player.PLAYER1.moves = moves1;
		Player.PLAYER2.moves = moves1;
		
		this.moves2 = moves2;
		this.clock2 = time2;
		this.relaxed = relaxed;
		
		mainframe = new MainFrame(this);
		mainframe.statuspanel.setActive(player);

		initializePossible();
		setControl();
		clock = new Clock(this);
	}
	
	public void finalize()
	{
		clock.stop();
	}
	
	private void declareWinner(Player p)
	{
		clock.stop();
		
		p.phase = Phase.WON;
		p.other.phase = Phase.LOST;
	}

	public void timerTick()
	{
		player.clock--;
		
		if (mainframe != null && mainframe.statuspanel != null)
			mainframe.statuspanel.setLabel();

		// WIN CONDITION : no more time
		if (player.clock <= 0)
			/**/
			if (relaxed && player.cpu || !relaxed)
			{
				declareWinner(player.other);
				setControl();
			}
			// TODO : remove bad debugging hack that fucks up entire game
			/** /
			player.clock += 100;
			/**/
	}
	
	public void doMove(Move m)
	{
		last = new UndoableMove(m, last, board, player.other.clock, player.other.phase);
		player.applyMove(board, m);

		System.out.printf("checkers moving #%d %s\n\n", last.index, m);
		
		checkWin_Moves();
		
		player = player.other;

		initializePossible();
		checkWin_Possible();

		mainframe.movepanel.addItem(
				last.index % 2 == 0
				? String.format("%3d. %s", last.index / 2 + 1, last)
				: String.format("     %s", last)
		);
		mainframe.boardpanel.repaint();
		mainframe.statuspanel.setActive(player);
		
		setControl();
	}

	public void undoMove()
	{
		System.out.printf("checkers undoing move %s\n", last);

		// going back while computer is thinking is more work than just refusing it
		if (player.cpu)
			return;

		// can't go back
		if (last == null)
			return;
		
		// go back to move of other player
		board.set(last.board);
		player.other.clock = last.clock;
		player.other.phase = last.phase;
		last = last.previous;
		mainframe.movepanel.removeItem();
		
		// go back to move of this player
		board.set(last.board);
		player.clock = last.clock;
		player.phase = last.phase;
		last = last.previous;
		mainframe.movepanel.removeItem();
		
		initializePossible();
		setControl();

		// update interface
		mainframe.boardpanel.repaint();
		mainframe.statuspanel.setLabel();
	}
	
	public void checkWin_Moves()
	{
		// WIN CONDITION : no moves left
		player.moves--;
		if (player.moves == 0)
		{
			if (player.phase == Phase.PHASE1)
			{
				player.clock += clock2;
				player.moves += moves2;
				player.phase = Phase.PHASE2;
			}
			else
				declareWinner(player.other);
		}
	}
	
	public void checkWin_Possible()
	{
		// WIN CONDITION : cannot move
		if (possible.size() == 0)
			declareWinner(player.other);
	}

	public void setControl()
	{
		Player other = player.other;
			
		System.out.printf("checkers setcontrol  to  %s %s clock=%d moves=%d \n", player, player.phase, player.clock, player.moves);
		System.out.printf("checkers setcontrol from %s %s clock=%d moves=%d \n", other, other.phase, other.clock, other.moves);
		System.out.printf("checkers board player1=%08X player2=%08X kings=%08X\n", board.player1, board.player2, board.kings);
		
		if (player.phase == Phase.PHASE1 || player.phase == Phase.PHASE2)
		{
			mainframe.boardpanel.setEnabled(! player.cpu);

			if (player.cpu)
			{
				search = new Search(this);
				search.start();
			}
		}
		else
		{
			mainframe.boardpanel.setEnabled(false);
			mainframe.statuspanel.endOfGame();
		}
	}
	
	private void initializePossible()
	{
		possible.initialize(player, board);
		System.out.printf("checkers possible %s\n", possible);
		
		mainframe.possiblepanel.clear(false);
		for (Move m : possible)
			mainframe.possiblepanel.addItem(m.toString());
		mainframe.possiblepanel.repaint();
	}

	public static String boardString(int b)
	{
		String r = Integer.toBinaryString(b);

		while (r.length() < 32)
			r = "0" + r;

		String result = "";
		for (int i = 0; i < 32; i += 8)
		{
			result += String.format(" %c %c %c %c\n", r.charAt(i+0), r.charAt(i+1), r.charAt(i+2), r.charAt(i+3));
			result += String.format("%c %c %c %c \n", r.charAt(i+4), r.charAt(i+5), r.charAt(i+6), r.charAt(i+7));
		}
		
		return result;
	}
	
	public static void printBB(int b)
	{
		System.out.println(boardString(b));
		System.out.println();
	}

	public static void main(String[] args)
	{
		System.out.println("java " + System.getProperty("java.version"));
		System.out.println(System.getProperties());
		new GameStart();
	}
}
