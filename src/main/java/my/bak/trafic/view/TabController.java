package my.bak.trafic.view;

import javafx.scene.Node;


public interface TabController {

    /**
     * Title of tab
     *
     * @return return title of tab which will be used in tab
     */
    String title();


    /**
     * Close tab
     *
     * @return return false if tab can not be closed
     */
    boolean close();


    /**
     * Return tab content
     *
     * @return content of tab
     */
    Node getContent();


    /**
     * Set tab to disable/enable state
     *
     * @param disable true for disable content
     */
    void setDisable(boolean disable);
}
