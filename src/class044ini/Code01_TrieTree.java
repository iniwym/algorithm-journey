package class044ini;

import java.util.HashMap;
import java.util.Map;


/**
 * @Description: 前缀树类描述的实现方式（动态结构）
 * @Author: iniwym
 * @Date: 2025-01-06
 */
public class Code01_TrieTree {

    public static class TrieTree1 {

        public class TrieNode {
            public int pass;
            public int end;
            public TrieNode[] nexts;

            public TrieNode() {
                pass = 0;
                end = 0;
                nexts = new TrieNode[26];
            }
        }

        private TrieNode root;

        public TrieTree1() {
            root = new TrieNode();
        }

        /**
         * 在Trie树中插入一个单词
         *
         * @param word 要插入的单词字符串
         */
        /**
         * 在Trie树中插入一个单词
         *
         * @param word 要插入的单词字符串，不能为空
         */
        public void insert(String word) {
            // 检查输入单词是否为空或null，如果是，则不进行插入操作
            if (word == null || word.isEmpty()) {
                return;
            }
            // 将输入的单词转换为字符数组，便于后续处理
            char[] chars = word.toCharArray();
            // 从Trie树的根节点开始插入单词
            TrieNode node = root;
            // 增加经过根节点的单词数量
            root.pass++;
            // 遍历单词的每个字符，将其插入到Trie树中
            for (int i = 0, path; i < word.length(); i++) {
                // 计算当前字符在字母表中的索引位置
                path = chars[i] - 'a';
                // 如果当前字符对应的节点不存在，则创建一个新的节点
                if (node.nexts[path] == null) {
                    node.nexts[path] = new TrieNode();
                }
                // 将当前节点指向下一个节点，以便插入下一个字符
                node = node.nexts[path];
                // 增加经过当前节点的单词数量
                node.pass++;
            }
            // 完成单词的插入，增加以当前节点结尾的单词数量
            node.end++;
        }

        /**
         * 计算给定单词在字典树中出现的次数
         *
         * @param word 待查询的单词
         * @return 单词在字典树中出现的次数如果单词不存在，则返回0
         */
        public int countWordsEqualTo(String word) {
            // 健壮性判断，如果输入的单词为空或null，则直接返回0
            if (word == null || word.isEmpty()) {
                return 0;
            }
            // 将单词转换为字符数组，以便逐个字符处理
            char[] chars = word.toCharArray();
            // 从字典树的根节点开始搜索
            TrieNode node = root;
            // 逐个字符在字典树中查找
            for (int i = 0, path; i < word.length(); i++) {
                // 计算字符在nexts数组中的索引
                path = chars[i] - 'a';
                // 如果当前字符对应的节点不存在，则说明单词不在字典树中，返回0
                if (node.nexts[path] == null) {
                    return 0;
                }
                // 移动到下一个节点：更新当前节点为下一个要查找的字符对应的节点
                node = node.nexts[path];
            }
            // 返回单词出现的次数：当前节点的end值表示单词出现的次数
            return node.end;
        }

        /**
         * 计算给定前缀在Trie树中出现的单词数量
         *
         * @param pre 前缀字符串，用于搜索Trie树中以此前缀开始的单词数量
         * @return 返回以此前缀开始的单词数量如果前缀为空或null，则返回0
         */
        public int countWordsStartingWith(String pre) {
            // 检查前缀是否为空或null，如果是，则返回0，因为没有有效的前缀可以搜索
            if (pre == null || pre.isEmpty()) {
                return 0;
            }
            // 将前缀字符串转换为字符数组，以便逐个字符搜索Trie树
            char[] chars = pre.toCharArray();
            // 从Trie树的根节点开始搜索
            TrieNode node = root;
            // 遍历前缀中的每个字符，path用于计算字符在nexts数组中的索引
            for (int i = 0, path; i < pre.length(); i++) {
                // 计算当前字符在nexts数组中的索引
                path = chars[i] - 'a';
                // 如果当前字符对应的节点不存在，则返回0，表示没有单词以给定前缀开始
                if (node.nexts[path] == null) {
                    return 0;
                }
                // 移动到当前字符对应的节点，继续搜索下一个字符
                node = node.nexts[path];
            }
            // 返回通过当前前缀搜索到的最后一个节点上的pass值，表示以此前缀开始的单词数量
            return node.pass;
        }

        /**
         * 从字典树中删除指定的单词
         *
         * @param word 待删除的单词如果为null或空字符串，则不执行任何操作
         */
        public void erase(String word) {
            // 检查输入是否为null或空，如果是，则直接返回，不进行删除操作
            if (word == null || word.isEmpty()) {
                return;
            }

            // 检查字典树中是否存在待删除的单词，如果存在，则进行删除操作
            if (countWordsEqualTo(word) > 0) {
                // 将单词转换为字符数组，便于逐个字符处理
                char[] chars = word.toCharArray();
                // 从字典树的根节点开始
                TrieNode node = root;
                // 减少经过根节点的单词数量
                node.pass--;
                // 遍历单词的每个字符
                for (int i = 0, path; i < word.length(); i++) {
                    // 计算当前字符对应的子节点路径
                    path = chars[i] - 'a';
                    // 减少经过当前节点的单词数量，如果减到0，说明当前节点不再被任何单词经过，可以删除
                    if (--node.nexts[path].pass == 0) {
                        // 将当前节点的子节点设置为null，表示删除该节点
                        node.nexts[path] = null;
                        // 删除完成后，直接返回
                        return;
                    }
                    // 移动到下一个节点
                    node = node.nexts[path];
                }
                // 减少以当前节点为结尾的单词数量
                node.end--;
            }
        }
    }

    public static class TrieTree2 {

        public class TrieNode {

            public int pass;
            public int end;
            public Map<Integer, TrieNode> nexts;

            public TrieNode() {
                pass = 0;
                end = 0;
                nexts = new HashMap<>();
            }
        }

        private TrieNode root;

        public TrieTree2() {
            root = new TrieNode();
        }

        /**
         * 在 Trie 树中插入一个单词
         *
         * @param word 要插入的单词，不能为空
         */
        public void insert(String word) {
            // 检查输入单词是否为空或 null，如果是，则直接返回，不进行插入操作
            if (word == null || word.isEmpty()) {
                return;
            }
            // 将单词转换为字符数组，便于逐个字符处理
            char[] chars = word.toCharArray();

            // 从 Trie 树的根节点开始插入单词
            TrieNode node = root;
            // 增加经过根节点的单词数量
            node.pass++;
            // 遍历单词中的每个字符，将其插入到 Trie 树中
            for (int i = 0, path; i < word.length(); i++) {
                // 计算当前字符对应的路径索引
                path = chars[i];
                // 检查当前节点的子节点中是否已存在当前字符，如果不存在，则创建一个新的子节点
                if (!node.nexts.containsKey(path)) {
                    node.nexts.put(path, new TrieNode());
                }
                // 移动到当前字符对应的子节点
                node = node.nexts.get(path);
                // 增加经过当前节点的单词数量
                node.pass++;
            }
            // 增加以当前单词结尾的单词数量
            node.end++;
        }

        /**
         * 计算给定单词在字典树中出现的次数
         * 如果单词为空或null，则返回0
         * 该方法通过遍历字典树来查找单词，利用字典树节点的end属性来获取单词出现的次数
         *
         * @param word 待查找的单词
         * @return 单词出现的次数如果单词为空或null，返回0
         */
        public int countWordsEqualTo(String word) {
            // 检查输入单词是否为空或null，如果是，则返回0
            if (word == null || word.isEmpty()) {
                return 0;
            }
            // 将单词转换为字符数组，便于逐个字符处理
            char[] chars = word.toCharArray();
            // 从字典树的根节点开始查找
            TrieNode node = root;
            // 遍历单词中的每个字符
            for (int i = 0, path; i < word.length(); i++) {
                // 获取当前字符的ASCII码
                path = chars[i];
                // 检查当前字符对应的节点是否存在，如果不存在，则说明单词不在字典树中，返回0
                if (!node.nexts.containsKey(path)) {
                    return 0;
                }
                // 移动到下一个节点，继续查找
                node = node.nexts.get(path);
            }
            // 返回单词出现的次数
            return node.end;
        }

        /**
         * 计算字典树中以特定前缀开头的单词数量
         *
         * @param pre 指定的前缀字符串
         * @return 以指定前缀开头的单词数量如果前缀为空或null，则返回0
         */
        public int countWordsStartingWith(String pre) {
            // 检查输入单词是否为空或null，如果是，则返回0
            if (pre == null || pre.isEmpty()) {
                return 0;
            }
            // 将单词转换为字符数组，便于逐个字符处理
            char[] chars = pre.toCharArray();
            // 从字典树的根节点开始查找
            TrieNode node = root;
            // 遍历单词中的每个字符
            for (int i = 0, path; i < pre.length(); i++) {
                // 获取当前字符的ASCII码
                path = chars[i];
                // 检查当前字符对应的节点是否存在，如果不存在，则说明单词不在字典树中，返回0
                if (!node.nexts.containsKey(path)) {
                    return 0;
                }
                // 移动到下一个节点，继续查找
                node = node.nexts.get(path);
            }
            // 返回单词出现的次数
            return node.pass;
        }

        /**
         * 从字典树中删除指定的单词
         *
         * @param word 待删除的单词字符串
         */
        public void erase(String word) {
            // 检查输入单词是否为空或null，如果是，则直接返回，不进行删除操作
            if (word == null || word.isEmpty()) {
                return;
            }
            // 检查字典树中是否存在与输入单词完全匹配的单词，如果存在，则进行删除操作
            if (countWordsEqualTo(word) > 0) {
                // 将输入的单词转换为字符数组，以便逐个字符处理
                char[] chars = word.toCharArray();
                // 从字典树的根节点开始遍历
                TrieNode node = root;
                // 减少当前节点的pass计数，表示经过该节点的单词数量减少
                node.pass--;
                // 遍历输入单词的每个字符
                for (int i = 0, path; i < word.length(); i++) {
                    // 获取当前字符的ASCII码，用作路径索引
                    path = chars[i];
                    // 减少当前字符对应子节点的pass计数，并检查是否变为0
                    if (--node.nexts.get(path).pass == 0) {
                        // 如果当前字符对应的子节点的pass计数为0，说明没有单词经过该节点，将其从父节点的子节点集合中移除
                        node.nexts.remove(path);
                        // 删除操作完成，直接返回
                        return;
                    }
                    // 移动到下一个节点，继续遍历
                    node = node.nexts.get(path);
                }
                // 减少当前节点的end计数，表示以该节点为结尾的单词数量减少
                node.end--;
            }
        }

    }

    // 使用HashMap实现的简单词频统计类
    public static class Right {

        // 存储单词及其出现次数的HashMap
        private HashMap<String, Integer> box;

        // 构造方法，初始化HashMap
        public Right() {
            box = new HashMap<>();
        }

        // 插入单词方法
        public void insert(String word) {
            // 忽略空或null字符串
            if (word == null || word.isEmpty()) {
                return;
            }
            // 如果单词不在HashMap中，则插入单词及其初始计数1
            if (!box.containsKey(word)) {
                box.put(word, 1);
            } else {
                // 如果单词已存在，则将其计数加1
                box.put(word, box.get(word) + 1);
            }
        }

        // 搜索单词方法
        public int search(String word) {
            // 对于空或null字符串，返回0
            if (word == null || word.isEmpty()) {
                return 0;
            }
            // 如果单词在HashMap中，则返回其计数
            if (box.containsKey(word)) {
                return box.get(word);
            }
            // 如果单词不在HashMap中，则返回0
            return 0;
        }

        // 统计具有相同前缀的单词数量方法
        public int prefixNumber(String pre) {
            // 对于空或null字符串前缀，返回0
            if (pre == null || pre.isEmpty()) {
                return 0;
            }
            int count = 0;
            // 遍历HashMap中的所有单词
            for (String str : box.keySet()) {
                // 如果单词以给定前缀开始，则将其计数累加到总计数中
                if (str.startsWith(pre)) {
                    count += box.get(str);
                }
            }
            // 返回总计数
            return count;
        }

        // 删除单词方法
        public void erase(String word) {
            // 忽略空或null字符串
            if (word == null || word.isEmpty()) {
                return;
            }
            // 如果单词在HashMap中
            if (box.containsKey(word)) {
                // 将其计数减1
                box.put(word, box.get(word) - 1);
                // 如果单词计数变为0，则从HashMap中移除该单词
                if (box.get(word) == 0) {
                    box.remove(word);
                }
            }
        }
    }

    /**
     * 生成指定长度的随机字符串
     * 该方法用于创建一个由小写字母组成的随机字符串，主要用于测试或填充数据
     *
     * @param strLength 期望的字符串最大长度，实际长度为从1到length之间的随机数
     * @return 生成的随机字符串
     */
    public static String generateRandomString(int strLength) {
        // 初始化字符数组，长度为1到length之间的随机数
        char[] ans = new char[(int) (Math.random() * strLength + 1)];
        // 遍历字符数组，为每个位置填充一个随机的小写字母
        for (int i = 0; i < ans.length; i++) {
            // 生成一个随机的小写字母，ASCII值为'a'加上0到25之间的随机数
            ans[i] = (char) ('a' + (int) (Math.random() * 26));
        }
        // 将字符数组转换为字符串并返回
        return String.valueOf(ans);
    }

    /**
     * 生成一个随机字符串数组
     * <p>
     * 本方法用于创建一个指定数组长度范围内的随机字符串数组每个字符串的长度也是随机生成的，但不超过给定的字符串长度
     * 这种方法可以用于测试、数据模拟等场景，以生成样本数据
     *
     * @param arrLength 期望的数组最大长度，实际长度将是一个小于或等于此值的随机整数
     * @param strLength 生成的字符串的最大长度
     * @return 一个由随机字符串组成的数组
     */
    public static String[] generateRandomStringArray(int arrLength, int strLength) {
        // 随机决定数组的实际长度，确保数组长度在1到arrLength之间
        String[] ans = new String[(int) (Math.random() * arrLength + 1)];
        // 遍历数组的每个元素，为其赋值
        for (int i = 0; i < ans.length; i++) {
            // 为数组的当前元素生成一个随机字符串
            ans[i] = generateRandomString(strLength);
        }
        // 返回填充了随机字符串的数组
        return ans;
    }

    /**
     * 主程序入口
     * 用于测试两种 Trie 树实现和一种参考实现的正确性和性能
     * 通过生成随机字符串数组并进行插入、删除、查询等操作来验证三种实现的一致性
     */
    public static void main(String[] args) {

        // 定义随机生成字符串数组的长度
        int arrlength = 100;
        // 定义随机字符串的长度
        int strlength = 10;
        // 定义测试的总次数
        int testTimes = 100000;

        // 循环进行测试
        for (int i = 0; i < testTimes; i++) {
            // 生成随机字符串数组
            String[] arr = generateRandomStringArray(arrlength, strlength);
            // 初始化两种 Trie 树和一种参考实现
            TrieTree1 trieTree1 = new TrieTree1();
            TrieTree2 trieTree2 = new TrieTree2();
            Right right = new Right();
            // 对数组中的每个字符串进行操作
            for (int j = 0; j < arr.length; j++) {
                // 随机决定操作类型
                double random = Math.random();
                // 25%的概率进行插入操作
                if (random < 0.25) {
                    trieTree1.insert(arr[j]);
                    trieTree2.insert(arr[j]);
                    right.insert(arr[j]);
                    // 25%的概率进行删除操作
                } else if (random < 0.5) {
                    trieTree1.erase(arr[j]);
                    trieTree2.erase(arr[j]);
                    right.erase(arr[j]);
                    // 25%的概率进行完全匹配查询操作
                } else if (random < 0.75) {
                    int ans1 = trieTree1.countWordsEqualTo(arr[j]);
                    int ans2 = trieTree2.countWordsEqualTo(arr[j]);
                    int ans3 = right.search(arr[j]);
                    // 比较三种实现的结果是否一致
                    if (ans1 != ans2 || ans1 != ans3) {
                        System.out.println("出错了!");
                    }

                    // 25%的概率进行前缀匹配查询操作
                } else {
                    int ans1 = trieTree1.countWordsStartingWith(arr[j]);
                    int ans2 = trieTree2.countWordsStartingWith(arr[j]);
                    int ans3 = right.prefixNumber(arr[j]);
                    // 比较三种实现的结果是否一致
                    if (ans1 != ans2 || ans1 != ans3) {
                        System.out.println("出错了!");
                    }
                }
            }
        }

        // 所有测试完成
        System.out.println("测试结束");
    }

}
           