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
        /*List<Transaction> transactions = new ArrayList<>();
        int loopIteration = 0;
        String daysIB, userName, bookName;
        int userId, bookId;
        while (loopIteration < sql.getList(SQL.Table.TRANSACTION, "id").size()) {
            daysIB = sql.daysInBetween(sql.getDate(),
                    sql.getList(SQL.Table.TRANSACTION, "rDate").get(loopIteration).toString());
            userId = Integer.parseInt(sql.getList(SQL.Table.TRANSACTION, "userId").get(loopIteration).toString());
            userName = BookFormView.getUserData(userId)[1] + ": " + userId;
            bookId = Integer.parseInt(sql.getList(SQL.Table.TRANSACTION, "bookId").get(loopIteration).toString());
            bookName = BookFormView.getBookData(bookId)[2] + ": " + bookId;
            transactions.add(new Transaction(userName, bookName, daysIB));
            if (Integer.valueOf(daysIB) <= 0) sql.editTransaction(SQL.Table.TRANSACTION, "fine", Math.abs(Integer.valueOf(daysIB)) * 10, bookId);
            loopIteration++;
        }
        grid.setItems(transactions);
        transactions = null;*/
    }
}
