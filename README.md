To run this project-

1. Install Node.js
2. Run npm install (to install all the dependencies for the project)
3. Run npm start (to start the project)
4. Run npm test (to run all tests of the project)

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
GET /bookList
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

Coding Best Practices-
Test Driven Development
Keep it Simple
Dont Repeat Yourself
You Ain't Gonna Need It
SOLID

