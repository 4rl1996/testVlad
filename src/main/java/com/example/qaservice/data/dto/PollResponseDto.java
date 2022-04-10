package com.example.qaservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollResponseDto {

    private Long pollId;

    private String name;

    private String endDate;

    private String description;
}
