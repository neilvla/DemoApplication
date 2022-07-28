package com.practice.demo;

import com.practice.demo.entity.Person;
import com.practice.demo.entity.PersonResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.observables.GroupedObservable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("HOLAAA");

        compose();
        //String[] letras = new String[]{"hola", "buenas", "noches"};

        //letras = ArrayUtils.remove(letras, 1);

        //Arrays.stream(letras).forEach(x -> System.out.println("VALUE: " + x));



        /*probandoFecha();

        System.out.println("****************************");

        LocalDateTime date = LocalDateTime.parse("2021-05-17T11:04:32.776-0500",
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));*/
        //probando().toList().subscribe(x -> log.info("VER: {}", x));
//        Map<String, String> map = new HashMap<>();
//        map.put("value", "value21");
//        map.put("value2", "value22");

        //System.out.println(map.values().toString());

    }

    private static Observable<Map<String, Collection<Person>>> probando() {
        List<Person> persons = new ArrayList<>();
        Person p = new Person();
        p.setName("Neil");
        p.setLastname("Llique");
        p.setDni("44562877");
        p.setBirthDay("1987-10-06");
        persons.add(p);
        p = new Person();
        p.setName("Nathali");
        p.setLastname("Llique");
        p.setDni("44562933");
        p.setBirthDay("1987-10-06");
        persons.add(p);
        p = new Person();
        p.setName("Maritza");
        p.setLastname("Mesía");
        p.setDni("33446332");
        p.setBirthDay("1964-07-18");
        persons.add(p);
        p = new Person();
        p.setName("Adan");
        p.setLastname("Origin");
        p.setDni("00000000");
        p.setBirthDay("2021-05-17T11:04:32");
        persons.add(p);
        p = new Person();
        p.setName("Eva");
        p.setLastname("Origin");
        p.setDni("00000000");
        p.setBirthDay("2021-05-07T08:04:32");
        persons.add(p);

        return Observable.fromIterable(persons)
                .compose(personGroupsByDate());
    }

    private static ObservableTransformer<Person, Map<String, Collection<Person>>>
    personGroupsByDate() {
        return upstream ->
                upstream.groupBy(person -> selectDate(person.getBirthDay()))
                        .compose(groupedObservableMaps());
    }

    private static ObservableTransformer<GroupedObservable<String, Person>,
            Map<String, Collection<Person>>> groupedObservableMaps() {
        return upstream -> upstream
                .flatMapSingle(
                        per -> per.toMultimap(d -> selectDate(d.getBirthDay()))
                );
    }

    private static String selectDate(String processingDate) {
        return processingDate.equals(LocalDateTime.now().toString())
                ? processingDate
                : processingDate.substring(0, 7);
    }

    private static void probandoFecha() {

        SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date d1 = sd1.parse("2021-05-17T11:04:32.776-0500");
            System.out.println("Fecha: " + d1);
            String str = sd2.format(d1);
            System.out.println("Fecha Final: " + str);
        } catch (ParseException exception) {

        }
    }

    private Completable method1() {
        return Completable.error(new CustomException("Probando excepción"))
                .doAfterTerminate(() -> log.info("METODO 1"));
    }

    private Completable method2() {
        log.info("VER LOG");
        return Completable.defer(() -> {
            log.info("METODO 2");
            return Completable.complete();
        });
    }

    class CustomException extends Exception {
        public CustomException(String mensaje) {
            super(mensaje);
        }
    }

//*****************************************************************/
// Probando Compose
//*****************************************************************/

    private void compose() {
//        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
//                .collect(StringBuilder::new, (sb, v) -> sb.append(v).append(" "))
//                .map(StringBuilder::toString)
//                .subscribe(x -> System.out.println("Viendo resultado: " + x));
//
//        Observable.range(1, 15)
//                .collect(StringBuilder::new, (sb, v) -> sb.append(v).append(" "))
//                .map(StringBuilder::toString)
//                .subscribe(x -> System.out.println("Viendo resultado: " + x));

        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .compose(stringCompose())
                .blockingSubscribe(System.out::println);

        Observable.range(1, 15)
                .compose(stringCompose())
                .blockingSubscribe(System.out::println);

    }

    private static <T> ObservableTransformer<T, String> stringCompose() {
        return upstream -> upstream
                .collect(StringBuilder::new, (sb, v) -> sb.append(v).append(" "))
                .map(StringBuilder::toString)
                .toObservable();
    }


}
