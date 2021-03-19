create sequence hibernate_sequence start 1 increment 1

create table commentary (
    id bigserial not null,
    commentary varchar(255),
    note_id int8,
    primary key (id)
);

create table film (
    id bigserial not null,
    description varchar(255),
    genre varchar(255),
    name varchar(255),
    released int4 not null,
    primary key (id)
);

create table note (
    id bigserial not null,
    changed timestamp,
    estimate varchar(255),
    title varchar(50) not null,
    watched boolean not null,
    film_id int8,
    user_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id bigserial not null,
    active boolean not null,
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

alter table if exists commentary
    add constraint commentary_user_fk
    foreign key (note_id) references note;

alter table if exists note
    add constraint note_film_fk
    foreign key (film_id) references film;

alter table if exists note
    add constraint note_user_kf
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;
