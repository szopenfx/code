package checkers.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import checkers.Checkers;
import checkers.engine.Move;
import checkers.engine.UndoableMove;
import checkers.enums.Direction;
import checkers.enums.State;

public class MainFrame extends Frame
{
	protected Checkers checkers;
	protected Move selection = new Move();

	protected int hover = -1;

	public BoardPanel boardpanel;
	public StringPanel movepanel;
	public StringPanel possiblepanel;
	public StatusPanel statuspanel;
	
	public MainFrame(Checkers checkers)
	{
		this.checkers = checkers;

		initComponents();
		initEvents();
		initFrame();
	}

	private void initComponents()
	{
		// board panel
		this.boardpanel = new BoardPanel(checkers.board, new BoardPanel.Observer() {
			public void squareClicked(int coord)
			{
				if (coord == -1)
					return;
				
				if (selection.size() == 0)
				{
					if (checkers.board.getState(coord) != State.EMPTY)
						if(checkers.player == checkers.board.getPlayer(coord))
							selection.add(coord);
				}
				else
				{
					if (checkers.board.getState(coord) == State.EMPTY)
					{
						if(! checkers.possible.contains(selection))
						{
							int lastcoord = selection.lastElement();
							if (Math.abs(lastcoord - coord) > 5)
								for (Direction d : Direction.values())
								{
									if (coord - lastcoord == d.jump_shl)
										selection.add(lastcoord + (coord % 8 < 4 ? d.move_shl_e : d.move_shl_o));
									if (lastcoord - coord == d.jump_shr)
										selection.add(lastcoord - (coord % 8 < 4 ? d.move_shr_e : d.move_shr_o));
								}
							selection.add(coord);
						}

						if (checkers.possible.contains(selection))
						{
							System.out.printf("mainframe selection %s\n", selection);
							checkers.doMove(selection.clone());
							selection.clear();
							return;
						}
					}
				}
				
				System.out.printf("mainframe selection %s\n", selection);
				boardpanel.repaint();
			}

			public void squarePointed(int coord)
			{
				hover = coord;
				if (statuspanel != null)
					statuspanel.setSquare(coord);
				if (boardpanel != null)
					boardpanel.repaint();
			}
			
			public void squareCanceled()
			{
				selection.clear();
				boardpanel.repaint();
			}
			
			public boolean isSquareSelected(int coord)
			{
				if (selection.contains(coord))
				{
					return true;
				}
				
				if (possiblepanel.item > -1 
					&& possiblepanel.item < checkers.possible.size()
					&& checkers.possible.get(possiblepanel.item).contains(coord))
				{
					return true;
				}
				
				return false;
			}
			
			public boolean isSquareLastMoved(int coord)
			{
				return (checkers.lastdisp != null && checkers.lastdisp.contains(coord)) 
					|| (checkers.lastdisp == null && checkers.last != null && checkers.last.contains(coord));
			}
			
			public boolean isHovered(int coord)
			{
				return coord != -1 && hover == coord;
			}

			public void cleanHover()
			{
				hover = -1;
			}
		});
		
		// status panel
		this.statuspanel = new StatusPanel(checkers);

		// move panel
		this.movepanel = new StringPanel("undo", new StringPanel.Observer() {
			public void buttonClicked(int item)
			{
				System.out.printf("movepanel buttonClicked %d\n", item);
				checkers.undoMove();
			}

			public void itemClicked(int item)
			{
				System.out.printf("movepanel itemClicked %d %s\n", item, checkers.last.getMove(item));
				if (item == checkers.last.index)
					itemCanceled();
				else
				{
					UndoableMove last = checkers.last.getMove(item + 1);
					checkers.lastdisp = last.previous;
					boardpanel.setBoard(last.board);
				}
			}

			public void itemCanceled()
			{
				checkers.lastdisp = null;
				boardpanel.setBoard(checkers.board);
			}
		});
		
		// possible panel
		this.possiblepanel = new StringPanel("do", new StringPanel.Observer() {
			public void buttonClicked(int item)
			{
				System.out.printf("possiblepanel buttonClicked %d\n", item);
				checkers.doMove(checkers.possible.get(item).clone());
			}

			public void itemClicked(int item)
			{
				System.out.printf("possiblepanel itemClicked %d %s\n", item, checkers.possible.get(item));
				boardpanel.repaint();
			}

			public void itemCanceled()
			{
				boardpanel.repaint();
			}
		});
	}

	protected void initFrame()
	{
		setLayout(null);
		
		add(boardpanel);
		add(movepanel);
		add(possiblepanel);
		add(statuspanel);

		boardpanel.setBounds(10, 30, 512, 512);
		movepanel.setBounds(532, 30, 258, 256);
		possiblepanel.setBounds(532, 286, 258, 256);
		statuspanel.setBounds(10, 545, 780, 45);
		
		setSize(800, 600);
		setLocationByPlatform(true);
		setBackground(Color.BLACK);
		setVisible(true);
		setTitle("Checkers");
	}
	
	protected void initEvents()
	{
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0)
			{
				/**/
				System.exit(0);
				/** /
				setVisible(false);
				checkers.clock.stop();
				new GameStart();
				/**/
			}
		});
	}
}
