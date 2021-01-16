package com.library.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.library.Model.Book;


public interface BookService {

	public List<Book> listBooks();
	public List<Book> getBorrowedBooksList();
	public ResponseEntity<String> borrowBook(String id);
	
}
