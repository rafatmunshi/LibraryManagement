package com.library.DAO;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.library.Model.Book;


public interface BookDAO {
	public List<Book> getLibraryBooks();
	public ResponseEntity<String> borrowBook(String id);
	public List<Book> getBorrowedBooks();
}
