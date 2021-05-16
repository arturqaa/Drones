package com.artur.valid.impl;

import com.artur.service.dto.UserOrderDto;
import com.artur.valid.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, Object> {
    private Pattern pattern;
    private Matcher matcher;
    private static final String PHONE_NUMBER_PATTERN = "^\\D*(?:\\d\\D*){12,}$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        UserOrderDto userOrderDto = (UserOrderDto) value;
        matcher = pattern.matcher(userOrderDto.getPhoneNumber());
        return matcher.matches();
    }
}
