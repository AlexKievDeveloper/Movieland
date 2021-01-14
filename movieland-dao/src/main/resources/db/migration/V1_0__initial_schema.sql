CREATE TYPE genres AS ENUM('драма', 'криминал', 'фэнтези', 'детектив', 'мелодрама', 'биография', 'комедия', 'фантастика',
    'боевик', 'триллер', 'приключения', 'аниме', 'мультфильм', 'семейный', 'вестерн');

CREATE TABLE IF NOT EXISTS users
(
    user_id  SERIAL PRIMARY KEY,
    name     VARCHAR(200) UNIQUE NOT NULL,
    email    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(200)       NOT NULL
);

CREATE TABLE IF NOT EXISTS movies
(
    movie_id    SERIAL PRIMARY KEY,
    nameRussian VARCHAR(200) UNIQUE NOT NULL,
    nameNative VARCHAR(200) UNIQUE  NOT NULL,
    yearOfRelease INTEGER           NOT NULL,
    country     VARCHAR(200)        NOT NULL,
    genre       genres[]            NOT NULL,
    description VARCHAR(1000)       NOT NULL,
    rating      DOUBLE PRECISION    NOT NULL,
    price       DOUBLE PRECISION    NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id  SERIAL PRIMARY KEY,
    review     VARCHAR(500) NOT NULL,
    user_name  VARCHAR(200) NOT NULL,
    movie_id   INTEGER NOT NULL,

    FOREIGN KEY (user_name) REFERENCES users (name),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);

CREATE TABLE IF NOT EXISTS posters
(
    poster_id  SERIAL PRIMARY KEY,
    movie_id   INTEGER NOT NULL,
    picturePath VARCHAR(500) NOT NULL,

    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);
