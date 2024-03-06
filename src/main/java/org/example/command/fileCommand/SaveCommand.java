package org.example.command.fileCommand;

import org.example.Document;
import org.example.command.Unskippable;

import java.io.IOException;
import java.io.Serializable;

public class SaveCommand implements Unskippable, Serializable {

    private final Document document;

    public SaveCommand(Document document) {this.document = document;}
    @Override
    public boolean execute() throws IOException {
        if(document == null) {
            System.out.println("未加载文件。");
            return false;
        }
        return document.save();
    }
    @Override
    public String toString() {
        return "save";
    }
}
