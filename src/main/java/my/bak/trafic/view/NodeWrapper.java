package my.bak.trafic.view;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;


public class NodeWrapper {

    private DateTimePicker picker;
    private TextField textField;


    public NodeWrapper(TextField textField) {
        checkValue(textField);
        this.textField = textField;
    }


    public NodeWrapper(DateTimePicker picker) {
        checkValue(picker);
        this.picker = picker;
    }


    private void checkValue(Object value) {
        if (Objects.isNull(value)) {
            throw new IllegalStateException("Can not set null Node to NodeWrapper");
        }
    }


    public Object getValue() {
        return picker != null ? Date.from(picker.getDateTimeValue().atZone(ZoneId.systemDefault()).toInstant())
                : textField.getText().trim();
    }


    public Node getNode() {
        return picker != null ? picker : textField;
    }


    @Override
    public String toString() {
        String value;
        if (picker != null) {
            value = "DateTimePicker";
        } else {
            value = "TextField";
        }
        return "NodeWrapper [component = " + value + "]";
    }

}
