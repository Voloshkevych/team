package com.teamproject.encryptor.mvc;

import java.util.Arrays;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.PBEKeySpec;

import javax.crypto.IllegalBlockSizeException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;

abstract class SymmetricCipher {
	public SecretKeySpec keySpec;
	public IvParameterSpec iv;
	public Cipher cipher;
	
	public SymmetricCipher(String password) {
		SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256); // AES-256
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
	byte[] _inputBytes;
	byte[] _outputBytes;

	public AESCipher(String password) {
		super(password);
	}

	public String encrypt(String text) {
		byte[] inputBytes = text.getBytes();
		byte[] outputBytes;
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			outputBytes = cipher.doFinal(inputBytes);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		_inputBytes = inputBytes;
		_outputBytes = outputBytes;
		return Base64.getEncoder().encodeToString(outputBytes);
	}
	
	public String decrypt(String text) {
		_inputBytes = Base64.getDecoder().decode(text);
		
		try {
			cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
			_outputBytes = cipher.doFinal(_inputBytes);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		return new String(_outputBytes, StandardCharsets.UTF_8);
	}
}
