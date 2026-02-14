INSERT INTO users (id, name, email, password, salt) VALUES
                                                        (1, 'John Doe', 'john@test.com', 'hashed_pwd_1', 'salt1'),
                                                        (2, 'Alice Smith', 'alice@test.com', 'hashed_pwd_2', 'salt2'),
                                                        (3, 'Bob Johnson', 'bob@test.com', 'hashed_pwd_3', 'salt3'),
                                                        (4, 'Charlie Brown', 'charlie@test.com', 'hashed_pwd_4', 'salt4'),
                                                        (5, 'David Miller', 'david@test.com', 'hashed_pwd_5', 'salt5'),
                                                        (6, 'Emma Wilson', 'emma@test.com', 'hashed_pwd_6', 'salt6'),
                                                        (7, 'Frank Thomas', 'frank@test.com', 'hashed_pwd_7', 'salt7'),
                                                        (8, 'Grace Lee', 'grace@test.com', 'hashed_pwd_8', 'salt8'),
                                                        (9, 'Henry White', 'henry@test.com', 'hashed_pwd_9', 'salt9'),
                                                        (10, 'Isabella Clark', 'isabella@test.com', 'hashed_pwd_10', 'salt10'),
                                                        (11, 'Jack Lewis', 'jack@test.com', 'hashed_pwd_11', 'salt11'),
                                                        (12, 'Sophia Hall', 'sophia@test.com', 'hashed_pwd_12', 'salt12');

INSERT INTO roles (name, description)
VALUES
    ('ADMIN', 'Full system management access'),
    ('USER', 'Standard user with limited permissions');



INSERT INTO artists (id, name) VALUES
                                   (1, 'A.R. Rahman'),
                                   (2, 'Anirudh Ravichander'),
                                   (3, 'Ed Sheeran'),
                                   (4, 'Taylor Swift'),
                                   (5, 'Arijit Singh'),
                                   (6, 'The Weeknd'),
                                   (7, 'Dua Lipa'),
                                   (8, 'Imagine Dragons');

INSERT INTO songs (id, title, album, artist_id) VALUES
                                                    (1, 'Song A1', 'Album A', 1),
                                                    (2, 'Song A2', 'Album A', 1),
                                                    (3, 'Song B1', 'Album B', 2),
                                                    (4, 'Song B2', 'Album B', 2),
                                                    (5, 'Shape of You', 'Divide', 3),
                                                    (6, 'Perfect', 'Divide', 3),
                                                    (7, 'Blank Space', '1989', 4),
                                                    (8, 'Love Story', 'Fearless', 4),
                                                    (9, 'Tum Hi Ho', 'Aashiqui 2', 5),
                                                    (10, 'Kesariya', 'Brahmastra', 5),
                                                    (11, 'Blinding Lights', 'After Hours', 6),
                                                    (12, 'Starboy', 'Starboy', 6),
                                                    (13, 'Levitating', 'Future Nostalgia', 7),
                                                    (14, 'New Rules', 'Dua Lipa', 7),
                                                    (15, 'Believer', 'Evolve', 8),
                                                    (16, 'Thunder', 'Evolve', 8),
                                                    (17, 'Song Extra 1', 'Extra Album', 1),
                                                    (18, 'Song Extra 2', 'Extra Album', 2),
                                                    (19, 'Song Extra 3', 'Extra Album', 3),
                                                    (20, 'Song Extra 4', 'Extra Album', 4);

INSERT INTO playlists (id, user_id, name, description) VALUES
                                                           (1, 1, 'John Favs', 'My favorite tracks'),
                                                           (2, 2, 'Alice Mix', 'Workout playlist'),
                                                           (3, 3, 'Bob Chill', 'Relax vibes'),
                                                           (4, 4, 'Charlie Hits', 'Top charts'),
                                                           (5, 5, 'David Rock', 'Rock collection');

INSERT INTO playlist_songs (playlist_id, song_id) VALUES
                                                      (1, 5),
                                                      (1, 7),
                                                      (2, 11),
                                                      (2, 15),
                                                      (3, 9),
                                                      (3, 10),
                                                      (4, 13),
                                                      (4, 14),
                                                      (5, 15),
                                                      (5, 16);

INSERT INTO liked_songs (user_id, song_id) VALUES
                                               (1, 5),
                                               (1, 11),
                                               (2, 7),
                                               (3, 9),
                                               (4, 13),
                                               (5, 15),
                                               (6, 12),
                                               (7, 6),
                                               (8, 8),
                                               (9, 3),
                                               (10, 4),
                                               (11, 1),
                                               (12, 2);

INSERT INTO play_history (user_id, song_id) VALUES
                                                (1, 5),
                                                (1, 6),
                                                (2, 7),
                                                (3, 9),
                                                (4, 13),
                                                (5, 15),
                                                (6, 12),
                                                (7, 6),
                                                (8, 8),
                                                (9, 3),
                                                (10, 4),
                                                (11, 1),
                                                (12, 2);

INSERT INTO follows (user_id, artist_id) VALUES
                                             (1, 3),
                                             (1, 6),
                                             (2, 4),
                                             (3, 5),
                                             (4, 7),
                                             (5, 8),
                                             (6, 1),
                                             (7, 2),
                                             (8, 3),
                                             (9, 4),
                                             (10, 5),
                                             (11, 6),
                                             (12, 7);







