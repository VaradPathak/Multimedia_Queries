package userInterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MultimediaQueryPlayer extends JFrame {

	private static final long serialVersionUID = 7145803905715809380L;
	private JCheckBox checkbox;
	private JTextField firstName;
	private JTextField lastName;

	public MultimediaQueryPlayer() {

		setTitle("My First UI");

		// We create a panel which will hold the UI components
		JPanel pane = new JPanel(new BorderLayout());
		// We always have two UI elements (columns) and we have three rows
		int numberOfRows = 4;
		int numberOfColumns = 3;
		pane.setLayout(new GridLayout(numberOfRows, numberOfColumns));

		// create and attach buttons
		// create a label and add it to the main window
		JLabel firstNamelabel = new JLabel(" Firstname: ");
		pane.add(firstNamelabel);
		firstName = new JTextField();
		pane.add(firstName);

		JLabel lastNamelabel = new JLabel(" Lastname: ");
		pane.add(lastNamelabel);
		lastName = new JTextField();
		pane.add(lastName);

		JButton sayHello = new JButton("Say something");
		pane.add(sayHello);

		checkbox = new JCheckBox("Nice");
		pane.add(checkbox);

		// add the pane to the main window
		getContentPane().add(pane);

		// Pack will make the size of window fitting to the compoents
		// You could also use for example setSize(300, 400);
//		pack();
		setSize(300, 400);

		// Set a tooltip for the button
		sayHello.setToolTipText("This button will say something really nice of something bad");
		// sayHello need to do something
		sayHello.addActionListener(new MyActionListener());
	}

	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!checkbox.isSelected()) {
				JOptionPane.showMessageDialog(null, "I do not like you, "
						+ firstName.getText() + " " + lastName.getText() + "!");
			} else {
				JOptionPane.showMessageDialog(
						null,
						"How are you, " + firstName.getText() + " "
								+ lastName.getText() + "?");
			}
		}

	}
}