package io.syebookstore.api.account;

import io.syebookstore.ServiceConfig;
import io.syebookstore.api.ServiceException;
import io.syebookstore.api.account.AuthUtils;
import io.syebookstore.api.account.repository.Account;
import io.syebookstore.api.account.repository.AccountRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

  private final AccountRepository accountRepository;
  private final ServiceConfig serviceConfig;

  public AccountService(AccountRepository accountRepository, ServiceConfig serviceConfig) {
    this.accountRepository = accountRepository;
    this.serviceConfig = serviceConfig;
  }

  public Account createAccount(CreateAccountRequest request) {
    final var now = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS);
    final var hashedPassword = AuthUtils.hash(request.password());

    final var account =
        new Account()
            .username(request.username())
            .email(request.email())
            .passwordHash(hashedPassword)
            .status(AccountStatus.NON_CONFIRMED)
            .createdAt(now)
            .updatedAt(now);

    return accountRepository.save(account);
  }

  @Transactional(readOnly = true)
  public String login(LoginRequest request) {
    final var account =
        accountRepository.findByEmailOrUsername(
            request.usernameOrEmail(), request.usernameOrEmail());
    if (account == null) {
      throw new ServiceException(404, "Cannot login: account not found");
    }

    if (!AuthUtils.check(request.password(), account.passwordHash())) {
      throw new ServiceException(400, "Login failed");
    }

    return AuthUtils.createToken(serviceConfig.jwtSecret(), account.id());
  }
}
