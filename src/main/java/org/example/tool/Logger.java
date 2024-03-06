package org.example.tool;

import org.example.command.Command;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.example.tool.Tool.getCurrentTimestamp;

public class Logger implements Observer{
    protected final PrintWriter logWriter;
    String logFilePath;

    public Logger(String logFilePath) throws IOException {
        this.logFilePath = logFilePath;
        logWriter = new PrintWriter(new FileWriter(logFilePath, true));
        String timestamp = getCurrentTimestamp();
        logWriter.println("session start at " + timestamp);
        logWriter.flush();
    }

    private void logHistory(Command command) {
        String timestamp = getCurrentTimestamp();
        logWriter.println(timestamp + " " + command.toString());
        logWriter.flush();
    }

    public void close() {
        logWriter.close();
    }

    @Override
    public void update(Command command) {
        logHistory(command);
    }
}
