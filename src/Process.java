public class Process {
    private String id;
    private int burstTime;
    private int arrivalTime;
    private int queue;
    private int priority;
    private int remainingTime;
    private int waitingTime;
    private int completionTime;
    private int responseTime;
    private int turnaroundTime;
    private boolean hasStarted;

    public Process(String id, int burstTime, int arrivalTime, int queue, int priority) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.queue = queue;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.completionTime = 0;
        this.responseTime = -1;
        this.turnaroundTime = 0;
        this.hasStarted = false;
    }

    // Getters
    public String getId() { return id; }
    public int getBurstTime() { return burstTime; }
    public int getArrivalTime() { return arrivalTime; }
    public int getQueue() { return queue; }
    public int getPriority() { return priority; }
    public int getRemainingTime() { return remainingTime; }
    public int getWaitingTime() { return waitingTime; }
    public int getCompletionTime() { return completionTime; }
    public int getResponseTime() { return responseTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public boolean hasStarted() { return hasStarted; }

    // Setters
    public void setRemainingTime(int time) { this.remainingTime = time; }
    public void setWaitingTime(int time) { this.waitingTime = time; }
    public void setCompletionTime(int time) { this.completionTime = time; }
    public void setResponseTime(int time) { this.responseTime = time; }
    public void setTurnaroundTime(int time) { this.turnaroundTime = time; }
    public void setStarted(boolean started) { this.hasStarted = started; }

    public void decrementRemainingTime(int time) {
        this.remainingTime -= time;
    }

    public boolean isCompleted() {
        return remainingTime <= 0;
    }

    @Override
    public String toString() {
        return String.format("%s;%d;%d;%d;%d;%d;%d;%d;%d",
                id, burstTime, arrivalTime, queue, priority,
                waitingTime, completionTime, responseTime, turnaroundTime);
    }
}