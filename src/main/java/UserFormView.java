


import Models.User;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;

import java.util.ArrayList;
import java.util.List;

public class UserFormView extends UserForm {
    private static SQL sql;
    private Binder<User> binder = new Binder<>(User.class);
    private Grid<User> grid;
    private boolean addPressed = false;
    UserFormView() {
        binder.forField ( this.id )
                .withNullRepresentation ( "" )
                .bind ( User::getUserId, User::setUserId );
        binder.forField ( this.limitOfBooks )
                .withNullRepresentation ( "" )
                .withConverter ( new StringToIntegerConverter(0, "integers only" ) )
                .bind ( User::getLimitOfBooks, User::setLimitOfBooks );
        binder.forField ( this.checkOutBooks )
                .withNullRepresentation ( "" )
                .withConverter ( new StringToIntegerConverter(0, "integers only" ) )
                .bind ( User::getCheckedOutBooks, User::setCheckedOutBooks );
        binder.forField ( this.idSchool )
                .withNullRepresentation ( "" )
                .withConverter ( new StringToIntegerConverter(0, "integers only" ) )
                .bind ( User::getSchoolId, User::setSchoolId);
        binder.forField ( this.name )
                .withNullRepresentation ( "" )
                .bind ( User::getUserName, User::setUserName );
        binder.forField ( this.userType )
                .withNullRepresentation ( "" )
                .bind ( User::getUserStatus, User::setUserStatus );
        binder.bindInstanceFields(this);

        save.addClickListener((Button.ClickListener) clickListener ->{
            if (!addPressed) {
                sql.refresh(this);
                refresh();
            }
            else {
                if (!name.getValue().equals("") && !userType.getValue().equals("")) {
                    String status;
                    if (userType.getValue().toLowerCase().contains("s")) status = "false";
                    else status = "true";
                    System.out.println("STATUS: " + status);
                    sql.addUser(new User(Integer.valueOf(id.getValue()), name.getValue(), Integer.valueOf(checkOutBooks.getValue()),
                            Integer.valueOf(limitOfBooks.getValue()), Integer.valueOf(idSchool.getValue()), status));
                    refresh();
                    addPressed = false;
                    limitOfBooks.setReadOnly(true);
                    name.setReadOnly(true);
                    userType.setReadOnly(true);
                    new Notification("Save Success. ", "",
                            Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
                }
                else nSave.setVisible(true);
            }
        });
        delete.addClickListener((Button.ClickListener) clickListener ->{
            sql.deleteUser(id.getValue().replaceAll(",", ""));
            sql.refresh(this);
            refresh();
            nSave.setVisible(false);
            cancel.setVisible(false);
            delete.setVisible(false);
            add.setVisible(true);
            new Notification("Delete Successful. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
        });
        cancel.addClickListener((Button.ClickListener) clickListener -> {
            grid.deselectAll();
            nSave.setVisible(false);
            cancel.setVisible(false);
            delete.setVisible(false);
            add.setVisible(true);
            new Notification("Operation Canceled. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
        });
        add.addClickListener((Button.ClickListener) clickListener -> {
            id.setValue(String.valueOf(genID()));
            checkOutBooks.setValue("0");
            addPressed = true;
            userType.setReadOnly(false);
            userType.setValue("");
            name.setReadOnly(false);
            name.setValue("");
            limitOfBooks.setReadOnly(false);
            limitOfBooks.setValue("10");
            idSchool.setValue("58");
            nSave.setVisible(false);
        });
    }

    protected void setSQL(SQL sql){
        UserFormView.sql = sql;
    }
    protected void setUser(User value) {
        binder.setBean(value);
    }
    protected void setGrid(Grid<User> userGrid){this.grid=userGrid;}
    public static User getUserById(int id){
        int idOfUser = 0;
        int index = 0;
        String status = "";
        for (Integer integer: SQL.controller.userid){
            if (integer == id) break;
            index++;
            if (SQL.controller.teacherYN.get(index).toString().equals("true")) status = "TEACHER";
            else status = "STUDENT";
        }
        return new User(String.valueOf(idOfUser),
                SQL.controller.userName.get(index),
                SQL.controller.numbooks.get(index).toString(),
                SQL.controller.lim.get(index).toString(),
                SQL.controller.userSchoolId.get(index).toString(),
                status);
    }
    private int genID (){
        return sql.genIdUsers() + 2;
    }
    public void refresh() {
        List<User> users = new ArrayList<>();
        int loopIteration = 0;
        String status;
        while (loopIteration < SQL.controller.userid.size()) {
            System.out.println("INSIDE THE LIST: " + SQL.controller.teacherYN.get(loopIteration));
            if (SQL.controller.teacherYN.get(loopIteration).toString().toLowerCase().contains("r")) status = "TEACHER";
            else status = "STUDENT";
            System.out.println("REFRESH STATUS: " + status);
            users.add(new User(SQL.controller.userid.get(loopIteration).toString(),
                               SQL.controller.userName.get(loopIteration),
                               SQL.controller.numbooks.get(loopIteration).toString(),
                               SQL.controller.lim.get(loopIteration).toString(),
                               SQL.controller.userSchoolId.get(loopIteration).toString(),
                               status));
            loopIteration++;
        }
        grid.setItems(users);
        users = null;
    }
}
