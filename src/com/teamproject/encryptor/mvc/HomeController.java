package com.teamproject.encryptor.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A controller for handling client requests on home page
 */
@Controller
public class HomeController {

  /**
   * Shows home page
   * @return URL for requested page
   */
  @RequestMapping("/")
  public String showPage() {
    return "main-menu";
  }
}
