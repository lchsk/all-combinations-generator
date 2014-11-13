package com.lchsk;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encryption {

	public class EncryptionData {
		private PBEKeySpec pbeKeySpec;
		private PBEParameterSpec pbeParamSpec;
		private SecretKeyFactory keyFac;
		private byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
		private Cipher cipher;
		private int count;

		public EncryptionData(int p_count) {
			count = p_count;
		}
	}

	private byte[] cleartext = "default".getBytes();
	private byte[] ciphertext;

	private EncryptionData e;
	private EncryptionData d;

	// Default iteration count
	private final int iterationCount = 2048;

	public void initEncryption(String p_password, int p_iterationCount) {
		try {
			e = new EncryptionData(p_iterationCount);
			e.pbeParamSpec = new PBEParameterSpec(e.salt, e.count);
			e.pbeKeySpec = new PBEKeySpec(p_password.toCharArray());
			e.keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			Key pbeKey = e.keyFac.generateSecret(e.pbeKeySpec);

			e.cipher = Cipher.getInstance("PBEWithMD5AndDES");
			e.cipher.init(Cipher.ENCRYPT_MODE, pbeKey, e.pbeParamSpec);
		} catch (Exception e) {

		}
	}

	public void initEncryption(String p_password) {
		initEncryption(p_password, iterationCount);
	}

	public void initDecryption(String p_password) {
		try {
			d = new EncryptionData(iterationCount);
			d.pbeParamSpec = new PBEParameterSpec(d.salt, d.count);
			d.pbeKeySpec = new PBEKeySpec(p_password.toCharArray());
			d.keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			Key pbeKey = d.keyFac.generateSecret(d.pbeKeySpec);

			d.cipher = Cipher.getInstance("PBEWithMD5AndDES");
			d.cipher.init(Cipher.DECRYPT_MODE, pbeKey, d.pbeParamSpec);
		} catch (Exception e) {

		}
	}

	public byte[] encrypt() {
		try {
			ciphertext = e.cipher.doFinal(cleartext);

			return ciphertext;
		} catch (Exception e) {
			throw new NullPointerException("Error during encryption");
		}
	}

	public byte[] decrypt() {
		try {
			return d.cipher.doFinal(ciphertext);
		} catch (Exception e) {
			throw new NullPointerException("Error during decryption");
		}
	}

	public byte[] getCipherText() {
		return ciphertext;
	}

	public byte[] getClearText() {
		return cleartext;
	}

	public void setSecretMessage(String p_msg) {
		cleartext = p_msg.getBytes();
	}

}
