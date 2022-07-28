package com.practice.demo.controller;

import com.practice.demo.entity.Person;
import com.practice.demo.util.ApiError;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
@RequestMapping("/person-control")
@Slf4j
public class PersonController {

    private final List<Person> personList = new ArrayList<>();

    @PostMapping
    public Single<Person> savePerson(@RequestBody @Valid Person person) {

        return Single.fromCallable(() -> {
            person.setId(UUID.randomUUID().toString());
            personList.add(person);
            return person;
        });
    }

    @PatchMapping("/{id}")
    public Single<Person> updatePerson(@PathVariable String id, @RequestBody @Valid Person person) {

        return findPerson(id)
                .map(p -> {
                    p.setName(person.getName());
                    p.setLastname(person.getLastname());
                    p.setAge(person.getAge());
                    p.setBirthDay(person.getBirthDay());
                    p.setDni(person.getDni());
                    p.setEstadoCivil(person.getEstadoCivil());
                    return p;
                });

    }

    @GetMapping
    public Observable<Person> getPersons() {

        return Observable.fromIterable(personList);
    }

    @GetMapping("/{id}")
    public Single<Person> getPerson(@PathVariable String id) {

        return findPerson(id);
    }

    private Single<Person> findPerson(String id) {
        return Single.fromObservable(Observable.fromIterable(personList)
                .filter(person -> person.getId().equals(id))
                .switchIfEmpty(Observable.error(new NoSuchElementException("Persona no encontrada")))
                .doOnError(ex -> log.error("Ocurrió un error al buscar a la persona: {}", ex.getMessage(), ex)));
    }

    @DeleteMapping("/{id}")
    public Completable deletePerson(@PathVariable String id) {
        personList.removeIf(person -> person.getId().equals(id));
        return Completable.complete();
    }

}
