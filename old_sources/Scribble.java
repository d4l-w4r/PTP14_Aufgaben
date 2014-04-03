// Adapted to JAVA 1.1 AWT event-model
// This example is from the book "Java in a Nutshell, Second Edition".
// Written by David Flanagan.  Copyright (c) 1997 O'Reilly & Associates.
// You may distribute this source code for non-commercial purposes only.
// You may study, modify, and use this example for any purpose, as long as
// this notice is retained.  Note that this example is provided "as is",
// WITHOUT WARRANTY of any kind either expressed or implied.
// <APPLET CODE=Scribble.class WIDTH= 300 HEIGHT=300></APPLET>

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Scribble extends Applet
                      implements MouseListener, MouseMotionListener,
                                 ActionListener, ItemListener {
  private int last_x, last_y;                // Store the last mouse position.
  private Color current_color = Color.black; // Store the current color.
  private Button clear_button;               // The clear button.
  private Choice color_choices;              // The color dropdown list.

  // This method is called to initialize the applet.
  // Applets don't have a main() method.
  public void init() {
    // Tell this applet what MouseListener and MouseMotionListener
    // objects to notify when mouse and mouse motion events occur.
    // Since we implement the interfaces ourself, our own methods are called.
    this.addMouseListener(this);
    this.addMouseMotionListener(this);

    // Set the background color
    this.setBackground(Color.white);

    // Create a button and add it to the applet.  Set the button's colors
    clear_button = new Button("Clear");
    clear_button.setForeground(Color.black);
    clear_button.setBackground(Color.lightGray);
    clear_button.addActionListener(this); 
    this.add(clear_button);

    // Create a menu of colors and add it to the applet.
    // Also set the menus's colors and add a label.
    color_choices = new Choice();
    color_choices.addItem("black");
    color_choices.addItem("red");
    color_choices.addItem("yellow");
    color_choices.addItem("green");
    color_choices.setForeground(Color.black);
    color_choices.setBackground(Color.lightGray);
    this.add(new Label("Color: "));
    color_choices.addItemListener(this);
    this.add(color_choices);
  }

  // A method from the MouseListener interface.  Invoked when the
  // user presses a mouse button.
  public void mousePressed(MouseEvent e) {
    last_x = e.getX();
    last_y = e.getY();
  }

  // A method from the MouseMotionListener interface.  Invoked when the
  // user drags the mouse with a button pressed.
  public void mouseDragged(MouseEvent e) {
    Graphics g = this.getGraphics();
    g.setColor(current_color);
    int x = e.getX(), y = e.getY();
    g.drawLine(last_x, last_y, x, y);
    last_x = x; last_y = y;
  }

  // This method is called when the user clicks the button
  public void actionPerformed(ActionEvent e) {
    String s = e.getActionCommand();
    // If the Clear button was clicked on, handle it.
    if (s.equals("Clear")) {
      Graphics g = this.getGraphics();
      Rectangle r = this.bounds();
      g.setColor(this.getBackground());
      g.fillRect(r.x, r.y, r.width, r.height);
    }
  }

  // This method is called when the user chooses a color
  public void itemStateChanged(ItemEvent e) {
    String s = (String) e.getItem();
    // If a color was chosen, handle that
      if (s.equals("black")) current_color = Color.black;
      else if (s.equals("red")) current_color = Color.red;
      else if (s.equals("yellow")) current_color = Color.yellow;
      else if (s.equals("green")) current_color = Color.green;
  }

  // The other, unused methods of the MouseListener interface.
  public void mouseReleased(MouseEvent e) {;}
  public void mouseClicked(MouseEvent e) {;}
  public void mouseEntered(MouseEvent e) {;}
  public void mouseExited(MouseEvent e) {;}

  // The other method of the MouseMotionListener interface.
  public void mouseMoved(MouseEvent e) {;}
}
