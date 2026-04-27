package com.example.todo;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de tabla encargado de administrar las tareas en memoria.
 */
public class TaskTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Titulo", "Descripcion", "Estado"};
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
        int newRowIndex = tasks.size() - 1;
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void removeTask(int rowIndex) {
        tasks.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Task getTaskAt(int rowIndex) {
        return tasks.get(rowIndex);
    }

    public void updateTaskStatus(int rowIndex, TaskStatus newStatus) {
        tasks.get(rowIndex).setStatus(newStatus);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return task.getTitle();
            case 1:
                return task.getDescription();
            case 2:
                return task.getStatus();
            default:
                return null;
        }
    }
}
