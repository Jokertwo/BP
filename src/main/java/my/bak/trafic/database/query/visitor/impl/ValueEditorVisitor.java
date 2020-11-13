package my.bak.trafic.database.query.visitor.impl;

import javafx.scene.control.TextField;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.view.NodeWrapper;
import tornadofx.control.DateTimePicker;


public class ValueEditorVisitor implements Visitor<NodeWrapper> {

    @Override
    public NodeWrapper accept(EndColumn column) {
        return new NodeWrapper(new DateTimePicker());
    }


    @Override
    public NodeWrapper accept(BeginColumn column) {
        return new NodeWrapper(new DateTimePicker());
    }


    @Override
    public NodeWrapper accept(DirectionColumn column) {
        return new NodeWrapper(new TextField());
    }


    @Override
    public NodeWrapper accept(ValueColumn column) {
        return new NodeWrapper(new TextField());
    }


    @Override
    public NodeWrapper accept(PlaceColumn column) {
        return new NodeWrapper(new TextField());
    }


    @Override
    public NodeWrapper accept(TypeColumn column) {
        return new NodeWrapper(new TextField());
    }

}
