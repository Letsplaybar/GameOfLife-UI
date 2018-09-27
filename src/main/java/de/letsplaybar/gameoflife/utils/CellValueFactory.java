package de.letsplaybar.gameoflife.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;

public class CellValueFactory implements Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>> {

    private int x;
    public CellValueFactory(int x){
        this.x = x;
    }

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> data) {
        List<String> list = data.getValue();
            return new SimpleStringProperty(list.get(x));

    }
}
