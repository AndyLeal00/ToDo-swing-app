package com.example.todo;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * Ventana principal de la aplicación To-Do.
 */
public class TodoAppFrame extends JFrame {
    private final JTextField titleField = new JTextField(25);
    private final JTextArea descriptionArea = new JTextArea(4, 25);
    private final JComboBox<TaskStatus> statusComboBox = new JComboBox<>(TaskStatus.values());
    private final JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"Todos", "Pendiente", "En progreso", "Completada"});

    private final TaskTableModel tableModel = new TaskTableModel();
    private final JTable taskTable = new JTable(tableModel);
    private final TableRowSorter<TaskTableModel> rowSorter = new TableRowSorter<>(tableModel);

    public TodoAppFrame() {
        setTitle("To-Do App - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        taskTable.setRowSorter(rowSorter);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setFillsViewportHeight(true);

        add(createFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registro de tareas"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Titulo:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Descripcion:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(statusComboBox, gbc);

        JButton addButton = new JButton("Agregar tarea");
        addButton.addActionListener(event -> addTask());

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton changeStatusButton = new JButton("Cambiar estado");
        JButton deleteButton = new JButton("Eliminar tarea seleccionada");

        changeStatusButton.addActionListener(event -> changeSelectedTaskStatus());
        deleteButton.addActionListener(event -> deleteSelectedTask());
        filterComboBox.addActionListener(event -> applyStatusFilter());

        panel.add(new JLabel("Filtro:"));
        panel.add(filterComboBox);
        panel.add(changeStatusButton);
        panel.add(deleteButton);

        return panel;
    }

    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El titulo de la tarea es obligatorio.",
                    "Error de validacion",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        tableModel.addTask(new Task(title, description, status));
        clearForm();
    }

    private void deleteSelectedTask() {
        int selectedViewRow = taskTable.getSelectedRow();

        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar una tarea para eliminar.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Seguro que deseas eliminar la tarea seleccionada?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            int selectedModelRow = taskTable.convertRowIndexToModel(selectedViewRow);
            tableModel.removeTask(selectedModelRow);
        }
    }

    private void changeSelectedTaskStatus() {
        int selectedViewRow = taskTable.getSelectedRow();

        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar una tarea para cambiar su estado.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        TaskStatus newStatus = (TaskStatus) JOptionPane.showInputDialog(
                this,
                "Selecciona el nuevo estado:",
                "Cambiar estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TaskStatus.values(),
                TaskStatus.COMPLETADA
        );

        if (newStatus != null) {
            int selectedModelRow = taskTable.convertRowIndexToModel(selectedViewRow);
            tableModel.updateTaskStatus(selectedModelRow, newStatus);
        }
    }

    private void applyStatusFilter() {
        String selectedFilter = (String) filterComboBox.getSelectedItem();

        if ("Todos".equals(selectedFilter)) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("^" + selectedFilter + "$", 2));
        }
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        statusComboBox.setSelectedItem(TaskStatus.PENDIENTE);
        titleField.requestFocus();
    }
}
