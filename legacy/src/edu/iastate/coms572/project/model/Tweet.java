package edu.iastate.coms572.project.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.iastate.coms572.project.constants.TweetConstants;
import edu.iastate.coms572.project.util.Util;

public class Tweet {
	private static final Logger LOGGER = Logger.getLogger(Tweet.class.getName());

	/*
	 * User sending the tweet
	 */
	private User fromUser;
	
	/*
	 * User receiving the tweet
	 */
	private User toUser;
	
	/*
	 * Twitter given tweet id
	 */
	private long tweetId;
	
	/*
	 * Text for the actual tweet
	 */
	private String tweetText;
	
	/**
	 * The time the tweet was sent
	 */
	private Calendar timestamp;

	public Tweet(User fromUser, User toUser, long id, String text, Calendar timestamp) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		tweetId = id;
		tweetText = text;
		this.timestamp = timestamp;
	}
	
	/**
	 * Returns the string for this tweet in the order:
	 * 	"tweet id, toUserUserName, fromUserUserName, tweetText"
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return builder
				.append(tweetId)
				.append(TweetConstants.DELIMIT)
				.append(fromUser.toString())
				.append(TweetConstants.DELIMIT)
				.append(toUser.toString())
				.append(TweetConstants.DELIMIT)
				.append(tweetText)
				.toString();
	}
	
	/**
	 * Method used to print out the contents of this tweet in a specific order
	 * @param order
	 * 				A list of Tweet Map keys in the order you want the values to appear in the return string
	 * @param printFullUser
	 * 						Flag to indicate whether all the information about the user should be printed
	 * @return
	 * 			A string repesentation of this tweet in the given order
	 */
	public String toStringWithOrder(List<String> order, boolean printFullUser) {
		StringBuilder builder = new StringBuilder();
		
		//We need to handle keys being incorrect so we check if they are ok and then convert if possible
		if (!Util.areValidTweetMapValues(order)) {
			LOGGER.log(Level.INFO, "Not all Tweet Map Keys are valid, attempting to fix.");
			boolean successConvert = Util.convertToValidKeys(order);
			if (!successConvert) {
				LOGGER.log(Level.WARNING, "Unable to convert all keys to valid tweet map keys, continuing with toString.");
			}
			else {
				LOGGER.log(Level.INFO, "Successfully able to fix keys.");
			}
		}
		
		Map<String, Object> valuesMap = convertToMap();
		
		for (int i = 0; i < order.size(); ++i) {
			String curKey = order.get(i);
			Object curValue = valuesMap.get(curKey);
			
			//Meaning curKey is an invalid key
			if (curValue == null) {
				continue;
			}
			else {
				//We put the delimit value on after adding the value so if we aren't the first element then we should
				//add it
				if (i != 0) {
					builder.append(TweetConstants.DELIMIT);
				}
				//If we are printing the full value of users we need to treat them specially and call their specific toStringFull method
				if (printFullUser && (curKey.equals(TweetConstants.TWEETFROMUSER) || curKey.equals(TweetConstants.TWEETTOUSER))) {
					User curUser = (User) curValue;
					builder.append(curUser.toStringFull());
				}
				else {
					builder.append(curValue.toString());
				}
			}
		}
		
		return builder.toString();
	}
	
	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}
	
	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	/*
	 * Converts this tweet into a map for easy look ups. Values for keys are:
	 * toUser, fromUser, tweetId, tweetText
	 */
	public Map<String, Object> convertToMap() {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		ret.put(TweetConstants.TWEETTOUSER, toUser);
		ret.put(TweetConstants.TWEETFROMUSER, fromUser);
		ret.put(TweetConstants.TWEETID, tweetId);
		ret.put(TweetConstants.TWEETTEXT, tweetText);
		
		return ret;
	}
}
