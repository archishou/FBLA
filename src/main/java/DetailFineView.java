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
        List<DetailFine> detailFines = new ArrayList<>();
        int loopIteration = 0;
        List<String> rDates = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        for (Integer i: SQL.controller.transUserId) {
            if (i == id) {
                userIds.add(i);
                indexes.add(sql.genIndex(SQL.controller.transUserId, id));
            }
        }
        while (loopIteration < userIds.size()) {
            rDates.add(String.valueOf(indexes.get(loopIteration)));
            loopIteration++;
        }
        loopIteration = 0;
        while (loopIteration < SQL.controller.transactionid.size()) {
            detailFines.add(new DetailFine(
                    BookFormView.getBookData(Integer.parseInt(String.valueOf(SQL.controller.bookid.get(loopIteration))))[0],
                    Double.valueOf(String.valueOf(SQL.controller.fine.get(loopIteration))),
                    Integer.parseInt(sql.daysInBetween(sql.getDate(),
                            rDates.get(loopIteration)))));
            loopIteration++;
        }
        detailFineGrid.setItems(detailFines);
    }

}
