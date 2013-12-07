package userInterface;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import playVideo.Main;
import playVideo.PlayVideo;
import resultComp.ResultComp;
import Result.Result;
import extractInformation.ExtractFeatures;

public class Frontend {

	public String queryName = null;
	int width = 352;
	int height = 288;
	//remember to store query video in query folder!
	String queryPath = "F:\\git\\test\\Multimedia_Queries\\FinalProject\\query\\";
	String dbPath = "F:\\git\\test\\Multimedia_Queries\\FinalProject\\video_data\\";
	JFrame PlayerFrame = new JFrame("Video Player");
	//JPanel ButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	//JFrame ButtonPanel = new JFrame();
	JPanel OriginalButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	public JLabel OriginalVideoLabel = new JLabel();

	JLabel MatchedVideoLabel = new JLabel();

	JTextField inputQuery = new JTextField(); // textfield to take query;
	JList resultList = new JList(); // result list;
	JScrollPane resultListScrollPane = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // scrollpane for
															// listbox;

	
	//slider for query
	JSlider queryscrollbar = new JSlider(JSlider.HORIZONTAL,
            0, Integer.MAX_VALUE, 0); 
	
	//slider for matched video
	JSlider matchedscrollbar = new JSlider(JSlider.HORIZONTAL,
            0, Integer.MAX_VALUE, 0); 
	
	
	
	
	static boolean IS_PLAYING = false;
	static boolean OIS_PLAYING = false;

	
	static boolean IS_SELECTED = false; //set to true after a result is selected from the list box
	static boolean IS_SEARCHED = false;
	
	Runnable matchedvideoTask;
	Thread matchedvideoWorker;
	PlayVideo matchedvideotemp; 
	
	Runnable queryvideoTask;
	Thread queryvideoWorker;
	PlayVideo queryvideotemp; 
	
	
	
	
	public void createUi() throws IOException {

		// CREATE PLAY BUTTON
		final JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (IS_PLAYING == false) {
					// insert actions for play button
					IS_PLAYING = true;
					
	
					if(IS_SELECTED == true) {
						IS_SELECTED = false;
						String videoname = resultList.getSelectedValue().toString()+".rgb";
						videoname = dbPath + videoname;
						matchedvideoTask = new PlayVideo(width, height, videoname, MatchedVideoLabel,matchedscrollbar);
						matchedvideotemp = (PlayVideo)matchedvideoTask;
						matchedvideoWorker = new Thread(matchedvideoTask);
						matchedvideoWorker.setName("videoplay");
						matchedvideoWorker.start();
						
					}
								
				}
				else if(matchedvideotemp.IS_PAUSED == true) {
					
					
					matchedvideotemp.resumeVideo();
				}
				else if(IS_PLAYING ==true && IS_SELECTED == true) {
					
					matchedvideotemp.stopVideo();
					String videoname = resultList.getSelectedValue().toString()+".rgb";
					videoname = dbPath + videoname;
					matchedvideoTask = new PlayVideo(width, height, videoname, MatchedVideoLabel,matchedscrollbar );
					matchedvideotemp = (PlayVideo)matchedvideoTask;
					matchedvideoWorker = new Thread(matchedvideoTask);
					matchedvideoWorker.setName("videoplay");
					matchedvideoWorker.start();
				}
				
			}
		});

		// CREATE PAUSE BUTTON
		final JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (true == IS_PLAYING) {
					//IS_PLAYING = false;

					if(matchedvideotemp.IS_PAUSED == false) {
						matchedvideotemp.PauseVideo();
					}
						
					
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
				
				matchedvideotemp.stopVideo();
				// insert actions for stop button
				MatchedVideoLabel.setBackground(Color.black);
				MatchedVideoLabel.setForeground(Color.black);
				MatchedVideoLabel.setIcon(null);
				matchedscrollbar.setValue(0);
			}
		});

		final JButton OriginalPlay = new JButton("Play");
		OriginalPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (OIS_PLAYING == false) {
					// insert actions for play button
					OIS_PLAYING = true;
					
					if (IS_SEARCHED == true) {
						String videoname = queryName.substring(0, queryName.lastIndexOf(" "));
						videoname = queryPath + videoname;
						queryvideoTask = new PlayVideo(width, height, videoname, OriginalVideoLabel,queryscrollbar);
						queryvideotemp = (PlayVideo)queryvideoTask;
						queryvideoWorker = new Thread(queryvideoTask);
						queryvideoWorker.setName("queryvideoplay");
						queryvideoWorker.start();
					}
				}
				else if(queryvideotemp.IS_PAUSED == true) {
					queryvideotemp.resumeVideo();
				}
				else if(OIS_PLAYING == true)
				{
					queryvideotemp.stopVideo();
					String videoname = queryName.substring(0, queryName.lastIndexOf(" "));
					videoname = queryPath + videoname;
					queryvideoTask = new PlayVideo(width, height, videoname, OriginalVideoLabel,queryscrollbar);
					queryvideotemp = (PlayVideo)queryvideoTask;
					queryvideoWorker = new Thread(queryvideoTask);
					queryvideoWorker.setName("queryvideoplay");
					queryvideoWorker.start();
					
				}
				
			}
		});

		// CREATE PAUSE BUTTON
		final JButton OriginalPause = new JButton("Pause");
		OriginalPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (true == OIS_PLAYING) {
					//OIS_PLAYING = false;

					// insert actions for pause button
					if(queryvideotemp.IS_PAUSED == false) {
						queryvideotemp.PauseVideo();
					}

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
				queryvideotemp.stopVideo();
				OriginalVideoLabel.setBackground(Color.black);
				OriginalVideoLabel.setIcon(null);
				queryscrollbar.setValue(0);
			}
		});
		
		
		final JButton Searchbutton = new JButton("Search");
		Searchbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				IS_PLAYING = false;
				OIS_PLAYING = false;
				IS_SEARCHED = true;
				
				queryName = inputQuery.getText();
				String videoFileName = queryPath + queryName.substring(0,
						queryName.lastIndexOf(" "));
				System.out.println(videoFileName);
				
				String audioFileName = queryPath +
						queryName.substring(queryName.lastIndexOf(" ")+1,queryName.length());
				System.out.println(audioFileName);
				// insert actions for search button
				List<Double[]> videoFeatures = new ArrayList<>();
				List<Double[]> audioFeatures = new ArrayList<>();

				Runnable extractTask = new ExtractFeatures(width, height,
						videoFileName, videoFeatures, audioFileName,
						audioFeatures, 1);

				Thread extractWorker = new Thread(extractTask);
				extractWorker.setName("query");
				//extract the features of query into the arraylist feature1
				extractWorker.start();
				try {
					extractWorker.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					List<Result> videoMatchResult = Main.match_with_all_videos(
							videoFeatures, videoFileName);

					Map<String, Result> audioMatchResult = Main
							.match_with_all_audios(audioFeatures, audioFileName);

					for (Result result : videoMatchResult) {
						if (audioMatchResult.containsKey(result.videoname)) {
							result.rank_value += audioMatchResult
									.get(result.videoname).rank_value;
						}
					}

					Collections.sort(videoMatchResult, new ResultComp());
					ArrayList<String> array = new ArrayList<>();
					
					for (int i = 0; i < videoMatchResult.size(); i++) {
						array.add(videoMatchResult.get(i).videoname);
					}
					Object[] arrayObject=array.toArray();
					resultList.setListData(arrayObject);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}

			
		});
		
		resultList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
			
				IS_SELECTED = true;
			}
		});
			
		matchedscrollbar.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (true == IS_PLAYING) {
					
					int temp  = matchedscrollbar.getValue();
					matchedvideotemp.i = temp;
					
				}
				
			}
		});
			
		queryscrollbar.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (true == IS_PLAYING) {
					
					int temp  = queryscrollbar.getValue();
					queryvideotemp.i = temp;
					
				}
				
			}
		});
			
		

		// PlayerFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		// PlayerFrame.setLayout(new GridBagLayout());
		// GridBagConstraints c = new GridBagConstraints();
		PlayerFrame.setLayout(null);
		String imgpath = "F:\\git\\test\\Multimedia_Queries\\FinalProject\\ninja.png";
		
		JLabel imgLabel =	new JLabel(new ImageIcon(ImageIO.read(new File(imgpath))));
		//JLabel imgforbutton = new JLabel(new ImageIcon(ImageIO.read(new File(imgpath))));
		//imgLabel.setSize(900,700);		
		PlayerFrame.setContentPane(imgLabel);
				// PlayerFrame.getContentPane().add(btnPlay);
		// PlayerFrame.getContentPane().add(btnPause);
		// PlayerFrame.getContentPane().add(btnStop);
		//OriginalButtonPanel
		//		.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//OriginalButtonPanel.add(OriginalPlay);
		//OriginalButtonPanel.add(OriginalPause);
		//OriginalButtonPanel.add(OriginalStop);
		
		//buttons for playing query video
		OriginalPlay.setSize(70,30);
		OriginalPlay.setLocation(65, 720);
		PlayerFrame.getContentPane().add(OriginalPlay);
		
		OriginalPause.setSize(80,30);
		OriginalPause.setLocation(145, 720);
		PlayerFrame.getContentPane().add(OriginalPause);
		
		OriginalStop.setSize(70,30);
		OriginalStop.setLocation(235, 720);
		PlayerFrame.getContentPane().add(OriginalStop);
		
		//buttons for matched video
		btnPlay.setSize(70,30);
		btnPlay.setLocation(765, 720);
		PlayerFrame.getContentPane().add(btnPlay);
		
		btnPause.setSize(80,30);
		btnPause.setLocation(845, 720);
		PlayerFrame.getContentPane().add(btnPause);
		
		btnStop.setSize(70,30);
		btnStop.setLocation(935, 720);
		PlayerFrame.getContentPane().add(btnStop);
		
		
		
		
		
		//ButtonPanel.add(imgforbutton);
		//ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		//ButtonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//ButtonPanel.add(btnPlay);
		//ButtonPanel.add(btnPause);
		//ButtonPanel.add(btnStop);
		//PlayerFrame.getContentPane().add(ButtonPanel);
		
		//OriginalButtonPanel.setSize(225, 30);
		//OriginalButtonPanel.setLocation(80, 600);
		//PlayerFrame.getContentPane().add(OriginalButtonPanel);

		//ButtonPanel.setSize(225, 30);
		//ButtonPanel.setLocation(570, 600);
		
		

		OriginalVideoLabel.setSize(352, 288);
		OriginalVideoLabel.setLocation(20, 400);
		OriginalVideoLabel.setBackground(Color.black);
		OriginalVideoLabel.setOpaque(true);

		MatchedVideoLabel.setSize(352, 288);
		MatchedVideoLabel.setLocation(710, 400);

		MatchedVideoLabel.setBackground(Color.black);
		MatchedVideoLabel.setOpaque(true);

		//add scrubber for query
		
		queryscrollbar.setSize(352, 20);
		queryscrollbar.setLocation(20, 688);
		PlayerFrame.getContentPane().add(queryscrollbar);
		
		//add video scrubber for matched video
		matchedscrollbar.setSize(352, 20);
		matchedscrollbar.setLocation(710, 688);
		PlayerFrame.getContentPane().add(matchedscrollbar);
		
		
		PlayerFrame.getContentPane().add(OriginalVideoLabel);
		PlayerFrame.getContentPane().add(MatchedVideoLabel);

		// add input query box

		inputQuery.setSize(300, 30);
		inputQuery.setLocation(50, 50);
		inputQuery.setText("Input Query to search from Database");
		PlayerFrame.getContentPane().add(inputQuery);
		
		//button to search the query
		Searchbutton.setSize(90, 25);;
		Searchbutton.setLocation (50,90);
		PlayerFrame.getContentPane().add(Searchbutton);
		

		// add result list box

		// resultList.setSize(300,200);
		// resultList.setLocation(550,50 );

		resultListScrollPane.setViewportView(resultList);
		resultListScrollPane.setSize(270, 390);
		resultListScrollPane.setLocation(1100, 400);

		PlayerFrame.getContentPane().add(resultListScrollPane);
		// PlayerFrame.getContentPane().add(resultList);

		PlayerFrame.setSize(1400, 800);
		
		
		//BufferedImage img = ImageIO.read(new File(imgpath));
		
		PlayerFrame.setVisible(true);

	}
}
