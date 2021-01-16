import React, { Component } from "react";
import PropTypes from "prop-types";

import Book from "./Book";

class BookList extends Component {
  render() {
    return this.props.bookList.map((book) => (
      <Book key={book.id} book={book} borrowIt={this.props.borrowIt} />
    ));
  }
}

BookList.propTypes = {
  bookList: PropTypes.array.isRequired,
};

export default BookList;