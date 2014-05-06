package gyges.view.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;

import javax.swing.JFrame;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

/**
 * Victory dialog box.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class OnWin extends JDialog implements MouseListener, WindowListener
{
	/** Hold the view of the application. */
	protected JFrame frame;

	/**
	 * Create a dialog box to announce the victory.
	 *
	 * @param frame The main frame of the application.
	 * @param status The information to display.
	 * @param title The title of the dialog box.
	 */
	public OnWin(JFrame frame, String status, String title)
	{
		super(frame, "", ModalityType.DOCUMENT_MODAL);
		setSize(170, 70);
		setResizable(false);
		setLocationRelativeTo(frame);
		setTitle(title);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		add(new JLabel(status));

		this.frame = frame;

		addMouseListener(this);
		addWindowListener(this);

		setVisible(true);
	}

	/**
	 * Destroy the dialog box.
	 */
	public void exit()
	{
		setVisible(false);
		dispose();
	}

	/**
	 * Destroy the dialog box when the mouse is clicked.
	 *
	 * @param e The MouseEvent.
	 */
	public void mouseClicked(MouseEvent e)
	{
		exit();
	}

	/**
	 * Destroy the dialog box when the exit button is pressed.
	 *
	 * @param e The WindowEvent.
	 */
	public void windowClosing(WindowEvent e)
	{
		exit();
	}

	// Do nothing, but must be redefined.
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}

	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowOpened(WindowEvent e) {}
}
