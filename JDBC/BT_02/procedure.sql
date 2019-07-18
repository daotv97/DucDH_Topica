
-- SAVE COMMIT
create
    definer = root@localhost procedure save(IN id mediumtext, IN username varchar(20), IN password varchar(100),
                                            IN age int, IN email varchar(100), IN status bit)
BEGIN
    SET AUTOCOMMIT = 0;
    SELECT * FROM user;
    INSERT INTO user VALUES(id, username, password, age, email, status);
    COMMIT;
    SELECT * FROM user;
END;

-- SAVE ROLL BACK
create
    definer = root@localhost procedure saveRollback(IN id mediumtext, IN username varchar(20), IN password varchar(100),
                                            IN age int, IN email varchar(100), IN status bit)
BEGIN
    SET AUTOCOMMIT = 0;
    SELECT * FROM user;
    INSERT INTO user VALUES(id, username, password, age, email, status);
    ROLLBACK;
    COMMIT;
    SELECT * FROM user;
END;


-- SAVE POINT - COMMIT - AND ROLLBACK

create
    definer = root@localhost procedure deleteByIds()
BEGIN
    SET AUTOCOMMIT = 0;
    DELETE FROM user WHERE id = 3;
    COMMIT;
    DELETE FROM user WHERE id = 5;
    COMMIT;
    SAVEPOINT VT1;
    DELETE FROM user WHERE id = 16;
    ROLLBACK TO VT1;
    COMMIT;
    SELECT * FROM user;
END