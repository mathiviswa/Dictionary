package com.wuhan.dict.common.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.util.Arrays;
import java.util.Vector;

// Custom scroll panel for displaying logs in a table
public class LogScrollPanel extends JScrollPane {
    private DicTableModel tableModel = null;

    // Constructor
    public LogScrollPanel() {
        // Create column vector with default column names
        Vector<String> columnVector = new Vector(Arrays.asList("No", "Operate", "Result"));

        // Create table model with the column vector
        tableModel = new DicTableModel(columnVector, null);
        JTable table = new JTable(tableModel);

        // Set table column properties
        TableColumnModel tcm = table.getColumnModel();
        DefaultTableCellRenderer defaultRender = new DefaultTableCellRenderer();
        defaultRender.setHorizontalAlignment(JLabel.LEFT);
        int[] columnsWidth = {30, 80, 100};
        for (int i = 0; i < columnVector.size(); i++) {
            tcm.getColumn(i).setCellRenderer(defaultRender);
            if (i == 2) {
                tcm.getColumn(i).sizeWidthToFit();
            } else {
                tcm.getColumn(i).setMinWidth(columnsWidth[i]);
                tcm.getColumn(i).setMaxWidth(columnsWidth[i]);
                tcm.getColumn(i).setPreferredWidth(columnsWidth[i]);
            }
        }

        // Set vertical scroll bar policy and add table to viewport
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getViewport().add(table);
    }

    // Method to set table data
    public void setTableData(Vector<Vector<Object>> tableData) {
        tableModel.setTableData(tableData);
    }

    // Method to refresh table data
    public void refreshTableData() {
        tableModel.fireTableDataChanged();
    }
}
