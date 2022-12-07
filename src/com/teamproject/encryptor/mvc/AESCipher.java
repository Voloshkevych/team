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
	public PBEKeySpec keySpec;
	public IvParameterSpec iv;
	public Cipher cipher;
	
	public SymmetricCipher(String key) {
		SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt, 65536, 256); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
		
		this.iv = iv;
		this.keySpec = keySpec;
		this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	}
	
	public abstract String encrypt(String text);
	public abstract String decrypt(String text);
}


public class AESCipher extends SymmetricCipher {
	public AESCipher(String key) {
		super(key);
	}

	public String encrypt(String text) {
		byte[] inputBytes = text.getBytes();
		int size = inputBytes.length;
		byte[] result = new byte[size];
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			byte[] outputBytes = cipher.doFinal(inputBytes);
			result = Arrays.copyOf(outputBytes, size);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		return Base64.getEncoder().encodeToString(result);
	}
	
	public String decrypt(String text) {
		byte[] inputBytes = Base64.getDecoder().decode(text);
		int size = inputBytes.length;
		byte[] result = new byte[size];
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			byte[] outputBytes = cipher.doFinal(inputBytes);
			result = Arrays.copyOf(outputBytes, size);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		return new String(result, StandardCharsets.UTF_8);
	}
}
