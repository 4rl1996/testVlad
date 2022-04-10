package com.example.qaservice.service;

import com.example.qaservice.data.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> save(List<Question> questions, Long pollId);

    Question update(Question question, Long questionId);

    void delete(List<Long> questionIdList);

    List<Question> findAllByPollId(Long id);

    void isExist(Long questionId, Long pollId);
}
