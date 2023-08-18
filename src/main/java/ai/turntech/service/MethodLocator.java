package ai.turntech.service;

import ai.turntech.model.JsonInfo;
import ai.turntech.model.MethodInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MethodLocator {
    private static List<JsonInfo> jsonInfoList;

    public static void main(String[] args) {
        String projectPath = "/home/vasilis/IdeaProjects/jackson-core/src/main/java/com/fasterxml/jackson/core/JsonFactory.java"; // Provide the actual project path
        String methodName = "_decorate"; // Provide the method name
        String basePath = "/home/vasilis/IdeaProjects";

        try {
            Files.walk(Paths.get(projectPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> findAndPrintMethodLines(path, basePath, methodName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main(List<MethodInfo> methodInfoList) throws IOException {
        parseProjectFiles(methodInfoList);
        createJson();
    }

    private static void parseProjectFiles(List<MethodInfo> methodInfoList) {
        jsonInfoList = new ArrayList<>();
        for (MethodInfo methodInfo : methodInfoList) {
            try {
                Files.walk(Paths.get(methodInfo.getClassPath()))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .forEach(path -> findAndPrintMethodLines(path, methodInfo.getBasePath(), methodInfo.getMethodName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(jsonInfoList);
            System.out.println(json);
            File file = new File("hotspots.json");
            objectMapper.writeValue(file, jsonInfoList);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    private static void findAndPrintMethodLines(Path path, String basePath, String methodName) {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            int lineNumber = 1;
            int curlyBraceCount = 0; // Track nested curly braces
            StringBuilder methodLines = new StringBuilder();
            int startLine = 0;
            int endLine = 0;

            while ((line = br.readLine()) != null) {
                if (line.contains(methodName + "(") && line.endsWith("{")) {
                    // Found a method start
                    startLine = lineNumber;
                    curlyBraceCount = 1;
                    methodLines = new StringBuilder(line + "\n");
                    System.out.println("-----------------------------");
                    System.out.println(path);
                    System.out.println(line);
                    // Continue reading lines until we find the end of the method
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        methodLines.append(line).append("\n");
                        curlyBraceCount += countOccurrences(line, '{');
                        curlyBraceCount -= countOccurrences(line, '}');
                        if (curlyBraceCount == 0) {
                            lineNumber++;
                            endLine = lineNumber;
                            String relativePath = getRelativePath(basePath, path.toString());
                            JsonInfo jsonInfo = new JsonInfo(relativePath, startLine, endLine, "popular-method", methodLines.toString());
                            jsonInfoList.add(jsonInfo);
                            break;
                        }
                        lineNumber++;
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int countOccurrences(String str, char target) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }

    public static String getRelativePath(String basePath, String absolutePath) {
        File baseFile = new File(basePath);
        File absoluteFile = new File(absolutePath);
        return baseFile.toURI().relativize(absoluteFile.toURI()).getPath();
    }
}
