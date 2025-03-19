package io.syebookstore.api.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.syebookstore.api.ServiceException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthUtils {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  public static boolean isEmailValid(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }

  public static String createToken(String secret, Long id) {
    try {
      final var algorithm = Algorithm.HMAC256(secret);
      final var now = Instant.now(Clock.systemUTC());

      return JWT.create()
          .withExpiresAt(now.plus(7, ChronoUnit.DAYS))
          .withPayload(Map.of("id", id))
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new ServiceException(500, "Internal service error");
    }
  }

  public static DecodedJWT verify(String secret, String token) {
    try {
      final var algorithm = Algorithm.HMAC256(secret);
      final var verifier = JWT.require(algorithm).build();
      return verifier.verify(token);
    } catch (JWTVerificationException ex) {
      throw new ServiceException(500, "Internal service error");
    }
  }

  public static String hash(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(4));
  }

  public static boolean check(String password, String hash) {
    return BCrypt.checkpw(password, hash);
  }
}
