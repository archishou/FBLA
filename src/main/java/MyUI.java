
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
    private HorizontalSplitPanel usersSplitPanel = new HorizontalSplitPanel();
    private HorizontalSplitPanel bookSplitPanel = new HorizontalSplitPanel();
    private Grid<User> userGrid = new Grid<>(User.class);
    private Grid<Book> bookGrid = new Grid<>(Book.class);
    private boolean currentState = false;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        userEditor.delete.setVisible(currentState);
        userEditor.cancel.setVisible(currentState);
        userEditor.setGrid(userGrid);
        bookEditor.setGrid(bookGrid);
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
                tabSheet.setSizeFull();
                sign.setVisible(false);
                userEditor.refresh();
                bookEditor.refresh();
                userEditor.setSizeFull();
                userGrid.setSizeFull();
                userGrid.removeAllColumns();
                userGrid.addColumn(User::getUserId).setCaption("USER ID");
                userGrid.addColumn(User::getUserName).setCaption("NAME");
                userGrid.addColumn(User::getUserStatus).setCaption("STATUS");
                userGrid.addColumn(User::getCheckedOutBooks).setCaption("BOOKS CHECKED OUT");
                userGrid.addColumn(User::getLimitOfBooks).setCaption("BOOK LIMIT");
                userGrid.addColumn(User::getSchoolId).setCaption("SCHOOL ID");
                userGrid.asSingleSelect().addValueChangeListener(evt -> userEditor.setUser(evt.getValue()));
                userGrid.addSelectionListener(event -> {
                    userEditor.delete.setVisible(true);
                    userEditor.cancel.setVisible(true);
                    userEditor.add.setVisible(false);
                    userEditor.name.setReadOnly(false);
                });
                bookEditor.setSizeFull();
                bookGrid.setSizeFull();
                bookGrid.removeAllColumns();
                bookGrid.addColumn(Book::getId).setCaption("Book ID");
                bookGrid.addColumn(Book::getBookName).setCaption("Book Name");
                bookGrid.addColumn(Book::getAuthor).setCaption("Author");
                bookGrid.addColumn(Book::getCheckedOut).setCaption("Checked Out");
                bookGrid.asSingleSelect().addValueChangeListener(evt -> {
                    bookEditor.setBook(evt.getValue());
                    bookEditor.userId.setVisible(false);
                    bookEditor.cancel.setVisible(true);
                    bookEditor.add.setVisible(false);
                    bookEditor.checkedOut.setVisible(true);
                });
                bookSplitPanel.setFirstComponent(bookGrid);
                bookSplitPanel.setSecondComponent(bookEditor);
                bookSplitPanel.setCaption("Books");
                usersSplitPanel.setFirstComponent(userGrid);
                usersSplitPanel.setSecondComponent(userEditor);
                usersSplitPanel.setCaption("User Data");
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
}
