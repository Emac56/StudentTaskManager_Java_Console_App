# `controller` — TaskController.java

This package contains the **Controller** layer — the coordinator of the application. It doesn't contain business logic (that belongs to the Service) and it doesn't handle raw console input/output directly (that belongs to the View). Its job is to **connect the two**: read what the user wants from the View, ask the Service to do it, and send the result back to the View to display.

## Menu Flow

```java
public void start()
```

This is the main application loop. It runs continuously until the user chooses to exit:

1. Calls `consoleView.showMainMenu()` to display the menu and get the user's numeric choice.
2. Uses a `switch` statement to route that choice to the correct flow method (e.g. option `1` → `addTaskFlow()`).
3. Wraps the whole loop in a `try/catch` for `IOException`, so a file error doesn't crash the whole program — it's displayed as a message instead and the loop continues.
4. Choosing option `9` sets `run = false`, ending the loop and exiting the program.

| Menu Option | Method Called |
|---|---|
| 1 – Add Task | `addTaskFlow()` |
| 2 – View All Tasks | `viewAllTaskFlow()` |
| 3 – Search Task | `searchTaskFlow()` |
| 4 – Update Task | `updateTaskFlow()` |
| 5 – Mark Completed | `markCompletedFlow()` |
| 6 – Delete Task | `deleteTaskFlow()` |
| 7 – View Pending Tasks | `viewPendingFlow()` |
| 8 – View Completed Tasks | `viewCompletedFlow()` |
| 9 – Exit | Ends the loop |
| Anything else | Displays `"Invalid input!"` |

## Flow Methods

Each "flow" method follows the same three-step pattern: **get input from the View → call the Service → send the result back to the View.**

- **`addTaskFlow()`** – Reads a new task from the View, passes it to `taskService.addTask()`, and displays the resulting message (success or duplicate warning).
- **`viewAllTaskFlow()`** – Fetches all tasks from the Service and passes the list to the View for display.
- **`searchTaskFlow()`** – Reads a Task ID from the View, asks the Service to find it, and displays the single task (or a "not found" message).
- **`updateTaskFlow()`** – Reads a Task ID, then reads updated task details, attaches the ID to the updated object, and sends it to the Service to save.
- **`markCompletedFlow()`** – Reads a Task ID and tells the Service to change that task's status to Completed.
- **`deleteTaskFlow()`** – Reads a Task ID and tells the Service to remove that task.
- **`viewPendingFlow()`** / **`viewCompletedFlow()`** – Ask the Service for the filtered list and pass it to the View.

## Why the Controller Stays "Thin"

Notice that the Controller never checks *whether* a task is valid, never touches the file, and never decides what counts as a duplicate — it just calls the Service and trusts it to handle the rules. This is intentional: keeping the Controller thin means the same business logic in the Service could later be reused by a different front end (a GUI, a web API, etc.) without rewriting any rules.
