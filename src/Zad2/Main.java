package Zad2;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        Person person = new Person("John Doe", 25);
        Student student = new Student("Jane Smith", 20, "ABC University");
        Employee employee = new Employee("Bob Johnson", 30, "XYZ Company");

        System.out.println(person.toString());
        System.out.println(student.toString());
        System.out.println(employee.toString());
    }
}

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = getClass();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (isGetter(method)) {
                String fieldName = getFieldNameFromGetter(method.getName());
                try {
                    Object value = method.invoke(this);
                    sb.append(fieldName).append(": ").append(value).append("\t");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    private boolean isGetter(Method method) {
        String name = method.getName();
        return name.startsWith("get") && !name.equals("getClass") && method.getParameterCount() == 0;
    }

    private String getFieldNameFromGetter(String methodName) {
        return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }
}

class Student extends Person {
    private String university;

    public Student(String name, int age, String university) {
        super(name, age);
        this.university = university;
    }

    public String getUniversity() {
        return university;
    }
}

class Employee extends Person {
    private String company;

    public Employee(String name, int age, String company) {
        super(name, age);
        this.company = company;
    }

    public String getCompany() {
        return company;
    }
}
