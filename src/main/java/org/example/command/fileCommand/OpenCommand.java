package org.example.command.fileCommand;

import org.example.Workspace;
import org.example.command.Command;

import java.io.IOException;
import java.io.Serializable;

public class OpenCommand implements Command, Serializable {
    private final String name;
    private final Workspace workspace;


    public OpenCommand(String name, Workspace workspace) {
        this.name = name;
        this.workspace = workspace;
    }

    @Override
    public boolean execute() throws IOException {
        if(workspace == null) {
            System.out.println("未加载该工作区 " + name);
            return false;
        } else {
            workspace.setActive(true);
            System.out.println("已打开工作区 " + name);
            return true;
        }
    }
    @Override
    public String toString() {
        return "open " + name;
    }
}
