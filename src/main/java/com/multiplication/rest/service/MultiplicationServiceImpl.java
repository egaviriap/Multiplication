package com.multiplication.rest.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.multiplication.rest.domain.Multiplication;
import com.multiplication.rest.domain.MultiplicationResultAttempt;
import com.multiplication.rest.domain.User;
import com.multiplication.rest.event.EventDispatcher;
import com.multiplication.rest.event.MultiplicationSolvedEvent;
import com.multiplication.rest.repository.MultiplicationResultAttemptRepository;
import com.multiplication.rest.repository.UserRepository;


@Service
public class MultiplicationServiceImpl implements MultiplicationService {

	private RandomGeneratorService randomGeneratorService;
	private MultiplicationResultAttemptRepository attemptRepository;
	private UserRepository userRepository;
	private EventDispatcher eventDispatcher;
    
	
	@Autowired
	public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
			final MultiplicationResultAttemptRepository attemptRepository,
			final UserRepository userRepository) {
		this.randomGeneratorService = randomGeneratorService;
		this.attemptRepository = attemptRepository;
		this.userRepository = userRepository;
		this.eventDispatcher = eventDispatcher;
	}
	
	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		return new Multiplication(factorA, factorB);
	}

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
        // Check if the user already exists for that alias
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        // Avoids 'hack' attempts
        Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!");

        // Check if the attempt is correct
        boolean isCorrect = attempt.getResultAttempt() ==
                        attempt.getMultiplication().getFactorA() *
                        attempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                user.orElse(attempt.getUser()),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                isCorrect
        );

        // Stores the attempt
        attemptRepository.save(checkedAttempt);
        
     // Communicates the result via Event
        eventDispatcher.send(
                new MultiplicationSolvedEvent(checkedAttempt.getId(),
                        checkedAttempt.getUser().getId(),
                        checkedAttempt.isCorrect())
        );

        return isCorrect;
    }
	
	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias){
		return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}
    @Override
    public MultiplicationResultAttempt getResultById(final Long resultId) {
        return attemptRepository.findById(resultId).orElseThrow(() -> new IllegalArgumentException(
                "The requested resultId [" + resultId +
                "] does not exist."));
    }

}
