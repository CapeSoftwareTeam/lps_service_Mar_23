package com.capeelectric.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capeelectric.model.FrequentlyAskedQuestions;

@Repository
public interface FrequentlyAskedQuestionRepository extends CrudRepository<FrequentlyAskedQuestions, Integer> {
	public Optional<FrequentlyAskedQuestions> findByFaqQuestion(String question);

	@Query(value="SELECT * FROM lv_safety_verification.frequently_Asked_Questions where STATUS = 'pending'",nativeQuery = true)
	public List<FrequentlyAskedQuestions> findByAllAndStatus(String status);

}
