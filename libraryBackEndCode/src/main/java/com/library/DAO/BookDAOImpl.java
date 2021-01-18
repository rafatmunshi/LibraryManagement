package com.library.DAO;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.library.Exceptions.BookDoesNotExistException;
import com.library.Exceptions.BookIsBorrowedException;
import com.library.Exceptions.BorrowBookException;
import com.library.Exceptions.UserBorrowLimitExceededException;
import com.library.Model.Book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("deprecation")
@Repository
@Transactional
public class BookDAOImpl implements BookDAO {

	private SessionFactory sessionFactory;
	ResponseEntity<String> responseEntity = new ResponseEntity<String>("Success", HttpStatus.OK);

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	public List<Book> getLibraryBooks() {
		String query = "SELECT b.id, b.name, b.author, c.borrower_id as borrowerId FROM BOOKS b, book_copies c where b.id= (SELECT distinct c.book_id where c.borrower_id=\"0\") group by b.id";
		return getBooks(query);
	}

	@Override
	public List<Book> getBorrowedBooks() {
		String query = "SELECT b.id, b.name, b.author, c.borrower_id as borrowerId FROM BOOKS b, book_copies c where b.id= (SELECT distinct c.book_id where c.borrower_id=\"1\") group by b.id";
		return getBooks(query);
	}

	@Override
	public ResponseEntity<String> borrowBook(String id) {

		try {
			// This order is IMPORTANT for the Integration test
			checkUserBorrowLimitViolation();
			checkBookExists(id);
			checkBookBorrowed(id);
		} catch (BorrowBookException e) {
			return handleBorrowBookException(e);
		}
		borrowBookCopy(id);
		return responseEntity;
	}

	@Override
	public ResponseEntity<String> returnBook(String id) {
		ResponseEntity<String> responseEntity = checkRequisiteForReturn(id);
		if (responseEntity.getBody().equals("Book is already borrowed")) {
			returnBookToLibrary(id);
			responseEntity = new ResponseEntity<String>("Success", HttpStatus.OK);
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<String> returnAllBooks() {
		// user id hardcoded to 1 here for demo
		String query = "UPDATE BOOK_COPIES SET borrower_id=\"0\" where borrower_id=\"1\"";
		executeUpdateQuery(query);
		return responseEntity;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Book> getBooks(String query) {
		Session session = this.sessionFactory.openSession();
		SQLQuery Query = session.createSQLQuery(query);
		Query.addEntity(Book.class);
		List<Book> borrowedBooks = Query.list();
		session.close();
		return borrowedBooks;
	}

	private ResponseEntity<String> handleBorrowBookException(BorrowBookException e) {
		// This makes it open for extension but closed for modification using
		// polymorphism and downcasting
		if (e instanceof BookIsBorrowedException) {
			return handleBookIsBorrowedException((BookIsBorrowedException) e);
		} else if (e instanceof BookDoesNotExistException) {
			return handleBookDoesNotExistException((BookDoesNotExistException) e);
		} else if (e instanceof UserBorrowLimitExceededException) {
			return handleUserBorrowLimitExceededException((UserBorrowLimitExceededException) e);
		}
		return responseEntity;
	}

	@ResponseStatus(reason = "Book is already borrowed", value = HttpStatus.BAD_REQUEST)
	private ResponseEntity<String> handleBookIsBorrowedException(BookIsBorrowedException e) {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book is already borrowed",
				HttpStatus.BAD_REQUEST);
		return responseEntity;
	}

	@SuppressWarnings("rawtypes")
	private void checkBookBorrowed(String id) throws BookIsBorrowedException {
		Session session = this.sessionFactory.openSession();
		// Query for userid 1 for demo
		SQLQuery doesBookExist = session
				.createSQLQuery("Select count(*) from BOOK_COPIES where book_id=\"" + id + "\" AND borrower_id=\"1\"");
		Boolean isBookBorrowed = ((BigInteger) doesBookExist.uniqueResult()).intValue() == 0 ? false : true;
		if (isBookBorrowed)
			throw new BookIsBorrowedException("Book is already borrowed");
		session.close();

	}

	@ResponseStatus(reason = "Book Does Not Exist", value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BookDoesNotExistException.class)
	private ResponseEntity<String> handleBookDoesNotExistException(BookDoesNotExistException e) {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book Does Not Exist",
				HttpStatus.BAD_REQUEST);
		return responseEntity;
	}

	@SuppressWarnings("rawtypes")
	private void checkBookExists(String id) throws BookDoesNotExistException {
		Session session = this.sessionFactory.openSession();
		SQLQuery doesBookExist = session.createSQLQuery("Select count(*) from BOOKS where id=\"" + id + "\"");
		Integer bookExistenceCount = ((BigInteger) doesBookExist.uniqueResult()).intValue();
		if (bookExistenceCount == 0)
			throw new BookDoesNotExistException("Book Does Not Exist");
		session.close();
	}

	private void borrowBookCopy(String id) {
		// user id hardcoded to 1 here for demo
		String query = "UPDATE BOOK_COPIES SET borrower_id=\"1\" where book_id=\"" + id
				+ "\" AND borrower_id=\"0\" LIMIT 1";
		executeUpdateQuery(query);
	}

	@SuppressWarnings("rawtypes")
	private void checkUserBorrowLimitViolation() throws UserBorrowLimitExceededException {
		Session session = this.sessionFactory.openSession();
		// user id 1 is mocked for demo
		SQLQuery createUserBorrowedEntry = session
				.createSQLQuery("Select count(*) from BOOK_COPIES where borrower_id=\"1\"");
		Integer userBorrowCount = ((BigInteger) createUserBorrowedEntry.uniqueResult()).intValue();
		if (userBorrowCount >= 2)
			throw new UserBorrowLimitExceededException("Borrow Limit Exceeded");
		session.close();
	}

	@ResponseStatus(reason = "Borrow Limit Exceeded", value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(UserBorrowLimitExceededException.class)
	private ResponseEntity<String> handleUserBorrowLimitExceededException(UserBorrowLimitExceededException e) {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Borrow Limit Exceeded",
				HttpStatus.FORBIDDEN);
		return responseEntity;
	}

	private ResponseEntity<String> checkRequisiteForReturn(String id) {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Book to be returned is not borrowed",
				HttpStatus.BAD_REQUEST);
		try {
			checkBookExists(id);
			checkBookBorrowed(id);
		} catch (BorrowBookException e) {
			responseEntity = handleBorrowBookException(e);
		}
		return responseEntity;
	}

	private void returnBookToLibrary(String id) {
		// user id hardcoded to 1 here for demo
		String query = "UPDATE BOOK_COPIES SET borrower_id=\"0\" where book_id=\"" + id + "\" AND borrower_id=\"1\"";
		executeUpdateQuery(query);
	}

	@SuppressWarnings("rawtypes")
	private void executeUpdateQuery(String query) {
		Session session = this.sessionFactory.openSession();
		Transaction txn = session.beginTransaction();
		SQLQuery updateQuery = session.createSQLQuery(query);
		updateQuery.executeUpdate();
		txn.commit();
		session.close();
	}

}
