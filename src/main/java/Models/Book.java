package Models;


public class Book {
    private int id;
    private String author, bookName, checkedOut;

    public Book(int id, String author, String bookName, String checkedOut) {
        this.id = id;
        this.author = author;
        this.bookName = bookName;
        this.checkedOut = checkedOut;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(String checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

}
