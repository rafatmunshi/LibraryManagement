import axios from "axios";
export const getBookList = async () => {
  const response = await axios.get(
    "/Library/bookList"
  );
  return response;
};

export default getBookList;