package io.syebookstore.api.account;

import static io.syebookstore.api.account.AuthUtils.hash;

import io.syebookstore.ServiceConfig;
import io.syebookstore.api.ServiceException;
import io.syebookstore.api.account.repository.Account;
import io.syebookstore.api.account.repository.AccountRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final AccountRepository accountRepository;
  private final ServiceConfig serviceConfig;

  public AccountService(AccountRepository accountRepository, ServiceConfig serviceConfig) {
    this.accountRepository = accountRepository;
    this.serviceConfig = serviceConfig;
  }

  public Account createAccount(CreateAccountRequest request) {
    final var now = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS);
    final var hashedPassword = hash(request.password());

    final var account =
        new Account()
            .username(request.username())
            .email(request.email())
            .passwordHash(hashedPassword)
            // TODO: https://github.com/syegod/syebookstore/issues/4
            .status(AccountStatus.CONFIRMED)
            .createdAt(now)
            .updatedAt(now);

    LOGGER.info("Creating account: {}", account);

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

    final var token = AuthUtils.createToken(serviceConfig.jwtSecret(), account.id());

    LOGGER.info("Creating jwt token: {}", token);

    return token;
  }

  @Transactional(readOnly = true)
  public Account getAccount(Long id) {
    final var account = accountRepository.findById(id).orElse(null);
    if (account == null) {
      throw new ServiceException(404, "Account not found");
    }
    return account;
  }

  public Account updateAccount(UpdateAccountRequest request, Long accountId) {
    var account = accountRepository.findById(accountId).orElse(null);
    if (account == null) {
      throw new ServiceException(404, "Account not found");
    }

    if (request.username() != null) {
      account.username(request.username());
    }
    if (request.email() != null) {
      account.email(request.email());
    }
    if (request.password() != null) {
      account.passwordHash(hash(request.password()));
    }
    if (request.description() != null) {
      account.description(request.description());
    }

    // TODO: https://github.com/syegod/syebookstore/issues/4
    account.status(AccountStatus.CONFIRMED);

    account.updatedAt(LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS));

    LOGGER.info("Updating account: {}", account);

    return account;
  }
}
