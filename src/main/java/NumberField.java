import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextField;

public class NumberField extends TextField implements ValueChangeListener<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String lastValue;

    public NumberField() {
        setValueChangeMode(ValueChangeMode.EAGER);
        addValueChangeListener(this);
        lastValue = "";
    }

    @Override
    public void valueChange(ValueChangeEvent<String> event) {
        String text = (String) event.getValue();
        try {
            new Double(text);
            lastValue = text;
        } catch (NumberFormatException e) {
            setValue(lastValue);
        }

    }
}