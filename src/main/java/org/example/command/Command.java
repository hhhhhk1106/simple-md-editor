package org.example.command;

import java.io.IOException;

public interface Command {
    boolean execute() throws IOException;
}
