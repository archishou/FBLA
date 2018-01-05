


import Models.User;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SQL {
    private Connection connection = null;
    private Statement stmt = null;

    private java.util.Date utilDate = new java.util.Date();
    public SQL () {

    }
    public enum Database {
        USERS("users");
        public final String database;
        Database (String database) {this.database = database;}
    }
    public enum Table{
        USERS("Users"),
        BOOK("Books"),
        SCHOOOL("School"),
        TRANSACTION("Transactions");
        public final String table;
        Table (String table) {this.table = table;}
    }
    public enum UserType {
        TEACHER(1),
        STUDENT(2);
        public final int userType;
        UserType (int userType) {this.userType = userType;}
    }
    public void connect (Database d) {
        try { Class.forName("com.mysql.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + d.database, "root", "R3as0n99");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
    public void addBook(int bookId, String author, String name, boolean checkedOut, Table t) {
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into users." + t.table + " values (?,?,?,?)");
                preparedStatement.setInt(1, bookId);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, name);
                preparedStatement.setBoolean(4,checkedOut);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Prepared Statement Failed! Check output console");
                e.printStackTrace();
                return;
            }
        } else {
            System.out.println("Failed to make connection!");
        }
    }
    public void addUser(int id, String name, int numBooks, int limit, int schoolId, String status, Table t){
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into users." + t.table + " values (?,?,?,?,?,?)");
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, numBooks);
                preparedStatement.setInt(4, limit);
                preparedStatement.setInt(5,schoolId);
                if (status.toLowerCase().contains("s")) {
                    preparedStatement.setBoolean(6, false);
                }
                else {
                    preparedStatement.setBoolean(6, true);
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Prepared Statement Failed! Check output console");
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to make connection!");
        }
        commit();
    }
    public void addUser(User user){
        addUser(Integer.valueOf(user.getUserId()), user.getUserName(), user.getCheckedOutBooks(), user.getLimitOfBooks(),
                user.getSchoolId(), user.getUserStatus(), Table.USERS);
    }
    public void addTransaction(int id, int userId, int bookId, String trnDate, String rDate, double fine, Table t){
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into users." + t.table + " values (?,?,?,?,?,?)");
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, bookId);
                preparedStatement.setString(4, trnDate);
                preparedStatement.setString(5, rDate);
                preparedStatement.setDouble(6, fine);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Invalid User or Book ID");
                return;
            }
        } else {
            System.out.println("Failed to make connection!");
        }
    }
    public Boolean exists(Table t, int userId){
        boolean e = false;
        if (connection != null){
            try {
                String SQL = "SELECT 1 FROM " + t.table + " WHERE id = " + String.valueOf(userId);
                System.out.println(SQL);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                if (isFilled(rs)) e = true;
            } catch (SQLException l) {
                l.printStackTrace();
            }
        }
        return e;
    }
    public boolean isFilled(ResultSet rs){
        boolean isEmpty = true;
        try {
            while(rs.next()){
                isEmpty = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !isEmpty;
    }
    public void delete(String key, Table t){
        runStatement("delete from " + t.table + " where id = " + key);
    }
    public void delete(String key, String column, Table t){
        runStatement("delete from " + t.table + " where " + column + " = " + key);
    }
    public void editUser(Table t, String col, String edit, int id){
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String sql = "UPDATE " + t.table + " SET "+  col + "= " +  "'" + edit + "'" + " WHERE id="+ String.valueOf(id) + ";";
        System.out.println(sql);
        runStatement(sql);
        commit();
    }
    public void edit(Table t, String col, Object edit, int id) {
        String sql = "UPDATE " + t.table + " SET "+  col + "= "  + edit.toString()  + " WHERE id="+ String.valueOf(id) + ";";
        runStatement(sql);
        commit();
    }
    public void editTransaction(Table t, String col, Object edit, int id) {
        String sql = "UPDATE " + t.table + " SET "+  col + "= "  + edit.toString()  + " WHERE bookId="+ String.valueOf(id) + ";";
        runStatement(sql);
        commit();
    }
    public void print(Table t){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("Select * from users." + t.table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(resultSet != null ){
            try {
                while(resultSet.next()){
                    int i = resultSet.getInt(1);
                    String user = resultSet.getString(2);
                    String password = resultSet.getString(3);
                    System.out.println("RECORD : " + i +" - " + user +" - " + password);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void runStatement(String sql){
        if (connection != null){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        if (connection != null){
            try {
                stmt = connection.createStatement();
                rs = stmt.executeQuery(sql);
            } catch (SQLException l) {
                l.printStackTrace();
            }
        }
        return rs;
    }
    void refresh (UserFormView u) {
        int id = Integer.parseInt(u.id.getValue().replaceAll("'",""));
        editUser(Table.USERS, "bookLim", u.limitOfBooks.getValue(), id);
        editUser(Table.USERS, "name", u.name.getValue(), id);
        commit();
    }
    int genID (Table table) {
        ResultSet resultSet = getResultSet("SELECT id from users." + table.table + " ORDER BY id DESC LIMIT 1;");
        int id = 0;
        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    void commit() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("COMMIT;");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Object> getList(SQL.Table table, String coloum) {
        List<Object> list = new ArrayList<>();
        ResultSet resultSet = getResultSet("SELECT * FROM " + table.table);
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
    public List<Object> getList(ResultSet rs, int column) {
        List<Object> strings = new ArrayList<>();
        resetResultSet(rs);
        try {
            while (rs.next()) {
                strings.add(String.valueOf(rs.getObject(column)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strings;
    }
    public List<Object> getList(ResultSet rs, String column) {
        List<Object> strings = new ArrayList<>();
        resetResultSet(rs);
        try {
            while (rs.next()) {
                strings.add(String.valueOf(rs.getObject(column)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strings;
    }
    public double getTotalFine(int id) {
        double total = 0;
        ResultSet rs = getResultSet("SELECT * FROM users.Transactions");
        resetResultSet(rs);
        try {
            while (rs.next())
                if (rs.getInt(2) == id) total+= rs.getInt(6);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public List<Integer> getIntegerList(SQL.Table table, String coloum) {
        List<Integer> list = new ArrayList<>();
        ResultSet resultSet = getResultSet("SELECT * FROM " + table.table);
        String elements;
        try {
            while (resultSet.next()) {
                elements = resultSet.getString(coloum);
                list.add(Integer.valueOf(elements));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getDayLimit(UserType userType) {
        String returnS = "";
        ResultSet rs = getResultSet("SELECT * from users.Settings WHERE idSettings = 1;");
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int columnsNumber = 0;
        try {
            columnsNumber = rsmd.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    returnS = returnS + (columnValue + " " + rsmd.getColumnName(i));
                }
                returnS = returnS + "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        returnS = returnS.replaceAll("[^0-9]+", " ");
        returnS = Arrays.asList(returnS.trim().split(" ")).get(userType.userType);
        return returnS;
    }
    public void resetResultSet(ResultSet rs) {
        try {
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

