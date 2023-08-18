package ai.turntech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class JsonInfo {
    @JsonProperty("file_path")
    private String filePath;
    @JsonProperty("start_line")
    private int startLine;
    @JsonProperty("start_col")
    private int startCol;
    @JsonProperty("end_line")
    private int endLine;
    @JsonProperty("end_col")
    private int endCol;
    @JsonProperty("node_type")
    private String nodeType;
    private String content;

    public JsonInfo() {
    }

    public JsonInfo(String filePath, int startLine, int endLine, String nodeType, String content) {
        this.filePath = filePath;
        this.startLine = startLine;
        this.endLine = endLine;
        this.nodeType = nodeType;
        this.content = content;
    }

    public JsonInfo(String filePath, int startLine, int startCol, int endLine, int endCol, String nodeType, String content) {
        this.filePath = filePath;
        this.startLine = startLine;
        this.startCol = startCol;
        this.endLine = endLine;
        this.endCol = endCol;
        this.nodeType = nodeType;
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getEndCol() {
        return endCol;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "JsonInfo{" +
                "filePath='" + filePath + '\'' +
                ", startLine=" + startLine +
                ", startCol=" + startCol +
                ", endLine=" + endLine +
                ", endCol=" + endCol +
                ", nodeType='" + nodeType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
