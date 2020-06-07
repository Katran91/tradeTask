package com.hackerrank.stocktrade.repository;

import com.hackerrank.stocktrade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
