package com.bookshopping.repository;

import com.bookshopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query(value = "select * from user where email = :email and id != :id", nativeQuery = true)
    User existByEmailOtherUser(Integer id, String email);
    @Query(value = "update user set email = :email where id = :id", nativeQuery = true)
    @Transactional
    @Modifying
    int updateEmail(Integer id, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET password = :newPassword WHERE id = :id", nativeQuery = true)
    void updatePassword(Integer id, String newPassword);

    @Query(value = "update user set name = :name, gender = :gender, birthday = :birthday, " +
            "address = :address, phone = :phone where id = :id", nativeQuery = true)
    @Transactional
    @Modifying
    int updateInfo(Integer id, String name, String gender, LocalDate birthday, String address, String phone);

    @Query(value = "update user set name = :name, birthday = :birthday, " +
            "address = :address, phone = :phone where id = :id", nativeQuery = true)
    @Transactional
    @Modifying
    int updateInfoNotGender(Integer id, String name, LocalDate birthday, String address, String phone);


    @Query(value = "select * from user where status = 1", nativeQuery = true)
    List<User> userList();
}
