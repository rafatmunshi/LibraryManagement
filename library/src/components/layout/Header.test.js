import toJson from "enzyme-to-json";
import React from "react";
import Header from "./Header";
import Enzyme, { shallow } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import {
  render,
  screen,
  waitForElementToBeRemoved,
} from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BrowserRouter as Router, Route } from "react-router-dom";
import "@testing-library/jest-dom/extend-expect";

import App from "../../App";

test("should navigate between book list and borrowed list page", async () => {
  render(
    <Router>
      <App />
    </Router>
  );
  expect(screen.getByText(/View Library Books/i)).toBeInTheDocument();

  const leftClick = { button: 0 };
  userEvent.click(screen.getByText(/Your Borrowed List/i), leftClick);

  expect(screen.getAllByText(/Borrowed List/i)[0]).toBeInTheDocument();

  userEvent.click(screen.getByText(/View Library Books/i), leftClick);
  expect(screen.getAllByText(/the Library/i)[0]).toBeInTheDocument();
});

Enzyme.configure({ adapter: new Adapter() });

describe("<Header />", () => {
  let wrapperForShallowCopy;
  const setState = jest.fn();
  const useStateSpy = jest.spyOn(React, "useState");
  useStateSpy.mockImplementation((init) => [init, setState]);

  beforeEach(() => {
    wrapperForShallowCopy = Enzyme.shallow(<Header />);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should render correctly as we know it", () => {
    const wrapperForShallowCopy = shallow(<Header />);
    expect(toJson(wrapperForShallowCopy)).toMatchSnapshot();
  });
});
