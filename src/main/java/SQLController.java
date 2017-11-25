import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;



public class SQLController {
    public List<String> userName, schoolName, author, bookName;
    public List<Integer> userId, checkedOutBooks, limitOfBooks, forirghschoolId, bookId, schoolId, transactionId, forirghUserId, forirghBookId, fine, tLim, sLim ;
    public List<Boolean> userStatus, checkOut;
    SQL sql = new SQL();
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
        checkOut = new ArrayList<>();
    }

    public List<String> getList(SQL.Table table, String coloum) {
        List<String> list = new ArrayList<>();
        ResultSet resultSet = sql.getResultSet("SELECT * FROM " + table.table);
        String elements;
        try {
            while (resultSet.next()) {
                elements = resultSet.getString(coloum);
                list.add(elements);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
