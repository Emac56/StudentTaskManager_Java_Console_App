# `view` — ConsoleView.java

This package contains the **View** layer — everything the user actually sees and types. It has no business logic and never touches the file system directly. Its only job is formatting output and collecting raw input, then handing that input up to the Controller.

## Responsibilities

- Displaying the main menu
- Prompting the user for task details
- Reading and returning raw user input
- Formatting and printing tasks to the console
- Displaying success/error messages

## Method Breakdown

### `showMainMenu()`
```java
public int showMainMenu()
```
Prints the menu banner and all 9 numbered options, then reads and returns the user's menu choice as an `int`. Also consumes the leftover newline character after `scanner.nextInt()` so the next `nextLine()` call (used elsewhere) works correctly.

### `readTask()`
```java
public Task readTask() throws IOException
```
Prompts the user, in order, for:
1. Task Title
2. Subject
3. Task Type
4. Due Date
5. Estimated Hours

It then builds and returns a new `Task` object with `taskId` and `status` left as `null` — since assigning those values is the Service layer's responsibility, not the View's.

### `readTaskId()`
```java
public Long readTaskId()
```
Prompts for a Task ID and returns it as a `Long`. Used by Search, Update, Delete, and Mark Completed flows.

### `displayTask(Task task)`
```java
public void displayTask(Task task)
```
Prints a single task's full details in a formatted block. If the task is `null` (not found), it prints `"Task not found"` instead.

### `displayTasks(List<Task> taskList)`
```java
public void displayTasks(List<Task> taskList)
```
Prints every task in a list, each formatted the same way as `displayTask()`. If the list is empty, it prints `"No Tasks Found"` instead of an empty block.

### `displayMessage(String message)`
```java
public void displayMessage(String message)
```
Prints any general-purpose message — used for success confirmations (`"Task added successfully."`), errors, and the exit message.

## Input/Output Design Notes

- All console output goes through `System.out` (imported statically as `out`), keeping print statements short and consistent.
- All input parsing (like converting a typed number into a `Long` or `int`) happens **in the View**, so the Controller and Service only ever work with already-typed Java objects (`Task`, `Long`), never raw strings from the console.
- The View intentionally does not validate business rules (e.g. it won't reject a duplicate task) — it only makes sure input is in the right basic format. Business validation is the Service layer's job.
