package model;

public class Task {
    private Long taskId;
    private String taskTitle,subject,taskType,dueDate,status;
    private int estimatedHours;

    public Task
            (Long taskId,String taskTitle,String subject,
             String taskType,String dueDate,int estimatedHours,
             String status) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.subject = subject;
        this.taskType = taskType;
        this.dueDate = dueDate;
        this.estimatedHours = estimatedHours;
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getSubject() {
        return subject;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass()
        != obj.getClass()) return false;

        Task other (Task) obj;

        return taskTitle.equals(other.taskTitle) &&
                subject.equals(other.subject) &&
                taskType.equals(other.taskType) &&
                dueDate.equals(other.dueDate) &&
                estimatedHours == other.estimatedHours &&
                status.equals(other.status);
    }

}
