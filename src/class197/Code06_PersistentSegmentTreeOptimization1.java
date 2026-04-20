package class197;

// 主席树优化建图，java版
// 一共有n个点，arr[i]表示i号点的点权，初始时没有边，实现如下三个方法
// 1) void rangeToX(l, r, v, x, w)
//         编号[l, r]范围，点权 <= v的每个点，向点x连权值为w的边
// 2) void xToRange(x, l, r, v, w)
//         点x向编号[l, r]范围，点权 <= v的每个点，连权值为w的边
// 3) void rangeToRange(l1, r1, v1, l2, r2, v2, w)
//         编号[l1, r1]范围，点权 <= v1的每个点
//         编号[l2, r2]范围，点权 <= v2的每个点
//         都连权值为w的边
// 1 <= 所有数值
// 建好图之后可以测试图的任何算法，比如dijkstra算法求最短路
// 对数器验证

import java.util.Arrays;
import java.util.PriorityQueue;

public class Code06_PersistentSegmentTreeOptimization1 {

	public static int MAXN = 1001;
	public static int MAXT = 10001;
	public static int MAXE = 10001;
	public static int n;

	public static int[] arr = new int[MAXN];
	public static int[] sortv = new int[MAXN];
	public static int[][] valIdx = new int[MAXN][2];
	public static int cntv;

	public static int[] head = new int[MAXT];
	public static int[] nxt = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cntg;

	public static int[] rootOut = new int[MAXN];
	public static int[] rootIn = new int[MAXN];
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static int cntt;

	// 返回<= num最右的下标，如果不存在返回0
	public static int kth(int num) {
		int l = 1, r = cntv, mid, ans = 0;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (sortv[mid] <= num) {
				ans = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return ans;
	}

	public static void addEdge(int u, int v, int w) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		weight[cntg] = w;
		head[u] = cntg;
	}

	public static int buildOut(int l, int r) {
		int rt = ++cntt;
		if (l < r) {
			int mid = (l + r) >> 1;
			ls[rt] = buildOut(l, mid);
			rs[rt] = buildOut(mid + 1, r);
			addEdge(ls[rt], rt, 0);
			addEdge(rs[rt], rt, 0);
		}
		return rt;
	}

	public static int buildIn(int l, int r) {
		int rt = ++cntt;
		if (l < r) {
			int mid = (l + r) >> 1;
			ls[rt] = buildIn(l, mid);
			rs[rt] = buildIn(mid + 1, r);
			addEdge(rt, ls[rt], 0);
			addEdge(rt, rs[rt], 0);
		}
		return rt;
	}

	public static int addOut(int jobi, int l, int r, int i) {
		int rt = ++cntt;
		ls[rt] = ls[i];
		rs[rt] = rs[i];
		addEdge(i, rt, 0);
		if (l == r) {
			addEdge(jobi, rt, 0);
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				ls[rt] = addOut(jobi, l, mid, ls[rt]);
				addEdge(ls[rt], rt, 0);
			} else {
				rs[rt] = addOut(jobi, mid + 1, r, rs[rt]);
				addEdge(rs[rt], rt, 0);
			}
		}
		return rt;
	}

	public static int addIn(int jobi, int l, int r, int i) {
		int rt = ++cntt;
		ls[rt] = ls[i];
		rs[rt] = rs[i];
		addEdge(rt, i, 0);
		if (l == r) {
			addEdge(rt, jobi, 0);
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				ls[rt] = addIn(jobi, l, mid, ls[rt]);
				addEdge(rt, ls[rt], 0);
			} else {
				rs[rt] = addIn(jobi, mid + 1, r, rs[rt]);
				addEdge(rt, rs[rt], 0);
			}
		}
		return rt;
	}

	public static void rangeToX(int jobl, int jobr, int jobx, int jobw, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobx, jobw);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				rangeToX(jobl, jobr, jobx, jobw, l, mid, ls[i]);
			}
			if (jobr > mid) {
				rangeToX(jobl, jobr, jobx, jobw, mid + 1, r, rs[i]);
			}
		}
	}

	public static void rangeToX(int l, int r, int v, int x, int w) {
		rangeToX(l, r, x, w, 1, n, rootOut[kth(v)]);
	}

	public static void xToRange(int jobx, int jobl, int jobr, int jobw, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(jobx, i, jobw);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				xToRange(jobx, jobl, jobr, jobw, l, mid, ls[i]);
			}
			if (jobr > mid) {
				xToRange(jobx, jobl, jobr, jobw, mid + 1, r, rs[i]);
			}
		}
	}

	public static void xToRange(int x, int l, int r, int v, int w) {
		xToRange(x, l, r, w, 1, n, rootIn[kth(v)]);
	}

	public static void rangeToRange(int l1, int r1, int v1, int l2, int r2, int v2, int w) {
		int x = ++cntt;
		int y = ++cntt;
		rangeToX(l1, r1, v1, x, 0);
		xToRange(y, l2, r2, v2, 0);
		addEdge(x, y, w);
	}

	public static void buildGraph() {
		for (int i = 1; i <= n; i++) {
			sortv[i] = arr[i];
			valIdx[i][0] = arr[i];
			valIdx[i][1] = i;
		}
		Arrays.sort(sortv, 1, n + 1);
		cntv = 1;
		for (int i = 2; i <= n; i++) {
			if (sortv[cntv] != sortv[i]) {
				sortv[++cntv] = sortv[i];
			}
		}
		Arrays.sort(valIdx, 1, n + 1, (a, b) -> Integer.compare(a[0], b[0]));
		cntt = n;
		rootOut[0] = buildOut(1, n);
		rootIn[0] = buildIn(1, n);
		int curOut = rootOut[0], curIn = rootIn[0], rank, idx;
		for (int i = 1; i <= n; i++) {
			rank = kth(valIdx[i][0]);
			idx = valIdx[i][1];
			curOut = addOut(idx, 1, n, curOut);
			curIn = addIn(idx, 1, n, curIn);
			rootOut[rank] = curOut;
			rootIn[rank] = curIn;
		}
	}

	public static int MAX_2 = 100001;
	public static int[] head_2 = new int[MAX_2];
	public static int[] nxt_2 = new int[MAX_2];
	public static int[] to_2 = new int[MAX_2];
	public static int[] weight_2 = new int[MAX_2];
	public static int cnt_2;

	public static void addEdge_2(int u, int v, int w) {
		nxt_2[++cnt_2] = head_2[u];
		to_2[cnt_2] = v;
		weight_2[cnt_2] = w;
		head_2[u] = cnt_2;
	}

	public static void rangeToX_2(int l, int r, int v, int x, int w) {
		for (int i = l; i <= r; i++) {
			if (arr[i] <= v) {
				addEdge_2(i, x, w);
			}
		}
	}

	public static void xToRange_2(int x, int l, int r, int v, int w) {
		for (int i = l; i <= r; i++) {
			if (arr[i] <= v) {
				addEdge_2(x, i, w);
			}
		}
	}

	public static void rangeToRange_2(int l1, int r1, int v1, int l2, int r2, int v2, int w) {
		for (int i = l1; i <= r1; i++) {
			for (int j = l2; j <= r2; j++) {
				if (arr[i] <= v1 && arr[j] <= v2) {
					addEdge_2(i, j, w);
				}
			}
		}
	}

	public static int INF = 1000000001;
	public static int[] dist = new int[MAXT];
	public static boolean[] vis = new boolean[MAXT];
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((x, y) -> x[1] - y[1]);

	public static int dijkstra(int s, int t) {
		for (int i = 1; i <= cntt; i++) {
			dist[i] = INF;
			vis[i] = false;
		}
		heap.clear();
		dist[s] = 0;
		heap.add(new int[] { s, 0 });
		while (!heap.isEmpty()) {
			int[] cur = heap.poll();
			int u = cur[0];
			int d = cur[1];
			if (u == t) {
				return d;
			}
			if (!vis[u]) {
				vis[u] = true;
				for (int e = head[u]; e > 0; e = nxt[e]) {
					int v = to[e];
					int w = weight[e];
					if (!vis[v] && dist[v] > d + w) {
						dist[v] = d + w;
						heap.add(new int[] { v, dist[v] });
					}
				}
			}
		}
		return -1;
	}

	public static int dijkstra2(int s, int t) {
		for (int i = 1; i <= n; i++) {
			dist[i] = INF;
			vis[i] = false;
		}
		heap.clear();
		dist[s] = 0;
		heap.add(new int[] { s, 0 });
		while (!heap.isEmpty()) {
			int[] cur = heap.poll();
			int u = cur[0];
			int d = cur[1];
			if (u == t) {
				return d;
			}
			if (!vis[u]) {
				vis[u] = true;
				for (int e = head_2[u]; e > 0; e = nxt_2[e]) {
					int v = to_2[e];
					int w = weight_2[e];
					if (!vis[v] && dist[v] > d + w) {
						dist[v] = d + w;
						heap.add(new int[] { v, dist[v] });
					}
				}
			}
		}
		return -1;
	}

	public static int random(int maxv) {
		return (int) (Math.random() * maxv) + 1;
	}

	public static void clear() {
		for (int i = 1; i <= cntt; i++) {
			head[i] = 0;
		}
		cntg = 0;
		cntt = 0;
		for (int i = 1; i <= n; i++) {
			head_2[i] = 0;
		}
		cnt_2 = 0;
	}

	public static void main(String[] args) {
		System.out.println("主席树优化建图");
		System.out.println("============");
		n = 100;
		int valMax = 1000;
		int weightMax = 10000;
		int round = 20;
		for (int t = 1; t <= round; t++) {
			System.out.println("第" + t + "轮");
			System.out.println("测试开始");
			for (int i = 1; i <= n; i++) {
				arr[i] = random(valMax);
			}
			buildGraph();
			int opCnt = 200;
			for (int i = 1; i <= opCnt; i++) {
				int op = random(3);
				int a = random(n);
				int b = random(n);
				int l = Math.min(a, b);
				int r = Math.max(a, b);
				int v = random(valMax);
				int x = random(n);
				int w = random(weightMax);
				if (op == 1) {
					rangeToX(l, r, v, x, w);
					rangeToX_2(l, r, v, x, w);
				} else if (op == 2) {
					xToRange(x, l, r, v, w);
					xToRange_2(x, l, r, v, w);
				} else {
					a = random(n);
					b = random(n);
					int l2 = Math.min(a, b);
					int r2 = Math.max(a, b);
					int v2 = random(valMax);
					rangeToRange(l, r, v, l2, r2, v2, w);
					rangeToRange_2(l, r, v, l2, r2, v2, w);
				}
			}
			for (int x = 1; x <= n; x++) {
				for (int y = 1; y <= n; y++) {
					int ans1 = dijkstra(x, y);
					int ans2 = dijkstra2(x, y);
					if (ans1 != ans2) {
						System.out.println("出错了!");
					}
				}
			}
			clear();
			System.out.println("测试结束");
			System.out.println("=======");
		}
	}

}
