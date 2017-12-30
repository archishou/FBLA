import Models.Transaction;
import Models.User;
import com.vaadin.data.Binder;
import com.vaadin.ui.Grid;

import java.util.ArrayList;
import java.util.List;

public class TransactionView {
    private Grid<Transaction> grid;
    private SQL sql;

    public void setSql(SQL sql) {
        this.sql = sql;
    }

    public void setGrid(Grid<Transaction> grid) {
        this.grid = grid;
    }
    public void refresh () {
        List<Transaction> transactions = new ArrayList<>();
        int loopIteration = 0;
        while (loopIteration < sql.getList(SQL.Table.TRANSACTION, "id").size()) {
            transactions.add(new Transaction(
                    sql.getList(SQL.Table.TRANSACTION, "userId").get(loopIteration).toString(),
                    sql.getList(SQL.Table.TRANSACTION, "bookId").get(loopIteration).toString(),
                    sql.getList(SQL.Table.TRANSACTION, "rDate").get(loopIteration).toString()));
            loopIteration++;
        }
        grid.setItems(transactions);
        transactions = null;
    }
}
