package com.artur.valid.impl;

import com.artur.service.dto.AccountDto;
import com.artur.valid.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements ConstraintValidator<ValidEmail, Object> {


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // Do nothing
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        AccountDto accountDto = (AccountDto) obj;
        Matcher matcher = pattern.matcher(accountDto.getEmail());
        return matcher.matches();
    }

}
