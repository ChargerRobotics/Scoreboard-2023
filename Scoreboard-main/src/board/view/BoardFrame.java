package board.view;

import javax.swing.JFrame;

import board.controller.Controller;

public class BoardFrame extends JFrame
{
	// INSTANTIATE CONTROLLER AND PANEL
	private Controller app;
	private BoardPanel panel;
	
	// CREATE FRAME
	public BoardFrame(Controller app)
	{
		this.app = app;
		this.panel = new BoardPanel(app);
		
		setupFrame();
	}
	
	//  CREATE AND SHOW WINDOW
	private void setupFrame()
	{
		this.setTitle("Charger Robotics Tournament");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setContentPane(panel);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	// PASS FUNCTIONS BETWEEN BOARDPANEL AND CONTROLLER
	public boolean updateTimer(long minutes, long seconds)
	{
		return this.panel.updateTimer(minutes, seconds);
	}
	
	public boolean checkReset()
	{
		return this.panel.checkReset();
	}
}
