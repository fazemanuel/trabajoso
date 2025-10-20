//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo de entrada
            String inputFile = "C:/Users/pepit/IdeaProjects/untitled5/datosEntrada/mlq_test2.txt";
            String outputFile = "C:/Users/pepit/IdeaProjects/untitled5/out.txt";

            System.out.println("Leyendo archivo: " + inputFile);
            List<Process> processes = FileHandler.readInputFile(inputFile);


            // Crear scheduler y agregar procesos
            MLQScheduler scheduler = new MLQScheduler();
            for (Process p : processes) {
                scheduler.addProcess(p);
            }

            // Ejecutar simulación
            scheduler.run();

            // Escribir resultados
            FileHandler.writeOutputFile(outputFile, scheduler.getCompletedProcesses());
            System.out.println("\nResultados guardados en: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error al leer/escribir archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
