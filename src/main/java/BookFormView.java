import Models.Book;
import Models.User;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookFormView extends BookForm {
    private Binder<Book> binder = new Binder<>(Book.class);
    private boolean checkOutClicked = false;
    private boolean addClicked = false;
    public UserFormView userFormView;
    private SQL sql;
    private Grid<Book> grid;
    private static List<Book> books;
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
                if (sql.sqlController.userId.contains(id))
                    UserFormView.getUserById(id).setCheckedOutBooks(UserFormView.getUserById(id).getCheckedOutBooks() + 1);
                userId.setValue("");
                sql.editUser(SQL.Table.USERS, "numbooks", String.valueOf(UserFormView.getUserById(id).getCheckedOutBooks()), id);
                sql.edit(SQL.Table.BOOK, "checkOut", true, Integer.parseInt(this.id.getValue().replaceAll(",","")));
                sql.commit();
                books.get(getIndexById(Integer.parseInt(this.id.getValue().replaceAll(",","")))).setCheckedOut("CHECKED OUT");
                grid.setItems(books);
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
                addBooks();
                grid.setItems(books);
            }
        });
        add.addClickListener((Button.ClickListener) click ->{
           addClicked = true;
           checkOutClicked = false;
           delete.setVisible(false);
           checkOut.setVisible(false);
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
            checkedOut.setVisible(true);
            addClicked = false;
            checkOutClicked = false;
            cancel.setVisible(false);
            userId.setValue("");
            userId.setVisible(false);
            grid.deselectAll();
        });
        delete.addClickListener((Button.ClickListener) click ->{
           add.setVisible(true);
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
    void setList(List<Book> list) {
        this.books = list;
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
    public int getIndexById (int id) {
        int index = 0;
        for (Book book: books) {
            index++;
            if (book.getId() == id) break;
        }
        return index;
    }
    private void addBooks () {
        books.clear();
        ResultSet resultSet = sql.getResultSet("SELECT * FROM users.Books");
        String author, name;
        int id;
        Object checkOut;
        String checkOutValue;
        try {
            while (resultSet.next()) {
                author = resultSet.getString("author");
                name = resultSet.getString("name");
                id = resultSet.getInt("id");
                checkOut = resultSet.getBoolean("checkOut");
                if (checkOut == "true") checkOutValue = "CHECKED OUT";
                else checkOutValue = "AVAILABLE";
                books.add(new Book(id, author, name, checkOutValue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
