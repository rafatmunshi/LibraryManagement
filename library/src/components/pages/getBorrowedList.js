import axios from "axios";
export const getBorrowedList = async () => {
  const response = await axios.get(
    "/Library/1/borrowedBookList"
  );
   return response;
};

export default getBorrowedList;