package com.example.qaservice.service.impl;

import com.example.qaservice.data.entity.Question;
import com.example.qaservice.exception.QAException;
import com.example.qaservice.repository.QuestionRepository;
import com.example.qaservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository repository;

    @Override
    @Transactional
    public List<Question> save(List<Question> questions, Long pollId) {

        questions.forEach(question -> question.setPollId(pollId));

        List<Question> savedQuestions = new ArrayList<>();

        repository.saveAll(questions).forEach(savedQuestions::add);

        return savedQuestions;
    }

    @Override
    @Transactional
    public Question update(Question question, Long questionId) {
        if (question != null) {
            Optional<Question> questionFromDB = repository.findById(questionId);
            questionFromDB.ifPresent(questionForUpdate -> {
                questionForUpdate.setContent(question.getContent() != null ? question.getContent() : questionForUpdate.getContent());
                questionForUpdate.setType(question.getType() != null ? question.getType() : questionForUpdate.getType());
            });
            Question updatedQuestion = questionFromDB.orElseThrow(() -> new QAException("Вопрос с ID =" + questionId + " не найден!"));
            return repository.save(Objects.requireNonNull(updatedQuestion));
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(List<Long> questionIdList) {
        repository.deleteAllById(questionIdList);
    }

    @Override
    public List<Question> findAllByPollId(Long id) {
        return repository.findAllByPollIdOrderById(id);
    }

    @Override
    public void isExist(Long questionId, Long pollId) {
        if (!(repository.existsByIdAndPollId(questionId, pollId))) {
            throw new QAException("Вопрос с ID=" + questionId + " не принадлежит опросу с ID=" + pollId);
        }
    }
}
