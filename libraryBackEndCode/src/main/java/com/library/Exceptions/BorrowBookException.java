package com.library.Exceptions;

@SuppressWarnings("serial")
public class BorrowBookException extends Exception {
    public BorrowBookException(String message) {
        super(message);
    }
}
