package org.example.command.fileCommand;

import org.example.Workspace;
import org.example.command.Unskippable;

import java.io.IOException;
import java.io.Serializable;

public class LoadCommand implements Unskippable, Serializable {
    private final String filePath;
    private final Workspace workspace;

    public LoadCommand(Workspace workspace, String loadFilePath) {
        this.filePath = loadFilePath;
        this.workspace = workspace;
    }

    @Override
    public boolean execute() throws IOException {
        if(workspace == null) return false;
        return workspace.load(filePath);
    }

    @Override
    public String toString() {
        return "load " + filePath;
    }
}
