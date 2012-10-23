package edu.iastate.coms572.project.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iastate.coms572.project.model.User;

public class UserTest {

	private User test;
	
	@Before
	public void setUp() {
		test = new User(123456, "bmaxwell", "Brandon", "Maxwell", 0);
	}
	
	@Test
	public void testToString() {
		assertEquals("bmaxwell", test.toString());
	}
	
	@Test
	public void testToStringFull() {
		assertEquals("123456, bmaxwell, Brandon, Maxwell, 0", test.toStringFull());
	}
	
	@After
	public void tearDown() {
		test = null;
	}

}
