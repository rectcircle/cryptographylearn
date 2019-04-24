package cn.rectcircle.cryptographylearn.rsa;

/**
 * Helper
 */
public class Helper {

	/**
	 * <p>欧几里得算法：</p>
	 * gcd(a, b) 表示 a, b的最大公约数; a, b不全为0
	 */
	static int gcd(int a, int b) {
		// gcd(a, b) => if(b=0) a else gcd(b, a%b)
		int r;
		while (b > 0) {
			r = a % b;
			a = b;
			b = r;
		}
		return a;
	}

	/**
	 * <p>
	 * 求解扩展欧几里得方程:
	 * </p>
	 * <p>
	 * gcd(a, b) = ax + by ; a, b不全为0
	 * </p>
	 * <p>
	 * 推导过程：
	 * 
	 * <pre>
	 * (1) 终结条件：b=0, gcd（a, b）= a; 显然此时 x=1, y=0;
	 *(2) 递推条件：a>b>0
	 *     ax1 + by1 = gcd(a,b) ①
	 *     bx2 + (a % b)y2 = gcd(b,a % b) ②
	 *     gcd(a,b) = gcd(b,a % b) ③
	 *   整理: ax1 + by1 = bx2 + (a % b)y2
	 *        ax1 + by1 = bx2 + (a - (a/b) * b)y2
	 *        ax1 + by1 = ay2 + bx2 - (a/b)*b)y2
	 *        ax1 + by1 = ay2 + b(x2 - (a/b))y2
	 *   解得: x1 = y2, y1 = x2 - (a/b)y2 [递推公式]
	 *   说明: 以上 / 和 % 表示整除和求余
	 * </pre>
	 * </p>
	 * @return [gcd(a, b), x, y]
	 */
	static int[] extgcd(int a, int b) {
		if (a == 0 && b == 0)
			throw new IllegalArgumentException("a, b 能全为0");
		int x, y;
		if(b==0){
			x=1;
			y=0;
			return new int[]{a, x, y};
		}
		int[] result = extgcd(b, a%b);
		x = result[2];
		y = result[1] - (a / b) * result[2];
		result[1] = x;
		result[2] = y;
		return result;
	}
	
	/**
	 * <p>
	 * 求乘法逆元：ax ≡ 1 (mod b), a⊥b, 求 x 的最小正整数解值
	 * <pre>
	 *   ax ≡ 1 (mod b)
	 *(ax - 1) / b = y
	 *ax - 1 = by
	 *ax - by = 1
	 *即: ax + by = gcd(a, b)
	 *通过扩展gcd可以求出x0, 则 x = x0 + k*b/gcd(a, b) k∈Z
	 * </pre>
	 * <p>
	 * 
	 * @see http://www.cnblogs.com/rir1715/p/7745110.html
	 */
	static int modReverse(int a, int b) {
		int result[] = extgcd(a, b);
		return (result[1] % b + b) % b;
	}

	/**
	 * 快速幂取模算法：
	 * <p>
	 * 公式如下：
	 * <pre>
	 * a^b % c = ((a^2)^(b/2) % c) b是偶数
	 * a^b % c = (a*(a^2)^(b/2) % c) b是计数
	 * </pre>
	 * </p>
	 */
	static int powerMod(int a, int b, int c) {
		int ans = 1;
		a = a % c;
		while (b > 0) {
			if ((b & 1) == 1) {
				ans = (ans * a) % c;
			}
			b >>= 1;
			a = (a * a) % c;
		}
		return ans;
	}
}