package com.multiplication.rest.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.multiplication.rest.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
Optional<User> findByAlias(final String Alias);

}
