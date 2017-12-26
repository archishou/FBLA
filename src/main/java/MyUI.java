
import javax.servlet.annotation.WebServlet;

import Models.Book;
import Models.User;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private UserFormView userEditor = new UserFormView();
    private BookFormView bookEditor = new BookFormView();
    private SignInView sign = new SignInView();
    private Home home = new Home();
    private SQL sql = new SQL();
    private TabSheet tabSheet = new TabSheet();
    private String u, p;
    private boolean credTrue;
    private List<User> users = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private HorizontalSplitPanel usersSplitPanel = new HorizontalSplitPanel();
    private HorizontalSplitPanel bookSplitPanel = new HorizontalSplitPanel();
    private Grid<User> userGrid = new Grid<>(User.class);
    Grid<Book> bookGrid = new Grid<>(Book.class);
    boolean currentState = false;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        userEditor.delete.setVisible(currentState);
        userEditor.cancel.setVisible(currentState);
        userEditor.setGrid(userGrid);
        userEditor.setList(users);
        bookEditor.setGrid(bookGrid);
        bookEditor.setList(books);
        bookEditor.setSql(sql);
        bookEditor.setUserFormView(userEditor);
        sql.connect(SQL.Database.USERS);
        userEditor.setSQL(sql);
        setContent(sign);
        sign.signinButton.addClickListener((Button.ClickListener) clickEvent -> {
            p = sign.passwordField.getValue();
            u = sign.username.getValue();
            credTrue = sign.isCred(u,p);
            if (credTrue) {
                System.out.println(sql.getList(SQL.Table.USERS, "numbooks").size());
                for(Object o: sql.getList(SQL.Table.USERS, "schoolid")) {
                    System.out.println(o.toString());
                }
                tabSheet.setSizeFull();
                sign.setVisible(false);
                addUsers();
                addBooks();
                userEditor.setSizeFull();
                userGrid.setItems(users);
                userGrid.setSizeFull();
                userGrid.removeAllColumns();
                userGrid.addColumn(User::getUserId).setCaption("USER ID");
                userGrid.addColumn(User::getUserName).setCaption("NAME");
                userGrid.addColumn(User::getUserStatus).setCaption("STATUS");
                userGrid.addColumn(User::getCheckedOutBooks).setCaption("BOOKS CHECKED OUT");
                userGrid.addColumn(User::getLimitOfBooks).setCaption("BOOK LIMIT");
                userGrid.addColumn(User::getForirghschoolId).setCaption("SCHOOL ID");
                userGrid.asSingleSelect().addValueChangeListener(evt -> userEditor.setUser(evt.getValue()));
                userGrid.addSelectionListener(event -> {
                    userEditor.delete.setVisible(true);
                    userEditor.cancel.setVisible(true);
                    userEditor.add.setVisible(false);
                });
                bookEditor.setSizeFull();
                bookGrid.setItems(books);
                bookGrid.setSizeFull();
                bookGrid.removeAllColumns();
                bookGrid.addColumn(Book::getId).setCaption("Models.Book ID");
                bookGrid.addColumn(Book::getBookName).setCaption("Models.Book Name");
                bookGrid.addColumn(Book::getAuthor).setCaption("Author");
                bookGrid.addColumn(Book::getCheckedOut).setCaption("Checked Out");
                bookGrid.asSingleSelect().addValueChangeListener(evt -> {
                    bookEditor.setBook(evt.getValue());
                    if (evt.getValue().getCheckedOut().equals("CHECKED OUT")) {
                        bookEditor.checkOut.setVisible(false);
                    }
                    bookEditor.userId.setVisible(false);
                    bookEditor.cancel.setVisible(true);
                    bookEditor.add.setVisible(false);
                });
                bookSplitPanel.setFirstComponent(bookGrid);
                bookSplitPanel.setSecondComponent(bookEditor);
                bookSplitPanel.setCaption("Books");
                usersSplitPanel.setFirstComponent(userGrid);
                usersSplitPanel.setSecondComponent(userEditor);
                usersSplitPanel.setCaption("Models.User Data");
                home.setCaption("Home");
                tabSheet.addComponent(home);
                tabSheet.addComponent(usersSplitPanel);
                tabSheet.addComponent(bookSplitPanel);
                setContent(tabSheet);
            }
            else {
                sign.signInError.setVisible(true);
            }

        });
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {}

    private void addUsers () {
        userEditor.refresh();
    }
    private void addBooks () {
        List<List<Object>> lists = new ArrayList<>();
        lists.add(sql.getList(SQL.Table.BOOK, "id"));
        lists.add(sql.getList(SQL.Table.BOOK, "author"));
        lists.add(sql.getList(SQL.Table.BOOK, "name"));
        lists.add(sql.getList(SQL.Table.BOOK, "checkOut"));
        int i = lists.get(0).size();
        int loopIteration = 0;
        String checkedOut;
        while (loopIteration < i) {
            if (lists.get(3).get(loopIteration).toString().equals("true")) checkedOut = "CHECKED OUT";
            else checkedOut = "AVAILABLE";
            books.add(new Book((Integer) lists.get(0).get(loopIteration), lists.get(1).get(loopIteration).toString(),
                    lists.get(2).get(loopIteration).toString(), checkedOut));
            loopIteration++;
        }
        lists.clear();
    }
}
