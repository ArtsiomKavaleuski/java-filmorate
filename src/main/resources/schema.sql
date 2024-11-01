CREATE SCHEMA IF NOT EXISTS filmorate;

SET SCHEMA filmorate;

CREATE TABLE IF NOT EXISTS rates (
    rateId INT PRIMARY KEY,
	rateName VARCHAR(5)
    );

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
    email VARCHAR(100) NOT NULL,
    login VARCHAR(100),
    name VARCHAR(100),
    birthday DATE
    );

CREATE TABLE IF NOT EXISTS films (
    id INT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
	name VARCHAR(100) NOT NULL,
	description TEXT,
	releaseDate DATE,
	duration INT,
	rateId INT,
	FOREIGN KEY (rateId) REFERENCES rates (rateId)
	);

CREATE TABLE IF NOT EXISTS friends (
    frId INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    userId INT NOT NULL,
    friendId INT,
    friendship BOOLEAN,
    FOREIGN KEY (userId) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS genres (
    genreId INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genreName VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS film_genres (
    film_genres_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    filmId INT NOT NULL,
    genreId INT NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(genreId)
    );

CREATE TABLE IF NOT EXISTS likes (
    likeId INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    userId INT NOT NULL,
	filmId INT NOT NULL,
	FOREIGN KEY (userId) REFERENCES users(id),
	FOREIGN KEY (filmId) REFERENCES films(id)
    );