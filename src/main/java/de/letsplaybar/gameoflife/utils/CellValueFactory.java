package de.letsplaybar.gameoflife.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;

public class CellValueFactory implements Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>> {

    private int x;
    private int amount;
    private int width;
    public CellValueFactory(int x, int width){
        this.x = x;
        amount = 0;
        this.width = width;
    }

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> data) {
        List<String> list = data.getTableView().getItems().get(amount);
        try {
            return new SimpleStringProperty(list.get(x));
        }finally {
            amount = amount+1;
            if (amount>=width){
                amount = 0;
            }
        }

    }
}
