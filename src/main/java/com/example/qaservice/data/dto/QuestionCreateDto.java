package com.example.qaservice.data.dto;

import com.example.qaservice.data.type.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDto {

    @NotBlank(message = "Добавьте вопрос")
    private String content;

    @NotNull(message = "Укажите тип вопроса")
    private QuestionType type;
}
