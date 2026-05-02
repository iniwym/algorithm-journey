package class197;

// 游戏，C++版
// 地面有x、a、b、c四种类型，赛车有A、B、C三辆
// 类型a不能跑车A，类型b不能跑车B、类型c不能跑车C，类型x没有限制
// 一共有n个地点，给定每个地点的地面类型，其中x类型的地面有d个
// 一共有m个限制，格式 g1 v1 g2 v2，含义如下
// 如果g1号地点使用v1赛车，那么g2号地点一定要使用v2赛车
// 每辆赛车可以使用无限次，需要满足以上的规则和限制来安排比赛
// 如果有方案，找到任意一种方案，打印每个地点用什么车，无方案打印-1
// 1 <= n <= 5 * 10^4    1 <= m <= 10^5    0 <= d <= 8
// 测试链接 : https://www.luogu.com.cn/problem/P3825
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 200001;
//int n, d, m;
//
//int ground[MAXN];
//int g1[MAXN];
//int v1[MAXN];
//int g2[MAXN];
//int v2[MAXN];
//
//int posx[MAXN];
//
//int head[MAXN];
//int nxt[MAXN];
//int to[MAXN];
//int cntg;
//
//int dfn[MAXN];
//int low[MAXN];
//int cntd;
//
//int sta[MAXN];
//int top;
//
//int belong[MAXN];
//int sccCnt;
//
//char ans[MAXN];
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
//int pick(int i, int car) {
//    int first = ground[i] == 1 ? 2 : 1;
//    return car == first ? i : i + n;
//}
//
//int other(int i) {
//    return i <= n ? i + n : i - n;
//}
//
//void buildGraph(int xstatus) {
//    for (int bit = 0, idx = 1; bit < d; bit++, idx++) {
//        if (((xstatus >> bit) & 1) == 1) {
//            ground[posx[idx]] = 1;
//        } else {
//            ground[posx[idx]] = 2;
//        }
//    }
//    for (int i = 1; i <= m; i++) {
//        if (ground[g1[i]] != v1[i]) {
//            int y1 = pick(g1[i], v1[i]);
//            int n1 = other(y1);
//            if (ground[g2[i]] != v2[i]) {
//                int y2 = pick(g2[i], v2[i]);
//                int n2 = other(y2);
//                addEdge(y1, y2);
//                addEdge(n2, n1);
//            } else {
//                addEdge(y1, n1);
//            }
//        }
//    }
//}
//
//char getCar(int groundType, bool first) {
//    if (groundType == 1) {
//        return first ? 'B' : 'C';
//    } else if (groundType == 2) {
//        return first ? 'A' : 'C';
//    } else {
//        return first ? 'A' : 'B';
//    }
//}
//
//void clear() {
//    for (int i = 1; i <= (n << 1); i++) {
//        head[i] = dfn[i] = belong[i] = 0;
//    }
//    cntg = cntd = top = sccCnt = 0;
//}
//
//bool compute() {
//    for (int i = 1, sizx = 0; i <= n; i++) {
//        if (ground[i] == 4) {
//            posx[++sizx] = i;
//        }
//    }
//    for (int xstatus = 0; xstatus < 1 << d; xstatus++) {
//        buildGraph(xstatus);
//        for (int i = 1; i <= (n << 1); i++) {
//            if (dfn[i] == 0) {
//                tarjan(i);
//            }
//        }
//        bool check = true;
//        for (int i = 1; i <= n; i++) {
//            if (belong[i] == belong[i + n]) {
//                check = false;
//                break;
//            }
//        }
//        if (check) {
//            for (int i = 1; i <= n; i++) {
//                ans[i] = getCar(ground[i], belong[i] < belong[i + n]);
//            }
//            return true;
//        }
//        clear();
//    }
//    return false;
//}
//
//int getType(char cha) {
//    if (cha == 'A' || cha == 'a') {
//        return 1;
//    } else if (cha == 'B' || cha == 'b') {
//        return 2;
//    } else if (cha == 'C' || cha == 'c') {
//        return 3;
//    } else {
//        return 4;
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> d;
//    char cha;
//    for (int i = 1; i <= n; i++) {
//        cin >> cha;
//        ground[i] = getType(cha);
//    }
//    cin >> m;
//    for (int i = 1; i <= m; i++) {
//        cin >> g1[i] >> cha;
//        v1[i] = getType(cha);
//        cin >> g2[i] >> cha;
//        v2[i] = getType(cha);
//    }
//    bool check = compute();
//    if (check) {
//        for (int i = 1; i <= n; i++) {
//            cout << ans[i];
//        }
//    } else {
//        cout << -1 << "\n";
//    }
//    return 0;
//}