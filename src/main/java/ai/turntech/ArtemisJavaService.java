package ai.turntech;

import ai.turntech.model.MethodInfo;
import ai.turntech.service.MethodLocator;
import ai.turntech.service.MethodProfiler;

import java.io.IOException;
import java.util.List;

public class ArtemisJavaService {
    //todo reduce number of paths
    private static final String profilerFilePath = "t.txt"; // Provide the actual file path
    private static final String methodPathPrefix = "com/fasterxml/jackson/core";
    private static final String projectPrefix = "/home/vasilis/IdeaProjects/jackson-core/src/main/java";
    private static final String basePath = "/home/vasilis/IdeaProjects/jackson-core";
    private static final int numberOfMethods = 30;

    public static void main(String[] args) throws IOException {
        List<MethodInfo> methodInfoList = MethodProfiler.findPopularProjectMethods(profilerFilePath, methodPathPrefix, basePath, projectPrefix, numberOfMethods);
        MethodLocator methodLocator = new MethodLocator();
        methodLocator.extractMethodContext(methodInfoList);
    }
}
