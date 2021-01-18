import React, { Component } from "react";

export class Book extends Component {
  loadPageorShowBook = () => {
    let showUserHTML, displayButton;
    const { id, name, author, borrowerId } = this.props.book;
    borrowerId == "0"
      ? (displayButton = "Borrow")
      : (displayButton = "Return");
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
          {displayButton === "Borrow" && (
            <button
              onClick={this.props.borrowIt.bind(this, id)}
              className="borrowButton"
            >
              Borrow
            </button>
          )}
          {displayButton === "Return" && (
            <button
              onClick={this.props.returnIt.bind(this, id)}
              className="returnButton"
            >
              Return
            </button>
          )}
        </p>
      );
    }
    return showUserHTML;
  };
  render() {
    return <div className="eachBook">{this.loadPageorShowBook()}</div>;
  }
}

export default Book;
