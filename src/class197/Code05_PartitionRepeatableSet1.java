package class197;

// 划分可重集，java版
// 测试链接 : https://www.luogu.com.cn/problem/P7477
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code05_PartitionRepeatableSet1 {

	public static int MAXN = 20001;
	public static int MAXM = 20001;
	public static int MAXT = 1000001;
	public static int MAXE = 2000001;
	public static int n, m, cntt;
	public static int[] v = new int[MAXN];
	public static int[] x = new int[MAXM];
	public static int[] y = new int[MAXM];

	public static int[] rak = new int[MAXN];
	public static int[][] arr = new int[MAXN][2];

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

	public static int[] inTree1 = new int[MAXN];
	public static int[] outTree1 = new int[MAXN];

	public static int[] inTree2 = new int[MAXN];
	public static int[] outTree2 = new int[MAXN];

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

	// <= num的范围长度
	public static int small(int num) {
		int l = 1, r = n, mid, ans = 0;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (arr[mid][0] <= num) {
				ans = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return ans;
	}

	// >= num的范围长度
	public static int big(int num) {
		int l = 1, r = n, mid, ans = n + 1;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (arr[mid][0] >= num) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return n - ans + 1;
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

	public static int other(int x) {
		return x <= n ? x + n : x - n;
	}

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void add(int[] inTree, int[] outTree, int i, int x) {
		while (i <= n) {
			int prei = inTree[i];
			int preo = outTree[i];
			int curi = ++cntt;
			int curo = ++cntt;
			if (prei > 0) {
				addEdge(curi, prei);
				addEdge(preo, curo);
			}
			addEdge(curi, other(x));
			addEdge(x, curo);
			inTree[i] = curi;
			outTree[i] = curo;
			i += lowbit(i);
		}
	}

	public static void xToRange(int[] inTree, int[] outTree, int i, int x) {
		while (i > 0) {
			if (inTree[i] > 0) {
				addEdge(x, inTree[i]);
				addEdge(outTree[i], other(x));
			}
			i -= lowbit(i);
		}
	}

	public static void buildGraph(int k) {
		cntt = n << 1;
		for (int i = 1; i <= m; i++) {
			addEdge(x[i], other(y[i]));
			addEdge(y[i], other(x[i]));
			addEdge(other(x[i]), y[i]);
			addEdge(other(y[i]), x[i]);
		}
		for (int i = 1; i <= n; i++) {
			xToRange(inTree1, outTree1, small(v[i] - k), i);
			xToRange(inTree2, outTree2, big(v[i] + k), other(i));
			add(inTree1, outTree1, rak[i], i);
			add(inTree2, outTree2, n - rak[i] + 1, other(i));
		}
	}

	public static void clear() {
		for (int i = 1; i <= cntt; i++) {
			head[i] = dfn[i] = belong[i] = 0;
		}
		for (int i = 1; i <= n; i++) {
			inTree1[i] = outTree1[i] = inTree2[i] = outTree2[i] = 0;
		}
		cntt = cntg = cntd = top = sccCnt = 0;
	}

	public static boolean check(int k) {
		buildGraph(k);
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
		clear();
		return check;
	}

	public static int compute() {
		for (int i = 1; i <= n; i++) {
			arr[i][0] = v[i];
			arr[i][1] = i;
		}
		Arrays.sort(arr, 1, n + 1, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
		for (int i = 1; i <= n; i++) {
			rak[arr[i][1]] = i;
		}
		int l = 0, r = arr[n][0] - arr[1][0], mid, ans = -1;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (check(mid)) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			v[i] = in.nextInt();
		}
		for (int i = 1; i <= m; i++) {
			x[i] = in.nextInt();
			y[i] = in.nextInt();
		}
		int ans = compute();
		out.println(ans);
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
