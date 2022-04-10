package com.example.qaservice.service;

import com.example.qaservice.data.dto.AnswerResponseDto;
import com.example.qaservice.data.entity.Answer;

import java.util.List;
import java.util.Map;

public interface AnswerService {

    void save(Long userId, Long pollId, List<Answer> answers);

    Map<Long, List<AnswerResponseDto>> getAnswers(Long userId);

}
