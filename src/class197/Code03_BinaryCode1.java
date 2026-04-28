package class197;

// 编码，java版
// 一共有n个字符串，都由字符 '0'、'1'、'?' 组成
// 每个字符串最多含有一个'?'，如果不含有，说明该字符串已经确定了
// 如果字符串含有'?'，那么这个'?'必须变成'0'或'1'中的一个
// 请把所有字符串都确定下来，要求任意一个字符串不是其它字符串的前缀
// 存在方案打印"YES"，然后打印每个确定后的字符串，不存在方案打印"NO"
// 1 <= n、字符总数 <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P6965
// 测试链接 : https://hydro.ac/p/bzoj-P4840
// 测试链接 : https://loj.ac/p/6036
// 第三个测试链接，只打印"YES"或"NO"，不打印每个确定后的字符串
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_BinaryCode1 {

	public static int MAXN = 500001;
	public static int MAXP = 1000002;
	public static int MAXT = 5000001;
	public static int MAXE = 10000001;
	public static int n, cntt;

	public static String[] arrs = new String[MAXN];
	public static int[] pending = new int[MAXN];

	public static int[] headg = new int[MAXT];
	public static int[] nextg = new int[MAXE];
	public static int[] tog = new int[MAXE];
	public static int cntg;

	public static int[] headx = new int[MAXP];
	public static int[] nextx = new int[MAXP];
	public static int[] tox = new int[MAXP];
	public static int cntx;

	public static int[][] tree = new int[MAXP][2];
	public static int[] fa = new int[MAXP];
	public static int[] up = new int[MAXP];
	public static int[] down = new int[MAXP];
	public static int cntp;

	public static int[] group = new int[MAXN];
	public static int gsiz;

	public static int[] dfn = new int[MAXT];
	public static int[] low = new int[MAXT];
	public static int cntd;

	public static int[] sta = new int[MAXT];
	public static int top;

	public static int[] belong = new int[MAXT];
	public static int sccCnt;

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

	public static void addEdge(int u, int v) {
		nextg[++cntg] = headg[u];
		tog[cntg] = v;
		headg[u] = cntg;
	}

	public static void addx(int u, int x) {
		nextx[++cntx] = headx[u];
		tox[cntx] = x;
		headx[u] = cntx;
	}

	// 递归版
	public static void tarjan1(int u) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = headg[u]; e > 0; e = nextg[e]) {
			int v = tog[e];
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
				e = headg[u];
			} else {
				v = tog[e];
				if (status == 0) {
					low[u] = Math.min(low[u], low[v]);
				}
				if (status == 1 && belong[v] == 0) {
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nextg[e];
			}
			if (e != 0) {
				v = tog[e];
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

	public static int other(int x) {
		return x <= n ? x + n : x - n;
	}

	public static void findPending(int i) {
		String str = arrs[i];
		pending[i] = -1;
		for (int si = 0; si < str.length(); si++) {
			if (str.charAt(si) == '?') {
				pending[i] = si;
				break;
			}
		}
		if (pending[i] == -1) {
			if (str.charAt(0) == '0') {
				addEdge(i + n, i);
			} else {
				addEdge(i, i + n);
			}
			pending[i] = 0;
		}
	}

	public static void insert(int i, int confirm, int x) {
		String str = arrs[i];
		int pi = pending[i];
		int cur = 1;
		for (int si = 0, path; si < str.length(); si++) {
			if (pi == si) {
				path = confirm;
			} else {
				path = str.charAt(si) == '0' ? 0 : 1;
			}
			if (tree[cur][path] == 0) {
				tree[cur][path] = ++cntp;
				fa[cntp] = cur;
				up[cntp] = ++cntt;
				down[cntp] = ++cntt;
				addEdge(up[cntp], up[cur]);
				addEdge(down[cur], down[cntp]);
			}
			cur = tree[cur][path];
		}
		addEdge(x, up[fa[cur]]);
		addEdge(up[cur], other(x));
		addEdge(down[fa[cur]], other(x));
		addEdge(x, down[cur]);
		addx(cur, x);
	}

	public static void groupLink() {
		if (gsiz > 1) {
			cntt++;
			addEdge(cntt, other(group[1]));
			for (int i = 2; i <= gsiz; i++) {
				cntt++;
				addEdge(cntt, other(group[i]));
				addEdge(group[i], cntt - 1);
				addEdge(cntt, cntt - 1);
			}
			cntt++;
			addEdge(cntt, other(group[gsiz]));
			for (int i = gsiz - 1; i >= 1; i--) {
				cntt++;
				addEdge(cntt, other(group[i]));
				addEdge(group[i], cntt - 1);
				addEdge(cntt, cntt - 1);
			}
		}
	}

	public static void buildGraph() {
		cntt = n << 1;
		cntp = 1;
		up[1] = ++cntt;
		down[1] = ++cntt;
		for (int i = 1; i <= n; i++) {
			findPending(i);
			insert(i, 0, i);
			insert(i, 1, i + n);
		}
		for (int u = 1; u <= cntp; u++) {
			gsiz = 0;
			for (int e = headx[u]; e > 0; e = nextx[e]) {
				group[++gsiz] = tox[e];
			}
			groupLink();
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arrs[i] = in.nextString();
		}
		buildGraph();
		for (int i = 1; i <= n << 1; i++) {
			if (dfn[i] == 0) {
				// tarjan1(i);
				tarjan2(i);
			}
		}
		boolean check = true;
		for (int i = 1; i <= n; i++) {
			if (belong[i] == belong[i + n]) {
				check = false;
				break;
			}
		}
		if (check) {
			out.println("YES");
			for (int i = 1; i <= n; i++) {
				String str = arrs[i];
				for (int j = 0; j < str.length(); j++) {
					if (pending[i] == j) {
						out.print(belong[i] < belong[i + n] ? '0' : '1');
					} else {
						out.print(str.charAt(j));
					}
				}
				out.println();
			}
		} else {
			out.println("NO");
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

		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			if (c == -1) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			while (c > ' ' && c != -1) {
				sb.append((char) c);
				c = readByte();
			}
			return sb.toString();
		}
	}

}
