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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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

public class LabelingFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2225603632488216748L;
	private JPanel contentPane;

	// Fields holding the two types of labels for labeling
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

	JButton openFile, prev, next, delete, finish, firstLabel, secondLabel;

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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		Panel buttonPanel = new Panel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		openFile = new JButton("Open File");
		openFile.addActionListener(this);
		buttonPanel.add(openFile);

		prev = new JButton("Previous");
		prev.addActionListener(this);
		buttonPanel.add(prev);

		next = new JButton("Next");
		next.addActionListener(this);
		buttonPanel.add(next);

		// delete = new JButton("Delete");
		// delete.addActionListener(this);
		// buttonPanel.add(delete);

		finish = new JButton("Finish");
		finish.addActionListener(this);
		buttonPanel.add(finish);

		JLabel Title = new JLabel("Labler");
		Title.setFont(new Font("Tahoma", Font.PLAIN, 32));
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(Title, BorderLayout.NORTH);

		Panel labelPanel = new Panel();
		contentPane.add(labelPanel, BorderLayout.CENTER);
		labelPanel.setLayout(new BorderLayout(0, 0));

		tweetArea = new JTextArea();
		tweetArea.setLineWrap(true);
		tweetArea.setWrapStyleWord(true);
		tweetArea.setEditable(false);
		labelPanel.add(tweetArea);

		Panel labelOptionsPanel = new Panel();
		labelPanel.add(labelOptionsPanel, BorderLayout.SOUTH);

		firstLabel = new JButton("Label as First");
		firstLabel.addActionListener(this);
		labelOptionsPanel.add(firstLabel);

		secondLabel = new JButton("Label as Second");
		secondLabel.addActionListener(this);
		labelOptionsPanel.add(secondLabel);

		Panel setLabelsPanel = new Panel();
		labelPanel.add(setLabelsPanel, BorderLayout.EAST);
		GridBagLayout gbl_setLabelsPanel = new GridBagLayout();
		gbl_setLabelsPanel.columnWidths = new int[] { 30, 30, 30 };
		gbl_setLabelsPanel.rowHeights = new int[] { 30, 30, 30 };
		gbl_setLabelsPanel.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl_setLabelsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		setLabelsPanel.setLayout(gbl_setLabelsPanel);

		JLabel lblLabels = new JLabel("Labels");
		GridBagConstraints gbc_lblLabels = new GridBagConstraints();
		gbc_lblLabels.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabels.gridx = 1;
		gbc_lblLabels.gridy = 0;
		setLabelsPanel.add(lblLabels, gbc_lblLabels);

		JLabel lblFirst = new JLabel("First: ");
		GridBagConstraints gbc_lblFirst = new GridBagConstraints();
		gbc_lblFirst.anchor = GridBagConstraints.EAST;
		gbc_lblFirst.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirst.gridx = 0;
		gbc_lblFirst.gridy = 1;
		setLabelsPanel.add(lblFirst, gbc_lblFirst);

		firstLabelField = new JTextField();
		firstLabelField.setText("<Enter text>");
		GridBagConstraints gbc_firstLabelField = new GridBagConstraints();
		gbc_firstLabelField.insets = new Insets(0, 0, 5, 5);
		gbc_firstLabelField.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstLabelField.gridx = 1;
		gbc_firstLabelField.gridy = 1;
		setLabelsPanel.add(firstLabelField, gbc_firstLabelField);
		firstLabelField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Second:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		setLabelsPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		secondLabelField = new JTextField();
		secondLabelField.setText("<Enter text>");
		GridBagConstraints gbc_secondLabelField = new GridBagConstraints();
		gbc_secondLabelField.insets = new Insets(0, 0, 0, 5);
		gbc_secondLabelField.fill = GridBagConstraints.HORIZONTAL;
		gbc_secondLabelField.gridx = 1;
		gbc_secondLabelField.gridy = 2;
		setLabelsPanel.add(secondLabelField, gbc_secondLabelField);
		secondLabelField.setColumns(10);

		tweetTitlePanel = new JLabel("Tweet");
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
			this.loadFile();
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
		tweetTitlePanel.setText("Tweet number " + curTweet + " of "
				+ totalTweets);
		firstLabel.setText("Label as: " + firstLabelField.getText());
		secondLabel.setText("Label as: " + secondLabelField.getText());
	}

	public void loadFile() {
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String line = "";
			int failedTweets = 0;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				List<Tweet> tweets = new LinkedList<Tweet>();
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
		}
	}

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
			hash = hash * 31 + text.hashCode();
			return (int) hash;
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
