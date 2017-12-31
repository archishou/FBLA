import Models.DetailFine;
import Models.Fine;
import com.vaadin.ui.Grid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FineView extends Grid {
    private Grid<Fine> fineGrid;
    private DetailFineView view;
    private SQL sql;

    public void setSql(SQL sql) {
        this.sql = sql;
        view.setSql(sql);
        fineGrid.addSelectionListener(selectionEvent -> {
            view.refresh(Integer.valueOf(String.valueOf(selectionEvent.getAllSelectedItems().toArray()[0])));
        });
    }

    public void setFineGrid(Grid<Fine> fineGrid) {
        this.fineGrid = fineGrid;
    }

    public void setView(DetailFineView view) {
        this.view = view;
    }

    public void refresh() {
        ResultSet rs = sql.getResultSet("SELECT * FROM users.Transactions WHERE fine >= 0");
        List<Object> userIds = sql.getList(rs, 2);
        List<Fine> fines = new ArrayList<>();
        int userId;
        int loopIteration = 0;
        sql.resetResultSet(rs);
        try {
            while (rs.next()) {
                userId = Integer.parseInt(BookFormView.getUserData(Integer.parseInt(String.valueOf(userIds.get(loopIteration))))[0]);
                fines.add(new Fine(String.valueOf(userId), String .valueOf(sql.getTotalFine(userId))));
                loopIteration++;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        fineGrid.setItems(fines);
        userIds = null;
        fines = null;
    }


}
