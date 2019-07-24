package vn.edu.topica;

import vn.edu.topica.beans.Role;
import vn.edu.topica.beans.User;
import vn.edu.topica.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    private static final int POINT = 4;

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        Role roleStudent = new Role(1L, "student");
        Role roleTeacher = new Role(2L, "teacher");
        users.add(new User(1L, "user1", "123", 18, 9, true, roleStudent));
        users.add(new User(2L, "user2", "123", 12, 4, true, roleStudent));
        users.add(new User(3L, "user3", "123", 12, 5, true, roleStudent));
        users.add(new User(4L, "user4", "123", 13, 2, true, roleStudent));
        users.add(new User(5L, "user5", "123", 14, 1, true, roleStudent));
        users.add(new User(6L, "user6", "123", 15, 4, true, roleStudent));
        users.add(new User(7L, "user7", "123", 16, 6, true, roleStudent));
        users.add(new User(8L, "user8", "123", 17, 3, true, roleStudent));
        users.add(new User(9L, "user9", "123", 18, 9, true, roleStudent));
        users.add(new User(10L, "user1", "123", 18, 9, true, roleTeacher));

        UserServiceImpl userService = new UserServiceImpl();
        long streamStart = System.currentTimeMillis();
        // sum
        LOGGER.info("Sum point: " + userService.getSumPoint(users));
        long streamEnd = System.currentTimeMillis();
        LOGGER.info("Time ( Stream ): " + (streamEnd - streamStart));

        // group
        Map<Role, List<User>> userMap = userService.getUsersByRole(users);
        userMap.forEach((role, users1) -> {
            LOGGER.info("Role: " + role.getRoleName());
            users1.forEach(itemUser -> LOGGER.info(itemUser.toString()));
        });

        // count
        LOGGER.info("Count student have point >= 4: " + userService.count(users));
        Map<Long, User> afterConvertList = userService.convertToMapByPoint(users, POINT);
        afterConvertList.forEach((aLong, user) -> LOGGER.info("Key: " + aLong + "- Value: " + user));
        LOGGER.info("Sum point: " + userService.getSumPoint(users));


        // parallel loop
        long parallelStreamStart = System.currentTimeMillis();
        LOGGER.info("Sum point: " + userService.getSumPoint(users));
        long parallelStreamEnd = System.currentTimeMillis();
        LOGGER.info("Time ( Parallel Stream): " + (parallelStreamEnd - parallelStreamStart));
    }
}
