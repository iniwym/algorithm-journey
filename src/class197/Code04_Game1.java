package class197;

// 游戏，java版
// 地面有x、a、b、c四种类型，赛车有A、B、C三辆
// 类型a不能跑车A，类型b不能跑车B、类型c不能跑车C，类型x没有限制
// 一共有n个地点，给定每个地点的地面类型，其中x类型的地面有d个
// 一共有m个限制，格式 p1 c1 p2 c2，含义如下
// 如果p1号地点使用c1赛车，那么p2号地点一定要使用c2赛车
// 每辆赛车可以使用无限次，需要满足以上的规则和限制来安排比赛
// 如果有方案，找到任意一种方案，打印每个地点用什么车，无方案打印-1
// 1 <= n <= 5 * 10^4    1 <= m <= 10^5    0 <= d <= 8
// 测试链接 : https://www.luogu.com.cn/problem/P3825
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code04_Game1 {

	public static int MAXN = 200001;
	public static int n, d, m;

	public static int[] ground = new int[MAXN];
	public static int[] pos1 = new int[MAXN];
	public static int[] car1 = new int[MAXN];
	public static int[] pos2 = new int[MAXN];
	public static int[] car2 = new int[MAXN];

	public static int[] posx = new int[MAXN];

	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXN];
	public static int[] to = new int[MAXN];
	public static int cntg;

	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int cntd;

	public static int[] sta = new int[MAXN];
	public static int top;

	public static int[] belong = new int[MAXN];
	public static int sccCnt;

	public static char[] ans = new char[MAXN];

	// 迭代版需要的栈，讲解118讲了递归改迭代的技巧
	public static int[] stau = new int[MAXN];
	public static int[] stas = new int[MAXN];
	public static int[] stae = new int[MAXN];
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

	public static int pick(int i, int car) {
		int first = ground[i] == 1 ? 2 : 1;
		return car == first ? i : i + n;
	}

	public static int other(int i) {
		return i <= n ? i + n : i - n;
	}

	public static void buildGraph(int xstatus) {
		for (int bit = 0, idx = 1; bit < d; bit++, idx++) {
			if ((xstatus >> bit & 1) == 1) {
				ground[posx[idx]] = 1;
			} else {
				ground[posx[idx]] = 2;
			}
		}
		for (int i = 1; i <= m; i++) {
			if (ground[pos1[i]] != car1[i]) {
				int y1 = pick(pos1[i], car1[i]);
				int n1 = other(y1);
				if (ground[pos2[i]] != car2[i]) {
					int y2 = pick(pos2[i], car2[i]);
					int n2 = other(y2);
					addEdge(y1, y2);
					addEdge(n2, n1);
				} else {
					addEdge(y1, n1);
				}
			}
		}
	}

	public static char getCar(int groundType, boolean first) {
		if (groundType == 1) {
			return first ? 'B' : 'C';
		} else if (groundType == 2) {
			return first ? 'A' : 'C';
		} else {
			return first ? 'A' : 'B';
		}
	}

	public static void clear() {
		for (int i = 1; i <= n << 1; i++) {
			head[i] = dfn[i] = belong[i] = 0;
		}
		cntg = cntd = top = sccCnt = 0;
	}

	public static boolean compute() {
		for (int i = 1, sizx = 0; i <= n; i++) {
			if (ground[i] == 4) {
				posx[++sizx] = i;
			}
		}
		for (int xstatus = 0; xstatus < 1 << d; xstatus++) {
			buildGraph(xstatus);
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
				for (int i = 1; i <= n; i++) {
					ans[i] = getCar(ground[i], belong[i] < belong[i + n]);
				}
				return true;
			}
			clear();
		}
		return false;
	}

	public static int getType(char cha) {
		if (cha == 'A' || cha == 'a') {
			return 1;
		} else if (cha == 'B' || cha == 'b') {
			return 2;
		} else if (cha == 'C' || cha == 'c') {
			return 3;
		} else {
			return 4;
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		d = in.nextInt();
		char cha;
		for (int i = 1; i <= n; i++) {
			cha = in.nextChar();
			ground[i] = getType(cha);
		}
		m = in.nextInt();
		for (int i = 1; i <= m; i++) {
			pos1[i] = in.nextInt();
			cha = in.nextChar();
			car1[i] = getType(cha);
			pos2[i] = in.nextInt();
			cha = in.nextChar();
			car2[i] = getType(cha);
		}
		boolean check = compute();
		if (check) {
			for (int i = 1; i <= n; i++) {
				out.print(ans[i]);
			}
		} else {
			out.println(-1);
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

		char nextChar() throws IOException {
			int c;
			do {
				c = readByte();
				if (c == -1)
					return 0;
			} while (!(c > ' ' && c != -1));
			return (char) c;
		}
	}

}
