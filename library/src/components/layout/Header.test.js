import toJson from 'enzyme-to-json';
import React from "react";
import Header from './Header';
import Enzyme, { shallow } from "enzyme";
import Adapter from "enzyme-adapter-react-16";

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

it('should render correctly as we know it', () => {
  const wrapperForShallowCopy = shallow(<Header />)
  expect(toJson(wrapperForShallowCopy)).toMatchSnapshot();
});

});
