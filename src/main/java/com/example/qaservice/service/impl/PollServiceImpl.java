package com.example.qaservice.service.impl;

import com.example.qaservice.data.dto.QuestionResponseDto;
import com.example.qaservice.data.entity.Poll;
import com.example.qaservice.data.entity.Question;
import com.example.qaservice.exception.QAException;
import com.example.qaservice.mapper.Mapper;
import com.example.qaservice.repository.PollRepository;
import com.example.qaservice.service.PollService;
import com.example.qaservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final PollRepository repository;
    private final QuestionService questionService;
    private final Mapper mapper;


    @Override
    @Transactional
    public Poll save(Poll poll) {
        poll.setStartDate(new Date());
        poll.setEndDate(poll.getEndDate());
        return repository.save(poll);
    }

    @Override
    @Transactional
    public Poll update(Poll poll, Long id) {
        if (poll != null) {
            Optional<Poll> pollFromDB = repository.findById(id);
            pollFromDB.ifPresent(pollForUpdate -> {
                pollForUpdate.setName(poll.getName() != null ? poll.getName() : pollForUpdate.getName());
                pollForUpdate.setDescription(poll.getDescription() != null ? poll.getDescription() : pollForUpdate.getDescription());
                pollForUpdate.setEndDate(poll.getEndDate() != null ? poll.getEndDate() : pollForUpdate.getEndDate());
            });
            Poll updatedPoll = pollFromDB.orElseThrow(() -> new QAException("Опрос с ID =" + id + " не был найден!"));
            return repository.save(Objects.requireNonNull(updatedPoll));
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deletePoll(id);
    }

    @Override
    public List<Poll> findActivePolls() {
        Date date = new Date();
        List<Poll> polls = repository.findAllByEndDateIsAfter(date);
        if (polls.isEmpty()) {
            throw new QAException("Активных опросов не найдено!");
        }
        return polls;
    }

    @Override
    public Map<Long, List<QuestionResponseDto>> findPollWithQuestions(Long id) {

        Map<Long, List<QuestionResponseDto>> pollWithQuestions = new HashMap<>();

        isActive(id);

        List<Question> questionsFromDB = questionService.findAllByPollId(id);

        List<QuestionResponseDto> questionResponseDtos = mapper.entityToQuestionResponseList(questionsFromDB);

        pollWithQuestions.put(id, questionResponseDtos);

        return pollWithQuestions;
    }

    public void isActive(Long pollId) {
        Date currentDate = new Date();
        if (!(repository.findByEndDateIsAfterAndId(currentDate, pollId).isPresent())) {
            throw new QAException("Опрос с ID = " + pollId + " неактивен либо не существует!");
        }
    }

    @Override
    public void isExist(Long pollId) {
        if(!(repository.existsById(pollId))) {
            throw new QAException("Опрос с ID = " + pollId + " не существует!");
        }
    }
}
