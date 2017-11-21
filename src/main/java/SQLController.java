import java.util.*;



public class SQLController {
    public List<String> userName, schoolName, author, bookName;
    public List<Integer> userId, checkedOutBooks, limitOfBooks, forirghschoolId, bookId, schoolId, transactionId, forirghUserId, forirghBookId, fine, tLim, sLim ;
    public List<Boolean> userStatus;
    public SQLController () {
        userId = new ArrayList<>();
        userName = new ArrayList<>();
        checkedOutBooks = new ArrayList<>();
        limitOfBooks = new ArrayList<>();
        forirghschoolId = new ArrayList<>();
        userStatus = new ArrayList<>();
        bookId = new ArrayList<>();
        author = new ArrayList<>();
        bookName= new ArrayList<>();
        tLim= new ArrayList<>();
        sLim = new ArrayList<>();
        schoolName = new ArrayList<>();
        fine = new ArrayList<>();
        schoolId = new ArrayList<>();
        transactionId = new ArrayList<>();
        forirghBookId = new ArrayList<>();
        forirghUserId = new ArrayList<>();
    }
}
