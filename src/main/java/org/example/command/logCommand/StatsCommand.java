package org.example.command.logCommand;

import org.example.command.Command;
import org.example.tool.Statistics;

import java.io.IOException;
import java.io.Serializable;

public class StatsCommand implements Command, Serializable {

    private final Statistics statistics;
    int choice; // 0:current, 1:all
    public StatsCommand(Statistics statistics, int choice) {
        this.statistics = statistics;
        this.choice = choice;
    }

    @Override
    public boolean execute() throws IOException {
        if(choice == 0) {
            statistics.printCurrentStats();
        } else if(choice == 1) {
            statistics.printAllStats();
        }
        return true;
    }
    @Override
    public String toString() {
        if (choice == 0) {
            return "stats current";
        } else {
            return "stats all";
        }
    }
}
