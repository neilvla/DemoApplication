package com.practice.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.demo.util.Conditional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Conditional(selected = "age", values = {"18"}, required = {"dni", "estadoCivil"})
@ToString
public class Person implements Serializable {

    private String id;

    private String name;

    private String lastname;

    @NotNull(message = "El elemento es requerido")
    private Integer age;

    private String dni;

    private String estadoCivil;

    private String birthDay;
}
