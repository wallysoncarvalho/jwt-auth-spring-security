package wally.info.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import wally.info.exception.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {

    var apiResponse = new ApiResponse(HttpStatus.FORBIDDEN.value(), "Access denied!");
    var mapper = new ObjectMapper();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(apiResponse.getStatus());
    response.getWriter().write(mapper.writeValueAsString(apiResponse));
    response.getWriter().flush();
  }
}
