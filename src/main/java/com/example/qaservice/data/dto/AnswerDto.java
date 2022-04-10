package com.example.qaservice.data.dto;

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
public class AnswerDto {

    @NotNull(message = "Укажите id вопроса!")
    private Long questionId;

    @NotBlank(message = "Дайте ответ на вопрос!")
    private String answer;
}
