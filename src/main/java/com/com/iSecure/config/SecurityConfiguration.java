package com.com.iSecure.config;

import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.com.iSecure.config.connect.UsersRepository;

@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UsersRepository.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	public void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth.userDetailsService(customUserDetailsService)
		.passwordEncoder(getPasswordEncoder());
//		
//			auth.inMemoryAuthentication().withUser("user").password("user").roles("USER")
//		.and().withUser("manager").password("manager").roles("MANAGER")
//		.and().withUser("admin").password("admin").roles("ADMIN");
	}
	
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
//		.anyRequest()
// add custom filter before basic authentication happen and outside config class
		.antMatchers("/admin/**","/manager/**").authenticated().anyRequest().permitAll()
//		.fullyAuthenticated() 
//		.and().addFilterBefore(customFilter(), BasicAuthenticationFilter.class)
		.and().formLogin()
//		.loginPage("/loginpage")
		.permitAll();
//		.httpBasic();
		
		http.logout();

	}
	
	
	
	/**
	 * 
	 * 
	 */
	@Bean
	public Filter customFilter() {
		return new CustomFilter();
	}
	// extracted method for read simplicity
	public PasswordEncoder getPasswordEncoder() {
		return new PasswordEncoder() {
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return true;
			}
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
		
	};
		}
	
}
