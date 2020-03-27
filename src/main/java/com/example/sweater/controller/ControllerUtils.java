package com.example.sweater.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                // Тут указывается ключ и значение через запятую
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );

        // Таким образом мы получаем лист с ошибками и с помощью stream преобразуем его в Map с ошибками
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
