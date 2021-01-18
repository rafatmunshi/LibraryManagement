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
	// Dependency Inversion
	private BookDAO bookDAO;

	public BookServiceImpl(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	public BookServiceImpl() {
		super();
	}

	public void setBookDAO(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	@Override
	@Transactional
	public List<Book> getLibraryBooks() {
		return this.bookDAO.getLibraryBooks();
	}

	@Override
	@Transactional
	public List<Book> getBorrowedBooks() {
		return this.bookDAO.getBorrowedBooks();
	}

	@Override
	@Transactional
	public ResponseEntity<String> borrowBook(String id) {
		return this.bookDAO.borrowBook(id);

	}

	@Override
	public ResponseEntity<String> returnBook(String id) {
		return this.bookDAO.returnBook(id);
	}

	@Override
	public ResponseEntity<String> returnAllBooks() {
		return this.bookDAO.returnAllBooks();
	}

}
