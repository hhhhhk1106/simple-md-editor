package org.example.command.logCommand;

import org.example.Invoker;
import org.example.command.Command;

import java.io.IOException;
import java.io.Serializable;

public class HistoryCommand implements Command, Serializable {
    private final Invoker invoker;
    private final int numLines;
    public HistoryCommand(Invoker invoker, int numLines) {
        this.invoker = invoker;
        this.numLines = numLines;
    }
    @Override
    public boolean execute() throws IOException {
        invoker.printCurrentHistory(numLines);
        return true;
    }

    @Override
    public String toString() {
        return "history";
    }
}
