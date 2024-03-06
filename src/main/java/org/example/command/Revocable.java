package org.example.command;

public interface Revocable extends Command{
    void undoExecute();
}
