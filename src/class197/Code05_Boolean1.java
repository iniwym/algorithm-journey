package class197;

// 布尔，java版
// 测试链接 : https://www.luogu.com.cn/problem/P7843
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code05_Boolean1 {

	public static int MAXN = 200001;
	public static int MAXM = 600001;
	public static int MAXQ = 1000001;
	public static int MAXP = 20;
	public static int n, m, q;

	public static int[] mistake = new int[MAXM];
	public static int[] u = new int[MAXM];
	public static int[] v = new int[MAXM];

	public static int[] fa = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] backx = new int[MAXN];
	public static int[] backy = new int[MAXN];
	public static int opsize = 0;

	public static int[] first = new int[MAXM];
	public static int[][] st = new int[MAXM + 1][MAXP];

	public static int find(int i) {
		while (i != fa[i]) {
			i = fa[i];
		}
		return i;
	}

	public static void union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (fx != fy) {
			if (siz[fx] < siz[fy]) {
				int tmp = fx;
				fx = fy;
				fy = tmp;
			}
			fa[fy] = fx;
			siz[fx] += siz[fy];
			backx[++opsize] = fx;
			backy[opsize] = fy;
		}
	}

	public static void undo(int oldsiz) {
		while (opsize > oldsiz) {
			int fx = backx[opsize];
			int fy = backy[opsize--];
			fa[fy] = fy;
			siz[fx] -= siz[fy];
		}
	}

	public static int other(int x) {
		return x <= n ? x + n : x - n;
	}

	public static boolean conflict(int x) {
		return find(x) == find(other(x));
	}

	public static void compute(int ql, int qr, int vl, int vr) {
		if (ql > qr) {
			return;
		}
		if (vl == vr) {
			for (int i = ql; i <= qr; i++) {
				first[i] = vl;
			}
			return;
		}
		int mid = (vl + vr) >> 1;
		int backup1 = opsize;
		boolean bad = false;
		for (int i = Math.max(qr + 1, vl); i <= mid; i++) {
			union(u[i], v[i]);
			union(other(u[i]), other(v[i]));
			if (conflict(u[i])) {
				bad = true;
				break;
			}
		}
		if (bad) {
			undo(backup1);
			compute(ql, qr, vl, mid);
		} else {
			int backup2 = opsize;
			int split = ql - 1;
			for (int i = Math.min(qr, mid); i >= ql; i--) {
				union(u[i], v[i]);
				union(other(u[i]), other(v[i]));
				if (conflict(u[i])) {
					split = i;
					break;
				}
			}
			undo(backup2);
			compute(split + 1, qr, mid + 1, vr);
			undo(backup1);
			for (int i = split + 1; i <= Math.min(qr, vl - 1); i++) {
				union(u[i], v[i]);
				union(other(u[i]), other(v[i]));
			}
			compute(ql, split, vl, mid);
		}
		undo(backup1);
	}

	public static void buildst() {
		opsize = 0;
		for (int i = 1; i <= n << 1; i++) {
			fa[i] = i;
			siz[i] = 1;
		}
		compute(1, m, 1, m + 1);
		for (int p = 0; p < MAXP; p++) {
			st[m + 1][p] = m + 1;
		}
		for (int i = m; i >= 1; i--) {
			st[i][0] = first[i];
			for (int p = 1; p < MAXP; p++) {
				st[i][p] = st[st[i][p - 1]][p - 1];
			}
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		q = in.nextInt();
		for (int i = 1, x, y; i <= m; i++) {
			u[i] = in.nextInt();
			x = in.nextInt();
			v[i] = in.nextInt();
			y = in.nextInt();
			mistake[i] = u[i] == v[i] && x != y ? 1 : 0;
			u[i] = x == 0 ? u[i] : (u[i] + n);
			v[i] = y == 0 ? v[i] : (v[i] + n);
		}
		buildst();
		for (int i = 1; i <= m; i++) {
			mistake[i] += mistake[i - 1];
		}
		for (int i = 1, l, r; i <= q; i++) {
			l = in.nextInt();
			r = in.nextInt();
			if (mistake[r] - mistake[l - 1] > 0) {
				out.println(-1);
			} else {
				int ans = 0;
				for (int p = MAXP - 1; p >= 0; p--) {
					if (st[l][p] <= r) {
						l = st[l][p];
						ans += 1 << p;
					}
				}
				ans++;
				out.println(ans);
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
