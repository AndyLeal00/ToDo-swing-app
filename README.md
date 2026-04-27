# To-Do App - Java Swing

Aplicación de escritorio desarrollada en Java Swing para gestionar una lista de tareas en memoria.

## Requisitos

- Java JDK 8 o superior.
- No requiere base de datos.
- No requiere librerías externas.

## Cómo ejecutar el proyecto

### Opción 1: Desde consola

Ubícate en la carpeta raíz del proyecto y ejecuta:

```bash
javac -d out src/main/java/com/example/todo/*.java
java -cp out com.example.todo.Main
```

### Opción 2: Desde un IDE

Puedes abrir el proyecto en IntelliJ IDEA, Eclipse o NetBeans.

Ejecuta la clase principal:

```text
com.example.todo.Main
```

## Funcionalidades completadas

- Registro de tareas con:
  - Título obligatorio.
  - Descripción opcional.
  - Estado: Pendiente, En progreso o Completada.
- Validación para no permitir tareas sin título.
- Mensajes de error y advertencia usando `JOptionPane`.
- Visualización de tareas en una tabla `JTable`.
- Eliminación de tarea seleccionada.
- Confirmación antes de eliminar una tarea.
- Cambio de estado de la tarea seleccionada.
- Filtro por estado.
- Código separado por responsabilidades:
  - `Task`: modelo de datos.
  - `TaskStatus`: estados disponibles.
  - `TaskTableModel`: modelo para la tabla.
  - `TodoAppFrame`: interfaz gráfica y lógica de interacción.
  - `Main`: punto de entrada.

## Decisiones tomadas

- Los datos se almacenan únicamente en memoria usando una lista dentro del `TaskTableModel`.
- Se utilizó `JTable` porque permite visualizar mejor los campos de cada tarea.
- Se creó un `TableRowSorter` para implementar el filtro por estado sin modificar la lista original.
- Se separó el código en varias clases para mantenerlo limpio, legible y fácil de mantener.
- La acción "Cambiar estado" permite seleccionar el nuevo estado mediante un `JOptionPane`.

## Limitaciones

- Las tareas se pierden al cerrar la aplicación, ya que el alcance del ejercicio no requiere base de datos ni persistencia en archivos.
