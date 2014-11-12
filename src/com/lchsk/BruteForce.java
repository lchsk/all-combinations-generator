package com.lchsk;

import java.util.ArrayList;
import java.util.HashMap;

public class BruteForce {

	private HashMap<String, String> lookup = new HashMap<String, String>();

	private int maxLength;

	private char[] characters = "0123456789".toCharArray();

	private boolean stopAtSuccess;
	private boolean printInformation;
	private boolean guessIterationCount;

	private Encryption e1, e2;
	private long start, finish;

	private int maxIterationCount;

	public BruteForce(String p_password, int p_maxLength, String p_characters, boolean p_stopAtSuccess, boolean p_printInformation,
			boolean p_guessIterationCount, int p_maxIterationCount) {
		maxLength = p_maxLength;
		characters = p_characters.toCharArray();
		stopAtSuccess = p_stopAtSuccess;
		printInformation = p_printInformation;
		guessIterationCount = p_guessIterationCount;
		maxIterationCount = p_maxIterationCount;

		e1 = new Encryption();
		e1.initEncryption(p_password);
		e1.encrypt();

		e2 = new Encryption();

		for (int i = 0; i < characters.length; ++i) {
			lookup.put(String.valueOf(i), String.valueOf(characters[i]));
		}
	}

	/**
	 * Changes representation from numbers to arbitrary set of characters.
	 * (Defined in the lookup map)
	 * 
	 * @param b
	 *            Input arraylist
	 * @param len
	 * @param p
	 *            Length of the final value
	 * @return
	 */
	private String getChars(ArrayList<String> b, int len, int p) {
		String ret = "";
		for (int i = 0; i < b.size(); ++i) {

			ret += lookup.get(b.get(i));

		}

		// optional first character in the characters array may be added at the
		// beginning
		if (ret.length() >= p)
			return ret;
		else {
			int diff = p - ret.length();

			String d = "";

			for (int i = 0; i < diff; ++i)
				d += characters[0];

			return d + ret;
		}
	}

	/**
	 * Changes the number from decimal into other positional system
	 * 
	 * @param n
	 *            number (decimal)
	 * @param r
	 *            radix
	 * @return
	 */
	private ArrayList<String> repr(int n, int r) {
		int d, m;

		ArrayList<Integer> b = new ArrayList<Integer>();

		// just changing representation
		do {
			d = n / r;
			m = n % r;

			b.add(m);

			n = d;

		} while (d != 0 || m != 0);

		ArrayList<String> ret = new ArrayList<String>();

		// going from the end
		for (int i = b.size() - 2; i >= 0; i--) {
			ret.add(String.valueOf(b.get(i)));
		}

		return ret;
	}

	public void attack() {

		int len = characters.length;

		// try all possible lengths up to specified value
		for (int p = 1; p <= maxLength; p++) {

			if (printInformation)
				System.out.println("\n\n\tTrying all passwords of length: " + p + "\n\n");

			// Find the final possible value for this length
			String endChar = String.valueOf(characters[len - 1]);
			String end = "";
			for (int i = 0; i < p; ++i)
				end += endChar;

			int i = 0;
			while (true) {

				// get another combination
				ArrayList<String> b = repr(i, len);

				// ...and change its representation
				String a = getChars(b, len, p);

				if (printInformation)
					System.out.println("Trying password: [" + a + "]");

				int count = 1;

				while (count <= maxIterationCount) {

					if (guessIterationCount)
						e2.initEncryption(a, count);
					else
						e2.initEncryption(a);
					e2.encrypt();

					// check for success...
					if (Utils.toHex(e1.getCipherText()).equals(Utils.toHex(e2.getCipherText()))) {
						System.out.println("\n\n\tPassword broken!\n\tPassword: " + a + "\n");
						if (guessIterationCount)
							System.out.println("\tGuessed Iteration Count: " + count + "\n\n");

						if (stopAtSuccess) {
							return;
						}
					}

					count++;

					if (!guessIterationCount)
						break;
				}

				i++;

				if (a.equals(end))
					break;
			}
		}

	}

	public void startTimer() {
		start = System.currentTimeMillis();
	}

	public void stopTimer() {
		finish = System.currentTimeMillis();

		long diff = finish - start;

		System.out.println("Finished in: " + diff + " [ms]");
	}
}
