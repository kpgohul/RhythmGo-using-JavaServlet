package com.gohul.rhythmgo.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ValidatorUtil {

    public static Validator getvalidator(){
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
