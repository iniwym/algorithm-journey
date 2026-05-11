package class198;

// 喵了个喵 II，树状数组优化建图，java版
// 给定一个长度为4n的数组arr，1~n每种数字都出现4次
// 从左到右把每个数字划分进序列0或序列1，要求划分出的两个序列完全相等
// 如果能做到，先打印"Yes"，然后从左到右打印每个位置划分进了什么序列
// 如果不能做到，打印"No"
// 1 <= n <= 5 * 10^4
// 测试链接 : https://www.luogu.com.cn/problem/P9139
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code06_Meow_II_java_1 {

	public static int MAXV = 50001;
	public static int MAXN = 200001;
	public static int MAXT = 5000001;
	public static int MAXE = 20000001;
	public static int v, n, cntt;

	// 值v、位置i
	public static int[][] vi = new int[MAXN][2];
	// 左l、右r、决定编号x
	public static int[][] group = new int[MAXN][3];
	public static int cntp;

	public static int[] head = new int[MAXT];
	public static int[] nxt = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int cntg;

	public static int[] dfn = new int[MAXT];
	public static int[] low = new int[MAXT];
	public static int cntd;

	public static int[] sta = new int[MAXT];
	public static int top;

	public static int[] belong = new int[MAXT];
	public static int sccCnt;

	// 树状数组
	public static int[] inTree = new int[MAXN];
	public static int[] outTree = new int[MAXN];

	public static int[] ans = new int[MAXN];

	// 迭代版需要的栈，讲解118讲了递归改迭代的技巧
	public static int[] stau = new int[MAXT];
	public static int[] stas = new int[MAXT];
	public static int[] stae = new int[MAXT];
	public static int u, status, e;
	public static int stacksize;

	public static void push(int u, int status, int e) {
		stau[stacksize] = u;
		stas[stacksize] = status;
		stae[stacksize] = e;
		stacksize++;
	}

	public static void pop() {
		stacksize--;
		u = stau[stacksize];
		status = stas[stacksize];
		e = stae[stacksize];
	}

	public static void addGroup(int l, int r, int x) {
		group[++cntp][0] = l;
		group[cntp][1] = r;
		group[cntp][2] = x;
	}

	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版
	public static void tarjan1(int u) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan1(v);
				low[u] = Math.min(low[u], low[v]);
			} else {
				if (belong[v] == 0) {
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}
		if (dfn[u] == low[u]) {
			sccCnt++;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = sccCnt;
			} while (pop != u);
		}
	}

	// 迭代版
	public static void tarjan2(int node) {
		stacksize = 0;
		push(node, -1, -1);
		int v;
		while (stacksize > 0) {
			pop();
			if (status == -1) {
				dfn[u] = low[u] = ++cntd;
				sta[++top] = u;
				e = head[u];
			} else {
				v = to[e];
				if (status == 0) {
					low[u] = Math.min(low[u], low[v]);
				}
				if (status == 1 && belong[v] == 0) {
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nxt[e];
			}
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					push(u, 0, e);
					push(v, -1, -1);
				} else {
					push(u, 1, e);
				}
			} else {
				if (dfn[u] == low[u]) {
					sccCnt++;
					int pop;
					do {
						pop = sta[top--];
						belong[pop] = sccCnt;
					} while (pop != u);
				}
			}
		}
	}

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void addOut(int i, int x) {
		while (i <= n) {
			int preo = outTree[i];
			int curo = ++cntt;
			if (preo > 0) {
				addEdge(preo, curo);
			}
			addEdge(x, curo);
			outTree[i] = curo;
			i += lowbit(i);
		}
	}

	public static void addIn(int i, int x) {
		while (i <= n) {
			int prei = inTree[i];
			int curi = ++cntt;
			if (prei > 0) {
				addEdge(curi, prei);
			}
			addEdge(curi, x);
			inTree[i] = curi;
			i += lowbit(i);
		}
	}

	public static void rangeToX(int i, int x) {
		while (i > 0) {
			if (outTree[i] > 0) {
				addEdge(outTree[i], x);
			}
			i -= lowbit(i);
		}
	}

	public static void xToRange(int x, int i) {
		while (i > 0) {
			if (inTree[i] > 0) {
				addEdge(x, inTree[i]);
			}
			i -= lowbit(i);
		}
	}

	public static int other(int x) {
		return x <= v ? x + v : x - v;
	}

	public static void buildGraph() {
		Arrays.sort(group, 1, cntp + 1, (a, b) -> a[1] - b[1]);
		cntt = v << 1;
		for (int i = 1; i <= cntp; i++) {
			int l = group[i][0];
			// 后缀查询转化成前缀查询
			int p = n - l + 1;
			int x = group[i][2];
			xToRange(x, p);
			rangeToX(p, other(x));
			addIn(p, other(x));
			addOut(p, x);
		}
	}

	public static boolean compute() {
		Arrays.sort(vi, 1, n + 1, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
		for (int i = 1; i <= n; i += 4) {
			int x = vi[i][0];
			int a = vi[i][1];
			int b = vi[i + 1][1];
			int c = vi[i + 2][1];
			int d = vi[i + 3][1];
			addGroup(a, c, x);
			addGroup(b, d, x);
			addGroup(a, b, x + v);
			addGroup(c, d, x + v);
		}
		buildGraph();
		for (int i = 1; i <= v << 1; i++) {
			if (dfn[i] == 0) {
				// tarjan1(i);
				tarjan2(i);
			}
		}
		boolean check = true;
		for (int i = 1; i <= v; i++) {
			if (belong[i] == belong[i + v]) {
				check = false;
				break;
			}
		}
		if (check) {
			for (int i = 1; i <= n; i += 4) {
				int x = vi[i][0];
				int a = vi[i][1];
				int b = vi[i + 1][1];
				int c = vi[i + 2][1];
				int d = vi[i + 3][1];
				if (belong[x] < belong[x + v]) {
					ans[a] = 0;
					ans[b] = 0;
					ans[c] = 1;
					ans[d] = 1;
				} else {
					ans[a] = 0;
					ans[b] = 1;
					ans[c] = 0;
					ans[d] = 1;
				}
			}
		}
		return check;
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		v = in.nextInt();
		n = v << 2;
		for (int i = 1; i <= n; i++) {
			vi[i][0] = in.nextInt();
			vi[i][1] = i;
		}
		boolean check = compute();
		if (check) {
			out.println("Yes");
			for (int i = 1; i <= n; i++) {
				out.print(ans[i]);
			}
		} else {
			out.println("No");
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
