package my.bak.trafic.view;

import javafx.scene.control.ListCell;
import my.bak.trafic.core.plugin.loader.PluginInfo;

/**
 * Cell factory used for show of plugin name
 *
 * @author Petr Lastovka
 */
public class PluginCellFactory extends ListCell<PluginInfo> {

    @Override
    public void updateItem(PluginInfo item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getName() + " " + "(" + item.getFlag().name() + ")");
        }
    }
}
