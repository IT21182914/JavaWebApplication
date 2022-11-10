package com.shop.model;

//feedback model
public class Feedback {
	private int feedback_id;
	private int userId;
	private String feedback;
		
	public Feedback(int id , String feedback ) {
		super();
		this.userId = id;
		this.feedback = feedback;
	}
	
	public Feedback( String feedback ) {
		super();
		this.feedback = feedback;
	}

	public int getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(int feedback_id) {
		this.feedback_id = feedback_id;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	
}
