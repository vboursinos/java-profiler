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
    public void extractMethodContext(List<MethodInfo> methodInfoList) throws IOException {
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

    public static void findAndPrintMethodLines(Path path, String basePath, String methodName) {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            processMethodLines(br, path, basePath, methodName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processMethodLines(BufferedReader br, Path path, String basePath, String methodName) throws IOException {
        String line;
        int lineNumber = 1;

        while ((line = br.readLine()) != null) {
            if (line.contains(methodName + "(") && line.endsWith("{")) {
                // Found a method start
                processMethodStart(br, line, lineNumber, path, basePath);
            }
            lineNumber++;
        }
    }


    private static void processMethodStart(BufferedReader br, String initialLine, int startLineNumber, Path path, String basePath) throws IOException {
        String line;
        int curlyBraceCount = 1; // Track nested curly braces
        StringBuilder methodLines = new StringBuilder(initialLine + "\n");
        System.out.println("-----------------------------");
        System.out.println(path);
        System.out.println(initialLine);
        int lineNumber = startLineNumber;

        // Continue reading lines until we find the end of the method
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            methodLines.append(line).append("\n");
            curlyBraceCount += countOccurrences(line, '{');
            curlyBraceCount -= countOccurrences(line, '}');
            if (curlyBraceCount == 0) {
                processMethodEnd(basePath, path, startLineNumber, lineNumber, methodLines.toString());
                break;
            }
            lineNumber++;
        }
    }

    private static void processMethodEnd(String basePath, Path path, int startLine, int endLine, String methodLines) {
        String relativePath = getRelativePath(basePath, path.toString());
        JsonInfo jsonInfo = new JsonInfo(relativePath, startLine, endLine, "popular-method", methodLines);
        jsonInfoList.add(jsonInfo);
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
