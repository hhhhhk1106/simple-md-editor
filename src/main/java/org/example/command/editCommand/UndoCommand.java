package org.example.command.editCommand;

import org.example.Invoker;
import org.example.command.Command;

import java.io.IOException;
import java.io.Serializable;

public class UndoCommand implements Command, Serializable {
    private final Invoker invoker;

    public UndoCommand(Invoker invoker) {
        this.invoker = invoker;
    }
    @Override
    public boolean execute() throws IOException {
        return invoker.undo();
    }
    @Override
    public String toString() {
        return "undo";
    }
}
