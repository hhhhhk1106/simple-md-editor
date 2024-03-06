package org.example.tree;

import java.util.List;

public interface TreeNode {
    String getName();
    List<TreeNode> getChildren(boolean isRoot);
    boolean hasNext();
}
