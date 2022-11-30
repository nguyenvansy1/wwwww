package com.bookshopping.service.impl;

import com.bookshopping.model.Cart;
import com.bookshopping.model.GenderType;
import com.bookshopping.model.Role;
import com.bookshopping.model.User;
import com.bookshopping.payload.request.UserRequest;
import com.bookshopping.repository.UserRepository;
import com.bookshopping.service.CartService;
import com.bookshopping.service.RoleService;
import com.bookshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CartService cartService;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User saveCreateRelationship(User user) {
        Role role = roleService.findByName("ROLE_USER");
        if(role != null) {
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
        }

        User user1 = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(user1);
        cartService.save(cart);
        return user1;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existByEmailOtherUser(Integer id, String email) {
        return userRepository.existByEmailOtherUser(id, email) != null;
    }

    @Override
    public int updateEmail(Integer id, String email) {
        return userRepository.updateEmail(id, email);
    }

    @Override
    public int updateInfo(Integer id, User user) {
        System.out.println("update service");
        if(user.getGender() != null) {
            return userRepository.updateInfo(id, user.getName(), String.valueOf(user.getGender()), user.getBirthday(), user.getAddress(), user.getPhone());
        } else {
            return userRepository.updateInfoNotGender(id, user.getName(), user.getBirthday(), user.getAddress(), user.getPhone());
        }
    }

    @Override
    public void updatePassword(Integer id, String newPassword) {
        userRepository.updatePassword(id, newPassword);
    }
}
