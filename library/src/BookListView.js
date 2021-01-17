import React, { Component } from "react";
import BookList from "./components/BookList";
import getBookList from "./getBookList";
import axios from "axios";
import { BrowserRouter as Router, Route } from "react-router-dom";
import borrowedListView from "./components/pages/borrowedListView";
class BookListView extends Component {
  state = {
    books: [{}],
  };
  componentDidMount() {
    getBookList().then((resp) => {
      this.setState(resp.data);
    });
  }
  borrowIt = (id) =>
    axios
      .post("/Library/1/borrow/" + id)
      .then(() =>
        this.setState({
          books: [...this.state.books.filter((book) => book.id !== id)],
        })
      )
      .catch((error) => {
        console.log(error.response.data)
        if (error.response.data === "Borrow Limit Exceeded") {
            alert("You have maximum books borrowed. Please return any before borrowing more.")
        }
        else
          if (error.response.data === "Book is already borrowed") { 
            alert("You have borrowed this book already.");
          }
      });

  render() {
    const bookList = this.state.books;
    const showEmptyorBookList = () => {
      let showUserHTML;
      bookList.length === 0
        ? (showUserHTML = (
            <div className="emptyLibrary">"The Library is Empty!"</div>
          ))
        : (showUserHTML = (
            <div>
              Books in the Library
              <BookList bookList={bookList} borrowIt={this.borrowIt} />
            </div>
          ));
      return showUserHTML;
    };

    return (
      <div>
        <Route
          exact
          path="/"
          render={(props) => (
            <React.Fragment>{showEmptyorBookList()}</React.Fragment>
          )}
        />

        <Route path="/borrowedList" component={borrowedListView} />
      </div>
    );
  }
}

export default BookListView;
