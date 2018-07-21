package com.iSecure;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iSecure.config.CustomUserDetails;
import com.iSecure.config.SecureEntity.Role;
import com.iSecure.config.SecureEntity.RoleRepository;
import com.iSecure.config.SecureEntity.Users;
import com.iSecure.config.SecureEntity.UsersRepository;



@Controller
class ControllerRegister {
	
	RoleRepository roleRepository;
	UsersRepository userRepo;
	public ControllerRegister(RoleRepository roleRepository, UsersRepository userRepo) {
		this.roleRepository = roleRepository;
		this.userRepo = userRepo;
	}

	@GetMapping("register")
	public String registrationForm(Model model) {
		model.addAttribute("role",roleRepository.findAll());
		return "register";
	}
	
	@PostMapping("register")
	public String registration(@ModelAttribute("reg") Users user) {
		
		System.out.println(user.getEmail());
		
//		List<Role> role = roleRepository.findAll();
//		Set<Role> roleSet = new HashSet<>(role);

		Set<Role> roleSet =new HashSet<>();
		Role singleRole = roleRepository.findByRole("user");
		System.out.println(singleRole);
		roleSet.add(roleRepository.findByRole("user"));
		
		user.setRoles(roleSet);
		user.setLastName(" ");
		
		// this should be delegated to some kind of automation upon email confirmation
		user.setActive(1);
		
		userRepo.save(user);
		
		return "register";
	}


}


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
	@PreAuthorize("hasAnyRole('ADMIN')")
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

