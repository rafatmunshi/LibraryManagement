import React, { Component } from "react";
import BookList from "../BookList";
import getBorrowedList from "../../getBorrowedList.js";

class borrowedListView extends Component {
  state = {
    books: [{}],
  };
  componentDidMount() {
    getBorrowedList().then((resp) => {
      this.setState(resp.data);
    });
  }

  render() {
    const borrowedList = this.state.books;
    const showEmptyorBookList = () => {
      let showUserHTML;
      borrowedList.length === 0
        ? (showUserHTML = (
            <div className="emptyLibrary">"Your Borrowed List is Empty!"</div>
          ))
        : (showUserHTML = (
            <div>
              Books in your Borrowed List
              <BookList bookList={borrowedList} borrowIt={() => {}} />
            </div>
          ));
      return showUserHTML;
    };
    return <React.Fragment>{showEmptyorBookList()}</React.Fragment>;
  }
}

export default borrowedListView;
