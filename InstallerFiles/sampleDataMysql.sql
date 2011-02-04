INSERT INTO users (name, namespace, persona) VALUES('test1','UTS','USER');
INSERT INTO users (name, namespace, persona) VALUES('test2','UTS','USER');
INSERT INTO user_class (active, bookable, kickable, name, priority, queuable, time_horizon, users_lockable) 
            VALUES (1, 1, 0, 'TestClass', 1, 1, 0, 1);
INSERT INTO user_association VALUES (1,1);
INSERT INTO user_association VALUES (1,2);
INSERT INTO request_capabilities (capabilities) VALUES ('tag1');
INSERT INTO resource_permission (use_activity_detection, allowed_extensions, display_name, expiry_time, extension_duration, maximum_bookings, 
            queue_activity_timeout, session_activity_timeout, session_duration, start_time, type, request_caps_id, name_id, type_id, user_class_id)
            VALUES (1, 1, 'Tag1 Rigs', now() + interval 30 day, 300, 5, 300, 300, 1800, now(), 'CAPABILITY', 1, NULL, NULL, 1);
