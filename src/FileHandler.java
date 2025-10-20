import java.io.*;
import java.util.*;

public class FileHandler {
    public static List<Process> readInputFile(String filename) throws IOException {
        List<Process> processes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split(";");
            if (parts.length >= 5) {
                String id = parts[0].trim();
                int burstTime = Integer.parseInt(parts[1].trim());
                int arrivalTime = Integer.parseInt(parts[2].trim());
                int queue = Integer.parseInt(parts[3].trim());
                int priority = Integer.parseInt(parts[4].trim());

                processes.add(new Process(id, burstTime, arrivalTime, queue, priority));
            }
        }
        reader.close();
        return processes;
    }

    public static void writeOutputFile(String filename, List<Process> processes) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        writer.write("# etiqueta; BT; AT; Q; Pr; WT; CT; RT; TAT\n");

        double avgWT = 0, avgCT = 0, avgRT = 0, avgTAT = 0;

        for (Process p : processes) {
            writer.write(p.toString() + "\n");
            avgWT += p.getWaitingTime();
            avgCT += p.getCompletionTime();
            avgRT += p.getResponseTime();
            avgTAT += p.getTurnaroundTime();
        }

        int n = processes.size();
        writer.write(String.format("WT=%.1f; CT=%.1f; RT=%.1f; TAT=%.1f;\n",
                avgWT/n, avgCT/n, avgRT/n, avgTAT/n));

        writer.close();
    }
}