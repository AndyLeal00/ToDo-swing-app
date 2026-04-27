package com.example.todo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * Ventana principal de la aplicacion.
 * Es una interfaz sencilla hecha con Java Swing para gestionar tareas en memoria.
 */
public class TodoAppFrame extends JFrame {
    private final JTextField titleField = new JTextField(20);
    private final JTextArea descriptionArea = new JTextArea(4, 20);
    private final JComboBox<TaskStatus> statusComboBox = new JComboBox<>(TaskStatus.values());
    private final JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"Todos", "Pendiente", "En progreso", "Completada"});

    private final TaskTableModel tableModel = new TaskTableModel();
    private final JTable taskTable = new JTable(tableModel);
    private final TableRowSorter<TaskTableModel> rowSorter = new TableRowSorter<>(tableModel);

    public TodoAppFrame() {
        setTitle("Lista de tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        taskTable.setRowSorter(rowSorter);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(createFormPanel(), BorderLayout.WEST);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nueva tarea"));
        formPanel.setPreferredSize(new Dimension(250, 0));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.weightx = 1;

        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Titulo:"), gbc);

        gbc.gridy = 1;
        fieldsPanel.add(titleField, gbc);

        gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Descripcion:"), gbc);

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        fieldsPanel.add(descriptionScroll, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        fieldsPanel.add(new JLabel("Estado:"), gbc);

        gbc.gridy = 5;
        fieldsPanel.add(statusComboBox, gbc);

        JButton addButton = new JButton("Agregar tarea");
        addButton.addActionListener(event -> addTask());

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(addButton, BorderLayout.SOUTH);
        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Tareas"));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrar por estado:"));
        filterComboBox.addActionListener(event -> applyFilter());
        filterPanel.add(filterComboBox);

        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton completeButton = new JButton("Terminar Tarea");
        completeButton.addActionListener(event -> markSelectedTaskAsCompleted());

        JButton deleteButton = new JButton("Eliminar tarea seleccionada");
        deleteButton.addActionListener(event -> deleteSelectedTask());

        buttonsPanel.add(completeButton);
        buttonsPanel.add(deleteButton);
        return buttonsPanel;
    }

    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El titulo es obligatorio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        tableModel.addTask(new Task(title, description, status));
        clearForm();
    }

    private void markSelectedTaskAsCompleted() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            showSelectionWarning();
            return;
        }

        tableModel.updateTaskStatus(modelRow, TaskStatus.COMPLETADA);
    }

    private void deleteSelectedTask() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            showSelectionWarning();
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar esta tarea?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            tableModel.removeTask(modelRow);
        }
    }

    private int getSelectedModelRow() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        return taskTable.convertRowIndexToModel(selectedRow);
    }

    private void applyFilter() {
        String selectedFilter = (String) filterComboBox.getSelectedItem();

        if ("Todos".equals(selectedFilter)) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter(selectedFilter, 2));
        }
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        statusComboBox.setSelectedIndex(0);
        titleField.requestFocus();
    }

    private void showSelectionWarning() {
        JOptionPane.showMessageDialog(
                this,
                "Debes seleccionar una tarea primero.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
