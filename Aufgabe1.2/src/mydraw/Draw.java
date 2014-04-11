package mydraw;

//This example is from _Java Examples in a Nutshell_. (http://www.oreilly.com)
//Copyright (c) 1997 by David Flanagan
//This example is provided WITHOUT ANY WARRANTY either expressed or implied.
//You may study, use, modify, and distribute it for non-commercial purposes.
//For any commercial use, see http://www.davidflanagan.com/javaexamples


import javax.swing.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;

import java.lang.System;

/** The application class. Processes high-level commands sent by GUI */
public class Draw
{
	//protected JFrame window;
	protected DrawGUI window; 
	/** Application constructor: create an instance of our GUI class */
	public Draw()
	{
		window = new DrawGUI(this);
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
			JPanel drawPane = window.getDrawPane();
			Graphics g = drawPane.getGraphics();
			g.setColor(drawPane.getBackground());
			g.fillRect(0, 0, drawPane.getSize().width, drawPane.getSize().height);
			drawPane.paintComponents(g);
		}
		else if (command.equals("quit"))
		{ // quit the application
			window.dispose(); // close the GUI
			System.exit(0); // and exit.
		}
	}
}

/** This class implements the GUI for our application */
class DrawGUI extends JFrame
{
	private static final long serialVersionUID = 1L; 
	Draw app; // A reference to the application, to send commands to.
	Color color;
	private JPanel drawPane;
	/**
	 * The GUI constructor does all the work of creating the GUI and setting up
	 * event listeners. Note the use of local and anonymous classes.
	 */
	public DrawGUI(Draw application)
	{
		super("Draw"); // Create the window
		app = application; // Remember the application reference
		color = Color.black; // the current drawing color

		// selector for drawing modes
		
		JComboBox<String> shape_chooser = new JComboBox<String>();
		//Choice shape_chooser = new Choice();
		shape_chooser.addItem("Scribble");
		shape_chooser.addItem("Rectangle");
		shape_chooser.addItem("Oval");

		// selector for drawing colors
		//Choice color_chooser = new Choice();
		JComboBox<String> color_chooser = new JComboBox<String>();
		color_chooser.addItem("Black");
		color_chooser.addItem("Green");
		color_chooser.addItem("Red");
		color_chooser.addItem("Blue");

		// Create two buttons
		JButton clear = new JButton("Clear");
		JButton quit = new JButton("Quit");
		
		//Create a panel to draw on
		drawPane = new JPanel();
		drawPane.setPreferredSize(new Dimension(500, 400)); //important to auto-calculate the window size with pack()
		
		// Create a Panel for the Menu, set a LayoutManager and add the choosers and buttons to the panel.
		JPanel menuPane = new JPanel();
		menuPane.setBackground(Color.LIGHT_GRAY);
		menuPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		menuPane.add(new JLabel("Shape:"));
		menuPane.add(shape_chooser);
		menuPane.add(new JLabel("Color:"));
		menuPane.add(color_chooser);
		menuPane.add(clear);
		menuPane.add(quit);
		
		//Set BorderLayout on the top level Container and add the menu and draw panel
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(menuPane, BorderLayout.NORTH);
		this.getContentPane().add(drawPane, BorderLayout.CENTER);
		
		// Here's a local class used for action listeners for the buttons
		class DrawActionListener implements ActionListener
		{
			private String command;

			public DrawActionListener(String cmd)
			{
				command = cmd;
			}

			public void actionPerformed(ActionEvent e)
			{
				app.doCommand(command);
			}
		}

		// Define action listener adapters that connect the buttons to the app
		clear.addActionListener(new DrawActionListener("clear"));
		quit.addActionListener(new DrawActionListener("quit"));

		// this class determines how mouse events are to be interpreted,
		// depending on the shape mode currently set
		class ShapeManager implements ItemListener
		{
			DrawGUI gui;
			//our panel to draw on
			JPanel drawPane; 

			abstract class ShapeDrawer extends MouseAdapter implements
					MouseMotionListener
			{
				public void mouseMoved(MouseEvent e)
				{ /*ignore*/
				}
			}

			// if this class is active, the mouse is interpreted as a pen
			class ScribbleDrawer extends ShapeDrawer
			{
				int lastx, lasty;

				public void mousePressed(MouseEvent e)
				{
					lastx = e.getX();
					lasty = e.getY();
				}

				public void mouseDragged(MouseEvent e)
				{
					//Graphics g = gui.getGraphics();
					//instead of pulling in the Graphics element of the toplevel container,
					//get it from our draw Panel
					Graphics g = drawPane.getGraphics();
					int x = e.getX(), y = e.getY();
					g.setColor(gui.color);
					g.setPaintMode();
					g.drawLine(lastx, lasty, x, y);
					lastx = x;
					lasty = y;
				}
			}

			// if this class is active, rectangles are drawn
			class RectangleDrawer extends ShapeDrawer
			{
				int pressx, pressy;
				int lastx = -1, lasty = -1;

				// mouse pressed => fix first corner of rectangle
				public void mousePressed(MouseEvent e)
				{
					pressx = e.getX();
					pressy = e.getY();
				}

				// mouse released => fix second corner of rectangle
				// and draw the resulting shape
				public void mouseReleased(MouseEvent e)
				{
					//Graphics g = gui.getGraphics();
					Graphics g = drawPane.getGraphics();
					if (lastx != -1)
					{
						// first undraw a rubber rect
						g.setXORMode(gui.color);
						g.setColor(gui.getBackground());
						doDraw(pressx, pressy, lastx, lasty, g);
						lastx = -1;
						lasty = -1;
					}
					// these commands finish the rubberband mode
					g.setPaintMode();
					g.setColor(gui.color);
					// draw the finel rectangle
					doDraw(pressx, pressy, e.getX(), e.getY(), g);
				}

				// mouse released => temporarily set second corner of rectangle
				// draw the resulting shape in "rubber-band mode"
				public void mouseDragged(MouseEvent e)
				{
					//Graphics g = gui.getGraphics();
					Graphics g = drawPane.getGraphics();
					// these commands set the rubberband mode
					g.setXORMode(gui.color);
					g.setColor(gui.getBackground());
					if (lastx != -1)
					{
						// first undraw previous rubber rect
						doDraw(pressx, pressy, lastx, lasty, g);

					}
					lastx = e.getX();
					lasty = e.getY();
					// draw new rubber rect
					doDraw(pressx, pressy, lastx, lasty, g);
				}

				public void doDraw(int x0, int y0, int x1, int y1, Graphics g)
				{
					// calculate upperleft and width/height of rectangle
					int x = Math.min(x0, x1);
					int y = Math.min(y0, y1);
					int w = Math.abs(x1 - x0);
					int h = Math.abs(y1 - y0);
					// draw rectangle
					g.drawRect(x, y, w, h);
				}
			}

			// if this class is active, ovals are drawn
			class OvalDrawer extends RectangleDrawer
			{
				public void doDraw(int x0, int y0, int x1, int y1, Graphics g)
				{
					int x = Math.min(x0, x1);
					int y = Math.min(y0, y1);
					int w = Math.abs(x1 - x0);
					int h = Math.abs(y1 - y0);
					// draw oval instead of rectangle
					g.drawOval(x, y, w, h);
				}
			}

			ScribbleDrawer scribbleDrawer = new ScribbleDrawer();
			RectangleDrawer rectDrawer = new RectangleDrawer();
			OvalDrawer ovalDrawer = new OvalDrawer();
			ShapeDrawer currentDrawer;

			public ShapeManager(DrawGUI itsGui)
			{
				gui = itsGui;
				drawPane = gui.getDrawPane();
				// default: scribble mode
				currentDrawer = scribbleDrawer;
				// activate scribble drawer
				//gui.addMouseListener(currentDrawer);
				//instead of registering the listener to the toplevel container,
				//register to drawPane.. otherwise there is a ~65 pixel offset
				drawPane.addMouseListener(currentDrawer);
				drawPane.addMouseMotionListener(currentDrawer);
			}

			// reset the shape drawer
			public void setCurrentDrawer(ShapeDrawer l)
			{
				if (currentDrawer == l) return;

				// deactivate previous drawer
				drawPane.removeMouseListener(currentDrawer);
				drawPane.removeMouseMotionListener(currentDrawer);
				// activate new drawer
				currentDrawer = l;
				drawPane.addMouseListener(currentDrawer);
				drawPane.addMouseMotionListener(currentDrawer);
			}

			// user selected new shape => reset the shape mode
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getItem().equals("Scribble"))
				{
					setCurrentDrawer(scribbleDrawer);
				}
				else if (e.getItem().equals("Rectangle"))
				{
					setCurrentDrawer(rectDrawer);
				}
				else if (e.getItem().equals("Oval"))
				{
					setCurrentDrawer(ovalDrawer);
				}
			}
		}

		shape_chooser.addItemListener(new ShapeManager(this));

		class ColorItemListener implements ItemListener
		{

			// user selected new color => store new color in DrawGUI
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getItem().equals("Black"))
				{
					color = Color.black;
				}
				else if (e.getItem().equals("Green"))
				{
					color = Color.green;
				}
				else if (e.getItem().equals("Red"))
				{
					color = Color.red;
				}
				else if (e.getItem().equals("Blue"))
				{
					color = Color.blue;
				}
			}
		}

		color_chooser.addItemListener(new ColorItemListener());

		// Handle the window close request similarly
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				app.doCommand("quit");
			}
		});

		// Finally, set the size of the window, and pop it up
		this.setBackground(Color.white);
		//this.setSize(500, 400);
		this.pack(); //calculated by the preferred size of the drawPane instead of explicitly setting the size
		//this.show();
		this.setVisible(true); //use setVisible(true) instead of deprecated show()
	}
	
	//getter for the private drawPane field
	public JPanel getDrawPane() {
		return drawPane;
	}
}