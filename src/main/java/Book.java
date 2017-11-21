


public class Book {
    private int id, tLim, sLim;
    private boolean checkedOut;
    private String author, bookName;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int gettLim() {
        return tLim;
    }

    public void settLim(int tLim) {
        this.tLim = tLim;
    }

    public int getsLim() {
        return sLim;
    }

    public void setsLim(int sLim) {
        this.sLim = sLim;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
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
