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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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
	public PBEKeySpec keySpec;
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
	public AESCipher(String password) {
		super(password);
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
			cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
			byte[] outputBytes = cipher.doFinal(inputBytes);
			result = Arrays.copyOf(outputBytes, size);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		return new String(result, StandardCharsets.UTF_8);
	}
}
