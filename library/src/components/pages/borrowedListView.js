import React, { Component } from "react";
import BookList from "../BookList";
import getBorrowedList from "./getBorrowedList.js";
import axios from "axios";
class borrowedListView extends Component {
  state = {
    books: [{}],
  };
  componentDidMount() {
    getBorrowedList().then((resp) => {
      this.setState(resp.data);
    });
  }
  returnIt = (id) => {
    axios
  .post("/Library/1/return/" + id)
      .then(() =>
        this.setState({
          books: [...this.state.books.filter((book) => book.id !== id)],
        })
      )
      .catch((error) => {
        alert(error.response.data);
      })
        //reload to show updated library books list with the returned book copy
        window.location.reload(false);
  };
  returnAll = () => {
    axios
  .post("/Library/1/returnAll/")
      .then(() => 
        this.setState({
          books: [...this.state.books.filter(() => { })],
        })
      )
      .catch((error) => {
        alert(error.response.data);
      });
    //reload to show updated library books list with all returned books copy
    window.location.reload(false);
  };
  showEmptyorBookList = () => {
    const borrowedList = this.state.books;
    let showUserHTML;
    borrowedList.length === 0
      ? (showUserHTML = (
          <div className="emptyLibrary">"Your Borrowed List is Empty!"</div>
        ))
      : (showUserHTML = (
          <div>
            <div>
              Books in your Borrowed List
              <button
                onClick={this.returnAll}
                className="returnAll"
              >
                Return All
              </button>
            </div>
            <BookList
              bookList={borrowedList}
              borrowIt={() => {}}
              returnIt={this.returnIt}
            />
          </div>
        ));
    return showUserHTML;
  };
  render() {
    return <React.Fragment>{this.showEmptyorBookList()}</React.Fragment>;
  }
}

export default borrowedListView;
