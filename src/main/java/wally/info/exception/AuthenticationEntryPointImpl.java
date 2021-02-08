package wally.info.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
      throws IOException, ServletException {

    var apiResponse = new ErrorResponse(response.getStatus(), e.getMessage());
    var mapper = new ObjectMapper();

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(mapper.writeValueAsString(apiResponse));
  }
}
