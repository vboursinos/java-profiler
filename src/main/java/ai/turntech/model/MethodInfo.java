package ai.turntech.model;

public class MethodInfo {
    private String classPath;
    private String methodName;

    public MethodInfo(String classPath, String methodName) {
        this.classPath = classPath;
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

    @Override
    public String toString() {
        return "MethodInfo{" +
                "classPath='" + classPath + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
