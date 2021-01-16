const mockResponse = {
  status: 200,
  data: {
    books: [
      {
        id: "5ff74638914fa154d4a870ec",
        name: "bookName1",
        author: "authorName1",
        borrowerId: "0",
      },
      {
        id: "5ff74638914fa154d4a870ed",
        name: "bookName2",
        author: "authorName2",
        borrowerId: "0",
      },
    ],
  },
};
function getBookList() {
  return new Promise((resolve, reject) => {
    resolve(mockResponse);
  });
}

export default getBookList;
