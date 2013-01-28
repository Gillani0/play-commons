/**
 * 
 */
package org.ow2.play.commons.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * Let's hash some passwords...
 * 
 * @author chamerling
 * 
 */
public class Crypto {

	public enum HashType {
		MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512");
		private String algorithm;

		HashType(String algorithm) {
			this.algorithm = algorithm;
		}

		@Override
		public String toString() {
			return this.algorithm;
		}
	}
	
    private static final HashType DEFAULT_HASH_TYPE = HashType.MD5;


	/**
	 * Create a password hash using the default hashing algorithm
	 * 
	 * @param input
	 *            The password
	 * @return The password hash
	 */
	public static String passwordHash(String input) {
		return passwordHash(input, DEFAULT_HASH_TYPE);
	}

	/**
	 * Create a password hash using specific hashing algorithm
	 * 
	 * @param input
	 *            The password
	 * @param hashType
	 *            The hashing algorithm
	 * @return The password hash
	 */
	public static String passwordHash(String input, HashType hashType) {
		try {
			MessageDigest m = MessageDigest.getInstance(hashType.toString());
			byte[] out = m.digest(input.getBytes());
			return new String(Base64.encodeBase64(out));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
