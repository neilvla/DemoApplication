package com.practice.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {

    private String selected;
    private String[] required;
    private String message;
    private String[] values;

    @Override
    public void initialize(Conditional requiredIfChecked) {
        selected = requiredIfChecked.selected();
        required = requiredIfChecked.required();
        message = requiredIfChecked.message();
        values = requiredIfChecked.values();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        log.info("Object validator :  {} - selected : {}", object, selected);
        Boolean valid;
        List<Boolean> validations;
        try {
            validations = new ArrayList<>();
            String checkedValue = BeanUtils.getProperty(object, selected);
            log.info("checkedValue :  {} - values : {}", checkedValue, values);
            //if (Arrays.asList(values).contains(checkedValue)) {
            //if (Arrays.asList(values).contains(checkedValue)) {
            if (Arrays.stream(values).anyMatch(x -> Integer.parseInt(x) <= Integer.parseInt(checkedValue))) {
                for (String propName : required) {
                    String requiredValue = BeanUtils.getProperty(object, propName);
                    log.info("requiredValue: {}", requiredValue);
                    valid = requiredValue != null && !StringUtils.isEmpty(requiredValue.trim());
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message).addPropertyNode(propName).addConstraintViolation();
                    }
                    validations.add(valid);
                }
            }
        } catch (IllegalAccessException | NestedNullException e) {
            log.error("El método de acceso no está disponible para la clase : {}, excepción : {}",
                    object.getClass().getName(), e);
            return false;
        } catch (NoSuchMethodException e) {
            log.error("El campo o método no está presente en la clase : {}, excepción : {}", object.getClass().getName(), e);
            return false;
        } catch (InvocationTargetException e) {
            log.error("Se produjo una excepción al acceder a la clase : {}, excepción : {}", object.getClass().getName(), e);
            return false;
        }
        return !validations.contains(Boolean.FALSE);
    }
}
