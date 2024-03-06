package org.example;

import org.example.command.Command;
import org.example.command.fileCommand.SaveCommand;
import org.example.tree.DirectoryAdapter;
import org.example.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.example.tool.Tool.getUserAnswerYes;
import static org.example.tree.TreePrinter.printTree;

public class WorkspaceManager implements Serializable {
    private final List<Workspace> workspaceList;
    public WorkspaceManager() {
        this.workspaceList = new ArrayList<>();
    }

    public Workspace createWorkspace(String filepath) {
        // 检查路径是否已经在某个workspace中打开
        if(!filepath.endsWith(".md")) {
            System.out.println("只能打开.md文件");
            return null;
        }
        String filename = filepath.substring(0, filepath.length() - 3);
        for(Workspace workspace:workspaceList) {
            if(workspace.getName().equals(filename)) {
                System.out.println("文件不能重复加载。");
                return null;
            }
        }
        Workspace workspace = new Workspace();
        workspaceList.add(workspace);
        return workspace;
    }
    public Workspace openWorkspace(String name) {
        for(Workspace workspace:workspaceList) {
            if(workspace.getName().equals(name)) {
                return workspace;
            }
        }
        return null;
    }
    public boolean closeWorkspace(Workspace workspace) {
        if(workspace == null) {
            System.out.println("当前未打开工作区。");
            return false;
        }
        if(workspace.isChanged()) {
            System.out.println("Do you want to save the current workspace [Y\\N] ?");
            if(getUserAnswerYes()) {
                workspace.getDocument().save();
            }
        }
        String closeFileName = workspace.getName();
        for(Workspace ws:workspaceList) {
            if(ws.getName().equals(closeFileName)) {
                workspaceList.remove(ws);
                break;
            }
        }
        System.out.println("已关闭工作区 "+closeFileName);
        return true;
    }
    public boolean exit() throws IOException {
        for (Workspace workspace:workspaceList) {
            if(workspace.isChanged()) {
                System.out.println("Do you want to save the unsaved workspace: "+workspace.getName()+" [Y\\N] ?");
                if(getUserAnswerYes()) {
                    Command saveCommand = new SaveCommand(workspace.getDocument());
                    workspace.getInvoker().setCommand(saveCommand);
                    workspace.getInvoker().executeCommand();
                }
            }
        }
        return true;
    }

    public void printAllWorkspaces() {
        if(workspaceList.isEmpty()) {
            System.out.println("当前没有打开的工作区。");
            return;
        }
        for (Workspace workspace:workspaceList) {
            String line;
            if(workspace.isActive()) {
                line = "-> ";
            } else {
                line = "   ";
            }
            line += workspace.getName();
            if(workspace.isChanged()) {
                line += " *";
            }
            System.out.println(line);
        }
    }
    public void ls() {
        List<String> openFiles = new ArrayList<>();
        for (Workspace workspace:workspaceList) {
            openFiles.add(workspace.getName() + ".md");
        }
        String currentDirectory = "./src/main/resources/";
        TreeNode rootNode = new DirectoryAdapter(new File(currentDirectory),openFiles,false);
        printTree(rootNode, 0, "");
    }

    public Workspace getActiveWorkspace() {
        for (Workspace workspace:workspaceList) {
            if(workspace.isActive()) {
                return workspace;
            }
        }
        return null;
    }
    public List<String> getAllFilepath() {
        List<String> filePath = new ArrayList<>();
        for (Workspace workspace:workspaceList) {
            filePath.add(workspace.getName() + ".md");
        }
        return filePath;
    }
}
