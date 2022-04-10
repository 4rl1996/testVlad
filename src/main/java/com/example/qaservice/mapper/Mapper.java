package com.example.qaservice.mapper;

import com.example.qaservice.data.dto.AdminRoleDto;
import com.example.qaservice.data.dto.AnswerDto;
import com.example.qaservice.data.dto.AnswerResponseDto;
import com.example.qaservice.data.dto.PollCreateDto;
import com.example.qaservice.data.dto.PollResponseDto;
import com.example.qaservice.data.dto.PollUpdateDto;
import com.example.qaservice.data.dto.QuestionCreateDto;
import com.example.qaservice.data.dto.QuestionResponseDto;
import com.example.qaservice.data.dto.QuestionUpdateDto;
import com.example.qaservice.data.entity.AdminRole;
import com.example.qaservice.data.entity.Answer;
import com.example.qaservice.data.entity.Poll;
import com.example.qaservice.data.entity.Question;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    AdminRole dtoToEntity(AdminRoleDto dto);

    @Mapping(target = "endDate", qualifiedByName = "time")
    Poll dtoToEntity(PollCreateDto dto);

    @Mapping(target = "endDate", qualifiedByName = "time")
    Poll dtoToEntity(PollUpdateDto dto);

    Question dtoToEntity(QuestionCreateDto dto);

    Question dtoToEntity(QuestionUpdateDto dto);

    @Mapping(target = "pollId", source = "id")
    PollResponseDto entityToDto(Poll poll);

    @Mapping(target = "type", expression = "java(question.getType().getDescription())")
    @Mapping(target = "questionId", source = "id")
    QuestionResponseDto entityToDto(Question question);

    List<Question> dtoToQuestionEntityList(List<QuestionCreateDto> questions);

    List<Answer> dtoToAnswerEntityList(List<AnswerDto> answers);

    List<AnswerResponseDto> entityToAnswerResponseList(List<Answer> answers);

    List<QuestionResponseDto> entityToQuestionResponseList(List<Question> questions);

    List<PollResponseDto> entityToPoolResponseList(List<Poll> polls);

    @Named("time")
    default Date timeUtil(String date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                return df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Mapping(target = "type", expression = "java(answer.getType().getDescription())")
    AnswerResponseDto map(Answer answer);
}
