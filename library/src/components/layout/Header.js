import React from "react";
import { Link } from "react-router-dom";
import "../../App.css";
const Header = () => {
  return (
    <header>
      <h1> Library Management System </h1>
      <Link className="link" to="/">
              View Library Books
      </Link> {" "}|{" "}    
      <Link className="link" to="/borrowedList">
          Your Borrowed List
      </Link>
    </header>
  );
};
export default Header;