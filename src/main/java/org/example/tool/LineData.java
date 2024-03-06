package org.example.tool;

import java.io.Serializable;

public class LineData implements Serializable {
    private final Integer lineNumber;
    private final String content;
    public LineData(Integer lineNumber, String content) {
        this.lineNumber = lineNumber;
        this.content = content;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getContent() {
        return content;
    }
}
