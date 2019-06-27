package com.topica.repository;

import com.topica.annotations.Column;
import com.topica.annotations.Repository;
import com.topica.annotations.Table;
import com.topica.bean.Student;

import java.lang.reflect.Field;

@Repository(name = StudentRepository.class)
public class StudentRepository implements CrudRepository<Student, Long> {

    @Override
    public String findById(Long aLong) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isAnnotationPresent(Student.class))
            return stringBuilder.append("SELECT * FROM ")
                    .append(Student.class.getDeclaredAnnotation(Table.class).name())
                    .append(" WHERE id = ")
                    .append(aLong).toString();
        return "";
    }

    @Override
    public String findAll() {
        StringBuilder stringBuilder = new StringBuilder();
        if (isAnnotationPresent(Student.class))
            return stringBuilder.append("SELECT * FROM ")
                    .append(Student.class.getDeclaredAnnotation(Table.class).name())
                    .toString();
        return "";
    }

    @Override
    public String save(Student entity) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isAnnotationPresent(Student.class)) {
            Field[] fields = Student.class.getDeclaredFields();
            stringBuilder.append("INSERT INTO ( ");
            if (fields.length > 0) {
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].isAnnotationPresent(Column.class)) {
                        if (i < fields.length - 1) {
                            stringBuilder.append(fields[i].getName() + ", ");
                        } else if (i < fields.length) {
                            stringBuilder.append(fields[i].getName() + " )");
                        }
                    }
                }
                stringBuilder.append(" VALUE ");
                return stringBuilder.toString();
            }
        }
        return "";
    }

    private boolean isAnnotationPresent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Table.class)
                && StudentRepository.class.isAnnotationPresent(Repository.class);
    }
}
