package org.example.memento;

import org.example.Workspace;
import org.example.WorkspaceManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Originator {
    private List<Workspace> workspaceList;
    private static final String FILE_PATH = "./src/main/resources/mem/mem.ser";

    public void setWorkspaceList(List<Workspace> workspaceList) {
        this.workspaceList = workspaceList;
    }
    public List<Workspace> getWorkspaceList() {
        return workspaceList;
    }

    public static void saveMemento(WorkspaceManager workspaceManager) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(FILE_PATH)))) {
            outputStream.writeObject(workspaceManager);
            System.out.println("State saved successfully.");
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("状态保存出错。");
        }
    }
    public static WorkspaceManager restoreFromMemento() {
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(FILE_PATH)))) {
            return (WorkspaceManager) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new WorkspaceManager();
        }
    }
}
