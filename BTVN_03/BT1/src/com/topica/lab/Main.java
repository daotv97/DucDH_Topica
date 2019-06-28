package com.topica.lab;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Attribute name
        System.out.println("== ATTRIBUTES == ");
        getAttributeName();

        // Method name
        System.out.print("\n== METHODS == \n");
        getMethodName();

        // Attribute name (static final)
        System.out.print("\n== STATIC FINAL == \n");
        getAttributeNameIsFinal();
    }

    private static void getAttributeName() {
        Arrays.stream(Student.class.getDeclaredFields())
                .filter(field -> field.getAnnotations().length > 0)
                .map(field -> "+ attribute: " + field.getName())
                .forEach(System.out::println);
    }

    private static void getMethodName() {
        Arrays.stream(Student.class.getMethods())
                .filter(method -> method.getAnnotations().length > 0)
                .map(method -> "+ method: " + method.getName())
                .forEach(System.out::println);
    }

    private static void getAttributeNameIsFinal() {
        Arrays.stream(Student.class.getDeclaredFields())
                .filter(field -> Modifier.isFinal(field.getModifiers()))
                .forEach(Main::accept);
    }

    private static void accept(Field field) {
        try {
            Field clazzDeclaredField = Student.class.getDeclaredField(field.getName());
            if (!clazzDeclaredField.isAccessible()) {
                clazzDeclaredField.setAccessible(true);
            }
            System.out.println(new StringBuilder().append("+ ")
                    .append(field.getName())
                    .append(" = ")
                    .append(clazzDeclaredField.get(new Student()))
                    .toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
