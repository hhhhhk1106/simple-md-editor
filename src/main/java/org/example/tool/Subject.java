package org.example.tool;

import org.example.command.Command;

public interface Subject {
    void registerObserver(Observer observer);
    void unregisterObserver(Observer observer);
    void notifyObservers(Command command);
}
