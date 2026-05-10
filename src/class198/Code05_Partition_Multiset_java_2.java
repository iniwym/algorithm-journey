package class198;

// 划分可重集，主席树优化建图，java版
// 给定长度为n的数组arr，从左到右把每个数划分进可重集a、b
// 每个数至少要进入a或b中的一个
// arr[i]能进集合a的条件为，i左侧任何 <= arr[i] - k的数字，都没进集合a
// arr[i]能进集合b的条件为，i左侧任何 >= arr[i] + k的数字，都没进集合b
// 然后有m组关系，每组 x y，保证 x < y，表示两个位置的数不能进入同一个集合
// 如果不存在合法划分，打印-1，如果存在方案，打印能划分成功的最小k值
// n、m <= 2 * 10^4
// arr[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P7477
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code05_Partition_Multiset_java_2 {

	public static int MAXN = 20001;
	public static int MAXM = 20001;
	public static int MAXT = 2000001;
	public static int MAXE = 5000001;
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

	// 主席树
	public static int[] rootOut1 = new int[MAXN];
	public static int[] rootIn1 = new int[MAXN];
	public static int[] rootOut2 = new int[MAXN];
	public static int[] rootIn2 = new int[MAXN];
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static int curVersion;

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

	public static int buildOut(int l, int r) {
		int rt = ++cntt;
		if (l < r) {
			int mid = (l + r) >> 1;
			ls[rt] = buildOut(l, mid);
			rs[rt] = buildOut(mid + 1, r);
			addEdge(ls[rt], rt);
			addEdge(rs[rt], rt);
		}
		return rt;
	}

	public static int buildIn(int l, int r) {
		int rt = ++cntt;
		if (l < r) {
			int mid = (l + r) >> 1;
			ls[rt] = buildIn(l, mid);
			rs[rt] = buildIn(mid + 1, r);
			addEdge(rt, ls[rt]);
			addEdge(rt, rs[rt]);
		}
		return rt;
	}

	public static int addOut(int jobx, int jobv, int l, int r, int i) {
		int rt = ++cntt;
		ls[rt] = ls[i];
		rs[rt] = rs[i];
		addEdge(i, rt);
		if (l == r) {
			addEdge(jobx, rt);
		} else {
			int mid = (l + r) >> 1;
			if (jobv <= mid) {
				ls[rt] = addOut(jobx, jobv, l, mid, ls[rt]);
				addEdge(ls[rt], rt);
			} else {
				rs[rt] = addOut(jobx, jobv, mid + 1, r, rs[rt]);
				addEdge(rs[rt], rt);
			}
		}
		return rt;
	}

	public static int addIn(int jobx, int jobv, int l, int r, int i) {
		int rt = ++cntt;
		ls[rt] = ls[i];
		rs[rt] = rs[i];
		addEdge(rt, i);
		if (l == r) {
			addEdge(rt, jobx);
		} else {
			int mid = (l + r) >> 1;
			if (jobv <= mid) {
				ls[rt] = addIn(jobx, jobv, l, mid, ls[rt]);
				addEdge(rt, ls[rt]);
			} else {
				rs[rt] = addIn(jobx, jobv, mid + 1, r, rs[rt]);
				addEdge(rt, rs[rt]);
			}
		}
		return rt;
	}

	public static void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobx);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				rangeToX(jobl, jobr, jobx, l, mid, ls[i]);
			}
			if (jobr > mid) {
				rangeToX(jobl, jobr, jobx, mid + 1, r, rs[i]);
			}
		}
	}

	public static void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(jobx, i);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				xToRange(jobx, jobl, jobr, l, mid, ls[i]);
			}
			if (jobr > mid) {
				xToRange(jobx, jobl, jobr, mid + 1, r, rs[i]);
			}
		}
	}

	public static void add(int x, int otherx, int si, int bi) {
		curVersion++;
		rootOut1[curVersion] = addOut(x, si, 1, n, rootOut1[curVersion - 1]);
		rootIn1[curVersion] = addIn(otherx, si, 1, n, rootIn1[curVersion - 1]);
		rootOut2[curVersion] = addOut(otherx, bi, 1, n, rootOut2[curVersion - 1]);
		rootIn2[curVersion] = addIn(x, bi, 1, n, rootIn2[curVersion - 1]);
	}

	public static void link(int x, int otherx, int lowCnt, int hightCnt) {
		if (lowCnt > 0) {
			xToRange(x, 1, lowCnt, 1, n, rootIn1[curVersion]);
			rangeToX(1, lowCnt, otherx, 1, n, rootOut1[curVersion]);
		}
		if (hightCnt > 0) {
			xToRange(otherx, 1, hightCnt, 1, n, rootIn2[curVersion]);
			rangeToX(1, hightCnt, x, 1, n, rootOut2[curVersion]);
		}
	}

	public static int other(int x) {
		return x <= n ? x + n : x - n;
	}

	public static void buildGraph(int k) {
		cntt = n << 1;
		rootOut1[0] = buildOut(1, n);
		rootIn1[0] = buildIn(1, n);
		rootOut2[0] = buildOut(1, n);
		rootIn2[0] = buildIn(1, n);
		for (int i = 1; i <= m; i++) {
			addEdge(x[i], other(y[i]));
			addEdge(y[i], other(x[i]));
			addEdge(other(x[i]), y[i]);
			addEdge(other(y[i]), x[i]);
		}
		for (int i = 1; i <= n; i++) {
			link(i, other(i), small(v[i] - k), big(v[i] + k));
			add(i, other(i), rak[i], n - rak[i] + 1);
		}
	}

	public static void clear() {
		for (int i = 1; i <= cntt; i++) {
			head[i] = dfn[i] = belong[i] = 0;
		}
		cntt = cntg = cntd = top = sccCnt = curVersion = 0;
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
