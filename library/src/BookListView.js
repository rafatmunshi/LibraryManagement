import React, { Component } from "react";
import BookList from "./components/BookList";
import getBookList from "./getBookList";

class BookListView extends Component {
  state = {
    books: [{}],
  };
  componentDidMount() {
    getBookList().then((resp) => {
      this.setState(resp.data);
    });
  }
  render() {
    const bookList = this.state.books;
    const showEmptyorBookList= () => { 
      let showUserHTML;
      bookList.length === 0
        ? (showUserHTML = (
            <div className="emptyLibrary">"The Library is Empty!"</div>
          ))
        : (showUserHTML = <BookList bookList={bookList} />);
      return showUserHTML;
    }
    
    return <React.Fragment>{showEmptyorBookList()}</React.Fragment>;
  }
}

export default BookListView;