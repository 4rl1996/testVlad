package com.example.qaservice.controller;

import com.example.qaservice.data.dto.AnswerDto;
import com.example.qaservice.data.dto.AnswerResponseDto;
import com.example.qaservice.data.dto.PollResponseDto;
import com.example.qaservice.data.dto.QuestionResponseDto;
import com.example.qaservice.data.entity.Answer;
import com.example.qaservice.mapper.Mapper;
import com.example.qaservice.service.AnswerService;
import com.example.qaservice.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user")
public class UserPanelController {

    private final PollService pollService;
    private final AnswerService answerService;
    private final Mapper mapper;

    @GetMapping("/polls")
    ResponseEntity<List<PollResponseDto>> getActivePolls() {
        return ResponseEntity.ok(mapper.entityToPoolResponseList(pollService.findActivePolls()));
    }

    @GetMapping("/polls/{id}")
    ResponseEntity<Map<Long, List<QuestionResponseDto>>> getPollWithQuestions(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(pollService.findPollWithQuestions(id));
    }

    @PostMapping("/polls")
    ResponseEntity<?> takePoll(@RequestParam Long userId, @RequestParam Long pollId, @RequestBody List<@Valid AnswerDto> answerDtoList) {

        List<Answer> answers = mapper.dtoToAnswerEntityList(answerDtoList);

        answerService.save(userId, pollId, answers);

        return ResponseEntity.ok("Ваши ответы сохранены!");
    }

    @GetMapping("/answers")
    ResponseEntity<Map<Long, List<AnswerResponseDto>>> getAnswers(@RequestParam Long userId) {

        return ResponseEntity.ok(answerService.getAnswers(userId));
    }
}
