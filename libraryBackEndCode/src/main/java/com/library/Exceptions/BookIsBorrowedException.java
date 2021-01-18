package com.library.Exceptions;

@SuppressWarnings("serial")
public class BookIsBorrowedException extends BorrowBookException {
	public BookIsBorrowedException(String message) {
		super(message);
	}
}
