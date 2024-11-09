DELETE FROM friends;

DELETE FROM film_genres;

DELETE FROM users;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;


DELETE FROM films;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;

DELETE FROM mpa;
ALTER TABLE mpa ALTER COLUMN id RESTART WITH 1;

DELETE FROM genres;
ALTER TABLE genres ALTER COLUMN genreId RESTART WITH 1;

INSERT INTO mpa (name)
VALUES ('G');

INSERT INTO mpa (name)
VALUES ('PG');

INSERT INTO mpa (name)
VALUES ('PG-13');

INSERT INTO mpa (name)
VALUES ('R');

INSERT INTO mpa (name)
VALUES ('NC-17');

INSERT INTO genres (genreName)
VALUES ('Комедия');

INSERT INTO genres (genreName)
VALUES ('Драма');

INSERT INTO genres (genreName)
VALUES ('Мультфильм');

INSERT INTO genres (genreName)
VALUES ('Триллер');

INSERT INTO genres (genreName)
VALUES ('Документальный');

INSERT INTO genres (genreName)
VALUES ('Боевик');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('koval@bff.by', 'koval', 'Artem', '1994-04-05');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('kopylov@bff.by', 'kopylov', 'Yauheni', '1992-02-28');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('vitushko@bff.by', 'sd', 'Siarhei', '1945-07-13');

INSERT INTO users (email, login, name, birthday)
VALUES ('tsapliuk@bff.by', 'tsapliuk', 'Ihar', '1968-11-03');

INSERT INTO films (name, description, releaseDate, duration)
VALUES ('Interstellar', 'Space odissey', '2018-01-02', 130);

INSERT INTO films (name, description, releaseDate, duration)
VALUES ('Interstellar', 'Space odissey', '2018-01-02', 130);

INSERT INTO films (name, description, releaseDate, duration)
VALUES ('Avatar', 'alternative universe', '2008-01-02', 120);

INSERT INTO films (name, description, releaseDate, duration)
VALUES ('Gladiator', 'history about Maximus', '2001-02-20', 110);

INSERT INTO film_genres (filmId, genreId)
VALUES (1, 6);

INSERT INTO film_genres (filmId, genreId)
VALUES (1, 3);

INSERT INTO film_genres (filmId, genreId)
VALUES (2, 4);

INSERT INTO film_genres (filmId, genreId)
VALUES (3, 5);