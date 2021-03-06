import BorrowedListView from "./BorrowedListView";
import "@testing-library/jest-dom";
import * as React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import {
  render,
  screen,
  act,
  waitForElementToBeRemoved,
} from "@testing-library/react";
import getBorrowedList from "./getBorrowedList";
jest.mock("./getBorrowedList");

test("should display Empty Library", async () => {
  const mockResponse = {
    status: 200,
    data: {
      books: [],
    },
  };
  getBorrowedList.mockResolvedValueOnce(Promise.resolve(mockResponse));
  render(
    <Router>
      <BorrowedListView />
    </Router>
  );

  await waitForElementToBeRemoved(document.querySelector("div.loading")).then(
    () => {
      expect(screen.getByText(/Empty/i)).toBeInTheDocument();
      expect(document.querySelector(".book")).toBe(null);
      expect(screen.queryByText(/Return/i)).not.toBeInTheDocument();
    }
  );
});

test("should display the List of Books with return buttons", async () => {
  const mockResponse = {
    status: 200,
    data: {
      books: [
        {
          id: "5ff746389",
          name: "bookName",
          author: "authorName",
          borrowerId: "1",
        },
        {
          id: "5ff7463891",
          name: "bookName2",
          author: "authorName2",
          borrowerId: "1",
        },
      ],
    },
  };
  getBorrowedList.mockResolvedValueOnce(Promise.resolve(mockResponse));
  let { container } = render();
  act(() => {
    container = render(
      <Router>
        <BorrowedListView />
      </Router>
    );
  });

  await waitForElementToBeRemoved(document.querySelector("div.loading")).then(
    () => {
      expect(document.querySelector(".book")).not.toBe(null);
      expect(document.querySelector(".borrowButton")).toBe(null);
      expect(screen.getByText(/Return All/i)).toBeInTheDocument();
      expect(document.querySelector(".returnButton")).not.toBe(null);
      expect(screen.queryAllByText(/Return/i)[0]).toBeInTheDocument();
    }
  );
});