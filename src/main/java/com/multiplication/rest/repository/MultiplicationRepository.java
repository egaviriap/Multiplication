package com.multiplication.rest.repository;

import org.springframework.data.repository.CrudRepository;

import com.multiplication.rest.domain.Multiplication;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long>{

}
