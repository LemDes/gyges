package gyges.view.gui;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The button used for launching a new game of Gygès.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class ResetButton extends JButton implements ActionListener
{
	/** Link to the view */
	UiView view;
	
	/**
	 * Create the reset button.
	 *
	 * @param view The view of the application.
	 * @param text The text of the button.
	 */
	public ResetButton(UiView view, String text)
	{
		super(text);

		setOpaque(false);
		setContentAreaFilled(false);

		this.view = view;

		setVisible(true);
		addActionListener(this);
	}

	/**
	 * Launch a new game when pressed.
	 * 
	 * @param e The event recieved.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		view.cbNewGame();
	}
}
