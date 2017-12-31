import Models.Fine;
import com.vaadin.ui.Grid;


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
        sql.getResultSet("SELECT * FROM users.Transactions WHERE fine > 0");
    }


}
