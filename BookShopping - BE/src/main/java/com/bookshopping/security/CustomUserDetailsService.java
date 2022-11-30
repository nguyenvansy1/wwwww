package com.bookshopping.security;

import com.bookshopping.model.User;
import com.bookshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Email user not found: " + username);
        }
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        User user = userService.findById(id);
        if(user == null) {
            throw new UsernameNotFoundException("Id user not found: " + id);
        }

        return UserPrincipal.create(user);
    }
}
