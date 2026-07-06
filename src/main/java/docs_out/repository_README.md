# `repository` — TaskRepository.java

This package contains the **Repository** layer: the only part of the application allowed to talk directly to the file system. It reads and writes the `tasks.txt` file, and converts between raw text lines and `Task` objects.

No business logic lives here — the Repository doesn't know what a "duplicate task" is or what "Pending" means; it just persists whatever `Task` objects it's given, and hands back whatever it finds.

## Setup (Constructor)

```java
public TaskRepository(File databaseFile) throws IOException
```

When a `TaskRepository` is created, it:
1. Stores the reference to the database file (`database/tasks.txt`).
2. Creates the parent folder (`database/`) if it doesn't already exist.
3. Creates the file itself if it doesn't already exist.

This means the application can run for the very first time on a fresh machine with no manual setup — the storage file is created automatically.

## File Handling Logic

Every task is stored as one line of text, with fields separated by a pipe character (`|`):

```
taskId|taskTitle|subject|taskType|dueDate|estimatedHours|status
```

Reading a task means splitting a line on `|` and rebuilding a `Task` object. Writing a task means joining its fields back together with `|` and appending a newline.

## CRUD Operations

### `loadTask()`
```java
public List<Task> loadTask() throws IOException
```
Reads every line in `tasks.txt`, parses each one into a `Task` object, and returns the full list. This is the foundation method — nearly every other method in the Repository (and Service) calls `loadTask()` first to get the current state of the data.

### `saveTask(Task task)`
```java
public void saveTask(Task task) throws IOException
```
Appends a single new task to the end of the file. Used when adding a brand-new task.

### `saveAllTask(List<Task> taskList)`
```java
public void saveAllTask(List<Task> taskList) throws IOException
```
**Overwrites** the entire file with the given list of tasks, one per line. Since a text file has no concept of "updating a row in place," this is the mechanism used to apply updates and deletions — the file is rebuilt from scratch with the corrected list.

### `updateTask(Task updatedTask)`
```java
public void updateTask(Task updatedTask) throws IOException
```
Loads all tasks, finds the one whose ID matches `updatedTask`, replaces it in the list, and calls `saveAllTask()` to rewrite the file with the change applied.

### `deleteTask(Long taskId)`
```java
public void deleteTask(Long taskId) throws IOException
```
Loads all tasks, removes the one matching the given ID, and calls `saveAllTask()` to persist the file without that task.

### `findTaskById(Long taskId)`
```java
public Task findTaskById(Long taskId) throws IOException
```
Loads all tasks and searches for the one matching the given ID, returning `null` if no match is found. Used by the Service layer to support Search, Update, Delete, and Mark Completed.

## Design Note

Because there's no database engine handling row-level updates, this Repository takes a simple **"load everything, modify in memory, save everything"** approach for updates and deletes. It's not the most efficient method for a huge dataset, but it's straightforward, easy to reason about, and appropriate for a learning project or a small personal task list.
