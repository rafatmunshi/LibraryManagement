package com.library.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.library.DAO.BookDAO;
import com.library.Model.Book;
@Repository
@Transactional
@Service
public class BookServiceImpl implements BookService {
	// Dependency Injection
	private BookDAO bookDAO;

	public BookServiceImpl(BookDAO bookDAO) {
		this.bookDAO= bookDAO;
	}
	public BookServiceImpl() {
		super();
	}

	public void setBookDAO(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	@Override
	@Transactional
	public List<Book> listBooks() {
		return this.bookDAO.listBooks();
	}

	@Override
	@Transactional
	public List<Book> getBorrowedBooksList() {
		return this.bookDAO.getBorrowedBooksList();
	}

	@Override
	@Transactional
	public ResponseEntity<String> borrowBook(String id) {
		return this.bookDAO.borrowBook(id);

	}

}
