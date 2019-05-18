package com.multiplication.rest.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.multiplication.rest.domain.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {
	
List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);

}
