package com.bookshopping.service.impl;

import com.bookshopping.model.Role;
import com.bookshopping.repository.RoleRepository;
import com.bookshopping.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
