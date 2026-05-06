package class154;

// 可并堆2，C++版
// 这道题课上没有讲，一个值得实现的左偏树模版题
// 完全是课上讲过的代码，看懂课就能看懂实现，注意打注释位置的代码即可
// 给定n个元素，编号为1到n，给定每个元素的值num[i]
// 初始时每个元素单独组成一个集合，接下来有m次操作，类型有四种，格式如下
// 操作 0 x y   : 集合x中删除元素y，题目保证y一定在集合x中
// 操作 1 x     : 查询集合x中的最小值，这是唯一需要打印的操作
// 操作 2 x y   : 集合y合并到集合x，题目保证以后不会再有以y做集合编号的操作
// 操作 3 x y z : 集合x中元素y的权值修改为z，题目保证修改操作值只可能变小
// 测试链接 : https://www.luogu.com.cn/problem/P11266
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 1000001;
//int n, m;
//
//int num[MAXN];
//int up[MAXN];
//int ls[MAXN];
//int rs[MAXN];
//int dist[MAXN];
//int father[MAXN];
//
//int root[MAXN];
//
//void prepare() {
//    dist[0] = -1;
//    for (int i = 1; i <= n; i++) {
//        up[i] = ls[i] = rs[i] = dist[i] = 0;
//        father[i] = i;
//        root[i] = i;
//    }
//}
//
//int find(int i) {
//    father[i] = father[i] == i ? i : find(father[i]);
//    return father[i];
//}
//
//int merge(int i, int j) {
//    if (i == 0 || j == 0) {
//        return i + j;
//    }
//    if (num[i] > num[j] || (num[i] == num[j] && i > j)) {
//        swap(i, j);
//    }
//    rs[i] = merge(rs[i], j);
//    up[rs[i]] = i;
//    if (dist[ls[i]] < dist[rs[i]]) {
//        swap(ls[i], rs[i]);
//    }
//    dist[i] = dist[rs[i]] + 1;
//    father[ls[i]] = father[rs[i]] = i;
//    return i;
//}
//
//int remove(int i) {
//    int h = find(i);
//    father[ls[i]] = ls[i];
//    father[rs[i]] = rs[i];
//    int s = merge(ls[i], rs[i]);
//    int f = up[i];
//    father[i] = s;
//    up[s] = f;
//    if (h != i) {
//        father[s] = h;
//        if (ls[f] == i) {
//            ls[f] = s;
//        } else {
//            rs[f] = s;
//        }
//        for (int d = dist[s]; dist[f] > d + 1; f = up[f], d++) {
//            dist[f] = d + 1;
//            if (dist[ls[f]] < dist[rs[f]]) {
//                swap(ls[f], rs[f]);
//            }
//        }
//    }
//    up[i] = ls[i] = rs[i] = dist[i] = 0;
//    return father[s];
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    prepare();
//    for (int i = 1; i <= n; i++) {
//        cin >> num[i];
//    }
//    for (int i = 1, op, x, y, z; i <= m; i++) {
//        cin >> op;
//        if (op == 0) {
//            cin >> x >> y;
//            root[x] = remove(y);
//            if (root[x] != 0) {
//                father[root[x]] = root[x];
//                up[root[x]] = 0;
//            }
//        } else if (op == 1) {
//            cin >> x;
//            cout << num[root[x]] << "\n";
//        } else if (op == 2) {
//            cin >> x >> y;
//            root[x] = merge(root[x], root[y]);
//            if (root[x] != 0) {
//                father[root[x]] = root[x];
//                up[root[x]] = 0;
//            }
//            root[y] = 0;
//        } else {
//            cin >> x >> y >> z;
//            int h = remove(y);
//            num[y] = z;
//            father[y] = y;
//            root[x] = merge(h, y);
//            if (root[x] != 0) {
//                father[root[x]] = root[x];
//                up[root[x]] = 0;
//            }
//        }
//    }
//    return 0;
//}