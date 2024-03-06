package org.example.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringListAdapter implements TreeNode {
    private final List<String> data;
    boolean hasNext;

    public StringListAdapter(List<String> data, boolean hasNext) {
        this.data = data;
        this.hasNext = hasNext;
    }

    @Override
    public String getName() {
        return getTrueContent(data.get(0));
    }

    @Override
    public List<TreeNode> getChildren(boolean isRoot) {
        List<TreeNode> children = new ArrayList<>();
        List<Integer> levels = getContentLevels();
        int start_index = 0;
        int end_index = data.size();
        if(isRoot) {
            // 当前node为根节点
            end_index = getNext(0,levels);
            start_index = 1;
        }
        for(int i = start_index; i < end_index;) {
            int next_index = getNext(i,levels);
            List<String> subContent = data.subList(i,next_index);
            i = next_index;
            if(next_index < end_index) {
                children.add(new StringListAdapter(subContent, true));
            } else {
                children.add(new StringListAdapter(subContent, false));
            }
        }
        return children;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    private Integer getLevel(String line) {
        String pattern = "^(#+)\\s";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);

        if (matcher.find()) {
            String match = matcher.group(1); // 获取匹配的#号字符串
            return match.length(); // 返回#号数量
        } else {
            return 0;
        }
    }
    private List<Integer> getContentLevels() {
        int preLevel = getLevel(data.get(0));
        int nowLevel;
        List<Integer> levels = new ArrayList<>();
        for(String s:data) {
            nowLevel = getLevel(s);
            if(nowLevel == 0) {
                // 文本项
                levels.add(preLevel + 1);
            } else {
                preLevel = nowLevel;
                levels.add(nowLevel);
            }
        }
        return levels;
    }
    private int getNext(int pos, List<Integer> levels) {
        int pos_level = levels.get(pos);
        for (int i = pos + 1; i <levels.size(); i ++) {
            if(levels.get(i) <= pos_level) {
                return i;
            }
        }
        return levels.size();
    }
    public static String getTrueContent(String rawStr) {
        String pattern1 = "^(#+)\\s(.*)";
        Pattern r1 = Pattern.compile(pattern1);
        Matcher matcher1 = r1.matcher(rawStr);
        String pattern2 = "^[*\\-+]\\s(.*)";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher matcher2 = r2.matcher(rawStr);
        if (matcher1.find()) {
            return matcher1.group(2);
        } else if (matcher2.find()){
            return "·" + matcher2.group(1);
        }
        return rawStr;
    }
}
