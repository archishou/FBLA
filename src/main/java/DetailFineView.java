import Models.DetailFine;
import com.vaadin.ui.Grid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailFineView extends Grid {//
    private Grid<DetailFine> detailFineGrid;
    private SQL sql;
    public DetailFineView () {

    }

    public void setSql(SQL sql) {
        this.sql = sql;
    }

    public void setDetailFineGrid(Grid<DetailFine> detailFineGrid) {
        this.detailFineGrid = detailFineGrid;
    }

    public void refresh(int id) {
        ResultSet rs = sql.getResultSet("SELECT * FROM users.Transactions WHERE userId = " + id);
        List<Object> bookIds = sql.getList(rs, 3);
        List<Object> fines = sql.getList(rs, 6);
        List<DetailFine> detailFines = new ArrayList<>();
        int loopIteration = 0;
        sql.resetResultSet(rs);
        //
        try {
            while (rs.next()) {
                detailFines.add(new DetailFine(
                        BookFormView.getBookData(Integer.parseInt(String.valueOf(bookIds.get(loopIteration))))[0],
                        Double.valueOf(String.valueOf(fines.get(loopIteration))),
                        Integer.parseInt(sql.daysInBetween(sql.getDate(),
                                sql.getList(SQL.Table.TRANSACTION, "rDate").get(loopIteration).toString()))));
                System.out.println(Integer.parseInt(sql.daysInBetween(sql.getDate(),
                        sql.getList(SQL.Table.TRANSACTION, "rDate").get(loopIteration).toString())));
                loopIteration++;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        detailFineGrid.setItems(detailFines);
        bookIds = null;
        fines = null;
        detailFines = null;
        detailFines = null;
    }

}
