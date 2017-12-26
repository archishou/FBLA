package Models;

public class User {
    private int userId, checkedOutBooks, limitOfBooks, schoolId;
    private String userStatus;

    private String userName;
    public User (int id, String name, int books, int limit, int schoolId, String status) {
        userId = id;
        userName = name;
        checkedOutBooks = books;
        limitOfBooks = limit;
        this.schoolId = schoolId;
        userStatus = status;
    }

    public User(Object id, Object name, Object books, Object limit, Object schoolId, String status) {
        userId = Integer.valueOf(id.toString());
        userName = (String) name;
        checkedOutBooks = (int) books;
        limitOfBooks = (int) limit;
        this.schoolId = (int) schoolId;
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

    public int getSchoolId() {
        return schoolId;
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

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}