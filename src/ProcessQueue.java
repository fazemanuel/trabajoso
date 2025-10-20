import java.util.*;

public class ProcessQueue {
    private List<Process> processes;
    private String schedulerType;
    private int quantum;

    public ProcessQueue(String schedulerType, int quantum) {
        this.processes = new ArrayList<>();
        this.schedulerType = schedulerType;
        this.quantum = quantum;
    }

    public void addProcess(Process process) {
        processes.add(process);
        // Ordenar por prioridad (mayor prioridad primero)
        processes.sort((p1, p2) -> Integer.compare(p2.getPriority(), p1.getPriority()));
    }

    public boolean isEmpty() {
        return processes.isEmpty();
    }

    public int size() {
        return processes.size();
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public int getQuantum() {
        return quantum;
    }

    public Process getNextProcess() {
        if (processes.isEmpty()) return null;

        if (schedulerType.equals("SJF")) {
            // Shortest Job First: proceso con menor tiempo restante
            Process shortest = processes.get(0);
            for (Process p : processes) {
                if (p.getRemainingTime() < shortest.getRemainingTime()) {
                    shortest = p;
                }
            }
            return shortest;
        } else {
            // RR y FCFS: primer proceso de la cola
            return processes.get(0);
        }
    }

    public void removeProcess(Process process) {
        processes.remove(process);
    }

    public List<Process> getAllProcesses() {
        return new ArrayList<>(processes);
    }
}