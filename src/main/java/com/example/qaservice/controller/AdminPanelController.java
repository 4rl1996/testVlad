package com.example.qaservice.controller;

import com.example.qaservice.data.dto.AdminRoleDto;
import com.example.qaservice.data.dto.PollCreateDto;
import com.example.qaservice.data.dto.PollUpdateDto;
import com.example.qaservice.data.dto.QuestionCreateDto;
import com.example.qaservice.data.dto.QuestionUpdateDto;
import com.example.qaservice.data.entity.AdminRole;
import com.example.qaservice.data.entity.Poll;
import com.example.qaservice.data.entity.Question;
import com.example.qaservice.mapper.Mapper;
import com.example.qaservice.service.AdminService;
import com.example.qaservice.service.PollService;
import com.example.qaservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.qaservice.config.WebConst.SESSION_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminPanelController {

    private final AdminService adminService;
    private final PollService pollService;
    private final QuestionService questionService;
    private final Mapper mapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AdminRoleDto admin, HttpSession session) {
        final AdminRole role = mapper.dtoToEntity(admin);
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute != null) {
            log.info("Попытка повторной авторизации");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Вы уже авторизованы!");
        }
        if (adminService.isAdmin(role)) {
            session.setAttribute(SESSION_KEY, admin);
            log.info("Пользователь авторизовался в системе");
            return ResponseEntity.status(HttpStatus.OK).body("Добро пожаловать!");
        }
        log.debug("Пользователь ввел неверные данные");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин/пароль!");
    }

    @PostMapping(value = "/polls")
    public ResponseEntity<?> createPoll(@RequestBody @Valid PollCreateDto dto, HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Попытка создания опроса без авторизации");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Авторизуйтесь в системе!");
        }
        Poll poll = mapper.dtoToEntity(dto);
        log.info("Создается опрос со следующим названием: {}", dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(pollService.save(poll)));
    }

    @PatchMapping(value = "/polls/{id}")
    public ResponseEntity<?> updatePoll(@RequestBody PollUpdateDto dto, @PathVariable(value = "id") Long id, HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Попытка изменения опроса c ID = {} без авторизации", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Авторизуйтесь в системе!");
        }
        Poll poll = mapper.dtoToEntity(dto);
        log.info("Изменяется опрос с ID = {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.entityToDto(pollService.update(poll, id)));
    }

    @DeleteMapping(value = "/polls/{id}")
    public ResponseEntity<?> deletePoll(@PathVariable(value = "id") Long pollId, HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Попытка удаления опроса c ID = {} без авторизации", pollId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Авторизуйтесь в системе!");
        }
        isExistPoll(pollId);
        pollService.delete(pollId);
        log.info("Опрос был удален. ID = {}", pollId);
        return ResponseEntity.status(HttpStatus.OK).body("Опрос с ID = " + pollId + " был удален");
    }

    @PostMapping(value = "/questions")
    public ResponseEntity<?> createQuestions(@RequestBody List<@Valid QuestionCreateDto> dtoList, @RequestParam Long pollId,
                                             HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Попытка создания вопросов в опросе с ID = {} без авторизации", pollId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Авторизуйтесь в системе!");
        }
        isExistPoll(pollId);
        List<Question> questions = mapper.dtoToQuestionEntityList(dtoList);
        log.info("Сохраняем вопросы для опроса с ID = {}", pollId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToQuestionResponseList(questionService.save(questions, pollId)));
    }

    @PatchMapping(value = "/questions/{id}")
    public ResponseEntity<?> updateQuestion(@RequestBody @NotNull QuestionUpdateDto dto, @PathVariable(value = "id") Long questionId,
                                            HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Попытка обновления вопроса с ID = {} без авторизации", questionId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Авторизуйтесь в системе!");
        }
        Question question = mapper.dtoToEntity(dto);
        log.info("Обновляем вопрос {}", questionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(questionService.update(question, questionId)));
    }

    @DeleteMapping(value = "/questions/{ids}")
    public ResponseEntity<?> deleteQuestion(@PathVariable(value = "ids") List<Long> questionIdList,
                                            HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Попытка удалить вопросы с ID = {} без авторизации", questionIdList);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Авторизуйтесь в системе!");
        }
        questionService.delete(questionIdList);
        log.info("Были удалены следующие вопросы {}", questionIdList);
        return ResponseEntity.status(HttpStatus.OK).body("Удаленные вопросы: " + questionIdList.toString());
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        Object attribute = httpSession.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("Неавторизованный пользователь пытался выйти из системы");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Вы не были авторизованы!");
        }
        httpSession.invalidate();
        log.info("Пользователь вышел из системы");
        return ResponseEntity.status(HttpStatus.OK).body("До встречи!");
    }

    private void isExistPoll(Long poolId) {
        pollService.isExist(poolId);
    }
}
