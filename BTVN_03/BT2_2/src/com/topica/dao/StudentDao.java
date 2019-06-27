package com.topica.dao;
import com.topica.annotations.Repository;
import com.topica.bean.Student;

@Repository
public class StudentDao implements Dao<Student, Long> {
}
