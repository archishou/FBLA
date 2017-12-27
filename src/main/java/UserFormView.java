


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
                    User user = new User(Integer.valueOf(id.getValue()), name.getValue(), Integer.valueOf(checkOutBooks.getValue()),
                            Integer.valueOf(limitOfBooks.getValue()), Integer.valueOf(idSchool.getValue()), userType.getValue());
                    System.out.println(user.getUserStatus());
                    sql.addUser(user);
                    refresh();
                    addPressed = false;
                    limitOfBooks.setReadOnly(true);
                    name.setReadOnly(true);
                    userType.setReadOnly(true);
                }
                else nSave.setVisible(true);
            }
            new Notification("Save Success. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
        });
        delete.addClickListener((Button.ClickListener) clickListener ->{
            sql.delete(id.getValue().replaceAll(",", ""), SQL.Table.USERS);
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
            new Notification("User Added. ", "",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
        });
    }

    protected void setSQL(SQL sql){
        this.sql = sql;
    }
    protected void setUser(User value) {
        binder.setBean(value);
    }
    private int getID(String i){
        return Integer.parseInt(i.replaceAll(",", ""));
    }
    protected void setGrid(Grid<User> userGrid){this.grid=userGrid;}
    public static User getUserById(int id){
        int idOfUser = 0;
        int index = 0;
        String status = "";
        for (Integer integer: sql.getIntegerList(SQL.Table.USERS, "id")){
            index++;
            if (integer == id) break;
            if (sql.getList(SQL.Table.USERS, "teacherYN").get(index).toString().equals("true")) status = "TEACHER";
            else status = "STUDENT";
        }
        return new User(String.valueOf(idOfUser),
                sql.getList(SQL.Table.USERS, "name").get(index).toString(),
                sql.getList(SQL.Table.USERS, "numbooks").get(index).toString(),
                sql.getList(SQL.Table.USERS, "bookLim").get(index).toString(),
                sql.getList(SQL.Table.USERS, "schoolid").get(index).toString(),
                status);
    }
    private int genID (){
        return sql.genID(SQL.Table.USERS) + 2;
    }
    public void refresh() {
        List<User> users = new ArrayList<>();
        int loopIteration = 0;
        String status;
        while (loopIteration < sql.getList(SQL.Table.USERS, "id").size()) {
            if (sql.getList(SQL.Table.USERS, "teacherYN").get(loopIteration).toString().equals("1")) status = "TEACHER";
            else status = "STUDENT";
            users.add(new User(sql.getList(SQL.Table.USERS, "id").get(loopIteration).toString(),
                               sql.getList(SQL.Table.USERS, "name").get(loopIteration).toString(),
                               sql.getList(SQL.Table.USERS, "numbooks").get(loopIteration).toString(),
                               sql.getList(SQL.Table.USERS, "bookLim").get(loopIteration).toString(),
                               sql.getList(SQL.Table.USERS, "schoolid").get(loopIteration).toString(),
                               status));
            loopIteration++;
        }
        grid.setItems(users);
        users = null;
    }


}
