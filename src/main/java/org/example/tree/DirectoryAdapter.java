package org.example.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryAdapter implements TreeNode{
    private final File file;
    private final List<String> openFiles;
    boolean hasNext;
    public DirectoryAdapter(File file, List<String> openFiles, boolean hasNext) {
        this.file = file;
        this.openFiles = openFiles;
        this.hasNext = hasNext;
    }

    @Override
    public String getName() {
        if(openFiles.contains(file.getName())) {
            return file.getName() + "  *";
        }
        return file.getName();
    }

    @Override
    public List<TreeNode> getChildren(boolean isRoot) {
        List<TreeNode> children = new ArrayList<>();
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null && files.length > 0) {
                for (int i = 0; i < files.length-1; i++) {
                    File subFile = files[i];
                    children.add(new DirectoryAdapter(subFile, openFiles, true));
                }
                children.add(new DirectoryAdapter(files[files.length-1], openFiles, false));
            }
        }
        return children;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }
}
