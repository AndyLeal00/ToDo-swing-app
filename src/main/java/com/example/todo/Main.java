package com.example.todo;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicacion.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoAppFrame().setVisible(true));
    }
}
