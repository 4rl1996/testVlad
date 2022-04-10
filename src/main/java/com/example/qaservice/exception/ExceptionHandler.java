package com.example.qaservice.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(BindingResult result) {
        return ResponseEntity.status(400).body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> validation(ConstraintViolationException ex) {
        List<String> violationMessageList = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            violationMessageList.add(violation.getMessage());
        }
        return ResponseEntity.badRequest().body(violationMessageList);
    }

//    @org.springframework.web.bind.annotation.ExceptionHandler(value = HttpMessageNotReadableException.class)
//    public ResponseEntity<?> validation() {
//        return ResponseEntity.badRequest().body("Добавьте тело запроса!");
//    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> validation(Exception exception) {
        return ResponseEntity.status(500).body(exception.getMessage());
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = QAException.class)
    public final ResponseEntity<?> customValidation(QAException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
