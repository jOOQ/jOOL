package org.jooq.lambda;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jooq.lambda.tuple.Tuple4;

public class Example {
    public static void main(String[] args) {
        List<Person> personsList = new ArrayList<>();

        personsList.add(new Person("John", "Doe", 25, 1.80, 80));
        personsList.add(new Person("Jane", "Doe", 30, 1.69, 60));
        personsList.add(new Person("John", "Smith", 35, 174, 70));

        Tuple4<Long, Optional<Integer>, Optional<Double>, Optional<Double>> result =
        Seq.seq(personsList)
           .collect(
                Agg.count(),
                Agg.max(Person::getAge),
                Agg.min(Person::getHeight),
                Agg.avg(Person::getWeight)
           );

        System.out.println(result);
    }
}

class Person {

    private String firstName;
    private String lastName;
    private int    age;
    private double height;
    private double weight;

    public Person(String firstName, String lastName, int age, double height, double weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

}
