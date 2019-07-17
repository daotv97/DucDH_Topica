create
    definer = root@localhost procedure SaveNoRollback(IN id mediumtext, IN username varchar(20),
                                                      IN password varchar(100), IN age int, IN email varchar(100),
                                                      IN status bit)
BEGIN
    SET AUTOCOMMIT = 0;
    SELECT * FROM user;
    INSERT INTO user VALUES(id, username, password, age, email, status);
    COMMIT;
    SELECT * FROM user;
END;

