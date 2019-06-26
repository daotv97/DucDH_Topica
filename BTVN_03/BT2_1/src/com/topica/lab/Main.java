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
            changeAttributeValue(studentClass, ATTRIBUTE_NAME);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void changeAttributeValue(@NotNull Class<?> clazz, String attributeName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(attributeName);
        field.setAccessible(true);

        // Modify field
        modifyField(field);
        System.out.println("Before change: " + attributeName + " = " + field.get(clazz));

        // Change value
        field.set(null, "172009990");
        System.out.println("After change: " + attributeName + " = " + field.get(clazz));
    }

    private static void modifyField(Field field) throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField(MODIFIERS);
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }
}
