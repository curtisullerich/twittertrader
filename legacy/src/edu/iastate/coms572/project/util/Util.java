package edu.iastate.coms572.project.util;

import java.util.List;

import edu.iastate.coms572.project.constants.TweetConstants;

public class Util {

	/**
	 * Reads a file full of JSON values representing tweets and writes to a file
	 * all of the tweets into a given format
	 * @param JSONFile 
	 * 					the file to read the JSON from
	 * @param destinationFile 
	 * 							the file to write the tweets to
	 */
	public static void parseTweetJSON(String JSONFile, String destinationFile) {
		/*
		 * Breakdown:
		 * 
		 * 1) Load some JSON tweets into memory
		 * 2) Split it into an array of tweets
		 * 3) Split the a specific tweet down into its components
		 * 4) Turn the split tweet into a Tweet Object
		 * 5) Write the tweet out to a file
		 */
	}
	
	/**
	 * Checks if the given list contains only values that are valid to Tweet look ups
	 * @param keys 
	 * 				the keys to check
	 * @return
	 * 			true if the list only contains valid keys
	 */
	public static boolean areValidTweetMapValues(List<String> keys) {
		return TweetConstants.validTweetKeys.containsAll(keys);
	}
	
	/**
	 * Converts the list to valid tweetMap key, if possible. 
	 * NOTE: If it's not possible to convert, the invalid keys will still remain in the list
	 * @param keys
	 * 				the keys to check and convert
	 * @return
	 * 			true if all the keys were either correct or able to be converted
	 */
	public static boolean convertToValidKeys(List<String> keys) {
		boolean success = true;
		
		for (int i = 0; i < keys.size(); ++i) {
			String cur = keys.get(i);
			
			if (!TweetConstants.validTweetKeys.contains(cur)) {
				cur = Util.convertToValidKey(cur);
				
				//After converting try to set the value as correct
				if (TweetConstants.validTweetKeys.contains((cur))) {
					keys.set(i, cur);
				}
				else {
					//But if we can't then we need to return false
					success = false;
				}
			}
		}
		
		return success;
	}

	private static String convertToValidKey(String cur) {
		//Currently the only way to fix the tweet key is to convert it to lower case
		return cur.toLowerCase();
	}
}
