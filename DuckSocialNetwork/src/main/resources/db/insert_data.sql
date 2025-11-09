INSERT INTO persons (
    id, username, password, email, first_name, last_name, occupation, date_of_birth, empathy_level
)
VALUES
    (21, 'john21', 'pwd21384345', 'john21@example.com', 'John', 'Smith', 'Engineer', '1990-05-12', 8.2),
    (22, 'jane22', 'pwd2297834', 'jane22@example.com', 'Jane', 'Doe', 'Doctor', '1989-11-03', 9.1),
    (23, 'alex23', 'pwd2397845', 'alex23@example.com', 'Alex', 'Brown', 'Teacher', '1992-07-21', 7.8),
    (24, 'lisa24', 'pwd273450784', 'lisa24@example.com', 'Lisa', 'White', 'Designer', '1991-02-10', 8.5),
    (25, 'mark25', 'pwd252078', 'mark25@example.com', 'Mark', 'Jones', 'Developer', '1993-08-15', 7.9),
    (26, 'emma26', 'pwd2648789', 'emma26@example.com', 'Emma', 'Green', 'Nurse', '1988-12-30', 9.0),
    (27, 'chris27', 'pwd22437', 'chris27@example.com', 'Chris', 'Black', 'Lawyer', '1990-06-18', 8.4),
    (28, 'sara28', 'pwd289782', 'sara28@example.com', 'Sara', 'Blue', 'Scientist', '1992-03-05', 8.7),
    (29, 'mike29', 'pwd297892', 'mike29@example.com', 'Mike', 'Gray', 'Architect', '1989-09-09', 7.6),
    (30, 'olivia30', 'pwd30278', 'olivia30@example.com', 'Olivia', 'Red', 'Chef', '1991-01-22', 8.9),
    (31, 'dan31', 'pwd312873', 'dan31@example.com', 'Dan', 'Yellow', 'Farmer', '1993-04-11', 8.0),
    (32, 'nina32', 'pwd32378966', 'nina32@example.com', 'Nina', 'Purple', 'Writer', '1990-10-07', 8.8),
    (33, 'tom33', 'pwd1235', 'tom33@example.com', 'Tom', 'Orange', 'Musician', '1989-08-19', 7.7),
    (34, 'lily34', 'pwd344654', 'lily34@example.com', 'Lily', 'Silver', 'Actor', '1992-05-27', 9.2),
    (35, 'paul35', 'pwd35243', 'paul35@example.com', 'Paul', 'Gold', 'Engineer', '1991-09-13', 8.3),
    (36, 'kate36', 'pwd3675', 'kate36@example.com', 'Kate', 'Plum', 'Designer', '1988-11-26', 8.6),
    (37, 'jack37', 'pwd3754376', 'jack37@example.com', 'Jack', 'Cyan', 'Doctor', '1990-07-02', 8.1),
    (38, 'emma38', 'pwd38643', 'emma38@example.com', 'Emma', 'Magenta', 'Teacher', '1992-12-14', 7.9),
    (39, 'leo39', 'pwd392426', 'leo39@example.com', 'Leo', 'Lime', 'Developer', '1991-03-19', 8.4),
    (40, 'zoe40', 'pwd404236', 'zoe40@example.com', 'Zoe', 'Teal', 'Nurse', '1989-06-08', 9.0);

INSERT INTO ducks (id, username, password, email, duck_type, speed, rezistance)
VALUES
    (1, 'quacky1', 'pwd111', 'quacky1@example.com', 'FLYING', 12.3, 8.7),
    (2, 'swimmy2', 'pwd211', 'swimmy2@example.com', 'SWIMMING', 10.5, 9.4),
    (3, 'wingy3', 'pwd311', 'wingy3@example.com', 'FLYING_AND_SWIMMING', 14.2, 7.6),
    (4, 'feathers4', 'pwd411', 'feathers4@example.com', 'FLYING', 13.1, 8.0),
    (5, 'flappy5', 'pwd511', 'flappy5@example.com', 'SWIMMING', 11.7, 8.9),
    (6, 'quacker6', 'pwd61', 'quacker6@example.com', 'FLYING', 12.8, 7.3),
    (7, 'splashy7', 'pwd731', 'splashy7@example.com', 'SWIMMING', 9.9, 9.5),
    (8, 'wingster8', 'pwd8312', 'wingster8@example.com', 'FLYING_AND_SWIMMING', 13.5, 8.2),
    (9, 'feathery9', 'pwd9423', 'feathery9@example.com', 'FLYING', 14.0, 7.9),
    (10, 'swimmer10', 'pwd101', 'swimmer10@example.com', 'SWIMMING', 11.2, 8.8),
    (11, 'quacko11', 'pwd11423', 'quacko11@example.com', 'FLYING', 12.6, 8.1),
    (12, 'splash12', 'pwd124242', 'splash12@example.com', 'SWIMMING', 10.8, 9.2),
    (13, 'wingman13', 'pwd1342', 'wingman13@example.com', 'FLYING_AND_SWIMMING', 13.9, 7.5),
    (14, 'fluff14', 'pwd14423', 'fluff14@example.com', 'FLYING', 12.0, 8.3),
    (15, 'duckster15', 'pwd1564', 'duckster15@example.com', 'SWIMMING', 11.5, 9.0),
    (16, 'quackster16', 'pwd164264', 'quackster16@example.com', 'FLYING', 13.2, 7.8),
    (17, 'splashy17', 'pwd174684', 'splashy17@example.com', 'SWIMMING', 10.7, 9.1),
    (18, 'winged18', 'pwd183453', 'winged18@example.com', 'FLYING_AND_SWIMMING', 14.1, 8.0),
    (19, 'feathers19', 'pwd198434', 'feathers19@example.com', 'FLYING', 12.4, 8.6),
    (20, 'swimmy20', 'pwd204873', 'swimmy20@example.com', 'SWIMMING', 11.0, 9.3);
INSERT INTO friendships (id, user1_id, user2_id)
VALUES
    (1, 1, 21),
    (2, 2, 22),
    (3, 3, 23),
    (4, 4, 24),
    (5, 5, 25),
    (6, 6, 26),
    (7, 7, 27),
    (8, 8, 28),
    (9, 9, 29),
    (10, 10, 30),
    (11, 11, 31),
    (12, 12, 32),
    (13, 13, 33),
    (14, 14, 34),
    (15, 15, 35);

INSERT INTO flocks (id, flock_name) VALUES
                                        (1, 'FlockA'),
                                        (2, 'FlockB'),
                                        (3, 'FlockC'),
                                        (4, 'FlockD'),
                                        (5, 'FlockE'),
                                        (6, 'FlockF'),
                                        (7, 'FlockG'),
                                        (8, 'FlockH'),
                                        (9, 'FlockI'),
                                        (10, 'FlockJ'),
                                        (11, 'FlockK'),
                                        (12, 'FlockL'),
                                        (13, 'FlockM'),
                                        (14, 'FlockN'),
                                        (15, 'FlockO');

-- Insert into flock_members
INSERT INTO flock_members (flock_id, duck_id) VALUES
                                                  (1, 1), (1, 2), (1, 3),
                                                  (2, 4), (2, 5), (2, 6),
                                                  (3, 7), (3, 8),
                                                  (4, 9), (4, 10), (4, 11),
                                                  (5, 12), (5, 13),
                                                  (6, 14), (6, 15),
                                                  (7, 16), (7, 17),
                                                  (8, 18), (8, 19),
                                                  (9, 20), (9, 1),
                                                  (10, 2), (10, 3), (10, 4),
                                                  (11, 5), (11, 6), (11, 7),
                                                  (12, 8), (12, 9),
                                                  (13, 10), (13, 11), (13, 12),
                                                  -- (14, ...) FlockN has no members
                                                  (15, 15), (15, 16), (15, 17);

-- Insert into race_events
INSERT INTO race_events (id, name, max_time) VALUES
                                                 (1, 'Dinamo', 0.0),
                                                 (3, 'Mihai', 0.0),
                                                 (4, 'test', 0.0),
                                                 (5, 'TEST', 0.0);

-- Insert into race_event_participants
INSERT INTO race_event_participants (event_id, duck_id) VALUES
                                                            (1, 10), (1, 5), (1, 15), (1, 17), (1, 12),
                                                            (3, 10), (3, 5), (3, 15),
                                                            (4, 10), (4, 5), (4, 15), (4, 17), (4, 12), (4, 20), (4, 2),
                                                            (5, 10), (5, 5), (5, 15), (5, 17), (5, 12), (5, 20);
