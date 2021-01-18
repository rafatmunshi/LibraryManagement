package com.library.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.Model.Book;
import com.library.Model.BookDTO;
import com.library.Service.BookService;

@Transactional
@RestController
public class BookController {
	// Dependency Inversion
	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	public BookController() {
		super();
	}

	@Autowired(required = true)
	@Qualifier(value = "bookService")
	public void setBookService(BookService ps) {
		this.bookService = ps;
	}

	@RequestMapping(value = "/bookList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getLibraryBooks() {
		return toJSONString(this.bookService.getLibraryBooks());
	}

	@RequestMapping(value = "/{userid}/borrow/{bookid}", method = RequestMethod.POST)
	public ResponseEntity<String> borrowBook(@PathVariable("bookid") String id) {
		ResponseEntity<String> responseEntity = this.bookService.borrowBook(id);
		return responseEntity;
	}

	@RequestMapping(value = "/{userid}/return/{bookid}", method = RequestMethod.POST)
	public ResponseEntity<String> returnBook(@PathVariable("bookid") String id) {
		ResponseEntity<String> responseEntity = this.bookService.returnBook(id);
		return responseEntity;
	}

	@RequestMapping(value = "/{userid}/returnAll", method = RequestMethod.POST)
	public ResponseEntity<String> returnAllBooks() {
		ResponseEntity<String> responseEntity = this.bookService.returnAllBooks();
		return responseEntity;
	}

	@RequestMapping(value = "/{user-id}/borrowedBookList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getBorrowedBooks() {
		return toJSONString(this.bookService.getBorrowedBooks());
	}

	public String toJSONString(List<Book> books) {
		GsonBuilder builder = new GsonBuilder();
		BookDTO bookdto = new BookDTO();
		bookdto.setBooks(books);
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		String json = gson.toJson(bookdto);
		return json;
	}

}
