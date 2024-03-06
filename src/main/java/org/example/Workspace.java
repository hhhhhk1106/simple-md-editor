package org.example;

import java.io.Serializable;

public class Workspace implements Serializable {
    private final Invoker invoker;
    private String name;
    private Document document;
    private boolean isActive;
    public Workspace() {
        this.invoker = new Invoker();
        this.isActive = true;
    }
    public Invoker getInvoker() {
        return invoker;
    }
    public Document getDocument() {
        return document;
    }
    public String getName() {
        return name;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setDocument(Document document) {
        this.document = document;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isChanged() {
        return invoker.isChanged();
    }


    public boolean load(String filePath) {
        String name = filePath;
        if(filePath.endsWith(".md")) {
            name = filePath.substring(0, filePath.length() - 3);
        }
        this.name = name;
        this.document = new Document();
        return document.load(filePath);
    }

}
