package com.library.Exceptions;

@SuppressWarnings("serial")
public class UserBorrowLimitExceededException extends BorrowBookException {
	public UserBorrowLimitExceededException(String message) {
		super(message);
	}
}
