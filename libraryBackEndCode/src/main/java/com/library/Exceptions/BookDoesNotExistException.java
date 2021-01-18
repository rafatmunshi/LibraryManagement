package com.library.Exceptions;

@SuppressWarnings("serial")
public class BookDoesNotExistException extends BorrowBookException {
	public BookDoesNotExistException(String message) {
		super(message);
	}
}
