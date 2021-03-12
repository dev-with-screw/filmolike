DROP TABLE IF EXISTS Commentary;
DROP TABLE IF EXISTS Note;
DROP TABLE IF EXISTS Film;

CREATE TABLE Film (
    id bigint,
    name varchar(100),
    description varchar(100),
    genre varchar(100),
    released int,
    PRIMARY KEY (id)
);

CREATE TABLE Note (
    id bigint,
    title varchar(255),
    watched boolean,
    estimate integer,
    film_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (film_id) REFERENCES Film(id) ON DELETE RESTRICT
);

CREATE TABLE Commentary (
    id bigint,
    note_id bigint,
    commentary varchar(100),
    PRIMARY KEY (id),
    FOREIGN KEY (note_id) REFERENCES Note(id) ON DELETE RESTRICT
);

