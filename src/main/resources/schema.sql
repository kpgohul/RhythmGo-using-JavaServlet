CREATE TABLE IF NOT EXISTS users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS roles (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS user_roles (
      user_id INT NOT NULL,
      role_id INT NOT NULL,
      PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id)
    REFERENCES roles(id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS artists (
   id INT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS songs (
     id INT AUTO_INCREMENT PRIMARY KEY,
     title VARCHAR(200) NOT NULL,
    album VARCHAR(100),
    artist_id INT NOT NULL,
    CONSTRAINT fk_songs_artist
    FOREIGN KEY (artist_id)
    REFERENCES artists(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS playlists (
     id INT AUTO_INCREMENT PRIMARY KEY,
     user_id INT NOT NULL,
     name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT fk_playlists_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS playlist_songs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  playlist_id INT NOT NULL,
  song_id INT NOT NULL,
  CONSTRAINT fk_playlist_songs_playlist
  FOREIGN KEY (playlist_id)
    REFERENCES playlists(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_playlist_songs_song
    FOREIGN KEY (song_id)
    REFERENCES songs(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT unique_playlist_song UNIQUE (playlist_id, song_id)
    );

CREATE TABLE IF NOT EXISTS liked_songs (
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL,
   song_id INT NOT NULL,
   CONSTRAINT fk_liked_songs_user
   FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_liked_songs_song
    FOREIGN KEY (song_id)
    REFERENCES songs(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT unique_liked_song UNIQUE (user_id, song_id)
    );

CREATE TABLE IF NOT EXISTS play_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    song_id INT NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_play_history_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_play_history_song
    FOREIGN KEY (song_id)
    REFERENCES songs(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS follows (
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL,
   artist_id INT NOT NULL,
   CONSTRAINT fk_follows_user
   FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_follows_artist
    FOREIGN KEY (artist_id)
    REFERENCES artists(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT unique_follow UNIQUE (user_id, artist_id)
    );
