package my.bak.trafic.view;

import javafx.scene.Node;


public interface Setting {

    /**
     * Save changes
     */
    void save();


    /**
     * Return setting content
     *
     * @return
     */
    Node getContent();
}
