package io.syebookstore.api.account.auth;

import static org.junit.jupiter.api.Assertions.*;

import io.syebookstore.api.account.AuthUtils;
import org.junit.jupiter.api.Test;

class AuthUtilsTest {

  @Test
  void testJWT() {
    final var secret = "test12345";
    final var id = 123445L;

    final var startCreate = System.nanoTime();
    final var token = AuthUtils.createToken(secret, id);
    final var endCreate = System.nanoTime();

    assertInstanceOf(String.class, token);

    final var startVerify = System.nanoTime();
    final var verified = AuthUtils.verify(secret, token);
    final var endVerify = System.nanoTime();
    final var actualId = verified.getClaim("id").asLong();
    assertEquals(id, actualId);
    System.out.printf("Creation nanos: %d", endCreate - startCreate);
    System.out.printf("\nVerification nanos: %d", endVerify - startVerify);
  }
}
