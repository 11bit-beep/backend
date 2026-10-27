-- MySQL 8: these queries must return no rows before the migration is run.
-- A student member must not point to an existing teacher or administrator account.
SELECT m.id AS member_id, m.username, u.role
FROM members m
JOIN users u ON u.username = m.username
WHERE u.role <> 'STUDENT';

-- Existing member records without a User will be given a student User.
-- Review these rows before copying their stored password hashes.
SELECT m.id AS member_id, m.username
FROM members m
LEFT JOIN users u ON u.username = m.username
WHERE u.id IS NULL;
