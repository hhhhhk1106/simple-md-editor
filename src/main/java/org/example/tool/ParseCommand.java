package org.example.tool;

public class ParseCommand {
    private final String type;
    private Integer lineNumber;
    private String content;

    public ParseCommand(String inputStr) {
        inputStr = inputStr.trim();
        String[] parts = inputStr.split("\\s+", 2);
        type = parts[0];
        this.lineNumber = null;
        this.content = null;

        if (parts.length > 1) {
            String[] remainingParts = parts[1].split("\\s+", 2);
            if (remainingParts.length > 1) {
                if (isNumeric(remainingParts[0])) {
                    this.lineNumber = Integer.parseInt(remainingParts[0]);
                    this.content = remainingParts[1];
                } else {
                    this.content = parts[1];
                }
            } else {
                if (isNumeric(remainingParts[0])) {
                    this.lineNumber = Integer.parseInt(remainingParts[0]);
                } else {
                    this.content = parts[1];
                }
            }
        }

    }

    public String getType() {
        return type;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getContent() {
        return content;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
