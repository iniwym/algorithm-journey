package class197;

// 布尔，C++版
// 一共n个变量，取值为0或1，一共m条限制，格式 u x v y，含义如下
// 如果u的取值为x，则v的取值为y，同时如果v的取值为y，则u的取值为x
// 限制的编号范围1~m，限制区间[a, b]如果满足如下条件，就认为是合法的
// 区间[a, b]内的所有限制，存在一种变量赋值方案，能让它们全部成立
// 一共有q条查询，格式 l r，含义如下
// 整个限制区间[l, r]，最少划分成多少段连续区间，使得每个连续区间都合法
// 打印划分的连续区间数量，如果怎么划分都做不到上述要求，打印-1
// 1 <= n <= 10^5
// 1 <= m <= 6 * 10^5
// 1 <= q <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P7843
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 200001;
//const int MAXM = 600001;
//const int MAXQ = 1000001;
//const int MAXP = 20;
//int n, m, q;
//
//int error[MAXM];
//int u[MAXM];
//int v[MAXM];
//
//int fa[MAXN];
//int siz[MAXN];
//int rollback[MAXN][2];
//int opsize;
//
//int first[MAXM];
//int st[MAXM + 1][MAXP];
//
//int find(int i) {
//    while (i != fa[i]) {
//        i = fa[i];
//    }
//    return i;
//}
//
//void unionn(int x, int y) {
//    int fx = find(x);
//    int fy = find(y);
//    if (fx != fy) {
//        if (siz[fx] < siz[fy]) {
//            swap(fx, fy);
//        }
//        fa[fy] = fx;
//        siz[fx] += siz[fy];
//        rollback[++opsize][0] = fx;
//        rollback[opsize][1] = fy;
//    }
//}
//
//void undo(int oldsiz) {
//    while (opsize > oldsiz) {
//        int fx = rollback[opsize][0];
//        int fy = rollback[opsize--][1];
//        fa[fy] = fy;
//        siz[fx] -= siz[fy];
//    }
//}
//
//int other(int x) {
//    return x <= n ? x + n : x - n;
//}
//
//bool conflict(int x) {
//    return find(x) == find(other(x));
//}
//
//void compute(int ql, int qr, int vl, int vr) {
//    if (ql > qr) {
//        return;
//    }
//    if (vl == vr) {
//        for (int i = ql; i <= qr; i++) {
//            first[i] = vl;
//        }
//        return;
//    }
//    int mid = (vl + vr) >> 1;
//    int backup1 = opsize;
//    bool bad = false;
//    for (int i = max(qr + 1, vl); i <= mid; i++) {
//        unionn(u[i], v[i]);
//        unionn(other(u[i]), other(v[i]));
//        if (conflict(u[i])) {
//            bad = true;
//            break;
//        }
//    }
//    if (bad) {
//        undo(backup1);
//        compute(ql, qr, vl, mid);
//    } else {
//        int backup2 = opsize;
//        int split = min(qr, mid);
//        for (; split >= ql; split--) {
//            unionn(u[split], v[split]);
//            unionn(other(u[split]), other(v[split]));
//            if (conflict(u[split])) {
//                break;
//            }
//        }
//        undo(backup2);
//        compute(split + 1, qr, mid + 1, vr);
//        undo(backup1);
//        for (int i = split + 1; i <= qr && i < vl; i++) {
//            unionn(u[i], v[i]);
//            unionn(other(u[i]), other(v[i]));
//        }
//        compute(ql, split, vl, mid);
//    }
//    undo(backup1);
//}
//
//void buildst() {
//    opsize = 0;
//    for (int i = 1; i <= (n << 1); i++) {
//        fa[i] = i;
//        siz[i] = 1;
//    }
//    compute(1, m, 1, m + 1);
//    for (int p = 0; p < MAXP; p++) {
//        st[m + 1][p] = m + 1;
//    }
//    for (int i = m; i >= 1; i--) {
//        st[i][0] = first[i];
//        for (int p = 1; p < MAXP; p++) {
//            st[i][p] = st[st[i][p - 1]][p - 1];
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m >> q;
//    for (int i = 1, x, y; i <= m; i++) {
//        cin >> u[i] >> x >> v[i] >> y;
//        error[i] = u[i] == v[i] && x != y ? 1 : 0;
//        u[i] = x == 0 ? u[i] : (u[i] + n);
//        v[i] = y == 0 ? v[i] : (v[i] + n);
//    }
//    buildst();
//    for (int i = 1; i <= m; i++) {
//        error[i] += error[i - 1];
//    }
//    for (int i = 1, l, r; i <= q; i++) {
//        cin >> l >> r;
//        if (error[r] - error[l - 1] > 0) {
//            cout << -1 << "\n";
//        } else {
//            int ans = 0;
//            for (int p = MAXP - 1; p >= 0; p--) {
//                if (st[l][p] <= r) {
//                    l = st[l][p];
//                    ans += 1 << p;
//                }
//            }
//            ans++;
//            cout << ans << "\n";
//        }
//    }
//    return 0;
//}