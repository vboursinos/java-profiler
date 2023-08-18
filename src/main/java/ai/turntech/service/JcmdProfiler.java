package ai.turntech.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JcmdProfiler {
    public static void main(String[] args) throws IOException {
        try {
            ProcessBuilder jcmdProcessBuilder = new ProcessBuilder("jcmd");
            jcmdProcessBuilder.redirectOutput(new File("jcmd.txt"));
            Process jcmdProcess = jcmdProcessBuilder.start();
            int jcmdExitCode = jcmdProcess.waitFor();

            if (jcmdExitCode == 0) {
                BufferedReader reader = new BufferedReader(new FileReader("jcmd.txt"));
                String line;
                String simpleBenchmarkOutput = null;

                while ((line = reader.readLine()) != null) {
                    if (line.contains("SimpleBenchmark")) {
                        simpleBenchmarkOutput = line.split("\\s+")[0];
                        break;
                    }
                }
                reader.close();

                if (simpleBenchmarkOutput != null) {
                    // Step 3: Run 'sudo jcmd <output> VM.print_touched_methods' and write output to profiler.txt
                    String[] profilerCommand = {"jcmd", simpleBenchmarkOutput, "VM.print_touched_methods"};
                    ProcessBuilder profilerProcessBuilder = new ProcessBuilder(profilerCommand);
                    profilerProcessBuilder.redirectOutput(new File("profiler.txt"));
                    Process profilerProcess = profilerProcessBuilder.start();
                    int profilerExitCode = profilerProcess.waitFor();

                    if (profilerExitCode == 0) {
                        System.out.println("Profiler script executed successfully.");
                    } else {
                        System.err.println("Error running profiler script.");
                    }
                } else {
                    System.err.println("SimpleBenchmark output not found.");
                }
            } else {
                System.err.println("Error running jcmd.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
