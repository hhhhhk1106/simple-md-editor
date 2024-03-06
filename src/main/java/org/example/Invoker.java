package org.example;

import org.example.command.Command;
import org.example.command.Revocable;
import org.example.command.Unskippable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.example.tool.Tool.getCurrentTimestamp;

public class Invoker implements Serializable {
    private Command command;    // 当前执行的命令
    private final Stack<Revocable> undoStack;
    private final Stack<Revocable> redoStack;
    private final List<String> historyMsg;


    public Invoker() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        historyMsg = new ArrayList<>();
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean executeCommand() throws IOException {
        boolean executeStat = command.execute();
        String history = getCurrentTimestamp() +" "+ command.toString();
        historyMsg.add(history);

        if(executeStat) {
            if(command instanceof Revocable) {
                undoStack.push((Revocable) command); // 将已执行的命令入栈
                redoStack.clear(); // 执行新命令时清空redo栈
            } else if (command instanceof Unskippable) {
                undoStack.clear();
//                redoStack.clear();
            }
        }
        return executeStat;
    }

    public boolean undo() {
        if (!undoStack.isEmpty()) {
            Revocable lastCommand = undoStack.pop();
            lastCommand.undoExecute(); // 撤销上一个命令
            redoStack.push(lastCommand); // 将撤销的命令入redo栈
            return true;
        } else {
            System.out.println("无法执行撤销操作，undo栈为空。");
        }
        return false;
    }

    public boolean redo() throws IOException {
        if (!redoStack.isEmpty()) {
            Revocable nextCommand = redoStack.pop();
            nextCommand.execute(); // 重做上一个撤销的命令
            undoStack.push(nextCommand); // 将重新执行的命令入undo栈
            return true;
        } else {
            System.out.println("无法执行重做操作，redo栈为空。");
        }
        return false;
    }

    public boolean isChanged() {
        return !undoStack.isEmpty();
    }

    public void printCurrentHistory(Integer num) {
        if (num == -1 || num >= historyMsg.size()) {
            // 打印所有
            for (int i = historyMsg.size() - 1; i >= 0; i--) {
                System.out.println(historyMsg.get(i));
            }
        } else {
            // 打印指定数量
            for (int i = historyMsg.size() - 1; i >= historyMsg.size() - num; i--) {
                System.out.println(historyMsg.get(i));
            }
        }
    }
}
