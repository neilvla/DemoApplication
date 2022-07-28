package com.practice.demo.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//@Repeatable(Conditionals.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConditionalValidator.class})
public @interface Conditional {

    /**
     * Message.
     * @return message.
     */
    String message() default "This field is required.";

    /**
     * Groups.
     * @return Groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     * @return Payload.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Item selected.
     * @return item selected.
     */
    String selected();

    /**
     * Items required.
     * @return items required.
     */
    String[] required();

    /**
     * Values.
     * @return Values.
     */
    String[] values();
}
