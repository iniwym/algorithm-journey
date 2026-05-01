package class197;

// 旗帜，C++版
// 一共n个旗子，第i号旗子有a[i]和b[i]两个可选位置
// 你需要决定每个旗子，到底放在两个可选位置中的哪一个
// 两面旗子之间的最小距离 d 越大，排列看起来就越美观
// 找到最美观的情况，即求出所有可能的 d 值中的最大值
// 2 <= n <= 10^4
// 1 <= a[i]、b[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/AT_arc069_d
// 测试链接 : https://atcoder.jp/contests/arc069/tasks/arc069_d
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//struct Node {
//    int pos, xid;
//};
//
//bool NodeCmp(Node a, Node b) {
//    if (a.pos != b.pos) {
//        return a.pos < b.pos;
//    }
//    return a.xid < b.xid;
//}
//
//const int MAXN = 10001;
//const int MAXT = 100001;
//const int MAXE = 1000001;
//int n, k;
//int a[MAXN];
//int b[MAXN];
//
//Node arr[MAXN << 1];
//
//int ls[MAXT];
//int rs[MAXT];
//int root;
//int cntt;
//
//int head[MAXT];
//int nxt[MAXE];
//int to[MAXE];
//int cntg;
//
//int dfn[MAXT];
//int low[MAXT];
//int cntd;
//
//int sta[MAXT];
//int top;
//
//int belong[MAXT];
//int sccCnt;
//
//int kth(int num) {
//    int l = 1, r = k, mid, ans = k + 1;
//    while (l <= r) {
//        mid = (l + r) >> 1;
//        if (arr[mid].pos >= num) {
//            ans = mid;
//            r = mid - 1;
//        } else {
//            l = mid + 1;
//        }
//    }
//    return ans;
//}
//
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];
//    to[cntg] = v;
//    head[u] = cntg;
//}
//
//void tarjan(int u) {
//    dfn[u] = low[u] = ++cntd;
//    sta[++top] = u;
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];
//        if (dfn[v] == 0) {
//            tarjan(v);
//            low[u] = min(low[u], low[v]);
//        } else {
//            if (belong[v] == 0) {
//                low[u] = min(low[u], dfn[v]);
//            }
//        }
//    }
//    if (dfn[u] == low[u]) {
//        sccCnt++;
//        int pop;
//        do {
//            pop = sta[top--];
//            belong[pop] = sccCnt;
//        } while (pop != u);
//    }
//}
//
//int other(int i) {
//    return i <= n ? i + n : i - n;
//}
//
//int build(int l, int r) {
//    int rt;
//    if (l == r) {
//        rt = other(arr[l].xid);
//    } else {
//        rt = ++cntt;
//        int mid = (l + r) >> 1;
//        ls[rt] = build(l, mid);
//        rs[rt] = build(mid + 1, r);
//        addEdge(rt, ls[rt]);
//        addEdge(rt, rs[rt]);
//    }
//    return rt;
//}
//
//void xToRange(int xid, int jobl, int jobr, int l, int r, int i) {
//    if (jobl > jobr) {
//        return;
//    }
//    if (jobl <= l && r <= jobr) {
//        addEdge(xid, i);
//    } else {
//        int mid = (l + r) >> 1;
//        if (jobl <= mid) {
//            xToRange(xid, jobl, jobr, l, mid, ls[i]);
//        }
//        if (jobr > mid) {
//            xToRange(xid, jobl, jobr, mid + 1, r, rs[i]);
//        }
//    }
//}
//
//void buildGraph(int d) {
//    cntt = k;
//    root = build(1, k);
//    for (int i = 1; i <= k; i++) {
//        int pos = arr[i].pos;
//        int l = kth(pos - d + 1);
//        int r = kth(pos + d) - 1;
//        int x = arr[i].xid;
//        xToRange(x, l, i - 1, 1, k, root);
//        xToRange(x, i + 1, r, 1, k, root);
//    }
//}
//
//void clear() {
//    for (int i = 1; i <= cntt; i++) {
//        head[i] = dfn[i] = belong[i] = 0;
//    }
//    cntt = cntg = cntd = top = sccCnt = 0;
//}
//
//bool check(int d) {
//    buildGraph(d);
//    for (int i = 1; i <= (n << 1); i++) {
//        if (dfn[i] == 0) {
//            tarjan(i);
//        }
//    }
//    bool check = true;
//    for (int i = 1; i <= n; i++) {
//        if (belong[i] == belong[i + n]) {
//            check = false;
//            break;
//        }
//    }
//    clear();
//    return check;
//}
//
//int compute() {
//    for (int i = 1; i <= n; i++) {
//        arr[i].pos = a[i];
//        arr[i].xid = i;
//        arr[i + n].pos = b[i];
//        arr[i + n].xid = i + n;
//    }
//    k = n << 1;
//    sort(arr + 1, arr + k + 1, NodeCmp);
//    int l = 0, r = arr[k].pos, d, ans = 0;
//    while (l <= r) {
//        d = (l + r) >> 1;
//        if (check(d)) {
//            ans = d;
//            l = d + 1;
//        } else {
//            r = d - 1;
//        }
//    }
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    for (int i = 1; i <= n; i++) {
//        cin >> a[i] >> b[i];
//    }
//    int ans = compute();
//    cout << ans << "\n";
//    return 0;
//}