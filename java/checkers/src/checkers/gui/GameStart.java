package checkers.gui;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import checkers.Checkers;

public class GameStart extends Frame
{
	public TextField name1 = new TextField();
	public TextField name2 = new TextField();
	
	public TextField time1 = new TextField();
	public TextField time2 = new TextField();
	
	public TextField moves1 = new TextField();
	public TextField moves2 = new TextField();
	
	public Checkbox relaxed = new Checkbox("Ignore clock");
	
	public Button start = new Button("Go");
	
	public static String FILENAME = "checkers.cfg";
	
	public GameStart()
	{
		initFrame();
		initEvents();

		try
		{
			load();
		}
		catch (IOException e)
		{
			defaults();
		}
	}
	
	protected void addLabel(String text)
	{
		add(new Label(text));
	}
	
	protected void initFrame()
	{
		setLayout(new GridLayout(8, 3, 5, 5));
	
		addLabel("");
		addLabel("Player 1");
		addLabel("Player 2");
		
		addLabel("Name");
		add(name1);
		add(name2);
		
		addLabel("");
		addLabel("");
		addLabel("");
		
		addLabel("");
		addLabel("Moves");
		addLabel("Time");

		addLabel("Stage 1");
		add(moves1);
		add(time1);
		
		addLabel("Stage 2");
		add(moves2);
		add(time2);
		
		addLabel("");
		addLabel("");
		addLabel("");
		
		add(relaxed);;
		addLabel("");
		add(start);
		
		for (Component c : this.getComponents())
			c.setBackground(Color.BLACK);
	
		setBackground(Color.BLACK);
		setForeground(Color.GREEN);

		pack();
	
		setLocationByPlatform(true);
		setResizable(false);
		setTitle("Checkers " + Checkers.VERSION);
		setVisible(true);
	}

	protected void initEvents()
	{
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0)
			{
				System.exit(0);
			}
		});
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					save();
				}
				catch (FileNotFoundException ex)
				{
					ex.printStackTrace();
				}
				setVisible(false);
				new Checkers(
						name1.getText(), 
						name2.getText(), 
						60 * Integer.parseInt(time1.getText()), 
						60 * Integer.parseInt(time2.getText()),
						Integer.parseInt(moves1.getText()),
						Integer.parseInt(moves2.getText()),
						relaxed.getState()
				);
			}
		});
	}
	
	public void save() throws FileNotFoundException
	{
		PrintWriter pw = new PrintWriter(FILENAME);
		
		pw.println(name1.getText());
		pw.println(name2.getText());
		
		pw.println(time1.getText());
		pw.println(time2.getText());
		
		pw.println(moves1.getText());
		pw.println(moves2.getText());
		
		pw.println(relaxed.getState() ? "true" : "Who-hah! Got you all in check");
		
		pw.close();
	}
	
	public void load() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(FILENAME));
			
		name1.setText(br.readLine());
		name2.setText(br.readLine());
		
		time1.setText(br.readLine());
		time2.setText(br.readLine());
		
		moves1.setText(br.readLine());
		moves2.setText(br.readLine());
		
		relaxed.setState(br.readLine().equals("true"));
		
		br.close();
	}
	
	public void defaults()
	{
		name1.setText("Human");
		name2.setText("Computer");
		
		time1.setText("5");
		time2.setText("5");
		
		moves1.setText("60");
		moves2.setText("60");
		
		relaxed.setState(true);
	}
	
}
