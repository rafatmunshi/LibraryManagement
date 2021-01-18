package com.library.test.Service;

import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.library.DAO.BookDAO;
import com.library.Model.Book;
import com.library.Service.BookServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceUnitTest {
	@Mock
	BookDAO bookDao;

	BookServiceImpl bookService;
	List<Book> books;
	Properties prop = new Properties();

	@Before
	public void init() {
		bookService = new BookServiceImpl(bookDao);
		books = createMockBooksList();
		try (InputStream input = new FileInputStream("./config.properties")) {
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@DisplayName("On call to list library books")
	@Test
	public void testMethodforLibraryBooks() {
		// arrange
		when(bookDao.getLibraryBooks()).thenReturn(books);
		// Act
		List<Book> response = bookService.getLibraryBooks();
		// Assert
		Assert.assertEquals(prop.getProperty("book.return"), books, response);
	}

	@DisplayName("On call to list borrowed books")
	@Test
	public void testMethodtforBorrowedBooks() {
		// arrange
		when(bookDao.getBorrowedBooks()).thenReturn(books);
		// Act
		List<Book> response = bookService.getBorrowedBooks();
		// Assert
		Assert.assertEquals(prop.getProperty("book.return"), books, response);
	}

	@DisplayName("On request to Borrow a book")
	@Test
	public void testBorrowBook() {
		testBookExists();
		testBookBorrowed();
		testBorrowLimitExceeded();
	}

	private void testBorrowLimitExceeded() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Borrow Limit Exceeded",
				HttpStatus.FORBIDDEN);
		// arrange
		when(bookDao.borrowBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookService.borrowBook("12345");
		// Assert
		Assert.assertEquals(prop.getProperty("borrow.limitreached"), HttpStatus.FORBIDDEN, response.getStatusCode());

	}

	private void testBookBorrowed() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book is already borrowed",
				HttpStatus.BAD_REQUEST);
		// arrange
		when(bookDao.borrowBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookService.borrowBook("12345");
		// Assert
		Assert.assertEquals(prop.getProperty("book.borrowed"), HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	private void testBookExists() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book Does Not Exist",
				HttpStatus.BAD_REQUEST);
		// arrange
		when(bookDao.borrowBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookService.borrowBook("12345");
		// Assert
		Assert.assertEquals(prop.getProperty("book.notexists"), HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

	@DisplayName("On request to return a book")
	@Test
	public void testReturnBook() {
		testBookExistsForReturn();
		testBookBorrowedForReturn();
	}

	private void testBookBorrowedForReturn() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book is already borrowed",
				HttpStatus.BAD_REQUEST);
		// arrange
		when(bookDao.returnBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookService.returnBook("12345");
		// Assert
		Assert.assertEquals(prop.getProperty("book.borrowed"), HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

	private void testBookExistsForReturn() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book Does Not Exist",
				HttpStatus.BAD_REQUEST);
		// arrange
		when(bookDao.returnBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookService.returnBook("12345");
		// Assert
		Assert.assertEquals(prop.getProperty("book.notexists"), HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

	@DisplayName("On request to return all books")
	@Test
	public void testReturnAllBooks() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Success", HttpStatus.OK);
		// arrange
		when(bookDao.returnAllBooks()).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookService.returnAllBooks();
		// Assert
		Assert.assertEquals(prop.getProperty("returnAll.success"), HttpStatus.OK, response.getStatusCode());
	}

	private List<Book> createMockBooksList() {
		Book book = new Book("12345", "MockName", "MockAuthor", "0");
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		return books;
	}
}
