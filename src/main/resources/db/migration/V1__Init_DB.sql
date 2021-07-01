create sequence hibernate_sequence start 1 increment 1;

create table note (
    id bigserial not null,
    changed timestamp,
    estimate varchar(255),
    title varchar(50) not null,
    watched boolean not null,
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

alter table if exists note
    add constraint note_user_kf
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;


