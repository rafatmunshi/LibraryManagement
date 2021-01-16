import React, { Component } from "react";

export class Book extends Component {
  render() {
    const loadPageorShowBook = () => {
      let showUserHTML, displayBorrowButton;
      const { id, name, author, borrowerId } = this.props.book;
      borrowerId == "0"
        ? (displayBorrowButton = "1")
        : (displayBorrowButton = "0");
      if (!name == true) {
        showUserHTML = (
          <div className="loading">
            <img src="https://miro.medium.com/max/882/1*9EBHIOzhE1XfMYoKz1JcsQ.gif" />
          </div>
        );
      } else {
        showUserHTML = (
          <p id={id} className="book">
            {name}, {author}
            {displayBorrowButton !== "0" && (
              <button
                onClick={this.props.borrowIt.bind(this, id)}
                class="borrowButton"
                style={{ float: "right" }}
              >
                Borrow
              </button>
            )}
          </p>
        );
      }
      return showUserHTML;
    };

    return <div className="eachBook">{loadPageorShowBook()}</div>;
  }
}

export default Book;
