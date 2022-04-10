package com.example.qaservice.repository;

import com.example.qaservice.data.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

    List<Question> findAllByPollIdOrderById(Long id);

    boolean existsByIdAndPollId(Long id, Long pollId);
}
