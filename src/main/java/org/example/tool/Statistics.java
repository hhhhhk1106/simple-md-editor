package org.example.tool;

import org.example.command.Command;
import org.example.command.fileCommand.CloseCommand;
import org.example.command.fileCommand.LoadCommand;
import org.example.command.fileCommand.OpenCommand;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.tool.Tool.getCurrentTimestamp;
import static org.example.tool.Tool.getTimePeriod;

public class Statistics implements Observer{

    protected final PrintWriter logWriter;
    private String activeFilePath;
    private final Map<String, String> fileStartTimeMap;
    public Statistics(String logFilePath) throws IOException {
        this.fileStartTimeMap = new HashMap<>();
        logWriter = new PrintWriter(new FileWriter(logFilePath, true));
    }

    public void fileInitial(List<String> filePath, String activeFilePath) {
        for (String f:filePath) {
            if(!fileStartTimeMap.containsKey(f)) {
                fileStartTimeMap.put(f, getCurrentTimestamp());
            }
        }
        if(activeFilePath != null) {
            this.activeFilePath = activeFilePath;
        }
    }
    private void fileOpen(String filePath) {
        this.activeFilePath = filePath;
        if(!fileStartTimeMap.containsKey(filePath)) {
            fileStartTimeMap.put(filePath, getCurrentTimestamp());
        }
    }
    private void fileClose() {
        logStats(activeFilePath);
        fileStartTimeMap.remove(activeFilePath);
        activeFilePath = null;
    }
    public void close() {
        for (String key:fileStartTimeMap.keySet()){
            logStats(key);
        }
    }

    public void printAllStats() {
        if(fileStartTimeMap.isEmpty()) {
            System.out.println("尚未打开文件。");
            return;
        }
        for (String key:fileStartTimeMap.keySet()){
            printStats(key);
        }
    }
    public void printCurrentStats() {
        if(activeFilePath == null) {
            System.out.println("尚未打开文件。");
            return;
        }
        printStats(activeFilePath);
    }


    private void logStats(String filePath) {
        String startTime = fileStartTimeMap.get(filePath);
        String endTime = getCurrentTimestamp();
        String period = getTimePeriod(startTime,endTime);
        logWriter.println(filePath + " " + period);
        logWriter.flush();
    }
    private void printStats(String filePath) {
        String startTime = fileStartTimeMap.get(filePath);
        String nowTime = getCurrentTimestamp();
        String timePeriod = getTimePeriod(startTime,nowTime);
        System.out.println(filePath + " " + timePeriod);
    }
    @Override
    public void update(Command command) {
        ParseCommand parseCommand = new ParseCommand(command.toString());
        if(command instanceof LoadCommand) {
            String filepath = parseCommand.getContent();
            fileOpen(filepath);
        } else if(command instanceof OpenCommand) {
            String filepath = parseCommand.getContent() + ".md";
            fileOpen(filepath);
        } else if(command instanceof CloseCommand) {
            fileClose();
        }
    }

}
