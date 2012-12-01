package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LabelingFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2225603632488216748L;
	private JPanel contentPane;
	
	List<Tweet> tweets;
	
	//Fields holding the two types of labels for labeling

	private JTextField firstLabelField;
	private JTextField secondLabelField;

	// Field to hold the tweet text
	private JTextArea tweetArea;

	JLabel tweetTitlePanel;

	// List iterator for moving thru tweets
	private ListIterator<Tweet> tweetIterator;

	// All the labelled tweets so far
	private Set<Tweet> labelledTweets;

	// Current tweet we are looking at
	private Tweet currentTweet;

	// Dummy tweet for beginning and ending
	private Tweet defaultTweet;
	
	JButton openFile, prev, next, delete, finish, firstLabel, secondLabel, check;
	
	private JFileChooser fc;

	private int curTweet, totalTweets;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LabelingFrame frame = new LabelingFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LabelingFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		Panel buttonPanel = new Panel();
		buttonPanel.setBackground(new Color(0, 0, 0));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		openFile = new JButton("Open File");
		openFile.setFont(new Font("Verdana", Font.PLAIN, 11));
		openFile.setBackground(new Color(0, 0, 0));
		openFile.setForeground(new Color(148, 0, 211));
		openFile.addActionListener(this);
		buttonPanel.add(openFile);

		prev = new JButton("Previous");
		prev.setFont(new Font("Verdana", Font.PLAIN, 11));
		prev.setForeground(new Color(148, 0, 211));
		prev.setBackground(new Color(0, 0, 0));
		prev.addActionListener(this);
		buttonPanel.add(prev);

		next = new JButton("Next");
		next.setFont(new Font("Verdana", Font.PLAIN, 11));
		next.setForeground(new Color(148, 0, 211));
		next.setBackground(new Color(0, 0, 0));
		next.addActionListener(this);
		buttonPanel.add(next);

		// delete = new JButton("Delete");
		// delete.addActionListener(this);
		// buttonPanel.add(delete);

		finish = new JButton("Finish");
		finish.setFont(new Font("Verdana", Font.PLAIN, 11));
		finish.setBackground(new Color(0, 0, 0));
		finish.setForeground(new Color(148, 0, 211));
		finish.addActionListener(this);
		buttonPanel.add(finish);
		
//		check = new JButton("Check Finished");
//		check.addActionListener(this);
//		buttonPanel.add(check);

		JLabel Title = new JLabel("Labler");
		Title.setForeground(new Color(148, 0, 211));
		Title.setBackground(new Color(0, 0, 0));
		Title.setFont(new Font("Verdana", Font.PLAIN, 32));
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(Title, BorderLayout.NORTH);

		Panel labelPanel = new Panel();
		contentPane.add(labelPanel, BorderLayout.CENTER);
		labelPanel.setLayout(new BorderLayout(0, 0));

		tweetArea = new JTextArea();
		tweetArea.setText("Sample");
		tweetArea.setFont(new Font("Verdana", Font.PLAIN, 20));
		tweetArea.setForeground(new Color(148, 0, 211));
		tweetArea.setBackground(Color.BLACK);
		tweetArea.setLineWrap(true);
		tweetArea.setWrapStyleWord(true);
		tweetArea.setEditable(false);
		labelPanel.add(tweetArea);

		Panel labelOptionsPanel = new Panel();
		labelOptionsPanel.setBackground(new Color(0, 0, 0));
		labelPanel.add(labelOptionsPanel, BorderLayout.SOUTH);

		firstLabel = new JButton("Label as First");
		firstLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
		firstLabel.setBackground(new Color(0, 0, 0));
		firstLabel.setForeground(new Color(148, 0, 211));
		firstLabel.addActionListener(this);
		labelOptionsPanel.add(firstLabel);

		secondLabel = new JButton("Label as Second");
		secondLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
		secondLabel.setForeground(new Color(148, 0, 211));
		secondLabel.setBackground(new Color(0, 0, 0));
		secondLabel.addActionListener(this);
		labelOptionsPanel.add(secondLabel);

		Panel setLabelsPanel = new Panel();
		setLabelsPanel.setBackground(new Color(0, 0, 0));
		labelPanel.add(setLabelsPanel, BorderLayout.EAST);
		GridBagLayout gbl_setLabelsPanel = new GridBagLayout();
		gbl_setLabelsPanel.columnWidths = new int[] { 30, 30, 30 };
		gbl_setLabelsPanel.rowHeights = new int[] { 30, 30, 30 };
		gbl_setLabelsPanel.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl_setLabelsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		setLabelsPanel.setLayout(gbl_setLabelsPanel);

		JLabel lblLabels = new JLabel("Labels");
		lblLabels.setFont(new Font("Verdana", Font.PLAIN, 11));
		lblLabels.setForeground(new Color(148, 0, 211));
		lblLabels.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_lblLabels = new GridBagConstraints();
		gbc_lblLabels.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabels.gridx = 1;
		gbc_lblLabels.gridy = 0;
		setLabelsPanel.add(lblLabels, gbc_lblLabels);

		JLabel lblFirst = new JLabel("First: ");
		lblFirst.setFont(new Font("Verdana", Font.PLAIN, 11));
		lblFirst.setForeground(new Color(148, 0, 211));
		GridBagConstraints gbc_lblFirst = new GridBagConstraints();
		gbc_lblFirst.anchor = GridBagConstraints.EAST;
		gbc_lblFirst.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirst.gridx = 0;
		gbc_lblFirst.gridy = 1;
		setLabelsPanel.add(lblFirst, gbc_lblFirst);

		firstLabelField = new JTextField();
		firstLabelField.setFont(new Font("Verdana", Font.PLAIN, 11));
		firstLabelField.setForeground(new Color(148, 0, 211));
		firstLabelField.setBackground(new Color(0, 0, 0));
		firstLabelField.setText("<Enter text>");
		GridBagConstraints gbc_firstLabelField = new GridBagConstraints();
		gbc_firstLabelField.insets = new Insets(0, 0, 5, 5);
		gbc_firstLabelField.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstLabelField.gridx = 1;
		gbc_firstLabelField.gridy = 1;
		setLabelsPanel.add(firstLabelField, gbc_firstLabelField);
		firstLabelField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Second:");
		lblNewLabel_1.setFont(new Font("Verdana", Font.PLAIN, 11));
		lblNewLabel_1.setForeground(new Color(148, 0, 211));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		setLabelsPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		secondLabelField = new JTextField();
		secondLabelField.setFont(new Font("Verdana", Font.PLAIN, 11));
		secondLabelField.setForeground(new Color(148, 0, 211));
		secondLabelField.setBackground(new Color(0, 0, 0));
		secondLabelField.setText("<Enter text>");
		GridBagConstraints gbc_secondLabelField = new GridBagConstraints();
		gbc_secondLabelField.insets = new Insets(0, 0, 0, 5);
		gbc_secondLabelField.fill = GridBagConstraints.HORIZONTAL;
		gbc_secondLabelField.gridx = 1;
		gbc_secondLabelField.gridy = 2;
		setLabelsPanel.add(secondLabelField, gbc_secondLabelField);
		secondLabelField.setColumns(10);

		tweetTitlePanel = new JLabel("Tweet");
		tweetTitlePanel.setFont(new Font("Verdana", Font.PLAIN, 15));
		tweetTitlePanel.setForeground(new Color(148, 0, 211));
		tweetTitlePanel.setBackground(new Color(0, 0, 0));
		tweetTitlePanel.setHorizontalAlignment(SwingConstants.LEFT);
		labelPanel.add(tweetTitlePanel, BorderLayout.NORTH);

		fc = new JFileChooser();
		fc.setFileFilter(new TweetFilter());

		labelledTweets = new HashSet<Tweet>();

		defaultTweet = new Tweet(0, "No more tweets!");
		currentTweet = defaultTweet;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFile) {
			this.loadUnLabeledFile();
			if (tweetIterator != null) {
				currentTweet = tweetIterator.next();
				this.updateUi(currentTweet);
			}
		} else if (e.getSource() == prev) {
			if (tweetIterator != null && tweetIterator.hasPrevious()) {
				currentTweet = tweetIterator.previous();
				--curTweet;
				this.updateUi(currentTweet);
			} else {
				JOptionPane.showMessageDialog(this, "You can't do that!",
						"User Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == next) {
			if (tweetIterator != null && tweetIterator.hasNext()) {
				currentTweet = tweetIterator.next();
				++curTweet;
				this.updateUi(currentTweet);
			}
		} else if (e.getSource() == delete) {
			try {
				if (tweetIterator != null) {
					tweetIterator.remove();
					currentTweet = new Tweet(0, "Freshly Deleted!");
					updateUi(currentTweet);
				} else {
					JOptionPane.showMessageDialog(this, "You can't do that!",
							"User Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (IllegalStateException ise) {
				JOptionPane.showMessageDialog(this, "You can't do that!",
						"User Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == finish) {
			this.saveAndQuit();
		} else if (e.getSource() == firstLabel || e.getSource() == secondLabel) {
			if (currentTweet != null
					&& !firstLabelField.getText().equals("<Enter text>")
					&& !secondLabelField.getText().equals("<Enter text>")) {
				currentTweet.label = (e.getSource() == firstLabel) ? firstLabelField
						.getText() : secondLabelField.getText();
				boolean moveTwice = (labelledTweets.contains(currentTweet));
				if (currentTweet != defaultTweet) {
					labelledTweets.add(currentTweet);
					System.out.println("Labelled tweet: " + currentTweet);
				}
				if (tweetIterator.hasNext()) {
					currentTweet = tweetIterator.next();
					++curTweet;
				} else {
					JOptionPane.showMessageDialog(this, "That's all of them!",
							"Info", JOptionPane.INFORMATION_MESSAGE);
					currentTweet = defaultTweet;
				}

				// If we moved backwards thru the list we need to compensate
				if (moveTwice && tweetIterator.hasNext()) {
					currentTweet = tweetIterator.next();
				}
				updateUi(currentTweet);
			} else {
				JOptionPane.showMessageDialog(this,
						"You need to set the labels!", "User Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
//		else if (e.getSource() == check) {
//			Set<Tweet> labelled = this.loadLabledFile();
//			List<Tweet> lk = new LinkedList<Tweet>(labelled);
//			List<Tweet> toRemove = new ArrayList<Tweet>();
//			if (labelled != null) {
//				labelledTweets.removeAll(labelled);
//				int removed = 0;
//				while (tweetIterator.hasNext()) {
//					Tweet cur = tweetIterator.next();
////					if (labelled.contains(cur)) {
////						tweetIterator.remove();
////						++removed;
////					}
//					
//					for (int i = 0; i < lk.size(); ++i) {
//						if (cur.tweetEquals(lk.get(i))) {
//							//tweetIterator.remove();
//							toRemove.add(cur);
//							++removed;
//							break;
//						}
//					}
//					//Fast break if we found everything already
//					if (removed == labelled.size()) {
//						break;
//					}
//				}
//				
//				tweets.removeAll(toRemove);
//				tweetIterator = tweets.listIterator();
//				if (tweetIterator.hasNext()) {
//					currentTweet = tweetIterator.next();
//				}
//				else {
//					currentTweet = defaultTweet;
//				}
//				JOptionPane.showMessageDialog(this, "Removed: " + removed + 
//						((removed != 1) ? " Tweets," : " Tweet,") + " already classified", "Info",
//            			JOptionPane.INFORMATION_MESSAGE);
//			}
//		}
		updateUi(currentTweet);
	}

	private void saveAndQuit() {
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (labelledTweets.size() != 0) {
				this.saveTweets(fc.getSelectedFile());
			}
			System.exit(0);
		}
	}

	private void saveTweets(File f) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(f));
			for (Tweet t : labelledTweets) {
				bw.write(t.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Unable to save file :(",
					"IO Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateUi(Tweet newTweet) {
		tweetArea.setText(newTweet.text);
		tweetTitlePanel.setText("Tweet number " + curTweet + " of " + totalTweets);
		String ftext = firstLabelField.getText().equals("<Enter text>") ? "first" : firstLabelField.getText();
		String stext = secondLabelField.getText().equals("<Enter text>") ? "second" : secondLabelField.getText();
		firstLabel.setText("Label as: " + (ftext));
		secondLabel.setText("Label as: " + stext);
	}
	
//	public Set<Tweet> loadLabledFile() {
//		int returnVal = fc.showOpenDialog(this);
//		
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			File file = fc.getSelectedFile();
//			String line = "";
//			try {
//				BufferedReader br = new BufferedReader(new FileReader(file));
//				Set<Tweet> tweets = new HashSet<Tweet>();
//				while ((line = br.readLine()) != null) {
//					Tweet n = makeLabelledTweet(line);
//					//Tweet n = makeTweet(line);
//					if (n != null) {
//						tweets.add(n);
//					}
//				}
//				br.close();
//				return tweets;
//					
//			} catch (IOException e) {
//				JOptionPane.showMessageDialog(this, "Unable to open file", "IO Error",
//            			JOptionPane.ERROR_MESSAGE);
//			}
//		}
//		return null;
//	}

	public void loadUnLabeledFile() {
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String line = "";
			int failedTweets = 0;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				tweets = new LinkedList<Tweet>();
				while ((line = br.readLine()) != null) {
					Tweet n = makeTweet(line);
					if (n != null) {
						tweets.add(n);
					} else {
						++failedTweets;
					}
				}
				br.close();
				JOptionPane.showMessageDialog(this, "Successfully read: "
						+ tweets.size()
						+ ((tweets.size() != 1) ? " Tweets. " : " Tweet. ")
						+ "Failed to read " + failedTweets
						+ ((failedTweets != 1) ? " Tweets." : " Tweet"),
						"IO Error", JOptionPane.INFORMATION_MESSAGE);
				tweetIterator = tweets.listIterator();
				totalTweets = tweets.size();
				curTweet = 1;

			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Unable to open file",
						"IO Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private Tweet makeTweet(String tweetText) {
		try {
			Tweet newTweet = new Tweet(tweetText);
			return newTweet;
		} catch (NumberFormatException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NoSuchElementException e) {
			return null;
		}

	}
	
//	private Tweet makeLabelledTweet(String tweetText) {
//		int firstSpace = tweetText.indexOf(" ");
//		String label = tweetText.substring(0, firstSpace);
//		Tweet t = makeTweet(tweetText.substring(firstSpace + 1, tweetText.length()));
//		return t;
//	}

	private class Tweet {
		public long id;
		public String text;
		public String label;

		public Tweet(long id, String text) {
			this.id = id;
			this.text = text;
			this.label = "";
		}

		public Tweet(String tweet) {
			StringTokenizer st = new StringTokenizer(tweet);
			id = Long.parseLong(st.nextToken());

			StringBuilder sb = new StringBuilder();

			while (st.hasMoreTokens()) {
				sb.append(st.nextToken());
				sb.append(" ");
			}

			text = sb.toString();
		}

		@Override
		public String toString() {
			return label.toLowerCase() + " " + id + " " + text;
		}

		@Override
		public int hashCode() {
			long hash = 7;
			hash = hash * 31 + id;
			//hash = hash * 31 + text.hashCode();
			for (String s: text.split(" ")) {
				hash = hash * 31 + s.hashCode();
			}
			return (int) hash;
		}
		
		public boolean tweetEquals(Tweet other) {
			if (this.id == other.id) {
				StringTokenizer me = new StringTokenizer(text);
				StringTokenizer them = new StringTokenizer(other.text);
				while (me.hasMoreTokens() && them.hasMoreElements()) {
					if (!me.nextToken().equals(them.nextToken())) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}
	}

	private class TweetFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			String name = f.getName().toLowerCase();
			return f.isDirectory() || name.endsWith(".txt")
					|| name.endsWith(".dan") || name.endsWith(".brandon")
					|| name.endsWith(".curtis");
		}

		@Override
		public String getDescription() {
			return "Allows files with txt, dan, brandon, or curtis extensions";
		}

	}

}
