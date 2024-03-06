package org.example.command.fileCommand;

import org.example.Workspace;
import org.example.WorkspaceManager;
import org.example.command.Unskippable;

import java.io.IOException;
import java.io.Serializable;

public class CloseCommand implements Unskippable, Serializable {
    private final Workspace workspace;
    private final WorkspaceManager workspaceManager;
    public CloseCommand(WorkspaceManager workspaceManager, Workspace workspace) {
        this.workspaceManager = workspaceManager;
        this.workspace = workspace;
    }
    @Override
    public boolean execute() throws IOException {
        return workspaceManager.closeWorkspace(workspace);
    }
    @Override
    public String toString() {
        return "close";
    }
}
