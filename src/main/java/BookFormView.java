import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.TextField;

public class BookFormView extends BookForm {
    private Binder<Book> binder = new Binder<>(Book.class);
    public BookFormView () {
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
    }
}
