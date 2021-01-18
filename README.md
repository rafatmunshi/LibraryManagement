# Library Management System
## To run this project-
- Library Front End-
1. Install Node.js
2. Open provided "library" folder-
3. Run npm install (to install all the dependencies for the project)
4. Run npm start (to start the project) (starts by default on localhost:3000)
5. Run npm test (to run all tests of the project)
- Library BackEnd-
1. Install JDK.
2. Set JDK and JRE environment variables
3. Install Tomcat Server. Paste the library war file in its webapps folder. Open Tomcat/bin and run startup. (runs the project on startup on localhost:8080 by default)
4. Install and start MySQL. (works with default settings. username- "root", blank password on localhost:3306)
5. Import in it the library.sql file provided. This has the table and entries needed to run the BackEnd Integration tests.
6. To check the APIs only, do the appropriate endpoint calls from Postman.

## Story 1-
```
User can view books in library
Scenario : As a User
I want to see the books present in the library
So that I can chose which book to borrow
Given , there are no books in the library
When , I view the books in the library
Then , I see an empty library
Given , there are books in the library
When , I view the books in the library
Then , I see the list of books in the library
```
- Design considerations-  
- Assumptions-  
Since the source of getting the data of the library is not mentioned in this story, it is assumed to be coming from a backend API. The response received by the Front End of the application would be having a list of books or an empty list if no books are present in the library. Hence a simple get request to a dummy API- https://my-json-server.typicode.com/rafatmunshi/LibraryManagementSystem/bookList
Is done and the response received is assumed to have a structure as-
- API design-  
GET (idempotent) /bookList  
```
{
	"books": [{
		"id": "5ff74638",
		"name": "bookName",
		"author": "authorName"
	}]
}
```
This same response structure is mocked for the two cases tests in the test file as well.  

Pagination for a huge list of books is not implemented for this story, after consideration, to avoid premature optimization and considering the principle of YAGNI (You Ain’t Gonna Need It)  

- Choice of technology-  
Since the parsing is done on the front end only, for this story, only front-end technologies are used. React is used along with React testing library, Jest and Enzyme for establishing User behaviour tests and Front End coding.  

- UI steps to see in browser-  
The Webpage is simple and fully responsive as would be expected from any modern web app.  
Once the project runs on the browser, the page for the Library Management System is visible. If there is an empty array of books in the response to the API, the message- “The Library is Empty!” is shown. Else a list of all the Books with their names and author names are visible as a list.  
  
- Backend work- not required for this story.  

- Code refactoring-  
The display components (ones which render view based on props) – Book, Books, BookList etc and container components (ones which are concerned with fetch calls)- getBookList are kept separate so as to keep the code simple and follow single responsibility principle.

## Story 2-
```
User can borrow a book from the library
Given , there are books in the library
When , I choose a book to add to my borrowed list
Then , the book is added to my borrowed list
And , the book is removed from the library
Note:
a. Each User has a borrowing limit of 2 books at any point of time
```
- UI steps to see in browser-  
Open the http://localhost:3000/ on the browser.   
The list of books in the library is visible here if any  once the call to the API is complete. There are two links in the header. On click of the "Your Borrowed List" text, the borrowed list of the user appears if any else a message- "Your borrowed list is empty" appears.  
On click of View Library Books text again, the list of books in the library appears.  
If a user tries to borrow more than two books, an alert message is shown "You have maximum books borrowed. Please return any before borrowing more."  

- Design considerations-
- Assumptions- 
The condition of each user's borrowing limit signals that there may be multiple users and it is assumed that this functionality of different user ids is supposed to be incorporated using other stories. For testing and demo in this story, a user-id of 1 is used.  
For this story only the books available to be borrowed are to be displayed in the list as they are the ones which are present in the library. The API call is the same as in the first story, with an addition of the borrowerId flag. It is assumed that a borrowerId of 0 will never exist in the system and is taken as a flag for a book which is not yet borrowed by any user yet.

- Architecture- The Front End is decoupled from the BackEnd. The Front End makes the REST API calls to the BackEnd which deals with the Database to give the correct response as expected. This decoupling avoids tight cohesion and provides more flexibility and maintainability.  
The Architecture is depicted in the Architecture.png image.  
The Class diagram is provided as a classDiagram image.  

- The Table structure for the Books table is-
```
ID | name | author | borrower_id
```

ID is the primary key here and the remaining columns are functionally dependent on the primary key. borrower_id has a one to many relation with book ID. This makes this table to be in Boyce Codd Normalised Form of the relational database.  

In the Java Spring Project, Dependency Inversion and MVC design pattern is used.  

- API design-  
To get all the books available in the Library-  
GET /bookList  
```
{
    "books": [
        {
            "id": "4",
            "name": "bookname2",
            "author": "author2",
            "borrowerId": "0"
        },
        {
            "id": "6",
            "name": "bookname3",
            "author": "author3",
            "borrowerId": "0"
        }
    ]
}
```
To borrow a book with book-id by a user with user-id (This path is important as a book is borrowed by a specific user only in his/her borrow limit) -   
POST - /{userid}/borrow/{bookid}  

To get a list of all books borrowed by a user with user-id  
GET /{userid}/borrowedBookList  
```
{
    "books": [
        {
            "id": "1",
            "name": "bookname1",
            "author": "author1",
            "borrowerId": "userid"
        },
        {
            "id": "2",
            "name": "bookname4",
            "author": "author4",
            "borrowerId": "userid"
        }
    ]
}
```
- Choice of technology-  
Front-End- React, Jest, Enzyme and React Testing Library  
BackEnd- Java Spring MVC 5, Maven, Junit5, Mockito, Hibernate  
DataBase- MySQL  

The choice of an RDBMS is to maintain ACID properties which may be crucial for an application like Library Management System with concurrent accesses and isolation requirements.  

## Story 3-
```
User can borrow a copy of a book from the library
Given , there are more than one copy of a book in the library
When , I choose a book to add to my borrowed list
Then , one copy of the book is added to my borrowed list
And , the library has at least one copy of the book left
Given , there is only one copy of a book in the library
When , I choose a book to add to my borrowed list
Then , one copy of the book is added to my borrowed list
And , the book is removed from the library
Note:
a. Only 1 copy of a book can be borrowed by a User at any point of time
```
- UI steps to see in browser-
This time since the user borrows a copy of the book, on click of borrow button and successful borrowed response from server, the book again gets removed from the list for a better user experience by showing immediate feedback to the user. When the page is visited again though, the book is still visible if any/some copies may still be available. If the user again clicks on borrow, an alert informs the user that "You have borrowed this book already."  

- Design considerations-  
The Table structures-  
To incorporate multiple copies of the same book and a user borrowing a copy of a book instead of the single books table, two tables are created for mapping copies with the books.  
The books table is-  
```
ID | name | author
```
The borrower_id is removed from here and only data specific to each book has to be in this table.

The book_copies table is-
```
copy_id | book_id | borrower_id
```
The primary key is copy_id which is mapped to the foreign key book_id in a many to one relationship. The borrower_id has a one to one relationship with the copy_id. The borrower_id and the book_id is dependent on the copy_id which makes this in the BCNF as well. 

- Assumptions-  
It is assumed that the integrity of the database on foreign key of book_id will always be maintained. Sample entries in the database tables are provided for testing UI, API calls and the BackEnd integrity test cases with.  
On borrow of a book, a copy is chosen at random and assigned to the user. The book borrowed is assumed to be the copy of the book borrowed, hence the next the same book_id cannot be issued whose copy_id already has the same borrower_id.  

- API design  
GET /bookList  
returns only books which have atleast once copy with borrower_id 0, using the same response body structure with id meaning the book_id as before.  
POST - /{userid}/borrow/{bookid}  
assigns one copy at random of the bookid whose borrower_id was 0, to the current user with id 1.  
GET /{userid}/borrowedBookList  
returns only books which have copies with borrower_id same as the user_id (1 in this case), using the same response body structure with id meaning the book_id as before  
   
-  Front End  
No change required in API response structure and hence the FrontEnd handling the response as generating the API response is dealt with in the BackEnd using appropriate SQL join queries between the two tables and refactoring the Model Java Class to now map to the other table's column value instead, from the SQL query result.  
From the UI, the user is not able to borrow the same book multiple times or more than two books as the state always changes and is disallowed. From the API endpoint, if the user is trying to borrow the same book again, a BAD_REQUEST response with message "Book is already borrowed" is returned (Backend handling as POST is not idempotent). If the user tries to borrow a book which does not exist in the books table, using the endpoint, the response BAD_REQUEST "Book Does Not Exist" is returned.  

## Story 4-
```
User can return books to the library
Given , I have 2 books in my borrowed list
When , I return one book to the library
Then , the book is removed from my borrowed list
And , the library reflects the updated stock of the book
Given , I have 2 books in my borrowed list
When , I return both books to the library
Then , my borrowed list is empty
And , the library reflects the updated stock of the books
```
- UI steps to see in browser-  
In the borrowed books list view, a return button is added for each book in the borrowed list. On click of that, the book is returned and is removed from the borrowed list of the user. Using the Return All button, both the books is returned and the borrowed list becomes empty. If there is any server error in both cases, it is alerted to the user.  
After the Return/Return All, the browser reloads to display the returned books for borrowing again in the library books list view.  

- Design considerations-
The Table structures are the same as before. When the book/s is returned, the borrower_id is reset to 0 for the book/s copy borrowed.

- Assumptions-
The state of the database is maintained and we always find the entry of the copy where book_id and borrower_id matches, for the reset operation to take place for returning. 

- API design-
POST /{userid}/return/{bookid}   
return the book with bookid of the user with userid to the library  
POST /{userid}/returnAll  
returns all books (2 in this story's case) borrowed by the user with userid to the library   

From the UI, the user is not able to return the same book multiple times as the state always changes. From the API endpoint, if the user is trying to return the same book again, a BAD_REQUEST response with message "Book to be returned is not borrowed" is returned (Backend handling as POST is not idempotent). If the user tries to return a book which does not exist in the books table, using the endpoint, the response BAD_REQUEST "Book Does Not Exist" is returned.

## Coding Best Practices used-
* Two different UI page views are created for separation of concerns.
* All JUnit tests have display and assert messages in such a way as to form well readable english sentences.
* Some comments are added only for assignment demo purposes.
* SOLID
* Java Class Structure is created in such a way that levels of abstraction are achieved with the Controller, Service and DAO layer. This makes it more maintainable. Having interfaces like Service, DAO and their implementations- ServiceImpl, DAOImpl differently helps us to program to an interface instead of implementation so that we may be able to extend instead of modify; or use another implementation for the interfaces without changing anything where those interfaces are used (for dependency inversion). This gives flexibility and maintainability.
* Dont Repeat Yourself- properties file used to keep repeated strings for Java project, creation of smaller helper methods, use of react axios global config parameter for default base URL for API calls in all other files
* Test Driven Development
* Keep it Simple
* You Ain't Gonna Need It

## Future work-
For the Front End, instead of a simple alert, a react modal could be implemented for better UX.  
The functionality for different user ids can be incorporated with authentication and concurrency in further stories.  
