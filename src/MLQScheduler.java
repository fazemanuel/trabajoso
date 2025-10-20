import java.util.*;

public class MLQScheduler {
    private ProcessQueue queue1; // RR(1)
    private ProcessQueue queue2; // RR(3)
    private ProcessQueue queue3; // SJF
    private int currentTime;
    private List<Process> allProcesses;
    private List<Process> completedProcesses;

    public MLQScheduler() {
        queue1 = new ProcessQueue("RR", 1);
        queue2 = new ProcessQueue("RR", 3);
        queue3 = new ProcessQueue("SJF", 0);
        currentTime = 0;
        allProcesses = new ArrayList<>();
        completedProcesses = new ArrayList<>();
    }

    public void addProcess(Process process) {
        allProcesses.add(process);
    }

    public void run() {
        System.out.println("=== Iniciando simulación MLQ ===");
        System.out.println("Cola 1: RR(q=1), Cola 2: RR(q=3), Cola 3: SJF\n");

        // Ordenar procesos por tiempo de llegada
        allProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        while (completedProcesses.size() < allProcesses.size()) {
            // Agregar procesos que han llegado a sus colas correspondientes
            addArrivingProcessesToQueues();

            // Ejecutar el siguiente proceso según prioridad de colas
            if (!queue1.isEmpty()) {
                executeProcess(queue1);
            } else if (!queue2.isEmpty()) {
                executeProcess(queue2);
            } else if (!queue3.isEmpty()) {
                executeProcess(queue3);
            } else {
                // No hay procesos listos, avanzar tiempo
                currentTime++;
            }
        }

        System.out.println("\n=== Simulación completada ===");
        calculateMetrics();
    }

    private void addArrivingProcessesToQueues() {
        for (Process p : allProcesses) {
            if (p.getArrivalTime() <= currentTime && !p.isCompleted() &&
                    !isInAnyQueue(p) && !completedProcesses.contains(p)) {

                switch (p.getQueue()) {
                    case 1:
                        queue1.addProcess(p);
                        break;
                    case 2:
                        queue2.addProcess(p);
                        break;
                    case 3:
                        queue3.addProcess(p);
                        break;
                }
            }
        }
    }

    private boolean isInAnyQueue(Process process) {
        return queue1.getAllProcesses().contains(process) ||
                queue2.getAllProcesses().contains(process) ||
                queue3.getAllProcesses().contains(process);
    }

    private void executeProcess(ProcessQueue queue) {
        Process currentProcess = queue.getNextProcess();
        if (currentProcess == null) return;

        // Establecer tiempo de respuesta la primera vez que se ejecuta
        if (!currentProcess.hasStarted()) {
            currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setStarted(true);
        }

        int executionTime;
        String schedulerType = queue.getSchedulerType();

        if (schedulerType.equals("SJF")) {
            // SJF: ejecutar hasta completar
            executionTime = currentProcess.getRemainingTime();
        } else {
            // RR: ejecutar por quantum o hasta completar
            executionTime = Math.min(queue.getQuantum(), currentProcess.getRemainingTime());
        }

        System.out.printf("Tiempo %d: Ejecutando proceso %s (Cola %d, %s) por %d unidades\n",
                currentTime, currentProcess.getId(), currentProcess.getQueue(),
                schedulerType, executionTime);

        // Ejecutar el proceso
        currentProcess.decrementRemainingTime(executionTime);
        currentTime += executionTime;

        // Verificar si el proceso ha terminado
        if (currentProcess.isCompleted()) {
            currentProcess.setCompletionTime(currentTime);
            currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

            queue.removeProcess(currentProcess);
            completedProcesses.add(currentProcess);

            System.out.printf("  -> Proceso %s completado en tiempo %d\n",
                    currentProcess.getId(), currentTime);
        } else if (schedulerType.equals("RR")) {
            // Si es Round Robin y no terminó, mover al final de la cola
            queue.removeProcess(currentProcess);
            queue.addProcess(currentProcess);
            System.out.printf("  -> Proceso %s regresa al final de la cola (restante: %d)\n",
                    currentProcess.getId(), currentProcess.getRemainingTime());
        }
    }

    private void calculateMetrics() {
        double avgWT = 0, avgCT = 0, avgRT = 0, avgTAT = 0;

        for (Process p : completedProcesses) {
            avgWT += p.getWaitingTime();
            avgCT += p.getCompletionTime();
            avgRT += p.getResponseTime();
            avgTAT += p.getTurnaroundTime();
        }

        int n = completedProcesses.size();
        avgWT /= n;
        avgCT /= n;
        avgRT /= n;
        avgTAT /= n;

        System.out.println("\n=== Métricas Promedio ===");
        System.out.printf("WT=%.2f; CT=%.2f; RT=%.2f; TAT=%.2f\n", avgWT, avgCT, avgRT, avgTAT);
    }

    public List<Process> getCompletedProcesses() {
        return completedProcesses;
    }
}