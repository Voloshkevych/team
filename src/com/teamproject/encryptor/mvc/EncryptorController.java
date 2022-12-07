package com.teamproject.encryptor.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EncryptorController {
  AESCipher cipher;
  
  @RequestMapping("/text-for-encryption")
  public String textForEncryption() {
    return "text-form-for-encryption";
  }
  
  @RequestMapping("/processEncryptionForm")
  public String processEncryptionForm(HttpServletRequest request, Model model) {
	  
	  String originalInputText = request.getParameter("inputText1");
	  // TODO add input for key
      String key = request.getParameter("keyInput1");

    if (cipher == null) {
      cipher = new AESCipher(key);
    }
	  
	  String encryptedText = cipher.encrypt(originalInputText);
	  
	  model.addAttribute("text1", originalInputText);
	  
	  model.addAttribute("text2", encryptedText);
	  
    return "confirmation";
  }
  
  @RequestMapping("/text-for-decryption")
  public String textForDecryption() {
    return "text-form-for-decryption";
  }
  
  @RequestMapping("/processDecryptionForm")
  public String processDecryptionForm(HttpServletRequest request, Model model) {
	  
	  String originalInputText = request.getParameter("inputText2");
	  // TODO add input for key
      String key = request.getParameter("keyInput2");

    if (cipher == null) {
      AESCipher cipher = new AESCipher(key);
    }
	  
	  String decryptedText = cipher.decrypt(originalInputText);
	  
	  model.addAttribute("text1", originalInputText);
	  
	  model.addAttribute("text2", decryptedText);
	  
	  return "confirmation";
  }
}
