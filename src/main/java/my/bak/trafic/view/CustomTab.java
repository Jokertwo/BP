package my.bak.trafic.view;

import javafx.scene.control.Tab;
import lombok.Getter;
import lombok.Setter;

public class CustomTab extends Tab {
    @Getter
    @Setter
    private TabController controller;

    public CustomTab() {
        super();
    }
}
