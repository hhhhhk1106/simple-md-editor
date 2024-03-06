package org.example.command.fileCommand;

import org.example.WorkspaceManager;
import org.example.command.Unskippable;

import java.io.IOException;
import java.io.Serializable;

public class ExitCommand implements Unskippable, Serializable {
    private final WorkspaceManager workspaceManager;
    public ExitCommand(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    @Override
    public boolean execute() throws IOException {
        return workspaceManager.exit();
    }
    @Override
    public String toString() {
        return "exit";
    }
}
