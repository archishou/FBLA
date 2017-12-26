import Models.Book;
import Models.User;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookFormView extends BookForm {
    private Binder<Book> binder = new Binder<>(Book.class);
    private boolean checkOutClicked = false;
    private boolean addClicked = false;
    public UserFormView userFormView;
    private SQL sql;
    private Grid<Book> grid;
    public BookFormView () {
        cancel.setComponentError(null);
        userId.setVisible(false);
        binder.forField(this.id)
                .withNullRepresentation ("")
                .withConverter(new StringToIntegerConverter(0, "integers only"))
                .bind(Book::getId, Book::setId);
        binder.forField(this.name)
                .withNullRepresentation("")
                .bind(Book::getBookName, Book::setBookName);
        binder.forField(this.author)
                .withNullRepresentation("")
                .bind(Book::getAuthor, Book::setAuthor);
        binder.forField(this.checkedOut)
                .withNullRepresentation("")
                .bind(Book::getCheckedOut, Book::setCheckedOut);
        binder.bindInstanceFields(this);
        save.addClickListener((Button.ClickListener) click -> {
            if (checkOutClicked && isNumeric(userId.getValue())) {
                int id = Integer.parseInt(userId.getValue());
                if (sql.getList(SQL.Table.USERS, "id").contains(id))
                    UserFormView.getUserById(id).setCheckedOutBooks(UserFormView.getUserById(id).getCheckedOutBooks() + 1);
                userId.setValue("");
                sql.editUser(SQL.Table.USERS, "numbooks", String.valueOf(UserFormView.getUserById(id).getCheckedOutBooks()), id);
                sql.edit(SQL.Table.BOOK, "checkOut", true, Integer.parseInt(this.id.getValue().replaceAll(",","")));
                sql.commit();
                userId.setVisible(false);
            }
            if (addClicked) {
                int copies = Integer.parseInt(userId.getValue());
                String name = this.name.getValue();
                String author = this.author.getValue();
                int loopIterations = 0;
                int id;
                while (loopIterations < copies) {
                    id = genID();
                    sql.addBook(id, author, name, false, SQL.Table.BOOK);
                    loopIterations++;
                }
                refresh();
            }
        });
        add.addClickListener((Button.ClickListener) click ->{
           addClicked = true;
           checkOutClicked = false;
           delete.setVisible(false);
           id.setValue(String.valueOf(genID()));
           userId.setCaption("Copies");
           userId.setVisible(true);
           id.setReadOnly(true);
           checkedOut.setValue("AVAILABLE");
           author.setReadOnly(false);
           name.setReadOnly(false);
        });
        checkOut.addClickListener((Button.ClickListener) clickListener -> {
            userId.setCaption("User ID");
            addClicked = false;
            checkOutClicked = true;
            author.setReadOnly(true);
            name.setReadOnly(true);
            id.setReadOnly(true);
            cancel.setVisible(true);
            userId.setVisible(true);
            checkedOut.setReadOnly(true);
        });
        cancel.addClickListener((Button.ClickListener) clickListener -> {
            add.setVisible(true);
            addClicked = false;
            checkOutClicked = false;
            cancel.setVisible(false);
            userId.setValue("");
            userId.setVisible(false);
            grid.deselectAll();
        });
        delete.addClickListener((Button.ClickListener) click ->{
           add.setVisible(true);
            sql.delete(this.id.getValue().replace(",",""), SQL.Table.BOOK);
            refresh();
        });
    }
    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    void setBook(Book value) {
        binder.setBean(value);
    }
    void setGrid(Grid<Book> grid) {
        this.grid = grid;
    }
    public void setSql(SQL sql) {
        this.sql = sql;
    }


    public void setUserFormView(UserFormView userFormView) {
        this.userFormView = userFormView;
    }
    private int genID (){
        return sql.genID(SQL.Table.BOOK) + 2;
    }
    public void refresh() {
        List<Book> books = new ArrayList<>();
        int loopIteration = 0;
        String checkedOut = "";
        while (loopIteration < sql.getList(SQL.Table.BOOK, "id").size()) {
            if (sql.getList(SQL.Table.BOOK, "checkOut").get(loopIteration).toString().equals("1")) checkedOut = "CHECKED OUT";
            else checkedOut = "AVALIABLE";
            books.add(new Book(
                    Integer.parseInt(sql.getList(SQL.Table.BOOK, "id").get(loopIteration).toString()),
                    sql.getList(SQL.Table.BOOK, "author").get(loopIteration).toString(),
                    sql.getList(SQL.Table.BOOK, "name").get(loopIteration).toString(),
                    checkedOut));
            loopIteration++;
        }
        grid.setItems(books);
        books = null;
    }
}
