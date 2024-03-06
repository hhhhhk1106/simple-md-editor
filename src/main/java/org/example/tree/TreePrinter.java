package org.example.tree;

public class TreePrinter {
    public static void printTree(TreeNode node, int depth, String preStr) {
        if(depth == 0) {
            for (TreeNode child : node.getChildren(false)) {
                printTree(child, depth+1,"");
            }
            return;
        }
        if(node.hasNext()) {
            System.out.println(preStr + "├── " + node.getName());
            for (TreeNode child : node.getChildren(true)) {
                printTree(child, depth+1,preStr + "│\t");
            }
        } else {
            System.out.println(preStr + "└── " + node.getName());
            for (TreeNode child : node.getChildren(true)) {
                printTree(child, depth+1, preStr + "\t");
            }
        }

    }

}

