package io.syebookstore.api.account;

import io.syemessenger.api.ServiceException;
import io.syemessenger.api.account.CreateAccountRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AccountController {

  @PostMapping("/createAccount")
  public String createAccount(CreateAccountRequest request) {
    throw new ServiceException(400, "Missing or invalid: username");
  };
}
