package io.syebookstore.api.account;

import io.syebookstore.api.ServiceException;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AccountController {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/createAccount")
  public String createAccount(@RequestBody CreateAccountRequest request) {
    final var username = request.username();
    if (username == null) {
      throw new ServiceException(400, "Missing or invalid: username");
    }
    if (username.length() < 8 || username.length() > 64) {
      throw new ServiceException(400, "Missing or invalid: username");
    }

    final var email = request.email();
    if (email == null) {
      throw new ServiceException(400, "Missing or invalid: email");
    }
    if (email.length() < 8 || email.length() > 64) {
      throw new ServiceException(400, "Missing or invalid: email");
    }

    if (!isEmailValid(email)) {
      throw new ServiceException(400, "Missing or invalid: email");
    }

    final var password = request.password();
    if (password == null) {
      throw new ServiceException(400, "Missing or invalid: password");
    }
    if (password.length() < 8 || password.length() > 64) {
      throw new ServiceException(400, "Missing or invalid: password");
    }

    accountService.createAccount();

    return "";
  }

  private static boolean isEmailValid(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }
}
