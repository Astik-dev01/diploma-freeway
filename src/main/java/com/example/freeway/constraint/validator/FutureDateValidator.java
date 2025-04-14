package com.example.freeway.constraint.validator;

import com.example.freeway.constraint.FutureDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

public class FutureDateValidator implements ConstraintValidator<FutureDate, Date> {

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Если дата null, пропускаем проверку (валидация других аннотаций выполнится)
        }

        // Получаем текущую дату
        Calendar currentDate = Calendar.getInstance();
        // Добавляем 1 день к текущей дате
        currentDate.add(Calendar.DAY_OF_YEAR, 1);

        // Сравниваем дату, чтобы она была не раньше чем currentDate
        return value.after(currentDate.getTime());
    }
}
