package ai.turntech;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MethodLocator {
    public static void main(String[] args) {
        String projectPath = "/home/vasilis/IdeaProjects/jackson-core/src/main/java/com/fasterxml/jackson/core/JsonFactory.java"; // Provide the actual project path
        String methodName = "_decorate"; // Provide the method name

        try {
            Files.walk(Paths.get(projectPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> findAndPrintMethodLines(path, methodName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findAndPrintMethodLines(Path path, String methodName) {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            int lineNumber = 1;
            boolean insideMethod = false;
            boolean foundMethodStart = false;
            boolean validMethod = false;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) {
                    if (word.startsWith(methodName + "(") && line.endsWith("{")) {
                        validMethod = true;
                    }
                }
                if (!insideMethod && validMethod) {
                    insideMethod = true;
                    foundMethodStart = true;
                    System.out.println("Method '" + methodName + "' Start Line: " + lineNumber);
                }

                if (insideMethod) {
                    System.out.println(line);
                }

                if (foundMethodStart && line.trim().endsWith("}")) {
                    insideMethod = false;
                    System.out.println("Method '" + methodName + "' End Line: " + lineNumber);
                    break;
                }

                lineNumber++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
