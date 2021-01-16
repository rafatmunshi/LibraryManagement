import "./App.css";
import React from "react";
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Header from "./components/layout/Header";
import BookListView from "./BookListView";

function App() {
  return (
    <Router>
    <div className="App">
      <div className="container">
        <Header />
        <br />
        <BookListView />
      </div>
      </div>
      </Router>
  );
}

export default App;