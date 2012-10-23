package edu.iastate.coms572.project.model;

import edu.iastate.coms572.project.constants.TweetConstants;

public class User {

	/**
	 * The id for this user
	 */
	private long id;

	/**
	 * This user's username
	 */
	private String username;
	
	/**
	 * This user's first name
	 */
	private String firstName;
	
	/**
	 * This user's last name
	 */
	private String lastName;
	
	/**
	 * This user's number of followers
	 */
	private int numFollowers;
	
	public User(long id, String userName, String firstName, String lastName, int numFollowers) {
		this.id = id;
		this.username = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.numFollowers = numFollowers;
	}
	
	/**
	 * For simplicity the toString just returns the username. If you need all the information call the toStringFull method
	 */
	@Override
	public String toString() {
		return this.username;
	}
	
	/**
	 * Returns the string representation of all the information about this User in the form:
	 * id, username, firstName, lastName, numFollowers
	 */
	public String toStringFull() {
		StringBuilder builder = new StringBuilder();
		
		return builder
				.append(id)
				.append(TweetConstants.DELIMIT)
				.append(username)
				.append(TweetConstants.DELIMIT)
				.append(firstName)
				.append(TweetConstants.DELIMIT)
				.append(lastName)
				.append(TweetConstants.DELIMIT)
				.append(numFollowers)
				.toString();
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(int numFollowers) {
		this.numFollowers = numFollowers;
	}
}
