package com.teamproject.encryptor.mvc;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

abstract class SymmetricCipher {
	public String text;
	public SecretKeySpec key;
	
	public SymmetricCipher(String text, String key) {
		this.text = text;
		this.key = new SecretKeySpec(key.getBytes(), "AES");
	}
	
	public abstract String encrypt();
	public abstract String decrypt();
}


public class AESCipher extends SymmetricCipher {
	public AESCipher(String text, String key) {
		super(text, key);
	}

	public String encrypt() {
		return encryptOrDecrypt(Cipher.ENCRYPT_MODE);
	}
	
	public String decrypt() {
		return encryptOrDecrypt(Cipher.DECRYPT_MODE);
	}
	
	protected String encryptOrDecrypt(int mode) {
		
		byte[] inputBytes = text.getBytes();
		
		int size = inputBytes.length;
		
		byte[] result = new byte[size];
		
		try {
			Cipher cipher = Cipher.getInstance("AES");
			
			cipher.init(mode, key);
			
			
			byte[] outputBytes = cipher.doFinal(inputBytes);
			
			result = Arrays.copyOf(outputBytes, size);
			
		} catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return new String(result, StandardCharsets.UTF_8);
	}
}
