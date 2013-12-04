package userInterface;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Frontend {

	JFrame PlayerFrame = new JFrame("Video Player");
	JPanel ButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

	JPanel OriginalButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JLabel OriginalVideoLabel = new JLabel();

	JLabel MatchedVideoLabel = new JLabel();

	JTextField inputQuery = new JTextField(); // textfield to take query;
	JList resultList = new JList(); // result list;
	JScrollPane resultListScrollPane = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // scrollpane for
															// listbox;

	static boolean IS_PLAYING = false;
	static boolean OIS_PLAYING = false;

	public void createUi() {

		// CREATE PLAY BUTTON
		final JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (IS_PLAYING == false) {
					// insert actions for play button
					IS_PLAYING = true;
				}
			}
		});

		// CREATE PAUSE BUTTON
		final JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (true == IS_PLAYING) {
					IS_PLAYING = false;

					// insert actions for pause button
				}
			}
		});

		// CREATE STOP BUTTON
		final JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IS_PLAYING = false;

				// insert actions for stop button
			}
		});

		final JButton OriginalPlay = new JButton("Play");
		OriginalPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (OIS_PLAYING == false) {
					// insert actions for play button
					OIS_PLAYING = true;
				}
			}
		});

		// CREATE PAUSE BUTTON
		final JButton OriginalPause = new JButton("Pause");
		OriginalPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (true == OIS_PLAYING) {
					OIS_PLAYING = false;

					// insert actions for pause button
				}
			}
		});

		// CREATE STOP BUTTON
		final JButton OriginalStop = new JButton("Stop");
		OriginalStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OIS_PLAYING = false;

				// insert actions for stop button
			}
		});

		// PlayerFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		// PlayerFrame.setLayout(new GridBagLayout());
		// GridBagConstraints c = new GridBagConstraints();
		PlayerFrame.setLayout(null);
		// PlayerFrame.getContentPane().add(btnPlay);
		// PlayerFrame.getContentPane().add(btnPause);
		// PlayerFrame.getContentPane().add(btnStop);
		OriginalButtonPanel
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		OriginalButtonPanel.add(OriginalPlay);
		OriginalButtonPanel.add(OriginalPause);
		OriginalButtonPanel.add(OriginalStop);

		ButtonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		ButtonPanel.add(btnPlay);
		ButtonPanel.add(btnPause);
		ButtonPanel.add(btnStop);

		/*
		 * c.fill = GridBagConstraints.HORIZONTAL; c.ipady = 0; //reset to
		 * default c.weighty = 1.0; //request any extra vertical space c.anchor
		 * = GridBagConstraints.LAST_LINE_END; //bottom of space c.insets = new
		 * Insets(10,0,0,0); //top padding c.gridx = 1; //aligned with button 2
		 * c.gridwidth = 2; //2 columns wide c.gridy = 2; //third row
		 */
		OriginalButtonPanel.setSize(300, 30);
		OriginalButtonPanel.setLocation(50, 600);
		PlayerFrame.getContentPane().add(OriginalButtonPanel);

		ButtonPanel.setSize(300, 30);
		ButtonPanel.setLocation(540, 600);
		PlayerFrame.getContentPane().add(ButtonPanel);

		OriginalVideoLabel.setSize(352, 288);
		OriginalVideoLabel.setLocation(20, 300);
		OriginalVideoLabel.setBackground(Color.black);
		OriginalVideoLabel.setOpaque(true);

		MatchedVideoLabel.setSize(352, 288);
		MatchedVideoLabel.setLocation(510, 300);

		MatchedVideoLabel.setBackground(Color.black);
		MatchedVideoLabel.setOpaque(true);

		PlayerFrame.getContentPane().add(OriginalVideoLabel);
		PlayerFrame.getContentPane().add(MatchedVideoLabel);

		// add input query box

		inputQuery.setSize(300, 30);
		inputQuery.setLocation(50, 50);
		inputQuery.setText("Input Query to search from Database");
		PlayerFrame.getContentPane().add(inputQuery);

		// add result list box

		// resultList.setSize(300,200);
		// resultList.setLocation(550,50 );

		resultListScrollPane.setViewportView(resultList);
		resultListScrollPane.setSize(300, 200);
		resultListScrollPane.setLocation(530, 50);

		PlayerFrame.getContentPane().add(resultListScrollPane);
		// PlayerFrame.getContentPane().add(resultList);

		PlayerFrame.setSize(900, 700);

		PlayerFrame.setVisible(true);

	}
}