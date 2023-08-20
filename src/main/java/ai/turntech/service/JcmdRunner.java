package ai.turntech.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JcmdRunner {

    public static void runJcmdPrintTouchedMethods() throws IOException, InterruptedException {
        String simpleBenchmarkPID = getSimpleBenchmarkPID();

        if (simpleBenchmarkPID != null) {
            runProfilerScript(simpleBenchmarkPID);
        } else {
            System.err.println("SimpleBenchmark PID not found.");
        }
    }

    public static String getSimpleBenchmarkPID() throws IOException, InterruptedException {
        Process jcmdProcess = executeJcmdCommand("jcmd");
        BufferedReader reader = new BufferedReader(new FileReader("jcmd.txt"));
        String line;
        String simpleBenchmarkPID = null;

        while ((line = reader.readLine()) != null) {
            if (line.contains("SimpleBenchmark")) {
                simpleBenchmarkPID = line.split("\\s+")[0];
                break;
            }
        }
        reader.close();
        return simpleBenchmarkPID;
    }

    public static Process executeJcmdCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder jcmdProcessBuilder = new ProcessBuilder(command);
        if (command.length > 1) {
            jcmdProcessBuilder.redirectOutput(new File("profiler.txt"));
        } else {
            jcmdProcessBuilder.redirectOutput(new File("jcmd.txt"));
        }
        Process jcmdProcess = jcmdProcessBuilder.start();
        int jcmdExitCode = jcmdProcess.waitFor();

        if (jcmdExitCode != 0) {
            System.err.println("Error running jcmd.");
        }

        return jcmdProcess;
    }

    public static void runProfilerScript(String simpleBenchmarkOutput) throws IOException, InterruptedException {
        String[] profilerCommand = {"jcmd", simpleBenchmarkOutput, "VM.print_touched_methods"};
        Process profilerProcess = executeJcmdCommand(profilerCommand);
        int profilerExitCode = profilerProcess.waitFor();

        if (profilerExitCode == 0) {
            System.out.println("Profiler script executed successfully.");
        } else {
            System.err.println("Error running profiler script.");
        }
    }
}
