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
            log.info("?????????????? ?????????????????? ??????????????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("???? ?????? ????????????????????????!");
        }
        if (adminService.isAdmin(role)) {
            session.setAttribute(SESSION_KEY, admin);
            log.info("???????????????????????? ?????????????????????????? ?? ??????????????");
            return ResponseEntity.status(HttpStatus.OK).body("?????????? ????????????????????!");
        }
        log.debug("???????????????????????? ???????? ???????????????? ????????????");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("???????????????? ??????????/????????????!");
    }

    @PostMapping(value = "/polls")
    public ResponseEntity<?> createPoll(@RequestBody @Valid PollCreateDto dto, HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("?????????????? ???????????????? ???????????? ?????? ??????????????????????");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("?????????????????????????? ?? ??????????????!");
        }
        Poll poll = mapper.dtoToEntity(dto);
        log.info("?????????????????? ?????????? ???? ?????????????????? ??????????????????: {}", dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(pollService.save(poll)));
    }

    @PatchMapping(value = "/polls/{id}")
    public ResponseEntity<?> updatePoll(@RequestBody PollUpdateDto dto, @PathVariable(value = "id") Long id, HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("?????????????? ?????????????????? ???????????? c ID = {} ?????? ??????????????????????", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("?????????????????????????? ?? ??????????????!");
        }
        Poll poll = mapper.dtoToEntity(dto);
        log.info("???????????????????? ?????????? ?? ID = {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.entityToDto(pollService.update(poll, id)));
    }

    @DeleteMapping(value = "/polls/{id}")
    public ResponseEntity<?> deletePoll(@PathVariable(value = "id") Long pollId, HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("?????????????? ???????????????? ???????????? c ID = {} ?????? ??????????????????????", pollId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("?????????????????????????? ?? ??????????????!");
        }
        isExistPoll(pollId);
        pollService.delete(pollId);
        log.info("?????????? ?????? ????????????. ID = {}", pollId);
        return ResponseEntity.status(HttpStatus.OK).body("?????????? ?? ID = " + pollId + " ?????? ????????????");
    }

    @PostMapping(value = "/questions")
    public ResponseEntity<?> createQuestions(@RequestBody List<@Valid QuestionCreateDto> dtoList, @RequestParam Long pollId,
                                             HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("?????????????? ???????????????? ???????????????? ?? ???????????? ?? ID = {} ?????? ??????????????????????", pollId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("?????????????????????????? ?? ??????????????!");
        }
        isExistPoll(pollId);
        List<Question> questions = mapper.dtoToQuestionEntityList(dtoList);
        log.info("?????????????????? ?????????????? ?????? ???????????? ?? ID = {}", pollId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToQuestionResponseList(questionService.save(questions, pollId)));
    }

    @PatchMapping(value = "/questions/{id}")
    public ResponseEntity<?> updateQuestion(@RequestBody @NotNull QuestionUpdateDto dto, @PathVariable(value = "id") Long questionId,
                                            HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("?????????????? ???????????????????? ?????????????? ?? ID = {} ?????? ??????????????????????", questionId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("?????????????????????????? ?? ??????????????!");
        }
        Question question = mapper.dtoToEntity(dto);
        log.info("?????????????????? ???????????? {}", questionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(questionService.update(question, questionId)));
    }

    @DeleteMapping(value = "/questions/{ids}")
    public ResponseEntity<?> deleteQuestion(@PathVariable(value = "ids") List<Long> questionIdList,
                                            HttpSession session) {
        Object attribute = session.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("?????????????? ?????????????? ?????????????? ?? ID = {} ?????? ??????????????????????", questionIdList);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("?????????????????????????? ?? ??????????????!");
        }
        questionService.delete(questionIdList);
        log.info("???????? ?????????????? ?????????????????? ?????????????? {}", questionIdList);
        return ResponseEntity.status(HttpStatus.OK).body("?????????????????? ??????????????: " + questionIdList.toString());
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        Object attribute = httpSession.getAttribute(SESSION_KEY);
        if (attribute == null) {
            log.debug("???????????????????????????????? ???????????????????????? ?????????????? ?????????? ???? ??????????????");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("???? ???? ???????? ????????????????????????!");
        }
        httpSession.invalidate();
        log.info("???????????????????????? ?????????? ???? ??????????????");
        return ResponseEntity.status(HttpStatus.OK).body("???? ??????????????!");
    }

    private void isExistPoll(Long poolId) {
        pollService.isExist(poolId);
    }
}
