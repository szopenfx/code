package checkers.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import checkers.engine.Board;
import checkers.enums.Player;
import checkers.enums.State;

public class BoardPanel extends DoubleBuffer
{
	protected Board board;
	protected Observer observer = null;
	protected boolean enabled = true;
	
	protected static final int SIZE = 64;
	
	protected static final Color[] SQUARECOLORS = { new Color(0x404040), new Color(0x303030) };
	protected static final Color SELECTEDCOLOR = new Color(0xb0b000);
	protected static final Color WASMOVEDCOLOR = new Color(0x00a0a0);
	protected static final Color HOVEREDCOLOR = new Color(0x00a000);

//	protected static final Color[] SQUARECOLORS = { new Color(255, 255, 0), new Color(0, 191, 0) }; 
//	protected static final Color SELECTEDCOLOR = new Color(0, 191, 0);
//	protected static final Color WASMOVEDCOLOR = new Color(0, 191, 0);
//	protected static final Color HOVEREDCOLOR = new Color(0, 191, 40);
	
	public static interface Observer
	{
		public void squarePointed(int coord);
		public void squareClicked(int coord);
		public void squareCanceled();
		public boolean isSquareSelected(int coord);
		public boolean isSquareLastMoved(int coord);
		public boolean isHovered(int coord);
		public void cleanHover();
	}
	
	public BoardPanel(Board board, Observer observer)
	{
		this.board = board;
		this.observer = observer;
		
		initEvents();
		repaint();
	}
	
	protected void initEvents()
	{
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				if (! enabled)
					return;
				if (e.getButton() != MouseEvent.BUTTON1)
					observer.squareCanceled();
				else
				{
					int x = 7 - e.getX() / SIZE;
					int y = 7 - e.getY() / SIZE;
					if (observer != null)
						observer.squareClicked((x + y) % 2 == 1 ? y * 4 + x / 2 : -1);
				}
			}

			public void mouseExited(MouseEvent e)
			{
				observer.cleanHover();
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e)
			{
				int x = 7 - e.getX() / SIZE;
				int y = 7 - e.getY() / SIZE;
				if (observer != null)
					observer.squarePointed((x + y) % 2 == 1 ? y * 4 + x / 2 : -1);
			}
		});
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public void setBoard(Board b)
	{
		board = b;
		repaint();
	}

	public void paintBuffer(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		drawBoard(g2);
		drawPieces(g2);
	}
	
	protected void drawOutline(Graphics2D g, int x, int y, Color c)
	{
		g.setColor(c);
		g.drawRect((7 - x) * SIZE, (7 - y) * SIZE, SIZE - 1, SIZE - 1);
	}
	
	protected void drawBoard(Graphics2D g)
	{
		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
			{
				int coord = y * 4 + x / 2;

				g.setColor(SQUARECOLORS[(x + y) % 2]);
				g.fillRect((7 - x) * SIZE, (7 - y) * SIZE, SIZE, SIZE);
				
				if ((x + y) % 2 == 1)
				{
					if (observer.isSquareLastMoved(coord))
						drawOutline(g, x, y, WASMOVEDCOLOR);
					
					if (observer.isHovered(coord))
					{
						drawOutline(g, x, y, HOVEREDCOLOR);
						int h = g.getFontMetrics().getAscent() - 1;
						g.drawString("" + (coord + 1), (7 - x) * SIZE + 2, (7 - y) * SIZE + h);
					}

					if (observer.isSquareSelected(coord))
						drawOutline(g, x, y, SELECTEDCOLOR);
				}
				
			}
	}
	
	protected void drawPieces(Graphics2D g)
	{
		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
				if ((x + y) % 2 == 1)
				{
					int coord = y * 4 + x / 2;
					
					State state = board.getState(coord);
					Player player = board.getPlayer(coord);

					if (state == State.EMPTY)
						continue;
					
					int baseX = (7 - x) * SIZE;
					int baseY = (7 - y) * SIZE;

					g.setColor(player.color);

					g.fillOval(baseX + SIZE / 8, baseY + SIZE / 8, 6 * SIZE / 8, 6 * SIZE / 8);
					
					if (state == State.KING)
					{
						g.setColor(Color.GRAY);
						Stroke tempstroke = g.getStroke();
						g.setStroke(new BasicStroke(6));
						g.drawOval(baseX + SIZE / 8, baseY + SIZE / 8, 6 * SIZE / 8, 6 * SIZE / 8);
						g.setStroke(tempstroke);
					}
				}
	}
}
