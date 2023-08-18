package ai.turntech.model;

public class MethodInfo {
    private String classPath;

    private String basePath;
    private String methodName;

    public MethodInfo(String classPath, String basePath, String methodName) {
        this.classPath = classPath;
        this.basePath = basePath;
        this.methodName = methodName;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "classPath='" + classPath + '\'' +
                ", basePath='" + basePath + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
