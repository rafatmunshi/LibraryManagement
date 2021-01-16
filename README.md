To run this project-
Library Front End-
1. Install Node.js
2. Open provided "library" folder-
3. Run npm install (to install all the dependencies for the project)
4. Run npm start (to start the project) (starts by default on localhost:3000)
5. Run npm test (to run all tests of the project)
Library BackEnd-
1. Install JDK.
2. Set JDK and JRE environment variables
3. Open provided Tomcat 8.5/bin and run startup. (it has the required war file in its webapp folder so runs the project on startup on localhost:8080 by default)
4. Install and start MySQL. (works with default settings. username- "root", blank password on localhost:3306)
5. Import in it the library.sql file provided. This has the table and entries needed to run the BackEnd Integration tests.
(Please note, after one run of the integration tests, the table state has to be reset back to the earlier state- borrower_id has to be made 0 for both books to rerun the integration tests again successfully)
6. To check the APIs only, do the appropriate endpoint calls from Postman.

Story 1-
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

Design considerations-
Assumptions-
Since the source of getting the data of the library is not mentioned in this story, it is assumed to be coming from a backend API. The response received by the Front End of the application would be having a list of books or an empty list if no books are present in the library. Hence a simple get request to a dummy API- https://my-json-server.typicode.com/rafatmunshi/LibraryManagementSystem/bookList
Is done and the response received is assumed to have a structure as-
API design-
GET (idempotent) /bookList
{
"books": [
{
"id": "5ff74638914fa154d4a870ec",
"name": "bookName",
"author": "authorName"
}
]
}

This same response structure is mocked for the two cases tests in the test file as well.

Pagination for a huge list of books is not implemented for this story, after consideration, to avoid premature optimization and considering the principle of YAGNI (You Ain’t Gonna Need It)

Choice of technology-
Since the parsing is done on the front end only, for this story, only front-end technologies are used. React is used along with React testing library, Jest and Enzyme for establishing User behaviour tests and Front End coding.

UI steps to see in browser-
The Webpage is simple and fully responsive as would be expected from any modern web app.
Once the project runs on the browser, the page for the Library Management System is visible. If there is an empty array of books in the response to the API, the message- “The Library is Empty!” is shown. Else a list of all the Books with their names and author names are visible as a list.

Backend work- not required for this story.

Code refactoring-
The display components (ones which render view based on props) – Book, Books, BookList etc and container components (ones which are concerned with fetch calls)- getBookList are kept separate so as to keep the code simple and follow single responsibility principle.

Story 2-
User can borrow a book from the library
Given , there are books in the library
When , I choose a book to add to my borrowed list
Then , the book is added to my borrowed list
And , the book is removed from the library
Note:
a. Each User has a borrowing limit of 2 books at any point of time

UI steps to see in browser-
Open the http://localhost:3000/ on the browser. 
The list of books in the library is visible here if any  once the call to the API is complete. There are two links in the header. On click of the "Your Borrowed List" text, the borrowed list of the user appears if any else a message- "Your borrowed list is empty" appears.
On click of View Library Books text again, the list of books in the library appears.

Design considerations-
Assumptions- 
The condition of each user's borrowing limit signals that there may be multiple users and it is assumed that this functionality of different user ids is supposed to be incorporated using other stories. For testing and demo in this story, a user-id of 1 is used.
For this story only the books available to be borrowed are to be displayed in the list as they are the ones which are present in the library. The API call is the same as in the first story, with an addition of the borrowerId flag.

Architecture- The Front End is decoupled from the BackEnd. The Front End makes the REST API calls to the BackEnd which deals with the Database to give the correct response as expected. This decoupling avoids tight cohesion and provides more flexibility and maintainability.
The Architecture is depicted in the Architecture.png image.
The Class diagram is provided as a classDiagram image.

The Table structure for the Books table is-
ID | name | author | borrower_id

ID is the primary key here and the remaining columns are functionally dependent on the primary key. borrower_id has a one to many relation with book ID. This makes this table to be in Boyce Codd Normalised Form of the relational database.

In the Java Spring Project, Dependency Injection and MVC design pattern is used.

API design-
To get all the books available in the Library-
GET /bookList
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
To borrow a book with book-id by a user with user-id (This path is important as a book is borrowed by a specific user only in his/her borrow limit) - 
POST - /{userid}/borrow/{bookid}

To get a list of all books borrowed by a user with user-id
GET /{userid}/borrowedBookList
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

Choice of technology-
Front-End- React, Jest, Enzyme and React Testing Library
BackEnd- Java Spring MVC 5, Maven, Junit5, Mockito, Hibernate
DataBase- MySQL

The choice of an RDBMS is to maintain ACID properties which may be crucial for an application like Library Management System with concurrent accesses and isolation requirements.

Coding Best Practices-
Two different UI page views are created for separation of concerns.
All JUnit tests have display and assert messages in such a way as to form well readable english sentences.
Some comments are added only for assignment demo purposes.
Java Class Structure is created in such a way that levels of abstraction are achieved with the Controller, Service and DAO layer. This makes it more maintainable. Having interfaces like Service, DAO and their implementation classes- ServiceImpl, DAOImpl differently helps us to program to an interface instead of implementation so that we may extend instead of modify or use another implementation. This gives flexibility and maintainability.
Dont Repeat Yourself- properties file used to keep repeated strings for Java project, creation of smaller helper methods, use of react axios global config parameter for default base URL for API calls
Test Driven Development
Keep it Simple
You Ain't Gonna Need It
SOLID