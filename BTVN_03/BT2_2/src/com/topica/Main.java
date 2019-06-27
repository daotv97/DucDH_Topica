package com.topica;

import com.topica.annotations.Repository;
import com.topica.bean.Student;
import com.topica.dao.StudentDao;

public class Main {
    public static void main(String[] args) {
        if (checkRepository(StudentDao.class)) {
            StudentDao studentRepository = new StudentDao();
            System.out.println(studentRepository.findAll(new Student()));
        }
    }

    private static boolean checkRepository(Class<?> clazz) {
        return clazz.isAnnotationPresent(Repository.class);
    }
}
