import org.example.Document;
import org.example.MarkdownEditor;
import org.example.Workspace;
import org.example.WorkspaceManager;
import org.example.command.fileCommand.LoadCommand;
import org.example.memento.Originator;
import org.example.tool.LineData;
import org.example.tool.Statistics;
import org.example.tree.StringListAdapter;
import org.example.tree.TreeNode;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.tree.TreePrinter.printTree;
import static org.junit.jupiter.api.Assertions.*;

public class MarkdownEditorTest {

    private MarkdownEditor markdownEditor;
    private Document document;

    @Test
    public void showTest() {
        document = new Document();
        document.load("test.md");
        document.list();
    }

    @Test
    public void loadSaveTest() {
        document = new Document();
        boolean saveFlag = document.save();
        assertFalse(saveFlag);

        boolean loadFlag = document.load("test.md");
        assertTrue(loadFlag);
        saveFlag = document.save();
        assertTrue(saveFlag);
    }
    @Test
    public void insertDeleteTest() {
        document = new Document();
        document.load("test.md");
        boolean insertFlag = document.insert(8,"aaa");
        assertTrue(insertFlag);
        // 找到内容删除
        List<LineData> deleteContent = document.delete(-1,"aaa");
        assertNotEquals(0, deleteContent.size());
        // 未找到内容
        deleteContent = document.delete(-1,"aaa");
        assertEquals(0, deleteContent.size());
    }
    @Test
    public void listTreeTest() {
        document = new Document();
        document.load("test.md");
        document.list();
        document.listTree();
    }
    @Test
    public void statsTimeTest() throws IOException, InterruptedException {
        // 写不来单元测试orz
        Statistics statistics = new Statistics("./src/main/resources/log/log.txt");
        List<String> files = new ArrayList<>();
        files.add("test.md");
        statistics.fileInitial(files,"test.md");
        Thread.sleep(1000);

        // 输出重定向
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        // 打印当前所有文件的stats
        statistics.printAllStats();

        System.setOut(new PrintStream(System.out));
        String capturedOutput = outputStream.toString();
        assertEquals("test.md 0min1s\r\n",capturedOutput);
    }
    @Test
    public void workspaceTest() {
        WorkspaceManager workspaceManager = new WorkspaceManager();
        Workspace workspace1 = workspaceManager.createWorkspace("test.md");
        workspace1.load("test.md");
        // 不能重复load
        assertNull(workspaceManager.createWorkspace("test.md"));

        Workspace workspace_active = workspaceManager.getActiveWorkspace();
        assertSame(workspace1,workspace_active);

        Workspace workspace2 = workspaceManager.createWorkspace("test1.md");
        workspace2.load("test1.md");
        workspace_active = workspaceManager.openWorkspace("test1");
        assertSame(workspace2,workspace_active);
        assertNotSame(workspace1,workspace_active);
    }
    @Test
    public void originatorTest() throws IOException {
        WorkspaceManager workspaceManager = new WorkspaceManager();
        Workspace ws = workspaceManager.createWorkspace("test.md");
        LoadCommand loadCommand = new LoadCommand(ws, "test.md");
        loadCommand.execute();
        Originator.saveMemento(workspaceManager);

        // 从文件加载状态
        WorkspaceManager loadedWorkspaceManager = Originator.restoreFromMemento();
        assertNotNull(loadedWorkspaceManager);
    }

    @Test
    public void treeAdapter() {
        List<String> dataList = Arrays.asList("Root", "# Node1", "## Node1.1", "## Node1.2", "# Node2", "## Node2.1");
        TreeNode stringListNode = new StringListAdapter(dataList,false);
        // 输出字符串数组的树形结构
        System.out.println("String Array Tree:");
        printTree(stringListNode, 0,"");
    }

    @Test
    public void modifyInput() throws IOException {
        // 模拟命令行输入
        // 不好的单元测试
        String str = "load test.md\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }

    @Test
    public void redoTest() throws IOException {
        String str = "load test.md\n" +
                "delete insert1\n" +
                "undo\n" +
                "redo\n" +
                "save\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }

    @Test
    public void wholeEditTest() throws IOException {
        String str = "load test1.md\n" +
                "insert ## 程序设计\n" +
                "append-head # 我的资源\n" +
                "append-tail ### 软件设计\n" +
                "append-tail #### 设计模式\n" +
                "append-tail 1. 观察者模式\n" +
                "append-tail 3. 单例模式\n" +
                "insert 6 2. 策略模式\n" +
                "delete 单例模式\n" +
                "append-tail 3. 组合模式\n" +
                "list-tree\n" +
                "append-tail ## ⼯具箱\n" +
                "append-tail ### Adobe\n" +
                "list-tree\n" +
                "save\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }

    @Test
    public void wholeUndoRedoTest() throws IOException {
        String str = "load test2.md\n" +
                "append-head # 旅⾏清单\n" +
                "append-tail ## 亚洲\n" +
                "append-tail 1. 中国\n" +
                "append-tail 2. ⽇本\n" +
                "delete 亚洲\n" +
                "undo\n" +
                "redo\n" +
                "list-tree\n" +
                "save\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }
    @Test
    public void mixEditUndoTest() throws IOException {
        String str = "load test3.md\n" +
                "append-head # 书籍推荐\n" +
                "append-tail * 《深入理解计算机系统》\n" +
                "undo\n" +
                "append-tail ## 编程\n" +
                "append-tail * 《设计模式的艺术》\n" +
                "redo\n" +
                "list-tree\n" +
                "append-tail * 《云原⽣：运⽤容器、函数计算和数据构建下⼀代应⽤》\n" +
                "append-tail * 《深入理解Java虚拟机》\n" +
                "undo\n" +
                "redo\n" +
                "list-tree\n" +
                "save\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }
    @Test
    public void changeSessionTest() throws IOException {
        String str = "load test4.md\n" +
                "append-head # 旅⾏清单\n" +
                "append-tail ## 亚洲\n" +
                "save\n" +
                "append-tail 1. 中国\n" +
                "append-tail 2. ⽇本\n" +
                "append-tail ## 欧洲\n" +
                "open test3\n" +
                "list-tree\n" +
                "open test4\n" +
                "list-tree\n" +
                "save\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }
    @Test
    public void mixAllTest() throws IOException {
        String str = "load test5.md\n" +
                "append-head # 旅⾏清单\n" +
                "append-tail ## 欧洲\n" +
                "insert 2 ## 亚洲\n" +
                "insert 3 1. 中国\n" +
                "insert 4 2. ⽇本\n" +
                "save\n" +
                "undo\n" +
                "list-tree\n" +
                "delete 亚洲\n" +
                "list-tree\n" +
                "history 2\n" +
                "undo\n" +
                "list-tree\n" +
                "redo\n" +
                "list-tree\n" +
                "redo\n" +
                "list-tree\n" +
                "save\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        System.setIn(inputStream);
        markdownEditor = new MarkdownEditor();
        markdownEditor.start();
    }
}
