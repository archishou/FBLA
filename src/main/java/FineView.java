import Models.DetailFine;
import Models.Fine;
import Models.Transaction;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalSplitPanel;

import java.util.ArrayList;
import java.util.List;

public class FineView {
    private Grid<Fine> fineGrid = new Grid<>(Fine.class);
    private HorizontalSplitPanel panel;
    private Grid<DetailFine> detailFineGrid = new Grid<>(DetailFine.class);
    private SQL sql;

    public void setSql(SQL sql) {
        this.sql = sql;
    }


}
