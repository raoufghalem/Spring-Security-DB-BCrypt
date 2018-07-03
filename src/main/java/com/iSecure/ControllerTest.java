package com.iSecure;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.com.iSecure.config.CustomUserDetails;

@RequestMapping("")
@RestController
public class ControllerTest {
	
	@GetMapping("/main")
	public String someTest() {
		return "hello index page";
	}

}

@RequestMapping("admin")
@RestController
class ControllerAdmin{
	@GetMapping("/main")
	public String someTest(@AuthenticationPrincipal CustomUserDetails ud) {
		return "hello index page for admin: " + ud.toString();
	}
	
}

@RequestMapping("manager")
@RestController
class ControllerManager{
	

	@PreAuthorize("hasAnyRole('MANAGER')")
	@GetMapping("/main")
	public String someTest(@AuthenticationPrincipal CustomUserDetails ud) {
		return "hello index page for manager: " + ud.toString();
	}
	
}

