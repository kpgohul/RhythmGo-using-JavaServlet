DELETE FROM playlist_songs;
DELETE FROM liked_songs;
DELETE FROM play_history;
DELETE FROM follows;
DELETE FROM playlists;
DELETE FROM songs;
DELETE FROM artists;
DELETE FROM users;

UPDATE user_roles
SET role_id = 1
WHERE user_id = 4;


SELECT * FROM users;
select * from user_roles;
select * from roles;
SELECT * FROM songs;
SELECT * FROM playlists;
SELECT * FROM playlist_songs;
SELECT * FROM liked_songs;
SELECT * FROM play_history;
SELECT * FROM artists;
SELECT * FROM follows;

DROP TABLE IF EXISTS playlist_songs;
DROP TABLE IF EXISTS liked_songs;
DROP TABLE IF EXISTS play_history;
DROP TABLE IF EXISTS follows;
DROP TABLE IF EXISTS playlists;
DROP TABLE IF EXISTS songs;
DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_roles;
