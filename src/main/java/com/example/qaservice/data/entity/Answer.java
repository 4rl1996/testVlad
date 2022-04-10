package com.example.qaservice.data.entity;

import com.example.qaservice.data.type.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long pollId;

    private Long questionId;

    private String answer;

//    @Transient
@Column(insertable = false, updatable = false)
@Enumerated(EnumType.STRING)
    private QuestionType type;

    @JsonIgnore
//    @Transient
    @Column(insertable = false, updatable = false)
    private String content;
}
