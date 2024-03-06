package org.example.command.editCommand;

import org.example.Document;
import org.example.command.Revocable;

import java.io.IOException;
import java.io.Serializable;

public class InsertCommand implements Revocable, Serializable {
    private final Document document;
    private final int lineNumber;
    private final String content;
    public InsertCommand(Document document, int lineNumber, String content) {
        this.document = document;
        this.lineNumber = lineNumber;
        this.content = content;
    }

    @Override
    public boolean execute() throws IOException {
        if(document == null) {
            System.out.println("未加载文件。");
            return false;
        }
        return document.insert(lineNumber,content);
    }

    @Override
    public void undoExecute() {
        document.delete(lineNumber,content);
    }

    @Override
    public String toString() {
        if (lineNumber == 0) {
            return "insert " + content;
        } else {
            return "insert " + lineNumber + " " + content;
        }
    }
}
