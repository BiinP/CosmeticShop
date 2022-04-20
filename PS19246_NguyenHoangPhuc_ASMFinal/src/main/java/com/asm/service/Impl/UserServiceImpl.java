package com.asm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.asm.bean.Account;
import com.asm.bean.MyUser;
import com.asm.service.AccountService;
@Service
public class UserServiceImpl implements UserDetailsService{
	@Autowired AccountService aService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		try {
			Account account = aService.findByUsername(username);
			String password = account.getPassword();
			String[] roles = account.getRoleDetails().stream().
					map(au -> au.getRole().getRole())
					.collect(Collectors.toList()).toArray(new String[0]);
			return User.withUsername(username).password(pe.encode(password)).roles(roles).build();
		} catch (Exception e) {
			throw new UsernameNotFoundException(username + " not found");
		}
	}
	
	
}
