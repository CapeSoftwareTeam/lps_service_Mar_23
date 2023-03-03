package com.capeelectric.exception;

public class FrequentlyAskedQuestionException extends Throwable {
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String message;

	public FrequentlyAskedQuestionException() {

	}

	public FrequentlyAskedQuestionException(String message) {
		super();
		this.message = message;

	}

}
