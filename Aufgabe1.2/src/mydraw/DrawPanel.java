package mydraw;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class DrawPanel extends JPanel {
	
	BufferedImage _buffer;
	
	public DrawPanel(int w, int h) {
		_buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(_buffer, 0, 0, null); 
		
	}
/*
	@Override
	public void setSize(int width, int height) {
		BufferedImage newBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = _buffer.getGraphics();
		g.drawImage(newBuff, 0, 0, null);
		_buffer = newBuff;
		this.resize(new Dimension(width, height));
		this.repaint();
	}
	*/
	public Graphics getBufferedGraphics() {
		return _buffer.getGraphics();
	}
	
	public void onResize() {
		int w = super.getWidth();
		int h = super.getHeight();
		if( w < 1 || h < 1) {
			BufferedImage newBuff = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			Graphics g = newBuff.getGraphics();
			g.drawImage(_buffer, 0, 0, null);
			_buffer = newBuff;
			return;
		}
		BufferedImage newBuff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = newBuff.getGraphics();
		g.drawImage(_buffer, 0, 0, null);
		_buffer = newBuff;		
	}
	
}