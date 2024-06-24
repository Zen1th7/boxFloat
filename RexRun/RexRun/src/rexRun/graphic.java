package rexRun;

import java.awt.Graphics;

import javax.swing.JPanel;

public class graphic extends JPanel {

	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		rexRun.rexRun.repaint(g);
	}
	
}
