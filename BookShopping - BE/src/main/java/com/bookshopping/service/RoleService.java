package com.bookshopping.service;

import com.bookshopping.model.Role;

public interface RoleService {
    Role findByName(String name);
}
