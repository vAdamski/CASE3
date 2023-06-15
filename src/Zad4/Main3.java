package Zad4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main3 {
    public static void main(String[] args) {
        Student student1 = new Student();
        student1.indeks = 12345;
        student1.oceny = new ArrayList<>();
        student1.oceny.add(4.5f);
        student1.imie = "John";
        student1.nazwisko = "Doe";
        student1.stopien_studiow = "Licencjat";

        Student student2 = new Student();
        student2.indeks = 54321;
        student2.oceny = new ArrayList<>();
        student2.oceny.add(4.0f);
        student2.imie = "Jane";
        student2.nazwisko = "Smith";
        student2.stopien_studiow = "Magister";

        boolean areEqual = student1.equals(student2);
        System.out.println("Students are equal: " + areEqual);
    }
}



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface IgnoreEquals {}
class Student {
    @IgnoreEquals
    int indeks;
    @IgnoreEquals
    List<Float> oceny;
    String imie;
    String nazwisko;
    @IgnoreEquals
    String stopien_studiow;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Student other = (Student) obj;
        Field[] fields = getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                if (field.getAnnotation(IgnoreEquals.class) == null) {
                    Object thisValue = field.get(this);
                    Object otherValue = field.get(other);
                    if (!Objects.equals(thisValue, otherValue)) {
                        return false;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}