package Models;

public class User {
    private int userId, checkedOutBooks, limitOfBooks, forirghschoolId;
    private String userStatus;

    private String userName;
    public User (int id, String name, int books, int limit, int schoolId, String status) {
        userId = id;
        userName = name;
        checkedOutBooks = books;
        limitOfBooks = limit;
        forirghschoolId = schoolId;
        userStatus = status;
    }
    public String getUserId() {
        return String.valueOf(userId);
    }

    public int getCheckedOutBooks() {
        return checkedOutBooks;
    }

    public int getLimitOfBooks() {
        return limitOfBooks;
    }

    public int getForirghschoolId() {
        return forirghschoolId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userId = Integer.parseInt(userId);
    }

    public void setCheckedOutBooks(int checkedOutBooks) {
        this.checkedOutBooks = checkedOutBooks;
    }

    public void setLimitOfBooks(int limitOfBooks) {
        this.limitOfBooks = limitOfBooks;
    }

    public void setForirghschoolId(int forirghschoolId) {
        this.forirghschoolId = forirghschoolId;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}