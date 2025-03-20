package io.syebookstore.api.account;

import static io.syebookstore.api.account.AccountMappers.toAccountInfo;
import static io.syebookstore.api.account.AuthUtils.isEmailValid;

import io.syebookstore.annotations.Protected;
import io.syebookstore.api.ServiceException;
import io.syebookstore.api.account.repository.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
      throw new ServiceException(400, "Missing or invalid: username or email");
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

  @PostMapping("/getAccount")
  @Protected
  public AccountInfo getAccount(
      @RequestAttribute("accountId") Long accountId, @RequestBody(required = false) Long id) {
    if (id == null) {
      return toAccountInfo(accountService.getAccount(accountId));
    }
    return toAccountInfo(accountService.getAccount(id));
  }

  @PostMapping("/updateAccount")
  @Protected
  public AccountInfo updateAccount(
      @RequestAttribute("accountId") Long userId, @RequestBody UpdateAccountRequest request) {
    final var username = request.username();
    if (username != null) {
      if (username.length() < 8 || username.length() > 64) {
        throw new ServiceException(400, "Invalid: username");
      }
    }

    final var email = request.email();
    if (email != null) {
      if (email.length() < 8 || email.length() > 64) {
        throw new ServiceException(400, "Invalid: email");
      }
      if (!isEmailValid(email)) {
        throw new ServiceException(400, "Invalid: email");
      }
    }

    final var description = request.description();
    if (description != null) {
      if (description.length() < 8 || description.length() > 500) {
        throw new ServiceException(400, "Invalid: description");
      }
    }

    final var password = request.password();
    if (password != null) {
      if (password.length() < 8 || password.length() > 64) {
        throw new ServiceException(400, "Invalid: password");
      }
    }

    Account account;
    try {
      account = accountService.updateAccount(request, userId);
    } catch (DataAccessException e) {
      if (e.getMessage().contains("duplicate key value violates unique constraint")) {
        throw new ServiceException(400, "Cannot update account: already exists");
      }
      throw e;
    }
    return toAccountInfo(account);
  }
}
