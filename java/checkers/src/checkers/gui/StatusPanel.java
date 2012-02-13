package checkers.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;

import checkers.Checkers;
import checkers.engine.Board;
import checkers.enums.Phase;
import checkers.enums.Player;

public class StatusPanel extends Panel
{
	protected Board board;
	
	protected Label square = new Label();
	protected Label depth = new Label();
	protected Label player1 = new Label();
	protected Label player2 = new Label();
	protected Label winner1 = new Label();
	protected Label winner2 = new Label();
	protected Label state = new Label();
	
	public StatusPanel(Checkers checkers)
	{
		board = checkers.board;
		
		initPanel();
	}

	protected void initPanel()
	{
		setLayout(null);
		
		add(player1);
		add(player2);
		add(winner1);
		add(winner2);
		add(square);
		add(depth);
		add(state);
		
		player1.setAlignment(Label.CENTER);
		player2.setAlignment(Label.CENTER);
		winner1.setAlignment(Label.CENTER);
		winner2.setAlignment(Label.CENTER);
		
		player1.setBounds(0, 19, 256, 25);
		player2.setBounds(256, 19, 256, 25);
		winner1.setBounds(0, 0, 256, 19);
		winner2.setBounds(256, 0, 256, 19);
		square.setBounds(512, 0, 278, 15);
		depth.setBounds(512, 15, 278, 15);
		state.setBounds(512, 30, 278, 15);
		
		square.setForeground(Color.GRAY);
		depth.setForeground(Color.GRAY);
		player1.setForeground(Player.PLAYER1.color);
		player2.setForeground(Color.GRAY);
		winner1.setForeground(Color.GREEN);
		winner2.setForeground(Color.GREEN);
		state.setForeground(Color.GRAY);
	}
	
	public void setSquare(int coord)
	{
		square.setText(String.format("Square: %s",
				coord == -1 
					? "-" 
					: "" + (coord + 1)
		));
	}
	
	public void setDepth(int d, int score, String best)
	{
		if (best == null)
			depth.setText(String.format("Depth: %d (%d)", d, score));
		else
			depth.setText(String.format("Depth: %d Best: %s (%d)", d, best, score));
	}
	
	public void setActive(Player p)
	{
		player1.setForeground(p == Player.PLAYER1 ? p.color : Color.GRAY);
		player2.setForeground(p == Player.PLAYER2 ? p.color : Color.GRAY);
		
		player1.setFont(player1.getFont().deriveFont(25.0f).deriveFont(p == Player.PLAYER1 ? Font.BOLD : Font.PLAIN));
		player2.setFont(player2.getFont().deriveFont(25.0f).deriveFont(p == Player.PLAYER2 ? Font.BOLD : Font.PLAIN));
		
		depth.setForeground(p.cpu ? Color.GREEN : Color.GRAY);
		square.setForeground(p.cpu ? Color.GRAY : Color.GREEN);
		
		setLabel();
	}
	
	public void setLabel()
	{
		for (Player p : Player.values())
		{
			String text = String.format(
					"%s (%s/%s) %s%d:%02d", 
					p.name, 
					Integer.bitCount(p.mine(board)),
					Integer.bitCount(p.mine(board) & board.kings),
					p.clock < 0 ? "-" : "",
					Math.abs(p.clock / 60),
					Math.abs(p.clock % 60)
			);
			
			if (p == Player.PLAYER1)
				player1.setText(text);
			else
				player2.setText(text);
		}
		
		state.setText(String.format("p1: %08X p2: %08X k: %08X", board.player1, board.player2, board.kings));
	}

	public void endOfGame()
	{
		winner1.setFont(winner1.getFont().deriveFont(Font.BOLD));
		winner2.setFont(winner1.getFont().deriveFont(Font.BOLD));
		
		winner1.setText(Player.PLAYER1.phase == Phase.WON ? "WINNER" : "LOSER");
		winner2.setText(Player.PLAYER2.phase == Phase.WON ? "WINNER" : "LOSER");
		
		player1.setForeground(Player.PLAYER1.color);
		player2.setForeground(Player.PLAYER2.color);
		
		player1.setFont(player1.getFont().deriveFont(Font.BOLD));
		player2.setFont(player2.getFont().deriveFont(Font.BOLD));
	}
}
