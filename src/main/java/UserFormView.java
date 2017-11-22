


import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

import java.util.List;

public class UserFormView extends UserForm {
    private int initId = 3769891;
    private SQL sql;
    private Binder<User> binder = new Binder<>(User.class);
    private static List<User> users;
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
                .bind ( User::getForirghschoolId, User::setForirghschoolId );
        binder.forField ( this.name )
                .withNullRepresentation ( "" )
                .bind ( User::getUserName, User::setUserName );
        binder.forField ( this.userType )
                .withNullRepresentation ( "" )
                .bind ( User::getUserStatus, User::setUserStatus );
        binder.bindInstanceFields(this);

        save.addClickListener((Button.ClickListener) clickListener ->{
            if (!addPressed) sql.refresh(this);
            else {
                if (!name.getValue().equals("") && !userType.getValue().equals("")) {
                    User user = new User(Integer.valueOf(id.getValue()), name.getValue(), Integer.valueOf(checkOutBooks.getValue()),
                            Integer.valueOf(limitOfBooks.getValue()), Integer.valueOf(idSchool.getValue()), userType.getValue());
                    users.add(user);
                    sql.addUser(user);
                    grid.setItems(users);
                    addPressed = false;
                    limitOfBooks.setReadOnly(true);
                    name.setReadOnly(true);
                    userType.setReadOnly(true);
                }
                else nSave.setVisible(true);
            }
        });
        delete.addClickListener((Button.ClickListener) clickListener ->{
            sql.delete(id.getValue().replaceAll(",", ""), SQL.Table.USERS);
            users.remove(getUserById(getID(id.getValue())));
            sql.refresh(this);
            grid.setItems(users);
            nSave.setVisible(false);
            cancel.setVisible(false);
            delete.setVisible(false);
            add.setVisible(true);
        });
        cancel.addClickListener((Button.ClickListener) clickListener -> {
            grid.setItems(users);
            nSave.setVisible(false);
            cancel.setVisible(false);
            delete.setVisible(false);
            add.setVisible(true);
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
        this.sql = sql;
    }
    protected void setUser(User value) {
        binder.setBean(value);
    }
    protected void setList(List<User> users){this.users = users;}
    private int getID(String i){
        return Integer.parseInt(i.replaceAll(",", ""));
    }
    protected void setGrid(Grid<User> userGrid){this.grid=userGrid;}
    public static User getUserById(int id){
        User getUser = null;
        for (User user: users){
            if (Integer.parseInt(user.getUserId()) == id)
                getUser = user;
        }
        return getUser;
    }
    private int genID (){
        return sql.genID(SQL.Table.USERS) + 2;
    }


}
