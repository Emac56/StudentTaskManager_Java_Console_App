# `model` — Task.java

This package contains the **Model** layer: the plain data structure that represents a single task in the system. It holds no business logic and does not know about the console, the file, or any other layer — it simply describes what a task *is*.

## `Task` Class

### Fields

| Field | Type | Description |
|---|---|---|
| `taskId` | `Long` | Unique identifier for the task. Assigned by the Service layer, not set by the user directly. |
| `taskTitle` | `String` | The name/title of the task (e.g. "Finish Lab Report"). |
| `subject` | `String` | The subject or course the task belongs to (e.g. "Programming Java"). |
| `taskType` | `String` | The category of the task (e.g. "Assignment", "Project", "Quiz"). |
| `dueDate` | `String` | The date the task is due. |
| `estimatedHours` | `int` | Estimated hours needed to complete the task. |
| `status` | `String` | Current state of the task — `"Pending"` or `"Completed"`. |

### Constructor

```java
public Task(Long taskId, String taskTitle, String subject,
            String taskType, String dueDate, int estimatedHours,
            String status)
```

Builds a fully-formed `Task` object. When a new task is first created through the console (before it's saved), `taskId` and `status` are passed in as `null` — the **Service** layer is responsible for assigning the real ID and default status (`"Pending"`) before saving.

### Getters / Setters

Every field has a standard getter (`getTaskId()`, `getTaskTitle()`, etc.) and setter (`setTaskId()`, `setStatus()`, etc.). These are used by the Repository (to read data for writing to the file) and the Service (to update fields like `status` when a task is marked completed).

### `equals()` Method

```java
@Override
public boolean equals(Object obj)
```

Two `Task` objects are considered equal if they have the same **title, subject, task type, due date, and estimated hours**. Note that `taskId` and `status` are **intentionally excluded** from this comparison.

This is a deliberate design choice: it lets the `TaskService` detect duplicate task entries (same task submitted twice) *before* an ID has even been generated, since a brand-new task naturally won't have an ID yet.

> **Note:** `hashCode()` is not overridden alongside `equals()` in this version. This means `Task` objects should not currently be used safely inside hash-based collections (like `HashSet` or as `HashMap` keys) — this only affects internal data structure usage, not the CRUD features exposed to the user.
