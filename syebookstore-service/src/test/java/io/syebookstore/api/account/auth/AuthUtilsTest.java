package io.syebookstore.api.account.auth;

import static org.junit.jupiter.api.Assertions.*;

import io.syebookstore.api.account.AuthUtils;
import org.junit.jupiter.api.Test;

class AuthUtilsTest {

  @Test
  void testJWT() {
    final var secret = "test12345";
    final var id = 123445L;

    final var token = AuthUtils.createToken(secret, id);

    assertInstanceOf(String.class, token);

    final var verified = AuthUtils.verify(secret, token);
    final var actualId = verified.getClaim("id").asLong();
    assertEquals(id, actualId);
  }
}
