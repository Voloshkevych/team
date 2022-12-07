import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public abstract class SymmetricCipher {
	private String text;
	private SecretKeySpec key;
	
	public SymmetricCipher(String text, String key) {
		this.text = text;
		this.key = new SecretKeySpec(key.getBytes(), "AES");
	}
	
	public abstract String encrypt();
	public abstract String decrypt();
}


public class AESCipher extends SymmetricCipher {
	public String encrypt() {
		return encryptOrDecrypt(Cipher.ENCRYPT_MODE);
	}
	
	public String decrypt() {
		return encryptOrDecrypt(Cipher.DECRYPT_MODE);
	}
	
	protected String encryptOrDecrypt(int mode) {
		var cipher = Cipher.getInstance("AES");
		cipher.init(mode, key);

		var inputBytes = text.getBytes();
		var outputBytes = cipher.doFinal(inputBytes);
		
		return new String(outputBytes, StandardCharsets.UTF_8);
	}
}
