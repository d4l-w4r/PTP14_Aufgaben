package testPackage;

import javax.swing.JFrame;
import java.awt.Graphics;

public class BetterDraw
{

	protected JFrame window;

	/** Application constructor: create an instance of our GUI class */
	public BetterDraw()
	{
		window = new BetterDrawGUI(this);
	}

	/** main entry point. Just create an instance of this application class */
	public static void main(String[] args)
	{
		new Draw();
	}

	/** This is the application method that processes commands sent by the GUI */
	public void doCommand(String command)
	{
		if (command.equals("clear"))
		{// clear the GUI window
			// It would be more modular to include this functionality in the GUI
			// class itself. But for demonstration purposes, we do it here.
			Graphics g = window.getGraphics();
			g.setColor(window.getBackground());
			g.fillRect(0, 0, window.getSize().width, window.getSize().height);
			window.paintComponents(g);
		}
		else if (command.equals("quit"))
		{ // quit the application
			window.dispose(); // close the GUI
			System.exit(0); // and exit.
		}
	}
}
