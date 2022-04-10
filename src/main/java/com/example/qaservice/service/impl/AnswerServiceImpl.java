package com.example.qaservice.service.impl;

import com.example.qaservice.data.dto.AnswerResponseDto;
import com.example.qaservice.data.entity.Answer;
import com.example.qaservice.exception.QAException;
import com.example.qaservice.mapper.Mapper;
import com.example.qaservice.repository.AnswerRepository;
import com.example.qaservice.service.AnswerService;
import com.example.qaservice.service.PollService;
import com.example.qaservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository repository;
    private final PollService pollService;
    private final QuestionService questionService;
    private final Mapper mapper;

    @Override
    @Transactional
    public void save(Long userId, Long pollId, List<Answer> answers) {

        pollService.isActive(pollId);

        answers.forEach(answer -> {
            questionService.isExist(answer.getQuestionId(), pollId);
            answer.setUserId(userId);
            answer.setPollId(pollId);
        });

        repository.saveAll(answers);

    }

    @Override
    public Map<Long, List<AnswerResponseDto>> getAnswers(Long userId) {

        Map<Long, List<AnswerResponseDto>> answersForPoll = new HashMap<>();

        List<Answer> allAnswersByUserId = repository.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(allAnswersByUserId)) {
            throw new QAException("Не найдено ни одного ответа для пользователя " + userId);
        }

        Set<Long> pollIds = allAnswersByUserId.stream().map(Answer::getPollId).collect(Collectors.toSet());
        for (Long pollId : pollIds) {
            answersForPoll.put(pollId, mapper.entityToAnswerResponseList(allAnswersByUserId.stream()
                .filter(answer -> Objects.equals(answer.getPollId(), pollId))
                .collect(Collectors.toList())));
        }

        return answersForPoll;
    }
}
