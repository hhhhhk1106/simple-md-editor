package org.example;

import org.example.tool.LineData;
import org.example.tree.StringListAdapter;
import org.example.tree.TreeNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.tree.StringListAdapter.getTrueContent;
import static org.example.tree.TreePrinter.printTree;

public class Document implements Serializable{
    private final List<String> documentContent;
    private String filePath;


    public Document() {
        this.documentContent = new ArrayList<>();
    }

    public boolean load(String filePath) {
        filePath = "./src/main/resources/" + filePath;
        if (!isValidFilePath(filePath)) { // 路径非法
            return false;
        }
        this.filePath = filePath;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                documentContent.add(line);
            }

            System.out.println("文件内容已加载到内存中。");
        } catch (IOException e) {
            System.out.println("加载文件时发生错误：" + e.getMessage());
            return false;
        }
        return true;
    }

    public void list() {
        for(String s:documentContent) {
            System.out.println(s);
        }
    }

    public void listTree() {
        TreeNode stringListNode = new StringListAdapter(documentContent,false);
        printTree(stringListNode, 0, "");
    }

    public boolean listDirTree(String title) {
        boolean flag = false;
        for(int i = 0; i < documentContent.size(); i ++) {
            if(getTrueContent(documentContent.get(i)).equals(title)) {
                List<String> subContent = documentContent.subList(i,documentContent.size());
                TreeNode stringListNode = new StringListAdapter(subContent,false);
                printTree(stringListNode, 1,"");
                flag = true;
            }
        }
        if(!flag) {
            System.out.println("未找到对应目录。");
        }
        return flag;
    }

    public boolean save() {
        if (filePath != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                int size = documentContent.size();
                for (int i = 0; i < size; i++) {
                    writer.write(documentContent.get(i));
                    if (i < size - 1) {
                        writer.newLine(); // 在非最后一行添加换行符
                    }
                }

                System.out.println("文档已成功保存到文件 " + filePath);
                return true;
            } catch (IOException e) {
                System.out.println("保存文件时发生错误：" + e.getMessage());
            }
        } else {
            System.out.println("未加载文件，无法保存。");
        }
        return false;
    }

    public boolean insert(Integer lineNumber, String content) {
        if (isValidLineNumber(lineNumber)) {
            System.out.println("已插入第"+lineNumber+"行："+content);
            documentContent.add(lineNumber-1,content);
            return true;
        } else {
            System.out.println("无效行号:"+lineNumber);
            return false;
        }
    }

    public List<LineData> delete(Integer lineNumber, String content) {
        List<LineData> records = new ArrayList<>();
        boolean found = false;
        int deleteLine = -1;
        String deleteContent = "";
        if(lineNumber >= 1 && lineNumber <= documentContent.size()) {
            deleteLine = lineNumber;
            deleteContent = documentContent.get(deleteLine - 1);
            found = true;
            documentContent.remove(lineNumber - 1);
            System.out.println("已删除第"+deleteLine+"行："+deleteContent);
            LineData record = new LineData(deleteLine,deleteContent);
            records.add(record);
        } else {
            for(int i = 0; i < documentContent.size(); i ++) {
                String temp = getText(documentContent.get(i));
                if(temp.equals(content)) {
                    deleteLine = i + 1;
                    deleteContent = documentContent.get(deleteLine - 1);
                    found = true;
                    documentContent.remove(i);
                    System.out.println("已删除第"+deleteLine+"行："+deleteContent);
                    LineData record = new LineData(deleteLine,deleteContent);
                    records.add(record);
                    i -= 1;
                }
            }
        }
        if(!found) {
            System.out.println("未找到匹配内容，不进行删除。");
        }
        return records;
    }

    public Integer getLineNum() {
        return documentContent.size();
    }

    private boolean isValidFilePath(String filePath) {
        try {
            if(!filePath.endsWith(".md")) {
                System.out.println("只能打开.md文件。");
                return false;
            }
            // 将文件路径转化为 Path 对象
            Path path = Paths.get(filePath);
            // 检查文件是否存在，如果不存在，则创建文件
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            // 检查文件路径是否合法
            if (Files.isDirectory(path)) {
                System.out.println("路径"+filePath+"是一个目录，不是文件。");
                return false;
            }
            return true; // 文件路径合法
        } catch (Exception e) {
            // 发生异常，说明文件路径非法
            System.out.println("无效的文件路径：" + e.getMessage());
            return false;
        }
    }

    private boolean isValidLineNumber(int lineNumber) {
        return lineNumber >= 1 && lineNumber <= documentContent.size() + 1;
    }

    private String getText(String rawContent) {
        String pattern1 = "^(#+)\\s(.*)";
        Pattern r1 = Pattern.compile(pattern1);
        Matcher matcher1 = r1.matcher(rawContent);
        String pattern2 = "^[*\\-+]\\s(.*)";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher matcher2 = r2.matcher(rawContent);
        String pattern3 = "\\d+\\.\\s(.*)";
        Pattern r3 = Pattern.compile(pattern3);
        Matcher matcher3 = r3.matcher(rawContent);
        if (matcher1.find()) {
            return matcher1.group(2); // 返回#号数量
        } else if (matcher2.find()){
            return matcher2.group(1);
        } else if(matcher3.find()) {
            return matcher3.group(1);
        }
        return rawContent;
    }
}
