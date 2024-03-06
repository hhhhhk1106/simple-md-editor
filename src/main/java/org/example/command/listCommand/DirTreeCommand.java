package org.example.command.listCommand;

import org.example.Document;
import org.example.command.Command;

import java.io.IOException;
import java.io.Serializable;

public class DirTreeCommand implements Command, Serializable {
    private final Document document;
    private final String content;
    public DirTreeCommand(Document document, String content) {
        this.document = document;
        this.content = content;
    }
    @Override
    public boolean execute() throws IOException {
        if(document == null) {
            System.out.println("未加载文件。");
            return false;
        }
        return document.listDirTree(content);

    }

    @Override
    public String toString() {
        if(content == null || content.isEmpty()) {
            return "dir-tree";
        }
        return "dir-tree " + content;
    }
}
