package com.example.qaservice.service;

import com.example.qaservice.data.dto.QuestionResponseDto;
import com.example.qaservice.data.entity.Poll;
import com.example.qaservice.exception.QAException;

import java.util.List;
import java.util.Map;

public interface PollService {

    Poll save(Poll poll);

    Poll update(Poll poll, Long id);

    void delete(Long id);

    List<Poll> findActivePolls();

    Map<Long, List<QuestionResponseDto>> findPollWithQuestions(Long id);

    void isActive(Long pollId);

    void isExist(Long pollId);
}
