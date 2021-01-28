CREATE TABLE IF NOT EXISTS genres
(
    genre_id  SERIAL PRIMARY KEY,
    name     VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS countries
(
    country_id  SERIAL PRIMARY KEY,
    name     VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS movies
(
    movie_id    SERIAL PRIMARY KEY,
    nameRussian VARCHAR(200) UNIQUE NOT NULL,
    nameNative  VARCHAR(200) UNIQUE NOT NULL,
    yearOfRelease INTEGER           NOT NULL,
    description VARCHAR(1000)       NOT NULL,
    rating      DOUBLE PRECISION    NOT NULL,
    price       DOUBLE PRECISION    NOT NULL
);

CREATE TABLE IF NOT EXISTS movies_genres
(
  movie_id INTEGER NOT NULL,
  genre_id INTEGER NOT NULL,

  FOREIGN KEY (movie_id) REFERENCES movies (movie_id),
  FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS movies_countries
(
    movie_id INTEGER NOT NULL,
    country_id INTEGER NOT NULL,

    FOREIGN KEY (movie_id) REFERENCES movies (movie_id),
    FOREIGN KEY (country_id) REFERENCES countries (country_id)
);

CREATE TABLE IF NOT EXISTS posters
(
    poster_id  SERIAL PRIMARY KEY,
    movie_id   INTEGER NOT NULL,
    picturePath VARCHAR(500) NOT NULL,

    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  SERIAL PRIMARY KEY,
    name     VARCHAR(200) UNIQUE NOT NULL,
    email    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(200)       NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id  SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL,
    movie_id   INTEGER NOT NULL,
    review     VARCHAR(1000) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);

INSERT INTO genres (name) VALUES ('драма');
INSERT INTO genres (name) VALUES ('криминал');
INSERT INTO genres (name) VALUES ('фэнтези');
INSERT INTO genres (name) VALUES ('детектив');
INSERT INTO genres (name) VALUES ('мелодрама');
INSERT INTO genres (name) VALUES ('биография');
INSERT INTO genres (name) VALUES ('комедия');
INSERT INTO genres (name) VALUES ('фантастика');
INSERT INTO genres (name) VALUES ('боевик');
INSERT INTO genres (name) VALUES ('триллер');
INSERT INTO genres (name) VALUES ('приключения');
INSERT INTO genres (name) VALUES ('аниме');
INSERT INTO genres (name) VALUES ('мультфильм');
INSERT INTO genres (name) VALUES ('семейный');
INSERT INTO genres (name) VALUES ('вестерн');



