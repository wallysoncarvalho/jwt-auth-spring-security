package wally.info.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wally.info.exception.ErrorResponse;
import wally.info.util.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
  private static Logger logger = LoggerFactory.getLogger(JwtFilter.class);
  private final JwtTokenProvider jwtTokenProvider;

  public JwtFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = jwtTokenProvider.resolveToken(request);
      jwtTokenProvider.validateToken(token);
      var auth = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      response.setStatus(400);

      var mapper = new ObjectMapper();
      var apiResponse = new ErrorResponse(response.getStatus(), e.getMessage());

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(mapper.writeValueAsString(apiResponse));

    }
  }
}
