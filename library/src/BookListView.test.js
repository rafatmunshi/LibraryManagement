import BookListView from "./BookListView";
import "@testing-library/jest-dom";
import * as React from "react";
import {
  render,
  screen,
  act,
  waitForElementToBeRemoved,
} from "@testing-library/react";
import getBookList from "./getBookList";
jest.mock("./getBookList");

test("should display Empty Library to the User", async () => {
  const mockResponse = {
    status: 200,
    data: {
      books: [],
    },
  };
  getBookList.mockResolvedValueOnce(Promise.resolve(mockResponse));
  render(<BookListView />);
  await waitForElementToBeRemoved(document.querySelector("div.loading")).then(
    () => {
      expect(screen.getByText(/Empty/i)).toBeInTheDocument();
      expect(document.querySelector(".book")).toBe(null);
    }
  );
});

test("should display the List of Books to the User", async () => {
  const mockResponse = {
    status: 200,
    data: {
      books: [
        {
          id: "5ff74638914fa154d4a870ec",
          name: "bookName",
          author: "authorName",
        },
        {
          id: "5ff74638914fa154d4a870ed",
          name: "bookName2",
          author: "authorName2",
        },
      ],
    },
  };
  getBookList.mockResolvedValueOnce(Promise.resolve(mockResponse));
  let { container } = render();
  act(() => {
    container = render(<BookListView />);
  });

  await waitForElementToBeRemoved(document.querySelector("div.loading")).then(
    () => {
      expect(document.querySelector(".book")).not.toBe(null);
    }
  );
});
