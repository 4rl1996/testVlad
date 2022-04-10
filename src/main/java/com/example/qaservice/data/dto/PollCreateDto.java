package com.example.qaservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollCreateDto {

    @NotBlank(message = "Опрос должен иметь название")
    private String name;

    @NotBlank(message = "Укажите дату окончания опроса")
    private String endDate;

    @NotBlank(message = "Добавьте описание опроса")
    private String description;
}
