package wally.info.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping
	private String teste() {
		return "protected resource !!!";
	}



}
