package io.syebookstore.api.account.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import io.syebookstore.api.account.AuthUtils;
import org.junit.jupiter.api.Test;

class AuthUtilsTest {

  @Test
  void testJWTVerifyFailedWrongSecret() {
    final var token = AuthUtils.createToken(randomAlphanumeric(10), Long.MAX_VALUE);

    assertInstanceOf(String.class, token);

    try {
      AuthUtils.verify(randomAlphanumeric(10), token);
    } catch (Exception ex) {
      assertInstanceOf(SignatureVerificationException.class, ex);
    }
  }

  @Test
  void testJWTVerificationFailed() {
    try {
      AuthUtils.verify("test12345", randomAlphanumeric(10));
      fail("Expected exception");
    } catch (Exception ex) {
      assertInstanceOf(JWTDecodeException.class, ex);
    }
  }

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
