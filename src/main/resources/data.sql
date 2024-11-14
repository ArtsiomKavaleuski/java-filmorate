DELETE FROM filmorate.friends;

DELETE FROM filmorate.filmGenres;

DELETE FROM filmorate.likes;

DELETE FROM filmorate.users;
ALTER TABLE filmorate.users ALTER COLUMN id RESTART WITH 1;

DELETE FROM filmorate.films;
ALTER TABLE filmorate.films ALTER COLUMN id RESTART WITH 1;

DELETE FROM filmorate.mpa;
ALTER TABLE filmorate.mpa ALTER COLUMN id RESTART WITH 1;

DELETE FROM filmorate.genres;
ALTER TABLE filmorate.genres ALTER COLUMN genreId RESTART WITH 1;

INSERT INTO filmorate.mpa (name)
VALUES ('G');

INSERT INTO filmorate.mpa (name)
VALUES ('PG');

INSERT INTO filmorate.mpa (name)
VALUES ('PG-13');

INSERT INTO filmorate.mpa (name)
VALUES ('R');

INSERT INTO filmorate.mpa (name)
VALUES ('NC-17');

INSERT INTO filmorate.genres (genreName)
VALUES ('Комедия');

INSERT INTO filmorate.genres (genreName)
VALUES ('Драма');

INSERT INTO filmorate.genres (genreName)
VALUES ('Мультфильм');

INSERT INTO filmorate.genres (genreName)
VALUES ('Триллер');

INSERT INTO filmorate.genres (genreName)
VALUES ('Документальный');

INSERT INTO filmorate.genres (genreName)
VALUES ('Боевик');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('koval@bff.by', 'koval', 'Artem', '1994-04-05');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('kopylov@bff.by', 'kopylov', 'Yauheni', '1992-02-28');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('vitushko@bff.by', 'sd', 'Siarhei', '1945-07-13');

INSERT INTO filmorate.users (email, login, name, birthday)
VALUES ('tsapliuk@bff.by', 'tsapliuk', 'Ihar', '1968-11-03');

INSERT INTO filmorate.films (name, description, releaseDate, duration, mpa)
VALUES ('Interstellar', 'Space odissey', '2018-01-02', 130, 2);

INSERT INTO filmorate.films (name, description, releaseDate, duration, mpa)
VALUES ('Avatar', 'alternative universe', '2008-01-02', 120, 1);

INSERT INTO filmorate.films (name, description, releaseDate, duration, mpa)
VALUES ('Gladiator', 'history about Maximus', '2001-02-20', 110, 4);