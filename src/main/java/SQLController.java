import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;



public class SQLController {
    SQL sql;

    public List<Object> getList(SQL.Table table, String coloum) {
        List<Object> list = new ArrayList<>();
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
    public void setSql(SQL sql) {
        this.sql = sql;
    }
}
