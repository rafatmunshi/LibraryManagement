package com.library.test.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.library.Controller.BookController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookController.class)
@ContextConfiguration("/servlet-context.xml")
@ActiveProfiles("local")
// use this tag to ignore these tests and only run unit tests
@Tag("integration")
public class BookControllerIntegrationTest {
	private MockMvc mockMvc;
	Properties prop = new Properties();
	@Autowired
	private WebApplicationContext context;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void orderedTests() throws Exception {
		getBookListTest();
		getBorrowedBookListTest();
		borrowBookTest();
		returnBookTest();
	}

	// This has integration tests which tests Rest APIs end to end.
	// Ignore these tests to avoid load/updates to the database while testing.
	public void getBookListTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/bookList").content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertTrue(prop.getProperty("book.return"), resultContent.contains("books"));
		Assert.assertTrue(prop.getProperty("book.library"), resultContent.contains("borrowerId\": \"0\""));
	}

	public void getBorrowedBookListTest() throws Exception {
		// userid mocked to 1
		MvcResult result = mockMvc.perform(get("/1/borrowedBookList").content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertTrue(prop.getProperty("book.return"), resultContent.contains("books"));
		Assert.assertTrue(prop.getProperty("borrowed.only"), !resultContent.contains("borrowerId\": \"0\""));
	}

	public void borrowBookTest() throws Exception {
		// Begin these tests when user with id 1 has not borrowed any book
		// and books with id 1,2 exist in the database unborrowed
		// and book with id 3 does not exist
		// This order is IMPORTANT
		bookNotExistsTest();
		bookSuccessfulBorrow();
		bookAlreadyBorrowedTest();
		borrowBookLimitExceededTest();
	}

	private void bookSuccessfulBorrow() throws Exception {
		MvcResult result = mockMvc.perform(post("/1/borrow/2").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("borrowed.success"), "Success", resultContent);
	}

	private void bookAlreadyBorrowedTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/1/borrow/2").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("borrowed.exception"), "Book is already borrowed", resultContent);
	}

	private void bookNotExistsTest() throws Exception {
		// userid mocked to 1
		MvcResult result = mockMvc.perform(post("/1/borrow/3").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("bookexist.exception"), "Book Does Not Exist", resultContent);
	}

	private void borrowBookLimitExceededTest() throws Exception {
		// userid mocked to 1
		mockMvc.perform(post("/1/borrow/1").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		MvcResult result = mockMvc.perform(post("/1/borrow/1").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("limitexceed.exception"), "Borrow Limit Exceeded", resultContent);
	}

	public void returnBookTest() throws Exception {
		bookNotExistsForReturnTest();
		bookSuccessfulReturn();
		bookNotBorrowedTest();
		// Finally tests for return all books
		allBooksReturnTest();
	}

	private void bookNotBorrowedTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/1/return/2").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("notborrowed.exception"), "Book to be returned is not borrowed",
				resultContent);

	}

	private void bookSuccessfulReturn() throws Exception {
		MvcResult result = mockMvc.perform(post("/1/return/2").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("return.success"), "Success", resultContent);
	}

	private void bookNotExistsForReturnTest() throws Exception {
		// userid mocked to 1
		// Assume book with id 3 does not exist
		MvcResult result = mockMvc.perform(post("/1/return/3").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("bookexist.exception"), "Book Does Not Exist", resultContent);
	}

	public void allBooksReturnTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/1/returnAll").content(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Assert.assertEquals(prop.getProperty("return.success"), "Success", resultContent);
	}
}
