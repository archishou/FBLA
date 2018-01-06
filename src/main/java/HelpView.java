import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class HelpView extends HelpDesighn {
    public HelpView () {
        desc.setSizeFull();
        desc.setValue(strings.users);
        user.addClickListener(clickEvent -> {
           desc.setValue(strings.users);
        });
        book.addClickListener(clickEvent -> {
            desc.setValue(strings.books);
        });
        report.addClickListener(clickEvent -> {
            desc.setValue(strings.reports);
        });
    }
}
