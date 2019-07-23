package vn.edu.topica.services;

import vn.edu.topica.beans.Role;
import vn.edu.topica.beans.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService<Role, User> {
    @Override
    public int getSumPoint(List<User> vList) {
        return vList.stream()
                .map(User::getPoint)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public Map<Role, List<User>> getUsersByRole(List<User> vList) {
        return vList.stream().collect(Collectors.groupingBy(User::getRole));
    }

    @Override
    public long count(List<User> vList) {
        return vList.stream()
                .map(User::getPoint)
                .filter(point -> point >= 4)
                .count();
    }

    @Override
    public Map<Integer, User> convertToMapByPoint(List<User> users, int point) {
        return users.stream().collect(Collectors.groupingBy(User::getAge));
    }


}
