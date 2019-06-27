package com.topica.lab;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Class<?> studentClass = Student.class;

        // Attribute
        System.out.println("- Attributes: ");
        getAttributeName(studentClass);

        // Method
        System.out.printf("\n- Methods: \n");
        getMethodName(studentClass);

        // Static final
        System.out.printf("\n- Static final: \n");
        getAttributeNameIsFinal(studentClass);
    }

    private static void getAttributeName(@NotNull Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    if (field.getAnnotations().length > 0) {
                        System.out.println("+ attribute: " + field.getName());
                    }
                });
    }

    private static void getMethodName(@NotNull Class<?> clazz) {
        Arrays.stream(clazz.getMethods())
                .forEach(method -> {
                    if (method.getAnnotations().length > 0) {
                        System.out.println("+ method: " + method.getName());
                    }
                });
    }

    private static void getAttributeNameIsFinal(@NotNull Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            if (Modifier.isFinal(field.getModifiers())) {
                try {
                    Field clazzDeclaredField = clazz.getDeclaredField(field.getName());
                    clazzDeclaredField.setAccessible(true);
                    System.out.println("+ " + field.getName() + " = " + clazzDeclaredField.get(new Student()));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
