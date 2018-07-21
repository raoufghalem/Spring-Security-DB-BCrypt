package com.iSecure.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.iSecure.config.SecureEntity.Users;

public class CustomUserDetails extends Users implements UserDetails {

	public CustomUserDetails (final Users user) {
		super(user);
	}

	private static final long serialVersionUID = 1L;
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// return roles from Users class and out each role into SimpleGrantedAuthority
		return	getRoles().stream()
		.map(r -> new SimpleGrantedAuthority("ROLE_"+r.getRole()))
		.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return super.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		if(super.getActive() == 1) 
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "CustomUserDetails [getAuthorities()=" + getAuthorities() + ", getPassword()=" + getPassword()
				+ ", getUsername()=" + getUsername() + ", isAccountNonExpired()=" + isAccountNonExpired()
				+ ", isAccountNonLocked()=" + isAccountNonLocked() + ", isCredentialsNonExpired()="
				+ isCredentialsNonExpired() + ", isEnabled()=" + isEnabled() + "]";
	}

	
	


}
