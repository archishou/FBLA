
import javax.servlet.annotation.WebServlet;
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
    UserFormView userEditor = new UserFormView();
    SignInView sign = new SignInView();
    private SQL sql = new SQL();
    private TabSheet tabSheet = new TabSheet();
    private SQLController sqlController;
    String u, p;
    boolean credTrue;
    private List<User> users = new ArrayList<>();
    HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
    Grid<User> userGrid = new Grid<>(User.class);
    boolean currentState = false;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        sqlController = sql.sqlController;
        userEditor.delete.setVisible(currentState);
        userEditor.cancel.setVisible(currentState);
        userEditor.setGrid(userGrid);
        userEditor.setList(users);
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
                addUsers();
                userGrid.setItems(users);
                userGrid.setSizeFull();
                userEditor.setSizeFull();
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

                splitPanel.setFirstComponent(userGrid);
                splitPanel.setSecondComponent(userEditor);
                splitPanel.setCaption("User Data");
                tabSheet.addComponent(splitPanel);
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
        int i = sqlController.userId.size();
        int loopIteration = 0;
        String t;
        while (loopIteration < i){
            if (sqlController.userStatus.get(loopIteration)) t = "TEACHER";
            else t = "STUDENT";
            users.add(new User(sqlController.userId.get(loopIteration),sqlController.userName.get(loopIteration),sqlController.checkedOutBooks.get(loopIteration),
                    sqlController.limitOfBooks.get(loopIteration),sqlController.forirghschoolId.get(loopIteration),t));
            loopIteration++;
        }

    }
}
