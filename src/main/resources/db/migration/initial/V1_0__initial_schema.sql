CREATE TABLE IF NOT EXISTS genres
(
    genre_id  SERIAL PRIMARY KEY,
    genre_name      VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS countries
(
    country_id  SERIAL PRIMARY KEY,
    country_name        VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS movies
(
    movie_id    SERIAL PRIMARY KEY,
    movie_name_russian VARCHAR(200) UNIQUE NOT NULL,
    movie_name_native  VARCHAR(200) UNIQUE NOT NULL,
    movie_year_of_release INTEGER,
    movie_description VARCHAR(1000),
    movie_rating      DOUBLE PRECISION,
    movie_price       DOUBLE PRECISION,
    movie_picture_path VARCHAR(500) NOT NULL
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

CREATE TABLE IF NOT EXISTS users
(
    user_id  SERIAL PRIMARY KEY,
    user_nickname VARCHAR(200) UNIQUE NOT NULL,
    user_email    VARCHAR(50) UNIQUE NOT NULL,
    user_password VARCHAR(200) NOT NULL,
    user_salt     VARCHAR(500) UNIQUE NOT NULL,
    user_role     VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id  SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL,
    movie_id   INTEGER NOT NULL,
    review_text VARCHAR(1000) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);

INSERT INTO genres (genre_name) VALUES ('драма');
INSERT INTO genres (genre_name) VALUES ('криминал');
INSERT INTO genres (genre_name) VALUES ('фэнтези');
INSERT INTO genres (genre_name) VALUES ('детектив');
INSERT INTO genres (genre_name) VALUES ('мелодрама');
INSERT INTO genres (genre_name) VALUES ('биография');
INSERT INTO genres (genre_name) VALUES ('комедия');
INSERT INTO genres (genre_name) VALUES ('фантастика');
INSERT INTO genres (genre_name) VALUES ('боевик');
INSERT INTO genres (genre_name) VALUES ('триллер');
INSERT INTO genres (genre_name) VALUES ('приключения');
INSERT INTO genres (genre_name) VALUES ('аниме');
INSERT INTO genres (genre_name) VALUES ('мультфильм');
INSERT INTO genres (genre_name) VALUES ('семейный');
INSERT INTO genres (genre_name) VALUES ('вестерн');

INSERT INTO countries (country_name) VALUES ('США');
INSERT INTO countries (country_name) VALUES ('Франция');
INSERT INTO countries (country_name) VALUES ('Великобритания');
INSERT INTO countries (country_name) VALUES ('Италия');
INSERT INTO countries (country_name) VALUES ('Германия');
INSERT INTO countries (country_name) VALUES ('Япония');
INSERT INTO countries (country_name) VALUES ('Испания');





