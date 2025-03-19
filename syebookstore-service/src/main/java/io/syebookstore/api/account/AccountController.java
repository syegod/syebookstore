package io.syebookstore.api.account;

import static io.syebookstore.api.account.AccountMappers.toAccountInfo;

import io.syebookstore.api.ServiceException;
import io.syebookstore.api.account.repository.Account;
import java.util.regex.Pattern;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/createAccount")
  public AccountInfo createAccount(@RequestBody CreateAccountRequest request) {
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

    if (!AuthUtils.isEmailValid(email)) {
      throw new ServiceException(400, "Missing or invalid: email");
    }

    final var password = request.password();
    if (password == null) {
      throw new ServiceException(400, "Missing or invalid: password");
    }
    if (password.length() < 8 || password.length() > 64) {
      throw new ServiceException(400, "Missing or invalid: password");
    }

    Account account;
    try {
      account = accountService.createAccount(request);
    } catch (DataAccessException e) {
      if (e.getMessage().contains("duplicate key value violates unique constraint")) {
        throw new ServiceException(400, "Cannot create account: already exists");
      }
      throw e;
    }

    return toAccountInfo(account);
  }

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest request) {
    final var usernameOrEmail = request.usernameOrEmail();
    if (usernameOrEmail == null) {
      throw new ServiceException(400, "Missing or invalid: username or email");
    }
    if (usernameOrEmail.length() < 8 || usernameOrEmail.length() > 64) {
      throw new ServiceException(400, "Missing or invalid: username");
    }

    final var password = request.password();
    if (password == null) {
      throw new ServiceException(400, "Missing or invalid: password");
    }
    if (password.length() < 8 || password.length() > 64) {
      throw new ServiceException(400, "Missing or invalid: password");
    }

    return accountService.login(request);
  }
}
