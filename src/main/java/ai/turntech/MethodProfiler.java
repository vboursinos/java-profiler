package ai.turntech;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodProfiler {
    public static void main(String[] args) {
        String filePath = "t.txt"; // Provide the actual file path
        String methodPathPrefix = "com/fasterxml/jackson/core";
        int numberOfMethods = 30;
        HashMap<String, Integer> tokenCounterMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                if (tokens.length > 0) {
                    String firstToken = tokens[0].trim();
                    tokenCounterMap.put(firstToken, tokenCounterMap.getOrDefault(firstToken, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map.Entry<String, Integer>> sortedTokens = new ArrayList<>(tokenCounterMap.entrySet());
        sortedTokens.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Print the top 10 tokens
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedTokens) {
            if (entry.getKey().startsWith(methodPathPrefix)) {
                if (count >= numberOfMethods) {
                    break;
                }
                System.out.println(entry.getKey() + " : " + entry.getValue());
                count++;
            }
        }
    }
}
