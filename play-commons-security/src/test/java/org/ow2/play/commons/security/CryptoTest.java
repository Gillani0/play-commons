/**
 * 
 */
package org.ow2.play.commons.security;

import org.ow2.play.commons.security.Crypto.HashType;

import junit.framework.TestCase;

/**
 * @author chamerling
 * 
 */
public class CryptoTest extends TestCase {

	public void testDefaultHashPassword() throws Exception {
		String password = "foo";
		String hash = Crypto.passwordHash(password);
		System.out.println(hash);
		assertEquals(hash, Crypto.passwordHash(password, HashType.MD5));
	}
}
