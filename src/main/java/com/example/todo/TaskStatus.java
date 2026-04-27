package com.example.todo;

/**
 * Estados disponibles para una tarea.
 */
public enum TaskStatus {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En progreso"),
    COMPLETADA("Completada");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
