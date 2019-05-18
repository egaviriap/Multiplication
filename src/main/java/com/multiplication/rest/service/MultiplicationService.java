package com.multiplication.rest.service;

import java.util.List;

import com.multiplication.rest.domain.Multiplication;
import com.multiplication.rest.domain.MultiplicationResultAttempt;

public interface MultiplicationService {

	/**
     * Creates a Multiplication object with two randomly-generated factors
     * between 11 and 99.
     *
     * @return a Multiplication object with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the
     *         multiplication, false otherwise.
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
    
    List<MultiplicationResultAttempt> getStatsForUser(final String userAlias);
    
    /**
     * Gets an attempt by its id
     *
     * @param resultId the identifier of the attempt
     * @return the {@link MultiplicationResultAttempt} object matching the id, otherwise null.
     */
    MultiplicationResultAttempt getResultById(final Long resultId);

}
