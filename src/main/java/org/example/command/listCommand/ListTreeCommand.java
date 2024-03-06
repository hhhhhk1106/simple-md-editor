package org.example.command.listCommand;

import org.example.Document;
import org.example.command.Command;

import java.io.IOException;
import java.io.Serializable;

public class ListTreeCommand implements Command, Serializable {
    private final Document document;
    public ListTreeCommand(Document document) {
        this.document = document;
    }
    @Override
    public boolean execute() throws IOException {
        if(document == null) {
            System.out.println("未加载文件。");
            return false;
        }
        document.listTree();
        return true;
    }

    @Override
    public String toString() {
        return "list-tree";
    }
}
