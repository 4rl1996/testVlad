package com.example.qaservice.data.type;


public enum QuestionType {

    TEXT("Вопрос с развернутым ответом"),
    SINGLE("Вопрос с одним вариантом ответа"),
    MULTIPLE("Вопрос с несколькими вариантами ответа");
    private String description;

    QuestionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
