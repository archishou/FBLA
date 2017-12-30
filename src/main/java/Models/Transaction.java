package Models;

public class Transaction {
    private String userId, bookId, dtr;

    public Transaction(String userId, String bookId, String dtr) {
        this.userId = userId;
        this.bookId = bookId;
        this.dtr = dtr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getDtr() {
        return dtr;
    }

    public void setDtr(String dtr) {
        this.dtr = dtr;
    }
}
