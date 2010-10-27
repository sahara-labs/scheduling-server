INSERT INTO users VALUES (NULL, 'test1', 'UTS', 'USER');INSERT INTO users VALUES (NULL, 'test2', 'UTS', 'USER');
INSERT INTO user_class VALUES (NULL, 1, 1, 0, 'TestClass', 1, 1, 0, 1);
INSERT INTO user_association VALUES (1,1);INSERT INTO user_association VALUES (1,2);
INSERT INTO request_capabilities VALUES (NULL, 'tag1');
INSERT INTO resource_permission VALUES (NULL, 1, 1, 'Tag1 Rigs', now() + interval 30 day, 300, 300, 300, 1800, now(), 'CAPABILITY', 1, NULL, NULL, 1);
