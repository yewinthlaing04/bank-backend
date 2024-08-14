package com.ye.bank.repository;

import com.ye.bank.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity , Long> {

    boolean existsByEmail(String email);
}
