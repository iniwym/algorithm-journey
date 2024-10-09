package class146;

// 约瑟夫环问题加强
// 来自真实大厂笔试，对数器验证
public class Code03_JosephFollowUp {

	// 为了测试
	// 暴力模拟
	public static int joseph1(int n, int[] arr) {
		if (n == 1) {
			return 1;
		}
		int[] next = new int[n + 1];
		for (int i = 1; i < n; i++) {
			next[i] = i + 1;
		}
		next[n] = 1;
		int pre = n, cur = 1;
		for (int i = 1; i < n; i++) {
			for (int cnt = 1; cnt < arr[i]; cnt++) {
				pre = cur;
				cur = next[cur];
			}
			next[pre] = next[cur];
			cur = next[cur];
		}
		return cur;
	}

	// 正式方法，时间复杂度O(n)
	public static int joseph2(int n, int[] arr) {
		if (n == 1) {
			return 1;
		}
		int ans = 1;
		for (int c = 2, i = n - 1; c <= n; c++, i--) {
			ans = (ans + arr[i] - 1) % c + 1;
		}
		return ans;
	}

	// 随机生成每轮报数
	// 为了测试
	// 如果一共有n个数，那么一共有n-1轮，用arr[1..n-1]表示
	public static int[] randomArray(int n, int v) {
		int[] arr = new int[n];
		for (int i = 1; i < n; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}

	// 对数器
	// 为了测试
	public static void main(String[] args) {
		int N = 100;
		int V = 500;
		int test = 10000;
		System.out.println("测试开始");
		for (int i = 1; i <= test; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[] arr = randomArray(n, V);
			int ans1 = joseph1(n, arr);
			int ans2 = joseph2(n, arr);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}