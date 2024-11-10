CREATE SCHEMA IF NOT EXISTS filmorate;

SET SCHEMA filmorate;

CREATE TABLE IF NOT EXISTS mpa (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(5)
    );

CREATE TABLE IF NOT EXISTS users (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    login VARCHAR(100),
    name VARCHAR(100),
    birthday DATE
    );

CREATE TABLE IF NOT EXISTS films (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	releaseDate DATE,
	duration INT,
	mpa INT,
	FOREIGN KEY (mpa) REFERENCES mpa (id)
	);

CREATE TABLE IF NOT EXISTS friends (
    userId INT NOT NULL,
    friendId INT,
    reciprocity BOOLEAN DEFAULT(FALSE),
    FOREIGN KEY (userId) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS genres (
    genreId INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genreName VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS film_genres (
    filmId INT NOT NULL,
    genreId INT NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(genreId),
    FOREIGN KEY (filmId) REFERENCES films(id)
    );

CREATE TABLE IF NOT EXISTS likes (
	filmId INT NOT NULL,
    userId INT NOT NULL,
	FOREIGN KEY (filmId) REFERENCES films(id),
	FOREIGN KEY (userId) REFERENCES users(id)
    );