


import Models.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SQL {
    private Connection connection = null;
    private Statement stmt = null;
    public static SQLController controller = new SQLController();
    private java.util.Date utilDate = new java.util.Date();
    public SQL () {

    }
    public enum Database {
        USERS("users");
        public final String database;
        Database (String database) {this.database = database;}
    }
    public enum Table{
        USERS("users"),
        BOOK("books"),
        SCHOOOL("school"),
        TRANSACTION("transactions");
        public final String table;
        Table (String table) {this.table = table;}
    }
    public enum UserType {
        TEACHER(1),
        STUDENT(2);
        public final int userType;
        UserType (int userType) {this.userType = userType;}
    }
    public void connect () {
        /*URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();
        try { Class.forName("com.mysql.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection(dbUrl, username, password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }*/
    }
    public void addBook(int bookId, String author, String name, boolean checkedOut) {
         controller.bookid.add(bookId);
         controller.author.add(author);
         controller.bookName.add(name);
         controller.checkOut.add(checkedOut);
    }
    public void addUser(int id, String name, int numBooks, int limit, int schoolId, String status){
        controller.userid.add(id);
        controller.userName.add(name);
        controller.numbooks.add(numBooks);
        controller.lim.add(limit);
        controller.userSchoolId.add(schoolId);
        controller.teacherYN.add(Boolean.valueOf(status));
    }
    public void addUser(User user){
        addUser(Integer.valueOf(user.getUserId()), user.getUserName(), user.getCheckedOutBooks(), user.getLimitOfBooks(),
                user.getSchoolId(), user.getUserStatus());
    }
    public void addTransaction(int id, int userId, int bookId, String trnDate, String rDate, double fine){
        controller.transactionid.add(id);
        controller.transUserId.add(userId);
        controller.transBookId.add(bookId);
        controller.trnDate.add(trnDate);
        controller.rDate.add(rDate);
        controller.fine.add(fine);
    }
    public void deleteBook(String key){
        int index = genIndex(controller.bookid, Integer.valueOf(key));
        controller.bookid.remove(index);
        controller.author.remove(index);
        controller.bookName.remove(index);
        controller.checkOut.remove(index);
    }
    public void deleteTransaction(String bookid) {
        int index = genIndex(controller.transBookId, Integer.valueOf(bookid));
        controller.transactionid.remove(index);
        controller.transUserId.remove(index);
        controller.transBookId.remove(index);
        controller.rDate.remove(index);
        controller.trnDate.remove(index);
        controller.fine.remove(index);
    }
    public void deleteUser(String key) {
        int index = genIndex(controller.userid, Integer.valueOf(key));
        controller.userid.remove(index);
        controller.userName.remove(index);
        controller.numbooks.remove(index);
        controller.userSchoolId.remove(index);
        controller.lim.remove(index);
        controller.teacherYN.remove(index);
    }
    public void editUserBookLimit(String edit, int id){
        int index = genIndex(controller.userid, id);
        controller.lim.set(index, Integer.valueOf(edit));
    }
    public void editUserName(String edit, int id) {
        int index = genIndex(controller.userid, id);
        controller.userName.set(index, edit);
    }
    public void editUserNumBooks(String edit, int id) {
        int index = genIndex(controller.userid, id);
        controller.numbooks.set(index, Integer.parseInt(edit));
    }
    public void editCheckOut(Object edit, int id) {
        int index = genIndex(controller.bookid, id);
        controller.checkOut.set(index, Boolean.valueOf(edit.toString()));
    }
    public void editTransaction(Table t, String col, Object edit, int id) {
        int index = genIndex(controller.transBookId, id);
        controller.fine.set(index, Double.valueOf(edit.toString()));
    }
    void refresh (UserFormView u) {
        int id = Integer.parseInt(u.id.getValue().replaceAll("'",""));
        editUserBookLimit(u.limitOfBooks.getValue(), id);
        editUserName(u.name.getValue(), id);
    }
    int genIDTransactions () {
        try {
            return controller.transactionid.get(controller.transactionid.size() - 1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return 1;
        }
    }
    int genIdUsers () {
        return controller.userid.get(controller.userid.size() - 1);
    }
    int genBookId () {
        return controller.bookid.get(controller.bookid.size() - 1);
    }
    public int genIndex(List<Integer> l, int key) {
        int loopIteration = 0;
        for (Integer i : l) {
            if (i==key) return loopIteration;
            loopIteration++;
        }
        return loopIteration;
    }
    public double getTotalFine(int id) {
        double total = 0;
        int loopIteration = 0;
        System.out.println(controller.transactionid.size());
        while (loopIteration < controller.transactionid.size()) {
            if (controller.transUserId.get(loopIteration) == id) total += controller.fine.get(loopIteration);
            loopIteration++;
        }
        return total;
    }
    public String getDayLimit(UserType userType) {
        if (userType.userType == 1) return String.valueOf(10);
        else return String.valueOf(5);
    }
    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }
    public String daysInBetween(String dayStart, String dayEnd) {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        String days = "";
        try {
            java.util.Date startDate = myFormat.parse(dayStart);
            java.util.Date endDate = myFormat.parse(dayEnd);
            long diff = endDate.getTime() - startDate.getTime();
            days = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
    public String addDays(int daysAddition) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date currentDate = new java.util.Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, daysAddition);
        java.util.Date currentDatePlusOne = c.getTime();
        return dateFormat.format(currentDatePlusOne);
    }
}

