package board.controller;

import board.view.BoardFrame;
import board.view.Popup;

public class Controller
{
	private BoardFrame frame;

	private Popup popup;

	private long startTime;

	public Controller()
	{
		this.frame = new BoardFrame(this);
		this.popup = new Popup();
		this.startTime = System.currentTimeMillis();
	}

	// START AND LOOP APP WITH TIMER
	public void start()
	{
		while (true)
		{
			popup.displayMessage(frame, "Press Enter to Start");

			long minutes = 5;
			long seconds = 00;
			long now = System.currentTimeMillis();
			
			boolean exit = true;
			boolean checkBonus= false;
			long lastUpdate = System.currentTimeMillis();
			
			// UPDATE TIMER
			while (exit)
			{
				if((minutes == 0 && seconds == 0) || frame.checkReset())
				{
					exit = false;
				}
				now = System.currentTimeMillis();

				if (now - lastUpdate > 1000)
				{
					seconds--;

					if (seconds == -1)
					{
						minutes--;
						seconds = 59;
					}

					checkBonus = this.frame.updateTimer(minutes, seconds);
					
					if(checkBonus)
					{
						minutes++;
					}
					
					lastUpdate = System.currentTimeMillis();
				}

			}

			// RESET APP
			popup.displayMessage(frame, "GAME OVER");
			this.frame.dispose();
			this.frame = new BoardFrame(this);
			this.startTime = System.currentTimeMillis();
		}
	}
}
