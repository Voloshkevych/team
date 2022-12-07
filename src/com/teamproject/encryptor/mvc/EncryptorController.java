package com.teamproject.encryptor.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EncryptorController {
  
  @RequestMapping("/text-for-encryption")
  public String textForEncryption() {
    return "text-form-for-encryption";
  }
  
  @RequestMapping("/processEncryptionForm")
  public String processEncryptionForm(HttpServletRequest request, Model model) {
	  
	  String originalInputText = request.getParameter("inputText1");
	  // TODO add input for key
    String key = request.getParameter("keyInput");


	  Cipher cipher = new Cipher(originalInputText, key);
	  String encryptedText = cipher.encrypt();
	  
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
    String key = request.getParameter("keyInput");


	  Cipher cipher = new Cipher(originalInputText, key);
	  String decryptedText = cipher.decrypt();
	  
	  model.addAttribute("text1", originalInputText);
	  
	  model.addAttribute("text2", decryptedText);
	  
	  return "confirmation";
  }
}
