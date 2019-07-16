# Find all user account.

DELIMITER $$
CREATE PROCEDURE findById()
BEGIN
    SELECT `us`.id, `us`.username, `us`.password, `us`.age, `us`.email, `us`.status
    FROM user as `us`;
END $$
DELIMITER ;

CREATE PROCEDURE findById(id LONG)
BEGIN
    SELECT `us`.id, `us`.username, `us`.password, `us`.age, `us`.email, `us`.status
    FROM user as `us`
    WHERE `us`.id = id;
END