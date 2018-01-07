

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
