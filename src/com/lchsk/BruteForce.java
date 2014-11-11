package com.lchsk;

import java.util.ArrayList;
import java.util.HashMap;

public class BruteForce {

	private HashMap<String, String> lookup = new HashMap<String, String>();

	private int maxLength;

	private char[] characters = "0123456789".toCharArray();

	private boolean stopAtSuccess;
	private boolean printInformation;

	private Encryption e1, e2;

	public BruteForce(String p_password, int p_maxLength, String p_characters,
			boolean p_stopAtSuccess, boolean p_printInformation) {
		maxLength = p_maxLength;
		characters = p_characters.toCharArray();
		stopAtSuccess = p_stopAtSuccess;
		printInformation = p_printInformation;

		e1 = new Encryption();
		e1.initEncryption(p_password);
		e1.encrypt();

		e2 = new Encryption();

		for (int i = 0; i < characters.length; ++i) {
			lookup.put(String.valueOf(i), String.valueOf(characters[i]));
		}
	}

	/*
	 * private int getGreatestPower(int n) {
	 * 
	 * int i = 8;
	 * 
	 * while (i >= 0) { if (n - Math.pow(chars, i) < 0) i--; else return i; //
	 * return (int)Math.pow(2, i); }
	 * 
	 * return 0; }
	 */

	private String getChars(ArrayList<String> b, int len, int p) {

		// String a = new String(b);
		String ret = "";

		// for (int i = 0; i < len; ++i)
		{
			// System.out.println(String.valueOf(i) + " " +
			// String.valueOf(characters[i]));
			// a = a.replaceAll(Integer.toString(i),
			// String.valueOf(characters[i]));
			// System.out.println("replace " + i + " " + a);
		}

		// a = a.replaceAll("012", "123");

		// System.out.println("SIALALA");
		// for (int i = 0; i < a.length(); ++i)
		for (int i = 0; i < b.size(); ++i) {
			// ret += lookup.get(String.valueOf(a.charAt(i)));
			ret += lookup.get(b.get(i));
			// System.out.println(a.charAt(i) + " " +
			// lookup.get(String.valueOf(a.charAt(i))));
		}

		// System.out.println("replaced: " + b + " " + a);

		if (ret.length() >= /* passwordLength */p)
			return ret;
		else {
			int diff = /* passwordLength */p - ret.length();

			String d = "";

			for (int i = 0; i < diff; ++i)
				d += characters[0];

			return d + ret;
		}
	}

	private ArrayList<String> bin(int n, int r) {
		int d, m;

		ArrayList<Integer> b = new ArrayList<Integer>();

		do {
			d = n / r;
			m = n % r;

			b.add(m);

			n = d;

		} while (d != 0 || m != 0);

		// String ret = "";
		ArrayList<String> ret = new ArrayList<String>();

		for (int i = b.size() - 2; i >= 0; i--) {
			// ret += b.get(i);
			ret.add(String.valueOf(b.get(i)));
		}

		return ret;
	}

	/*
	 * private String getBinary(int n) {
	 * 
	 * // int places = 8; // String[] list = new String[5];
	 * 
	 * int g = getGreatestPower(n);
	 * 
	 * if (g > 0) System.out.print("1"); else System.out.print(n % chars);
	 * 
	 * n -= Math.pow(chars, g);
	 * 
	 * for (int d = g - 1; d >= 0; d--) { if (n >= Math.pow(chars, d)) {
	 * System.out.print("1"); n -= Math.pow(chars, d); } else {
	 * System.out.print("0"); } }
	 * 
	 * System.out.println(); return ""; }
	 */

	public void attack() {
		// for (int a = 0; a < 48; a++)
		// System.out.println(getBinary(a));

		int len = characters.length;
		boolean finish = false;

		// e.initDecryption("123");
		// System.out.println("PLAINTEXT: " + new String(e.decrypt()));

		// int options = (int)Math.pow(passwordLength, len);
		// System.out.println("options: " + options);

		// System.out.println(getChars(bin(1, len), len));

		for (int p = 1; p <= maxLength; p++) {

			if (printInformation)
				System.out.println("\n\n\tTrying all passwords of length: " + p + "\n\n");

			String endChar = String.valueOf(characters[len - 1]);
			String end = "";
			for (int i = 0; i < p /* passwordLength */; ++i)
				end += endChar;

			int i = 0;
			// while (i < options)
			while (true) {

				ArrayList<String> b = bin(i, len);
				String a = getChars(b, len, p);
				if (printInformation)
					System.out.println("Trying password: [" + a + "]");
				// e2.initEncryption(getChars(bin(i, len), len, p));
				e2.initEncryption(a);
				e2.encrypt();

				if (Utils.toHex(e1.getCipherText()).equals(
						Utils.toHex(e2.getCipherText()))) {
					System.out.println("\n\n\tPassword broken!\n\tPassword: "
							+ a + "\n\n");

					if (stopAtSuccess) {
						finish = true;
						break;
					}
				}

				i++;

				if (a.equals(end))
					break;
			}

			if (finish)
				break;
		}

		// System.out.println(getChars(bin(12034, len), len));

		//
		// for (int i = 0; i < 4; i++)
		// {
		// for (int j = 0; j < 3; j++)
		// {
		// for (int k = 0; k < 3; k++)
		// {
		// for (int m = 0; m < characters.length; m++)
		// {
		// System.out.println(characters[i % 3] + " " + characters[j % 3] + " "
		// + characters[k % 3]);
		// }
		//
		//
		// }
		// }
		// }

		// for (int i = 0; i < passwordLength; ++i) {
		// p = "";
		//
		// for (int j = 0; j < characters.length; ++j) {
		// p += characters[i * j % 4];
		// }
		//
		// System.out.println(p);
		//
		// }
	}
}
