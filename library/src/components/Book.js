import React, { Component } from "react";

export class Book extends Component {
  getStyle = () => {
    return {
      background: "#F4F4F4",
      padding: "10px",
      borderBottom: "1px #ccc dotted",
    };
  };

  
  render() {
    const loadPageorShowBook = () => { 
      let showUserHTML;
      const { id, name, author } = this.props.book;
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
          </p>
        );
      }
      return showUserHTML;
    }

    return <div style={this.getStyle()}>{loadPageorShowBook()}</div>;
  }
}

export default Book;