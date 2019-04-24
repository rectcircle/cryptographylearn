package cn.rectcircle.cryptographylearn.rsa;

/**
 * Experiment
 */
public class Experiment {
	private int p; // 素数1
	private int q; // 素数2
	private int N; // p*q
	private int r; // φ(N) = (p-1)*(q-1)
	private int e; // e ⊥ r, 公钥(N, e), 找一个任意e
	private int d; // ed ≡ 1 (mod r), d为e关于r的乘法逆元, 私钥(N, e)

	public Experiment(int p, int q) {
		this.p = p;
		this.q = q;
		this.generateKey();
	}

	private void generateKey() {
		this.N = p * q;
		this.r = (p - 1) * (q - 1);
		// 选择公钥
		for (this.e = 3; Helper.gcd(this.e, r) != 1; this.e++)
			;
		System.out.println(Helper.gcd(r, e));
		// 计算私钥
		// 暴力求解
		// for (int z = 1;; z++) {
		// 	this.d = r * z + 1;
		// 	if (this.d % e == 0) {
		// 		this.d /= e;
		// 		break;
		// 	}
		// }
		// 乘法逆元extgcd法
		this.d = Helper.modReverse(e, r);
	}

	public String getPublicKey() {
		return String.format("public-key: %d, %d", this.N, this.e);
	}

	public String getPrivateKey() {
		return String.format("private-key: %d, %d", this.N, this.d);
	}

	/**
	 * m^e ≡ c (mod N)
	 * @param m 明文
	 * @return c 密文
	 */
	private int encrypt(byte m) {
		return Helper.powerMod(m, e, N);
	}

	public int[] encrypt(byte[] origin) {
		int[] result = new int[origin.length];
		for (int i = 0; i < origin.length; i++) {
			result[i] = this.encrypt(origin[i]);
		}
		return result;
	}

	/**
	 * c^d ≡ m (mod N)
	 * 
	 * <p>
	 * 证明： 从<code>m^e ≡ c (mod N)</code>推导出<code>c^d ≡ m (mod N)</code>
	 * 
	 * <pre>
	 * m^e ≡ c (mod N)
	 * m^e = c + k0N                (k0∈Z)
	 * c = m^e + k1N                (k1∈Z)
	 * c^d = (m^e + k1N)^d 
	 *     = m^ed + k2N             (根据二项式定理展开：形如 (x+y)^n )
	 *     = m^(1+k3r) + k2N        (k3∈Z) (根据条件：ed ≡ 1 (mod r))
	 *     = m * m^(k3r) + k2N
	 *     = m * m^(k3r) + k2N
	 *     = m * ((m^φ(N))^k3) + k2N 
	 *     = m * (1+k4N) + k2N      (k4∈Z)(根据费马欧拉定理替换：a^φ(N) ≡ 1 (mod N))
	 *     = m + k5N                (k5∈Z)
	 * 因此显然：c^d ≡ m (mod N)
	 * </pre>
	 * </p>
	 * 
	 * @param c 密文
	 * @return m 明文
	 */
	private byte decrypt(int c) {
		return (byte) Helper.powerMod(c, d, N);
	}
	
	public byte[] decrypt(int[] encryption) {
		byte[] result = new byte[encryption.length];
		for (int i = 0; i < encryption.length; i++) {
			result[i] = this.decrypt(encryption[i]);
		}
		return result;
	}

	@Override
	public String toString() {
		return this.getPublicKey()+ "\n" + this.getPrivateKey();
	}

	public static void main(String[] args) {
		Experiment p = new Experiment(17, 19);
		System.out.println(p);
		byte[] origin = new byte[] { 2 };
		int[] encryption = p.encrypt(origin);
		System.out.println(encryption[0]);
		byte[] result = p.decrypt(encryption);
		System.out.println(result[0]);
		System.out.println(new String(p.decrypt(p.encrypt("abcdefg你好".getBytes()))));
		
	}

}