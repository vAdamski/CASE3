package Zad3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Main2 {
    public static void main(String[] args) {
        Student student = new Student();
        System.out.println(student);
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface DefaultStudent {
    String imie() default "";
    String nazwisko() default "";
}

@DefaultStudent(imie = "Jan", nazwisko = "Nowak")
class Student {
    private String imie;
    private String nazwisko;
    private int ocena;

    public Student() {
        DefaultStudent annotation = this.getClass().getAnnotation(DefaultStudent.class);
        if (annotation != null) {
            this.imie = annotation.imie();
            this.nazwisko = annotation.nazwisko();
        }
        // Domyślne wartości dla oceny
        this.ocena = 5;
    }

    @Override
    public String toString() {
        return "Student{" +
                "imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", ocena=" + ocena +
                '}';
    }
}
