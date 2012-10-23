package edu.iastate.coms572.project.constants;

import java.util.HashSet;
import java.util.Set;

public class TweetConstants {

	/**
	 * String used to delimit tweet toString values
	 */
	public static final String DELIMIT = ", ";
	
	
	/*
	 * 							-----------------------------------------------------------------
	 *  						NOTE: KEYS ARE ALL IN LOWER CASE TO EASILY CONVERT INVALID KEYS
	 * 							-----------------------------------------------------------------
	 */
	
	/**
	 * String used to look up the tweet toUser in tweet maps
	 */
	public static final String TWEETTOUSER = "touser";
	
	/**
	 * String used to look up the tweet fromUser in tweet maps
	 */
	public static final String TWEETFROMUSER = "fromuser";
	
	/**
	 * String used to look up the tweet id in tweet maps
	 */
	public static final String TWEETID = "tweetid";
	
	/**
	 * String used to look up the tweet tweet in tweet maps
	 */
	public static final String TWEETTEXT = "tweettext";
	
	/**
	 * A set of all the valid keys for tweet maps
	 */
	public static Set<String> validTweetKeys;
	
	
	static {
		validTweetKeys = new HashSet<String>();
		validTweetKeys.add(TWEETTOUSER);
		validTweetKeys.add(TWEETFROMUSER);
		validTweetKeys.add(TWEETID);
		validTweetKeys.add(TWEETTEXT);
	}
}
