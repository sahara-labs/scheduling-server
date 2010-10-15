SET NOCOUNT ON;
INSERT INTO users VALUES('test1','UTS','USERS');
INSERT INTO users VALUES('test2','UTS','USERS');
INSERT INTO user_class VALUES (1, 0, 'TestClass', 1, 1, 1);
INSERT INTO user_association VALUES (1,1);
INSERT INTO user_association VALUES (1,2);
INSERT INTO request_capabilities VALUES ('tag1');
INSERT INTO resource_permission VALUES (1, 1, 'Tag1 Rigs', dateadd(day,30,{fn NOW()}), 300, 300, 300, 1800, {fn NOW()}, 'CAPABILITY', 1, NULL, NULL, 1);
