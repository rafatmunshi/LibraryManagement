package com.library.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.library.Model.Book;


public interface BookService {

	public List<Book> getLibraryBooks();
	public List<Book> getBorrowedBooks();
	public ResponseEntity<String> borrowBook(String id);
	
}
