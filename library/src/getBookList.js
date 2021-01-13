import axios from "axios";
export const getBookList = async () => {
  const response = await axios.get(
    "https://my-json-server.typicode.com/rafatmunshi/LibraryManagementSystem/bookList"
  );
  // use this to check empty response- https://my-json-server.typicode.com/rafatmunshi/LMS/bookList
  return response;
};

export default getBookList;