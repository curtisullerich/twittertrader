package edu.iastate.coms572.project.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iastate.coms572.project.constants.TweetConstants;
import edu.iastate.coms572.project.model.Tweet;
import edu.iastate.coms572.project.model.User;

public class TweetTest {

	private Tweet test;
	
	private User to;
	
	private User from;
	
	private String tweetText;
	
	@Before
	public void setUp() {
		tweetText = "Hello! This is a tweet! #twitterIsGay";
		
		to = new User(123456, "bmaxwell", "Brandon", "Maxwell", 0);
		
		from = new User(123455, "jOlds", "Jesse", "Olds", 1000000);
		
		test = new Tweet(from, to, 1, tweetText, Calendar.getInstance());
	}
	
	@Test
	public void testToString() {
		assertEquals("1, jOlds, bmaxwell" + ", " + tweetText, test.toString());
	}
	
	@Test
	public void testToStringWithOrderValid() {
		List<String> order = Arrays.asList(TweetConstants.TWEETTEXT, TweetConstants.TWEETTOUSER, 
				TweetConstants.TWEETFROMUSER, TweetConstants.TWEETID);
		
		//Users' non full toString
		assertEquals(tweetText + ", " + "bmaxwell, jOlds, 1", test.toStringWithOrder(order, false));
		
		
		//Users' full toString
		assertEquals(tweetText + ", " + to.toStringFull() + ", " + from.toStringFull() + ", " + 1, test.toStringWithOrder(order, true));	
	}
	
	@Test
	public void testToStringWithOrderOneFixable() {
		List<String> order = Arrays.asList("TWEETTEXT", TweetConstants.TWEETTOUSER, 
				TweetConstants.TWEETFROMUSER, TweetConstants.TWEETID);
		
		//Users' non full toString
		assertEquals(tweetText + ", " + "bmaxwell, jOlds, 1", test.toStringWithOrder(order, false));
		
		
		//Users' full toString
		assertEquals(tweetText + ", " + to.toStringFull() + ", " + from.toStringFull() + ", " + 1, test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringWithOrderALLFixable() {
		List<String> order = Arrays.asList("TWEETTEXT", "TOUSER", 
				"FROMUSER", "TWEETID");
		
		//Users' non full toString
		assertEquals(tweetText + ", " + "bmaxwell, jOlds, 1", test.toStringWithOrder(order, false));
		
		
		//Users' full toString
		assertEquals(tweetText + ", " + to.toStringFull() + ", " + from.toStringFull() + ", " + 1, test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringWithOrderOneNotFixable() {
		List<String> order = Arrays.asList(TweetConstants.TWEETTEXT, TweetConstants.TWEETTOUSER, 
				TweetConstants.TWEETFROMUSER, "Garbage");
		
		//Users' non full toString
		assertEquals(tweetText + ", " + "bmaxwell, jOlds", test.toStringWithOrder(order, false));
				
				
		//Users' full toString
		assertEquals(tweetText + ", " + to.toStringFull() + ", " + from.toStringFull(), test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringWithOrderOneNotFixableOfMultipleInvalid() {
		List<String> order = Arrays.asList("TWEETTEXT", TweetConstants.TWEETTOUSER, 
				TweetConstants.TWEETFROMUSER, "Garbage");
		
		//Users' non full toString
		assertEquals(tweetText + ", " + "bmaxwell, jOlds", test.toStringWithOrder(order, false));
				
				
		//Users' full toString
		assertEquals(tweetText + ", " + to.toStringFull() + ", " + from.toStringFull(), test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringWithOrderAllUnfixable() {
		List<String> order = Arrays.asList("Garbage", "Garbage", "Garbage", "Garbage");
		
		//Users' non full toString
		assertEquals("", test.toStringWithOrder(order, false));
				
				
		//Users' full toString
		assertEquals("", test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringOrderNotReturningEverything() {
		List<String> order = Arrays.asList(TweetConstants.TWEETTEXT);
		
		//Users' non full toString
		assertEquals(tweetText, test.toStringWithOrder(order, false));
						
						
		//Users' full toString
		assertEquals(tweetText, test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringOrderNotReturningEverythingWithInvalidUnfixable() {
		List<String> order = Arrays.asList(TweetConstants.TWEETTEXT, "Garbage");
		
		//Users' non full toString
		assertEquals(tweetText, test.toStringWithOrder(order, false));
						
						
		//Users' full toString
		assertEquals(tweetText, test.toStringWithOrder(order, true));
	}
	
	@Test
	public void testToStringOrderNotReturningEverythingWithInValidFixable() {
		List<String> order = Arrays.asList(TweetConstants.TWEETTEXT, "TOUSER");
		
		//Users' non full toString
		assertEquals(tweetText + ", " + to.toString(), test.toStringWithOrder(order, false));
						
						
		//Users' full toString
		assertEquals(tweetText + ", " + to.toStringFull(), test.toStringWithOrder(order, true));
	}
	
	@After
	public void tearDown() {
		test = null;
		to = null;
		from = null;
		tweetText = null;
	}

}
