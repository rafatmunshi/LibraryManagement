import BookListView from "./BookListView";
import "@testing-library/jest-dom";
import * as React from "react";
import { BrowserRouter as Router } from 'react-router-dom';
import {
  render,
  screen,
  act,
  waitForElementToBeRemoved
} from "@testing-library/react";
import getBookList from "./getBookList";
jest.mock("./getBookList");

test("should display Empty Library", async () => {
  const mockResponse = {
    status: 200,
    data: {
      books: [],
    },
  };
  getBookList.mockResolvedValueOnce(Promise.resolve(mockResponse));
  render(<Router><BookListView /></Router>);

  await waitForElementToBeRemoved(document.querySelector("div.loading")).then(
    () => {
      expect(screen.getByText(/Empty/i)).toBeInTheDocument();
      expect(document.querySelector(".book")).toBe(null);
      expect(screen.queryByText(/Borrow/i)).not.toBeInTheDocument();
    }
  );
});

test("should display the List of Books with borrow buttons", async () => {
  const mockResponse = {
    status: 200,
    data: {
      books: [
        {
          id: "5ff7463891",
          name: "bookName",
          author: "authorName",
          borrowerId: "0"
        },
        {
          id: "5ff74638914",
          name: "bookName2",
          author: "authorName2",
          borrowerId: "0"
        },
      ],
    },
  };
  getBookList.mockResolvedValueOnce(Promise.resolve(mockResponse));
  let { container } = render();
  act(() => {
    container = render(<Router><BookListView /></Router>);
  });
  await waitForElementToBeRemoved(document.querySelector("div.loading")).then(
    () => {
      expect(document.querySelector(".book")).not.toBe(null);
      expect(document.querySelector(".borrowButton")).not.toBe(null);
      expect(document.querySelector(".returnButton")).toBe(null);
      expect(screen.queryByText(/Return/i)).not.toBeInTheDocument();
      expect(screen.queryByText(/Return All/i)).not.toBeInTheDocument();
      expect(screen.queryAllByText(/Borrow/i)[0]).toBeInTheDocument();
    }
  );
});

