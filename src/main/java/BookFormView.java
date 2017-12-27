import Models.Book;
import Models.User;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;

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
            if (checkOutClicked) {
                int id = Integer.parseInt(userId.getValue().replaceAll(",",""));
                userId.setValue("");
                System.out.println(String.valueOf(Integer.parseInt(getUserData(id)[2])));
                sql.editUser(SQL.Table.USERS, "numbooks", String.valueOf(Integer.parseInt(getUserData(id)[2]) + 1), id);
                sql.edit(SQL.Table.BOOK, "checkOut", true, Integer.parseInt(this.id.getValue().replaceAll(",","")));
                sql.commit();
                refresh();
                userFormView.refresh();
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
            new Notification("Save Successful", "",
                    Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());
        });
        add.addClickListener((Button.ClickListener) click ->{
           if (addClicked) new Notification("Click Save. ", "",
                   Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
           addClicked = true;
           checkOutClicked = false;
           id.setValue(String.valueOf(genID()));
           userId.setCaption("Copies");
           userId.setVisible(true);
           id.setReadOnly(true);
           checkedOut.setValue("AVAILABLE");
           checkedOut.setReadOnly(true);
           author.setReadOnly(false);
           name.setReadOnly(false);
        });
        checkOut.addClickListener((Button.ClickListener) clickListener -> {
            if (addClicked) new Notification("Click Save. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
            else {
                if (checkedOut.getValue().toLowerCase().contains("u")) {
                    System.out.println(checkedOut.getValue());
                    Notification notification = new Notification("This books is already checked out", "",
                            Notification.Type.WARNING_MESSAGE, true);
                    notification.show(Page.getCurrent());
                } else {
                    userId.setCaption("User ID");
                    addClicked = false;
                    checkOutClicked = true;
                    author.setReadOnly(true);
                    name.setReadOnly(true);
                    id.setReadOnly(true);
                    cancel.setVisible(true);
                    userId.setVisible(true);
                    checkedOut.setReadOnly(true);
                }
            }
        });
        cancel.addClickListener((Button.ClickListener) clickListener -> {
            add.setVisible(true);
            addClicked = false;
            checkOutClicked = false;
            cancel.setVisible(false);
            userId.setValue("");
            userId.setVisible(false);
            grid.deselectAll();
            new Notification("Operation Canceled. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
        });
        returnBook.addClickListener((Button.ClickListener) clickListener -> {
            if (checkedOut.getValue().toLowerCase().contains("a")) {
                System.out.println(checkedOut.getValue());
                Notification notification = new Notification("This books is already in stock", "",
                        Notification.Type.WARNING_MESSAGE, true);
                notification.show(Page.getCurrent());
            }
            else {
                sql.edit(SQL.Table.BOOK, "checkOut", false, Integer.parseInt(id.getValue().replaceAll(",","")));
                refresh();
                Notification notification = new Notification("Returned to the library", "",
                        Notification.Type.WARNING_MESSAGE, true);
                notification.show(Page.getCurrent());
            }
        });
        delete.addClickListener((Button.ClickListener) click ->{
            if (addClicked) new Notification("Click Save. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
            else {
                add.setVisible(true);
                sql.delete(this.id.getValue().replace(",", ""), SQL.Table.BOOK);
                refresh();
                new Notification("Delete Successful. ", "",
                        Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
            }
        });
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
    public String[] getUserData(int id) {
        String[] returnString = new String[]{"","","","","",""};
        returnString[0] = String.valueOf(id);
        int index = 0;
        for (Integer integer: sql.getIntegerList(SQL.Table.USERS, "id")) {
            if (integer == id){
                returnString[1] = String.valueOf(sql.getList(SQL.Table.USERS, "name").get(index));
                returnString[2] = String.valueOf(sql.getList(SQL.Table.USERS, "numbooks").get(index));
                returnString[3] = String.valueOf(sql.getList(SQL.Table.USERS, "bookLim").get(index));
                returnString[4] = String.valueOf(sql.getList(SQL.Table.USERS, "schoolid").get(index));
                returnString[5] = String.valueOf(sql.getList(SQL.Table.USERS, "teacherYN").get(index));
            }
            index++;
        }
        for (String s: returnString) {
            System.out.print(s);
        }
        return returnString;
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
            else checkedOut = "AVAILABLE";
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
