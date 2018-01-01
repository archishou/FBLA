
import javax.servlet.annotation.WebServlet;

import Models.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

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
    private DetailFineView detailFineView = new DetailFineView();
    private FineView fineView = new FineView();
    private HorizontalSplitPanel finesSplitPanel = new HorizontalSplitPanel();
    private TransactionView transactionEditor = new TransactionView();
    private SignInView sign = new SignInView();
    private Home home = new Home();
    private SQL sql = new SQL();
    private TabSheet tabSheet = new TabSheet();
    private String u, p;
    private HorizontalSplitPanel usersSplitPanel = new HorizontalSplitPanel();
    private HorizontalSplitPanel bookSplitPanel = new HorizontalSplitPanel();
    private Grid<User> userGrid = new Grid<>(User.class);
    private Grid<Book> bookGrid = new Grid<>(Book.class);
    private Grid<Fine> fineGrid = new Grid<>(Fine.class);
    private Grid<Transaction> transactionGrid = new Grid<>(Transaction.class);
    private Grid<DetailFine> detailFineGrid = new Grid<>(DetailFine.class);
    private boolean currentState = false;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        initializeSQL();
        sign.signinButton.addClickListener((Button.ClickListener) clickEvent -> {
            p = sign.passwordField.getValue();
            u = sign.username.getValue();
            if (sign.isCred(u,p)) {
                tabSheet.setSizeFull();
                sign.setVisible(false);
                userEditor.refresh();
                bookEditor.refresh();
                transactionEditor.refresh();
                detailFineView.refresh(3769891);
                fineView.refresh();
                createUserPanel();
                createBookPanel();
                createTransactionPanel();
                createSplitPanels();
                createFinePanel();
                createDetailFinePanel();
                home.setCaption("Home");
                transactionGrid.setCaption("Transactions");
                tabSheet.addComponent(home);
                tabSheet.addComponent(usersSplitPanel);
                tabSheet.addComponent(bookSplitPanel);
                tabSheet.addComponent(transactionGrid);
                tabSheet.addComponent(finesSplitPanel);
                setContent(tabSheet);
            }
            else {
                sign.signInError.setVisible(true);
            }

        });
    }
    private void createSplitPanels () {
        bookSplitPanel.setFirstComponent(bookGrid);
        bookSplitPanel.setSecondComponent(bookEditor);
        bookSplitPanel.setCaption("Books");
        usersSplitPanel.setFirstComponent(userGrid);
        usersSplitPanel.setSecondComponent(userEditor);
        usersSplitPanel.setCaption("User Data");
        finesSplitPanel.setSizeFull();
        finesSplitPanel.setFirstComponent(fineGrid);
        finesSplitPanel.setSecondComponent(detailFineGrid);
        finesSplitPanel.setCaption("Fines");
    }
    private void initializeSQL() {
        userEditor.delete.setVisible(currentState);
        userEditor.cancel.setVisible(currentState);
        fineView.setGrid(fineGrid);
        userEditor.setGrid(userGrid);
        bookEditor.setGrid(bookGrid);
        transactionEditor.setGrid(transactionGrid);
        bookEditor.setUserFormView(userEditor);
        bookEditor.setTransactionView(transactionEditor);
        sql.connect(SQL.Database.USERS);
        bookEditor.setSql(sql);
        transactionEditor.setSql(sql);
        userEditor.setSQL(sql);
        fineView.setView(detailFineView);
        fineView.setSql(sql);
        detailFineView.setDetailFineGrid(detailFineGrid);
        setContent(sign);
    }
    private void createUserPanel () {
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
    }
    private void createTransactionPanel() {
        transactionGrid.setSizeFull();
        transactionGrid.removeAllColumns();
        transactionGrid.addColumn(Transaction::getUserId).setCaption("USER ID");
        transactionGrid.addColumn(Transaction::getBookId).setCaption("BOOK ID");
        transactionGrid.addColumn(Transaction::getDtr).setCaption("DAYS TILL RETURN");
    }
    private void createFinePanel() {
        fineGrid.setSizeFull();
        fineGrid.removeAllColumns();
        fineGrid.addColumn(Fine::getUserId).setCaption("User");
        fineGrid.addColumn(Fine::getFine).setCaption("Current Fine");
        fineGrid.asSingleSelect();
        fineGrid.addItemClickListener(selectionEvent -> {
            detailFineView.refresh(Integer.parseInt(selectionEvent.getItem().getUserId()));
        });
    }
    private void createDetailFinePanel() {
        detailFineGrid.setSizeFull();
        detailFineGrid.removeAllColumns();
        detailFineGrid.addColumn(DetailFine::getBookNames).setCaption("Book Name");
        detailFineGrid.addColumn(DetailFine::getDaysLate).setCaption("Days Late");
        detailFineGrid.addColumn(DetailFine::getFines).setCaption("Fines");
    }
    private void createBookPanel () {
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
            bookEditor.add.setVisible(true);
            bookEditor.checkedOut.setVisible(true);
        });
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {}
}
