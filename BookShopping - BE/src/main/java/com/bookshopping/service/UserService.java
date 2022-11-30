package com.bookshopping.service;

import com.bookshopping.model.User;
import com.bookshopping.payload.request.UserRequest;

public interface UserService {
    User save(User user);
    User saveCreateRelationship(User user);
    User findByEmail(String email);
    User findById(Integer id);
    Boolean existsByEmail(String email);
    boolean existByEmailOtherUser(Integer id, String email);
    int updateEmail(Integer id, String email);
    int updateInfo(Integer id, User user);
    void updatePassword(Integer id, String newPassword);
}
