package com.example.qaservice.data.dto;

import com.example.qaservice.data.type.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateDto {

    private String content;

    private QuestionType type;
}
