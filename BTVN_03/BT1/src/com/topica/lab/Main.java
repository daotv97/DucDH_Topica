package com.topica.lab;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Class<?> studentClass = Student.class;
        // The attribute have annotations
        System.out.println("- Attributes: ");
        Arrays.stream(studentClass.getDeclaredFields())
                .forEach(field -> {
                    if (field.getAnnotations().length > 0) {
                        System.out.println(field.getName());
                    }
                });

        // The method have annotations
        System.out.printf("\n- Methods: \n");
        Arrays.stream(studentClass.getMethods())
                .forEach(method -> {
                    if (method.getAnnotations().length > 0) {
                        System.out.println(method.getName());
                    }
                });

        // Get attributes static final
        System.out.printf("\n- Static final: \n");
        Arrays.stream(studentClass.getDeclaredFields()).forEach(field -> {
            if (Modifier.isFinal(field.getModifiers())) {

            }
        });
    }

}
