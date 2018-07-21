package com.iSecure.config;

import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iSecure.config.SecureEntity.UsersRepository;

@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UsersRepository.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		// With Plain Text password
//		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}
	
	public void configure(AuthenticationManagerBuilder auth)throws Exception{
		/**
		 * Using UserDetailService + PasswordEncoder
		 */
//		auth.userDetailsService(customUserDetailsService)
//		.passwordEncoder(getPasswordEncoder());
		/**
		 * Using authentication Provider
		 */
		auth.authenticationProvider(authenticationProvider());

		/**
		 * InMemory login  *Only use for dev*  never to be used in Prod
		 */
//			auth.inMemoryAuthentication().withUser("user").password("user").roles("USER")
//		.and().withUser("manager").password("manager").roles("MANAGER")
//		.and().withUser("admin").password("admin").roles("ADMIN");
	}
	
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
		.antMatchers("/admin/**","/manager/**").authenticated().anyRequest().permitAll()
		.and().formLogin()
		.permitAll();
		http.logout().clearAuthentication(true);

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
