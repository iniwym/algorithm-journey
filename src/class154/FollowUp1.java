package class154;

// 可并堆2，java版
// 这道题课上没有讲，一个值得实现的左偏树模版题
// 完全是课上讲过的代码，看懂课就能看懂实现，注意打注释位置的代码即可
// 给定n个元素，编号为1到n，给定每个元素的值num[i]
// 初始时每个元素单独组成一个集合，接下来有m次操作，类型有四种，格式如下
// 操作 0 x y   : 集合x中删除元素y，题目保证y一定在集合x中
// 操作 1 x     : 查询集合x中的最小值，这是唯一需要打印的操作
// 操作 2 x y   : 集合y合并到集合x，题目保证以后不会再有以y做集合编号的操作
// 操作 3 x y z : 集合x中元素y的权值修改为z，题目保证修改操作值只可能变小
// 测试链接 : https://www.luogu.com.cn/problem/P11266
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FollowUp1 {

	public static int MAXN = 1000001;
	public static int n, m;

	public static int[] num = new int[MAXN];
	public static int[] up = new int[MAXN];
	public static int[] ls = new int[MAXN];
	public static int[] rs = new int[MAXN];
	public static int[] dist = new int[MAXN];
	public static int[] father = new int[MAXN];

	// root[x]表示集合x的堆头节点编号
	public static int[] root = new int[MAXN];

	public static void prepare() {
		dist[0] = -1;
		for (int i = 1; i <= n; i++) {
			up[i] = ls[i] = rs[i] = dist[i] = 0;
			father[i] = i;
			// 初始每个集合i只有元素i
			root[i] = i;
		}
	}

	public static int find(int i) {
		father[i] = father[i] == i ? i : find(father[i]);
		return father[i];
	}

	public static int merge(int i, int j) {
		if (i == 0 || j == 0) {
			return i + j;
		}
		int tmp;
		// 小根堆
		// 值越小越优先
		// 值一样，按照编号越小越优先
		if (num[i] > num[j] || (num[i] == num[j] && i > j)) {
			tmp = i;
			i = j;
			j = tmp;
		}
		rs[i] = merge(rs[i], j);
		up[rs[i]] = i;
		if (dist[ls[i]] < dist[rs[i]]) {
			tmp = ls[i];
			ls[i] = rs[i];
			rs[i] = tmp;
		}
		dist[i] = dist[rs[i]] + 1;
		father[ls[i]] = father[rs[i]] = i;
		return i;
	}

	public static int remove(int i) {
		int h = find(i);
		father[ls[i]] = ls[i];
		father[rs[i]] = rs[i];
		int s = merge(ls[i], rs[i]);
		int f = up[i];
		father[i] = s;
		up[s] = f;
		if (h != i) {
			father[s] = h;
			if (ls[f] == i) {
				ls[f] = s;
			} else {
				rs[f] = s;
			}
			for (int d = dist[s], tmp; dist[f] > d + 1; f = up[f], d++) {
				dist[f] = d + 1;
				if (dist[ls[f]] < dist[rs[f]]) {
					tmp = ls[f];
					ls[f] = rs[f];
					rs[f] = tmp;
				}
			}
		}
		up[i] = ls[i] = rs[i] = dist[i] = 0;
		return father[s];
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		prepare();
		for (int i = 1; i <= n; i++) {
			num[i] = in.nextInt();
		}
		for (int i = 1, op, x, y, z; i <= m; i++) {
			op = in.nextInt();
			if (op == 0) {
				x = in.nextInt();
				y = in.nextInt();
				// 删除后需要更新集合x的堆头
				root[x] = remove(y);
				if (root[x] != 0) {
					father[root[x]] = root[x];
					up[root[x]] = 0;
				}
			} else if (op == 1) {
				x = in.nextInt();
				out.println(num[root[x]]);
			} else if (op == 2) {
				x = in.nextInt();
				y = in.nextInt();
				// 合并集合x和集合y
				root[x] = merge(root[x], root[y]);
				if (root[x] != 0) {
					father[root[x]] = root[x];
					up[root[x]] = 0;
				}
				// 集合y被清空
				// 题目保证不会再涉及这个集合
				root[y] = 0;
			} else {
				x = in.nextInt();
				y = in.nextInt();
				z = in.nextInt();
				int h = remove(y);
				num[y] = z;
				// y改值后重新作为单点堆
				father[y] = y;
				// 合并到集合x
				root[x] = merge(h, y);
				if (root[x] != 0) {
					father[root[x]] = root[x];
					up[root[x]] = 0;
				}
			}
		}
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {

		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}
