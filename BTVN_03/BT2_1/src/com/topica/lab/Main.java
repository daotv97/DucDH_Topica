package com.topica.lab;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Main {
    private static final String ATTRIBUTE_NAME = "IDENTITY_CARD_NUMBER";
    private static final String MODIFIERS = "modifiers";

    public static void main(String[] args) {
        Class<?> studentClass = Student.class;
        try {
            changeAttributeValue(studentClass);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void changeAttributeValue(@NotNull Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(Main.ATTRIBUTE_NAME);
        field.setAccessible(true);

        // Modify field
        modifyField(field);
        System.out.printf("Before change: %s = %s%n", Main.ATTRIBUTE_NAME, field.get(clazz));

        // Change value
        field.set(null, 14);
        System.out.printf("After change: %s = %s%n", Main.ATTRIBUTE_NAME, field.get(clazz));
    }

    private static void modifyField(Field field) throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField(MODIFIERS);
        if (!modifiersField.isAccessible()) {
            modifiersField.setAccessible(true);
        }
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }
}
