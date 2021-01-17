package com.library.test.Controller;

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

import com.library.Controller.BookController;
import com.library.Model.Book;
import com.library.Service.BookService;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerUnitTest {
	@Mock
	BookService bookService;

	BookController bookController;
	Properties prop = new Properties();

	@Before
	public void init() {
		bookController = new BookController(bookService);
		try (InputStream input = new FileInputStream("./config.properties")) {
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@DisplayName("On call to list library books")
	@Test
	public void testMethodforLibraryBooks() {
		List<Book> books = createMockBooksList();
		// Arrange
		when(bookService.getLibraryBooks()).thenReturn(books);
		// Act
		String response = bookController.getLibraryBooks();
		// Assert
		Assert.assertTrue(prop.getProperty("book.return"), response.contains("books"));
		Assert.assertTrue(prop.getProperty("book.library"), response.contains("borrowerId\": \"0"));
	}

	@DisplayName("On call to list borrowed books")
	@Test
	public void testMethodtforBorrowedBookList() {
		List<Book> books = createMockBooksList();
		books.get(0).setBorrowerId("1");
		// Arrange
		when(bookService.getBorrowedBooks()).thenReturn(books);
		// Act
		String response = bookController.getBorrowedBooks(null);
		// Assert
		Assert.assertTrue(prop.getProperty("book.return"), response.contains("books"));
		Assert.assertTrue(prop.getProperty("borrowed.only"), !response.contains("borrowerId\": \"0"));
	}

	@DisplayName("On call to borrow a book")
	@Test
	public void testBorrowBook() {
		testBookExists();
		testBookBorrowed();
		testBorrowLimitExceeded();
	}

	private void testBorrowLimitExceeded() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Borrow Limit Exceeded",
				HttpStatus.FORBIDDEN);
		// Arrange
		when(bookService.borrowBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookController.borrowBook("12345", null, null);
		// Assert
		Assert.assertEquals(prop.getProperty("borrow.limitreached"), HttpStatus.FORBIDDEN, response.getStatusCode());
	}

	private void testBookBorrowed() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book is already borrowed",
				HttpStatus.BAD_REQUEST);
		// Arrange
		when(bookService.borrowBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookController.borrowBook("12345", null, null);
		// Assert
		Assert.assertEquals(prop.getProperty("book.borrowed"), HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

	private void testBookExists() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book Does Not Exist",
				HttpStatus.BAD_REQUEST);
		// Arrange
		when(bookService.borrowBook("12345")).thenReturn(responseEntity);
		// Act
		ResponseEntity<String> response = bookController.borrowBook("12345", null, null);
		// Assert
		Assert.assertEquals(prop.getProperty("book.notexists"), HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@DisplayName("For the books list")
	public void getJSONStringTest() {
		List<Book> books = createMockBooksList();
		String s = bookController.toJSONString(books);
		Assert.assertTrue("should generate JSON string", s.contains("Mock"));
	}

	private List<Book> createMockBooksList() {
		Book book = new Book("12345", "MockName", "MockAuthor", "0");
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		return books;
	}
}
