package org.example.command.listCommand;

import org.example.WorkspaceManager;
import org.example.command.Command;

import java.io.IOException;
import java.io.Serializable;

public class LsCommand implements Command, Serializable {
    private final WorkspaceManager workspaceManager;
    public LsCommand(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }
    @Override
    public boolean execute() throws IOException {
        workspaceManager.ls();
        return true;
    }
    @Override
    public String toString() {
        return "ls";
    }
}
