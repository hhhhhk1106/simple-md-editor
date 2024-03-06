package org.example.command.editCommand;

import org.example.Document;
import org.example.command.Revocable;

import java.io.Serializable;

public class AppendTailCommand implements Revocable, Serializable {
    private final Document document;
    private final String content;

    public AppendTailCommand(Document document, String content) {
        this.document = document;
        this.content = content;
    }

    @Override
    public boolean execute() {
        if(document == null) {
            System.out.println("未加载文件。");
            return false;
        }
        return document.insert(document.getLineNum()+1,content);
    }

    @Override
    public void undoExecute() {
        document.delete(document.getLineNum(),content);
    }

    @Override
    public String toString() {
        return "append-tail " + content;
    }

}
