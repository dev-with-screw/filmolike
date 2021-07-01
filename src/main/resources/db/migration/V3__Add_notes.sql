insert into note(title, watched, estimate, changed, user_id)
values ('first note', 'true', 'GOOD', now(), 1),
       ('second note', 'false', 'NOT_ESTIMATE', now(), 1),
       ('third note', 'true', 'GOOD', now(), 1),
       ('first note for user u2', 'true', 'GOOD', now(), 2);