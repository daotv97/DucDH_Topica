# Find all user account.

DELIMITER $$
CREATE PROCEDURE findAll()
BEGIN
    SELECT `us`.id, `us`.username, `us`.password, `us`.age, `us`.email, `us`.status FROM user as us;
END $$
DELIMITER ;