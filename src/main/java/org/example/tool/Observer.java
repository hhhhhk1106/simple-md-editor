package org.example.tool;

import org.example.command.Command;

public interface Observer {
    void update(Command command);
}
