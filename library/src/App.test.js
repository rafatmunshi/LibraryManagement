import App from './App';
import { render } from '@testing-library/react';

test('should display the App main page', () => {
  const { container }= render(<App />);
  expect(container.firstChild.classList.contains('App')).toBe(true)
});