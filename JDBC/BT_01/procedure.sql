# Find all user account.

create
    definer = root@localhost procedure findAll()
BEGIN
    SELECT `us`.id, `us`.username, `us`.password, `us`.age, `us`.email, `us`.status
    FROM user as `us`;
END;

# Find user by id.
create
    definer = root@localhost procedure findById(IN id mediumtext)
BEGIN
    SELECT `us`.id, `us`.username, `us`.password, `us`.age, `us`.email, `us`.status
    FROM user as `us`
    WHERE `us`.id = id;
END;