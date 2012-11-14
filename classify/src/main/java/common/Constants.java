package common;

import java.text.SimpleDateFormat;

public class Constants {

	//String url for getting tweets to classify
	public static final String NEW_TWEET_GET_URL = "http://danielstiner.com:9001/unclassified/random/200";
	
	//String url for posting classified tweets
	public static final String CLASSIFIED_TWEET_POST_URL = "http://danielstiner.com:9001/classified/classify";

	//Strings used in the ModelUpdater class. I thought it would be
	//good to put these here since everything else is here too
	public final static String VERIFIED_URL_PRE = "http://danielstiner.com:9001/verified/company/";
	public final static String VERIFIED_URL_POST = "/count/";
	public static final String VERIFIED_COUNTS_URL = "http://danielstiner.com:9001/verified/companies";
	
	//Classifier constant literals
	public static final String COMPANY_MODEL = "companyModel";
	public static final String SENTIMENT_MODEL = "sentimentModel";
	
	//Json tweet properties
	public static final String ID_PROPERTY_KEY = "id_str";
	public static final String COMPANY_PROPERTY_KEY = "company";
	public static final String SENTIMENT_PROPERTY_KEY = "sentiment";
	public static final String COMPANY_CONFIDENCE_PROPERTY_KEY = "companyConfidence";
	public static final String SENTIMENT_CONFIDENCE_PROPERTY_KEY = "sentimentConfidence";
	public static final String CSTAMP_PROPERTY_KEY = "cstamp";
	
	//Date format for saving classifiers
	public static final SimpleDateFormat PRETTY_SDF = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	public static final SimpleDateFormat UGLY_SDF = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat FRIENDLY_SDF = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	//Misc. constants
	public static final String CHAR_SET = "UTF-8";
	//Cause literals are dangerous! :O
	//public static final String MALLET_EXTENSION = ".mallet";
	public static final String CSV_ITERATOR_REGEX = "(\\w+)\\s+(\\w+)\\s+(.*)";
}
