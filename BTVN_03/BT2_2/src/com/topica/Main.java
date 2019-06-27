package com.topica;

import com.topica.bean.Student;
import com.topica.repository.StudentRepository;

public class Main {

    public static void main(String[] args) {
        StudentRepository studentRepository = new StudentRepository();
        System.out.println("Generate query:");
        // FindById
        System.out.println("findById(): " + studentRepository.findById((long) 10));

        // FindAll
        System.out.println("findAll(): " + studentRepository.findAll());

        // Save
        System.out.println("save(): " + studentRepository.save(new Student()));
    }
}
