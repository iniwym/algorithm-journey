package class197;

// 编码，C++版
// 一共有n个字符串，都由字符 '0'、'1'、'?' 组成
// 每个字符串最多含有一个'?'，如果不含有，说明该字符串已经确定了
// 如果字符串含有'?'，那么这个'?'必须变成'0'或'1'中的一个
// 请把所有字符串都确定下来，要求任意一个字符串不是其它字符串的前缀
// 存在方案打印"YES"，然后打印每个确定后的字符串，不存在方案打印"NO"
// 1 <= n、字符总数 <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P6965
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 500001;
//const int MAXP = 1000002;
//const int MAXT = 5000001;
//const int MAXE = 10000001;
//int n, cntt;
//
//string arrs[MAXN];
//int pending[MAXN];
//
//int headg[MAXT];
//int nextg[MAXE];
//int tog[MAXE];
//int cntg;
//
//int headx[MAXP];
//int nextx[MAXP];
//int tox[MAXP];
//int cntx;
//
//int tree[MAXP][2];
//int fa[MAXP];
//int up[MAXP];
//int down[MAXP];
//int cntp;
//
//int group[MAXN];
//int gsiz;
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
//void addEdge(int u, int v) {
//    nextg[++cntg] = headg[u];
//    tog[cntg] = v;
//    headg[u] = cntg;
//}
//
//void addx(int u, int x) {
//    nextx[++cntx] = headx[u];
//    tox[cntx] = x;
//    headx[u] = cntx;
//}
//
//void tarjan(int u) {
//    dfn[u] = low[u] = ++cntd;
//    sta[++top] = u;
//    for (int e = headg[u]; e > 0; e = nextg[e]) {
//        int v = tog[e];
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
//int other(int x) {
//    return x <= n ? x + n : x - n;
//}
//
//void findPending(int i) {
//    string str = arrs[i];
//    pending[i] = -1;
//    for (int si = 0; si < (int)str.length(); si++) {
//        if (str[si] == '?') {
//            pending[i] = si;
//            break;
//        }
//    }
//    if (pending[i] == -1) {
//        if (str[0] == '0') {
//            addEdge(i + n, i);
//        } else {
//            addEdge(i, i + n);
//        }
//        pending[i] = 0;
//    }
//}
//
//void insert(int i, int confirm, int x) {
//    string str = arrs[i];
//    int pi = pending[i];
//    int cur = 1;
//    for (int si = 0, path; si < (int)str.length(); si++) {
//        if (pi == si) {
//            path = confirm;
//        } else {
//            path = str[si] == '0' ? 0 : 1;
//        }
//        if (tree[cur][path] == 0) {
//            tree[cur][path] = ++cntp;
//            fa[cntp] = cur;
//            up[cntp] = ++cntt;
//            down[cntp] = ++cntt;
//            addEdge(up[cntp], up[cur]);
//            addEdge(down[cur], down[cntp]);
//        }
//        cur = tree[cur][path];
//    }
//    addEdge(x, up[fa[cur]]);
//    addEdge(up[cur], other(x));
//    addEdge(down[fa[cur]], other(x));
//    addEdge(x, down[cur]);
//    addx(cur, x);
//}
//
//void groupLink() {
//    if (gsiz > 1) {
//        cntt++;
//        addEdge(cntt, other(group[1]));
//        for (int i = 2; i <= gsiz; i++) {
//            cntt++;
//            addEdge(cntt, other(group[i]));
//            addEdge(group[i], cntt - 1);
//            addEdge(cntt, cntt - 1);
//        }
//        cntt++;
//        addEdge(cntt, other(group[gsiz]));
//        for (int i = gsiz - 1; i >= 1; i--) {
//            cntt++;
//            addEdge(cntt, other(group[i]));
//            addEdge(group[i], cntt - 1);
//            addEdge(cntt, cntt - 1);
//        }
//    }
//}
//
//void buildGraph() {
//    cntt = n << 1;
//    cntp = 1;
//    up[1] = ++cntt;
//    down[1] = ++cntt;
//    for (int i = 1; i <= n; i++) {
//        findPending(i);
//        insert(i, 0, i);
//        insert(i, 1, i + n);
//    }
//    for (int u = 1; u <= cntp; u++) {
//        gsiz = 0;
//        for (int e = headx[u]; e > 0; e = nextx[e]) {
//            group[++gsiz] = tox[e];
//        }
//        groupLink();
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    for (int i = 1; i <= n; i++) {
//        cin >> arrs[i];
//    }
//    buildGraph();
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
//    if (check) {
//        cout << "YES" << "\n";
//        for (int i = 1; i <= n; i++) {
//            string str = arrs[i];
//            for (int j = 0; j < (int)str.length(); j++) {
//                if (pending[i] == j) {
//                    cout << (belong[i] < belong[i + n] ? '0' : '1');
//                } else {
//                    cout << str[j];
//                }
//            }
//            cout << "\n";
//        }
//    } else {
//        cout << "NO" << "\n";
//    }
//    return 0;
//}