package wally.info.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
      throws IOException, ServletException {

    var apiResponse = new ErrorResponse(403, e.getMessage());
    var mapper = new ObjectMapper();

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(apiResponse.getStatus());
    response.getWriter().write(mapper.writeValueAsString(apiResponse));
  }
}
