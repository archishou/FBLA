import Models.Fine;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FineView extends Grid {
    private Grid<Fine> grid;
    private DetailFineView view;
    private SQL sql;


    public void setSql(SQL sql) {
        this.sql = sql;
        view.setSql(sql);
        grid.addSelectionListener(selectionEvent -> {
            for (Object o: grid.getSelectedItems()) {
                System.out.println(o);
            }
        });
    }

    public void setGrid(Grid<Fine> grid) {
        this.grid = grid;
    }

    public void setView(DetailFineView view) {
        this.view = view;
    }

    public void refresh() {
        List<Integer> userIds = new ArrayList<>();
        for (Integer i: SQL.controller.transUserId) {
            if (!userIds.contains(i)) userIds.add(i);
        }
        List<Fine> fines = new ArrayList<>();
        int userId;
        int loopIteration = 0;
            while (loopIteration < userIds.size()) {
                userId = Integer.parseInt(BookFormView.getUserData(Integer.parseInt(String.valueOf(userIds.get(loopIteration))))[0]);
                fines.add(new Fine(String.valueOf(userId), String.valueOf(sql.getTotalFine(userId))));
                loopIteration++;
            }
        grid.setItems(fines);
        userIds = null;
        fines = null;
    }


}
