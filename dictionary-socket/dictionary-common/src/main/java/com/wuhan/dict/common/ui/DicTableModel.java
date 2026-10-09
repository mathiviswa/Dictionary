package com.wuhan.dict.common.ui;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

// Custom table model for displaying dictionary data
public class DicTableModel extends AbstractTableModel {
    private Vector<String> columnVector; // Stores column names
    private Vector<Vector<Object>> tableData = new Vector<>(); // Stores table data

    // Constructor
    public DicTableModel(Vector<String> columnVector, Vector<Vector<Object>> dataVector) {
        this.columnVector = columnVector;
        this.tableData = dataVector;
    }

    // Method to get the number of rows in the table
    @Override
    public int getRowCount() {
        if (tableData == null) {
            return 0;
        }
        return tableData.size();
    }

    // Method to get the name of a specific column
    @Override
    public String getColumnName(int column) {
        return columnVector.get(column);
    }

    // Method to get the number of columns in the table
    @Override
    public int getColumnCount() {
        return columnVector.size();
    }

    // Method to get the value at a specific cell in the table
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (tableData != null && tableData.size() > 0) {
            return tableData.get(rowIndex).get(columnIndex);
        } else {
            return null;
        }
    }

    // Method to set the table data and notify listeners
    public void setTableData(Vector<Vector<Object>> tableData) {
        this.tableData = tableData;
        fireTableDataChanged();
    }
}
