package board.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Popup {

	public void displayMessage(JFrame frame, String message) {
		JOptionPane.showMessageDialog(frame, message, null, JOptionPane.PLAIN_MESSAGE, null);
	}
}
