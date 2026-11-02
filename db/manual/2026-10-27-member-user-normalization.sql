-- MySQL 8. Run once, after the preflight and a database backup, before deploying
-- the Member.user mapping. Keep application writes stopped while this runs.

ALTER TABLE members ADD COLUMN user_id BIGINT NULL;

-- Legacy MemberService.signUp could create a Member without a User.
-- Reuse its existing password hash; never encode the hash a second time.
INSERT INTO users (username, password, role)
SELECT m.username, m.password, 'STUDENT'
FROM members m
LEFT JOIN users u ON u.username = m.username
WHERE u.id IS NULL;

UPDATE members m
JOIN users u ON u.username = m.username
SET m.user_id = u.id;

-- This query must return zero before the following ALTER TABLE statements.
SELECT COUNT(*) AS unlinked_members
FROM members m
LEFT JOIN users u ON u.id = m.user_id
WHERE m.user_id IS NULL OR u.id IS NULL OR u.username <> m.username;

ALTER TABLE members MODIFY COLUMN user_id BIGINT NOT NULL;
ALTER TABLE members ADD CONSTRAINT uk_members_user_id UNIQUE (user_id);
ALTER TABLE members ADD CONSTRAINT fk_members_user
    FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE members
    DROP COLUMN username,
    DROP COLUMN password,
    DROP COLUMN role;
