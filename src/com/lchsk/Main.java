package com.lchsk;

import java.util.Scanner;

public class Main {
	// Settings below

	// Characters that will be used while performing
	// brute-force search for all possible passwords
	private static final String characters = "0123456789";

	// If set to true brute-force search will stop
	// when it finds the correct password
	private static final boolean stopAtSuccess = false;

	// If set to true, you will be asked
	// to define a password during runtime
	private static final boolean askForPassword = false;

	// If setting 'askForPassword' == false, then
	// this password will be used
	private static final String defaultPassword = "123";

	// Secret text that will be encrypted
	private static final String secret = "Secret Text";

	// If set to true, then a lot of information
	// will be shown in console during searching for password
	private static final boolean printInformation = true;

	// Brute-force algorithm will look for all possible
	// combinations of length 1..Max
	private static final int maxPasswordLength = 4;

	// --------------

	public static void main(String[] args) {

		if (askForPassword) {
			System.out.println("Type your password:");
			Scanner s = new Scanner(System.in);
			String pass = s.nextLine();

			Encryption e = new Encryption();
			e.setSecretMessage(secret);
			e.initEncryption(pass);

			System.out.println("Your secret will be encrypted.");
			System.out
					.println("Here's ciphertext: " + Utils.toHex(e.encrypt()));

			System.out.println();

			boolean tryAgain = true;

			while (tryAgain) {
				System.out
						.println("Please type your password again to decrypt the secret message:");
				pass = s.nextLine();
				e.initDecryption(pass);

				try {
					System.out.println("Here's your decrypted message: \n"
							+ new String(e.decrypt()));
					tryAgain = false;
					System.exit(0);
				} catch (Exception exception) {
					tryAgain = true;
					System.out
							.println("Password you've just typed in does not match the one you've typed before. Please try again.");
				}
			}
		} else {
			// Brute-force search goes here...
			BruteForce bf = new BruteForce(defaultPassword, maxPasswordLength,
					characters, stopAtSuccess, printInformation);
			bf.startTimer();
			bf.attack();
			bf.stopTimer();
		}

	}
}
