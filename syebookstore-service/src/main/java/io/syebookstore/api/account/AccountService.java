package io.syebookstore.api.account;

import io.syebookstore.api.account.auth.PasswordHashing;
import io.syebookstore.api.account.repository.Account;
import io.syebookstore.api.account.repository.AccountRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(CreateAccountRequest request) {
    final var now = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS);
    final var hashedPassword = PasswordHashing.hash(request.password());

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
}
