import "./App.css";
import React from "react";

import Header from "./components/layout/Header";
import BookListView from "./BookListView";

function App() {
  return (
    <div className="App">
      <div className="container">
        <Header />
        <br />
        <BookListView />
      </div>
    </div>
  );
}

export default App;