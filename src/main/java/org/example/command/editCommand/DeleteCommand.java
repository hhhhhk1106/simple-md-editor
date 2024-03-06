package org.example.command.editCommand;

import org.example.Document;
import org.example.command.Revocable;
import org.example.tool.LineData;

import java.io.Serializable;
import java.util.List;

public class DeleteCommand implements Revocable, Serializable {
    private final Document document;
    private final Integer lineNumber;
    private final String content;
    private List<LineData> records;
    public DeleteCommand(Document document, Integer lineNumber, String content) {
        this.document = document;
        this.lineNumber = lineNumber;
        this.content = content;
    }

    @Override
    public boolean execute() {
        if(document == null) {
            System.out.println("未加载文件。");
            return false;
        }
        records = document.delete(lineNumber, content);
        // 未找到要删除的内容
        return !records.isEmpty();
    }

    @Override
    public void undoExecute() {
        for(int i = records.size()-1; i >= 0; i --) {
            LineData record = records.get(i);
            document.insert(record.getLineNumber(),record.getContent());
        }
    }

    @Override
    public String toString() {
        if(lineNumber == -1) {
            return "delete " + content;
        } else {
            return "delete " + lineNumber;
        }
    }
}
