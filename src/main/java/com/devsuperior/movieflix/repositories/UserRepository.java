package com.devsuperior.movieflix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String username);

	Page<User> findByName(User user, Pageable pageable);

}
