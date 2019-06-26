package com.topica.lab;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args) {
        Class<?> studentClass = Student.class;
        try {
            Field field = studentClass.getDeclaredField( "IDENTITY_CARD_NUMBER" );
            field.setAccessible( true );

            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt( field, field.getModifiers() & ~Modifier.FINAL );

            System.out.println("Before change: IDENTITY_CARD_NUMBER = " + field.get(studentClass));
            field.set( null, "172009990" );
            System.out.println("After change: IDENTITY_CARD_NUMBER = " + field.get(studentClass));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
