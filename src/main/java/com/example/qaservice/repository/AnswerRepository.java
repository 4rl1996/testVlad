package com.example.qaservice.repository;

import com.example.qaservice.data.entity.Answer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    @Query(value = "select answer.*, question.type, question.content from answer" +
        " left join question on answer.question_id = question.id" +
        " where answer.user_id = :id order by question.id asc", nativeQuery = true)
    List<Answer> findAllByUserId(Long id);
}
