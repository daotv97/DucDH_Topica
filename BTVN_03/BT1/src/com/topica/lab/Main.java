package com.topica.lab;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Class<?> studentClass = Student.class;

        // The attribute have annotations
        System.out.println("- Attributes: ");
        getAttributeName(studentClass);

        // The method have annotations
        System.out.printf("\n- Methods: \n");
        getMethodName(studentClass);

        // The attribute field is "final"
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
                    Field f = clazz.getDeclaredField(field.getName());
                    f.setAccessible(true);
                    System.out.println("+ " + field.getName() + " = " + f.get(new Student()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
