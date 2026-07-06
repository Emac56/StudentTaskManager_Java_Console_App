# `service` — TaskService.java

This package contains the **Service** layer — the business logic layer of the application. It sits between the Controller and the Repository, and it's where all the *rules* of the system live. The Repository only knows how to read and write files; the Service decides *what should happen* before that reading or writing occurs.

## Responsibilities

- Enforcing rules (e.g. no duplicate tasks)
- Generating unique Task IDs
- Managing task status transitions (Pending → Completed)
- Filtering tasks by status
- Returning clear success/failure messages back to the Controller

## Method Breakdown

### `addTask(Task task)`
```java
public String addTask(Task task) throws IOException
```
1. Loads the current task list.
2. Checks if an equivalent task already exists (using `Task.equals()`, which compares title, subject, type, due date, and hours).
3. If it's a duplicate, returns `"Task already exist."` without saving.
4. Otherwise, generates a new task ID, sets the status to `"Pending"`, saves the task, and returns `"Task added successfully."`

This is the core validation rule of the system: **a task is only rejected if it duplicates another task's identifying details** — not based on ID, since a new task doesn't have one yet.

### `viewAllTask()` / `getAllTasks()`
```java
public List<Task> viewAllTask() throws IOException
public List<Task> getAllTasks() throws IOException
```
Both simply return the full task list from the Repository. (`getAllTasks()` is the version currently used by the Controller for the "View All Tasks" menu option.)

### `searchTask(Long taskId)`
```java
public Task searchTask(Long taskId) throws IOException
```
Delegates to `TaskRepository.findTaskById()` and returns the matching task, or `null` if not found.

### `updateTask(Task updatedTask)`
```java
public String updateTask(Task updatedTask) throws IOException
```
Confirms the task exists (by ID) before attempting an update. Returns `"Task updated successfully."` on success, or `"Task not found."` if the ID doesn't match any existing task — preventing the Repository from writing an update for a task that doesn't exist.

### `deleteTask(Long taskId)`
```java
public String deleteTask(Long taskId) throws IOException
```
Same pattern as `updateTask()` — confirms the task exists first, then deletes it. Returns `"Task deleted Successfully."` or `"Task not found."`

### `markTaskCompleted(Long taskId)`
```java
public String markTaskCompleted(Long taskId) throws IOException
```
Finds the task, changes its `status` field to `"Completed"`, and saves the change via `updateTask()`. Returns `"Task not found"` if no matching task exists.

### `viewPendingTask()` / `viewCompletedTask()`
```java
public List<Task> viewPendingTask() throws IOException
public List<Task> viewCompletedTask() throws IOException
```
Loads all tasks and filters them by status (case-insensitive comparison), returning only tasks marked `"Pending"` or `"Completed"` respectively. This powers the two filter features in the menu.

### `generateTaskId()`
```java
public Long generateTaskId() throws IOException
```
Looks through all existing tasks, finds the highest current ID, and returns `lastId + 1`. This gives each new task a unique, auto-incrementing ID without needing a database sequence.

## Task Status Rules

| Status | Meaning |
|---|---|
| `Pending` | Default status assigned automatically when a task is created. |
| `Completed` | Set only through the "Mark Completed" action — never set directly by the user during creation. |

This keeps status changes controlled and predictable: a task can never be created as already "Completed" by mistake.
