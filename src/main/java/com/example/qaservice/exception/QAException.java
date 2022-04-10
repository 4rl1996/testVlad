package com.example.qaservice.exception;

import liquibase.pro.packaged.R;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QAException extends RuntimeException {

    private String message;
}
