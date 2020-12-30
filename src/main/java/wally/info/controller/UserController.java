package wally.info.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wally.info.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
	private final AuthService authService;

	public UserController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/client")
	@PreAuthorize("hasRole('CLIENT')")
	public String auth_client(HttpServletRequest http) {

		return "Client authorized";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String auth_admin() {
		return "Admin authorized";
	}



}
