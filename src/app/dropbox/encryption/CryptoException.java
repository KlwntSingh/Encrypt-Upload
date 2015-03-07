package app.dropbox.encryption;

/**
 * A utility class that encrypts or decrypts a file. 
 * @author www.codejava.net
 *
 */

public class CryptoException extends Exception {

	public CryptoException() {
	}

	public CryptoException(String message, Throwable throwable) {
		super(message, throwable);
	}
}