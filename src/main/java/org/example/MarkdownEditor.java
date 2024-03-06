package org.example;

import org.example.command.Command;
import org.example.command.editCommand.*;
import org.example.command.fileCommand.*;
import org.example.command.listCommand.*;
import org.example.command.logCommand.HistoryCommand;
import org.example.command.logCommand.StatsCommand;
import org.example.tool.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.memento.Originator.restoreFromMemento;
import static org.example.memento.Originator.saveMemento;

public class MarkdownEditor implements Subject {

    private Invoker invoker;
    private Document document;
    private Logger historyLogger;
    private Statistics statsLogger;
    private final List<Observer> observers = new ArrayList<>();
    private final WorkspaceManager workspaceManager;
    private Workspace activeWorkspace;

    public MarkdownEditor() {
        this.invoker = new Invoker();
        this.workspaceManager = restoreFromMemento();
        initializeEditor(workspaceManager);
    }

    private void initializeEditor(WorkspaceManager workspaceManager) {
        this.activeWorkspace = workspaceManager.getActiveWorkspace();
        if(activeWorkspace != null) {
            this.invoker = activeWorkspace.getInvoker();
            this.document = activeWorkspace.getDocument();
        }
    }

    public void start() throws IOException {
        historyLogger = new Logger("./src/main/resources/log/log.txt");
        statsLogger = new Statistics("./src/main/resources/log/log.txt");
        registerObserver(historyLogger);
        registerObserver(statsLogger);
        if(activeWorkspace != null) {
            statsLogger.fileInitial(workspaceManager.getAllFilepath(), activeWorkspace.getName()+".md");
        } else {
            statsLogger.fileInitial(workspaceManager.getAllFilepath(),null);
        }

        // 从用户输入中获取命令
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("请输入命令：");
            String userInput = scanner.nextLine();

            if (userInput.equals("exit")) {
                Command exitCommand = new ExitCommand(workspaceManager);
                exitCommand.execute();
                saveMemento(workspaceManager);
                notifyObservers(exitCommand);

                unregisterObserver(historyLogger);
                unregisterObserver(statsLogger);
                historyLogger.close();
                statsLogger.close();
                break;
            }
            if(activeWorkspace == null) {
                document = null;
            } else {
                document = activeWorkspace.getDocument();
            }
            Command inputCommand = processUserInput(userInput);
            if(inputCommand != null) {
                invoker.setCommand(inputCommand);
                boolean executeStat = invoker.executeCommand();
                if(executeStat) {
                    notifyObservers(inputCommand);
                } else {
                    System.out.println(inputCommand + "执行失败");
                }
            } else {
                System.out.println("无效的命令格式。");
            }
        }
    }

    private Command processUserInput(String userInput) {
        Command command = null;
        ParseCommand parseCommand = new ParseCommand(userInput);
        switch (parseCommand.getType()) {
            case "load": command = dealLoad(parseCommand);break;
            case "save": command = dealSave(parseCommand);break;
            case "open": command = dealOpen(parseCommand);break;
            case "close": command = dealClose(parseCommand);break;
            case "undo": command = dealUndo(parseCommand);break;
            case "redo": command = dealRedo(parseCommand);break;
            case "insert": command = dealInsert(parseCommand);break;
            case "append-head": command = dealAppendHead(parseCommand);break;
            case "append-tail": command = dealAppendTail(parseCommand);break;
            case "delete": command = dealDelete(parseCommand);break;
            case "list": command = dealList(parseCommand);break;
            case "list-tree": command = dealListTree(parseCommand);break;
            case "dir-tree": command = dealDirTree(parseCommand);break;
            case "list-workspace": command = dealListWorkspace(parseCommand);break;
            case "ls": command = dealLs(parseCommand);break;
            case "history": command = dealHistory(parseCommand);break;
            case "stats": command = dealStats(parseCommand);break;
            default:
                break;
        }
        return command;
    }


    private Command dealLoad(ParseCommand parseCommand) {
        Command loadCommand = null;
        String loadFilePath = parseCommand.getContent();
        if(loadFilePath != null) {
            Workspace workspace = workspaceManager.createWorkspace(loadFilePath);
            loadCommand = new LoadCommand(workspace, loadFilePath);
            if(workspace != null) {
                if(activeWorkspace != null) {
                    activeWorkspace.setActive(false);
                }
                activeWorkspace = workspace;
                invoker = activeWorkspace.getInvoker();
            }
        }
        return loadCommand;
    }
    private Command dealSave(ParseCommand parseCommand) {
        if(parseCommand.getContent() == null && parseCommand.getLineNumber() == null) {
            return new SaveCommand(document);
        }
        return null;
    }
    private Command dealOpen(ParseCommand parseCommand) {
        Command openCommand = null;
        String loadFilename = parseCommand.getContent();
        if(loadFilename != null) {
            Workspace workspace = workspaceManager.openWorkspace(loadFilename);
            openCommand = new OpenCommand(loadFilename, workspace);
            if(workspace != null) {
                if(activeWorkspace != null) {
                    activeWorkspace.setActive(false);
                }
                activeWorkspace = workspace;
                invoker = activeWorkspace.getInvoker();
            }
        }
        return openCommand;
    }
    private Command dealClose(ParseCommand parseCommand) {
        if(parseCommand.getContent() == null && parseCommand.getLineNumber() == null) {
            CloseCommand closeCommand = new CloseCommand(workspaceManager,activeWorkspace);
            activeWorkspace = null;
            return closeCommand;
        }
        return null;
    }
    private Command dealUndo(ParseCommand parseCommand) {
        if(parseCommand.getContent() == null && parseCommand.getLineNumber() == null) {
            return new UndoCommand(invoker);
        }
        return null;
    }
    private Command dealRedo(ParseCommand parseCommand) {
        if(parseCommand.getContent() == null && parseCommand.getLineNumber() == null) {
            return new RedoCommand(invoker);
        }
        return null;
    }
    private Command dealInsert(ParseCommand parseCommand) {
        Command insertCommand = null;
        String content = parseCommand.getContent();
        int lineNumber = document.getLineNum() + 1;
        if(parseCommand.getLineNumber() != null) {
            lineNumber = parseCommand.getLineNumber();
        }
        if(content == null || content.isEmpty()) {
            System.out.println("内容为空");
        } else {
            insertCommand = new InsertCommand(document,lineNumber,content);
        }
        return insertCommand;
    }
    private Command dealAppendHead(ParseCommand parseCommand) {
        Command appendHeadCommand = null;
        if(parseCommand.getLineNumber() == null) {
            String content = parseCommand.getContent();
            if(!content.isEmpty()) {
                appendHeadCommand = new AppendHeadCommand(document,content);
            }
        }
        return appendHeadCommand;
    }
    private Command dealAppendTail(ParseCommand parseCommand) {
        Command appendTailCommand = null;
        if(parseCommand.getLineNumber() == null) {
            String content = parseCommand.getContent();
            if(!content.isEmpty()) {
                appendTailCommand = new AppendTailCommand(document,content);
            }
        }
        return appendTailCommand;
    }
    private Command dealDelete(ParseCommand parseCommand) {
        Command deleteCommand = null;
        int lineNumber = -1;
        String content = null;
        if(parseCommand.getLineNumber() != null) {
            lineNumber = parseCommand.getLineNumber();
        }
        if(parseCommand.getContent() == null) {
            if(lineNumber == -1) {
                return null;
            }
            content = String.valueOf(lineNumber);
        } else {
            content = parseCommand.getContent();
        }
        deleteCommand = new DeleteCommand(document,lineNumber,content);
        return deleteCommand;
    }
    private Command dealList(ParseCommand parseCommand) {
        Command listCommand = null;
        if(parseCommand.getContent()==null && parseCommand.getLineNumber()==null) {
            listCommand = new ListCommand(document);
        }
        return listCommand;
    }
    private Command dealListTree(ParseCommand parseCommand) {
        Command listTreeCommand = null;
        if(parseCommand.getContent()==null && parseCommand.getLineNumber()==null) {
            listTreeCommand = new ListTreeCommand(document);
        }
        return listTreeCommand;
    }
    private Command dealDirTree(ParseCommand parseCommand) {
        Command dirTreeCommand = null;
        if(parseCommand.getLineNumber()==null) {
            dirTreeCommand = new DirTreeCommand(document, parseCommand.getContent());
        }
        return dirTreeCommand;
    }
    private Command dealListWorkspace(ParseCommand parseCommand) {
        Command listWorkspaceCommand = null;
        if(parseCommand.getContent()==null && parseCommand.getLineNumber()==null) {
            listWorkspaceCommand = new ListWorkspaceCommand(workspaceManager);
        }
        return listWorkspaceCommand;
    }
    private Command dealLs(ParseCommand parseCommand) {
        Command lsCommand = null;
        if(parseCommand.getContent()==null && parseCommand.getLineNumber()==null) {
            lsCommand = new LsCommand(workspaceManager);
        }
        return lsCommand;
    }
    private Command dealHistory(ParseCommand parseCommand) {
        Command historyCommand = null;
        int num = -1;
        if(parseCommand.getLineNumber() != null) {
            num = parseCommand.getLineNumber();
        }
        if(parseCommand.getContent() == null) {
            historyCommand = new HistoryCommand(invoker, num);
        }
        return historyCommand;
    }
    private Command dealStats(ParseCommand parseCommand) {
        Command statsCommand = null;
        if(parseCommand.getLineNumber() == null) {
            String choice = parseCommand.getContent();
            if(choice == null || choice.equals("current")) {
                statsCommand = new StatsCommand(statsLogger, 0);
            } else if (choice.equals("all")) {
                statsCommand = new StatsCommand(statsLogger,1);
            }
        }
        return statsCommand;
    }
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Command command) {
        for (Observer observer : observers) {
            observer.update(command);
        }
    }

}
