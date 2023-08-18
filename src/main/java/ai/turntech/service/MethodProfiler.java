package ai.turntech.service;

import ai.turntech.model.MethodInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodProfiler {

    public static List<MethodInfo> findPopularProjectMethods(String filePath, String methodPathPrefix, String basePath, String projectPrefix, int numberOfMethods) throws IOException {
        HashMap<String, Integer> methodsCounterMap = countMethods(filePath);
        List<Map.Entry<String, Integer>> sortedTokens = sortMethods(methodsCounterMap);

        return extractMethodInfoList(sortedTokens, methodPathPrefix, projectPrefix, basePath, numberOfMethods);
    }

    private static HashMap<String, Integer> countMethods(String filePath) throws IOException {
        HashMap<String, Integer> methodsCounterMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                if (tokens.length > 0) {
                    String firstToken = tokens[0].trim();
                    methodsCounterMap.put(firstToken, methodsCounterMap.getOrDefault(firstToken, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return methodsCounterMap;
    }

    private static List<Map.Entry<String, Integer>> sortMethods(HashMap<String, Integer> tokenCounterMap) {
        List<Map.Entry<String, Integer>> sortMethods = new ArrayList<>(tokenCounterMap.entrySet());
        sortMethods.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return sortMethods;
    }

    private static List<MethodInfo> extractMethodInfoList(List<Map.Entry<String, Integer>> sortedTokens, String methodPathPrefix, String projectPrefix, String basePath, int numberOfMethods) {
        List<MethodInfo> methodInfoList = new ArrayList<>();
        int count = 0;

        for (Map.Entry<String, Integer> entry : sortedTokens) {
            if (entry.getKey().startsWith(methodPathPrefix)) {
                if (count >= numberOfMethods) {
                    break;
                }

                System.out.println(entry.getKey() + " : " + entry.getValue());

                String[] tokens = entry.getKey().split("\\.");

                String classPath = projectPrefix.concat("/").concat(tokens[0]).concat(".java");
                MethodInfo methodInfo = new MethodInfo(classPath, basePath, tokens[1]);
                methodInfoList.add(methodInfo);
                count++;
            }
        }

        return methodInfoList;
    }
}