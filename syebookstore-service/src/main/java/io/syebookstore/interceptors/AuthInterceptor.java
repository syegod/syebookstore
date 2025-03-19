package io.syebookstore.interceptors;

import static io.syebookstore.api.account.AuthUtils.verify;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.syebookstore.ServiceConfig;
import io.syebookstore.annotations.Protected;
import io.syebookstore.api.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

  private final ServiceConfig serviceConfig;

  public AuthInterceptor(ServiceConfig serviceConfig) {
    this.serviceConfig = serviceConfig;
  }

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    final var method = ((HandlerMethod) handler).getMethod();
    if (method.isAnnotationPresent(Protected.class)) {
      var token = request.getHeader("Authorization");
      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
        try {
          final var decodedJWT = verify(serviceConfig.jwtSecret(), token);
          request.setAttribute("userId", decodedJWT.getClaim("id").asLong());
        } catch (JWTVerificationException ex) {
          throw new ServiceException(403, "Not authenticated");
        }
      } else {
        throw new ServiceException(403, "Not authenticated");
      }
    }
    return true;
  }
}
