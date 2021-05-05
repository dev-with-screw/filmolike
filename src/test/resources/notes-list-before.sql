delete from note;

INSERT INTO Note (id, title, watched, estimate, user_id)
VALUES (1, 'Властелин колец - крутой фильм', true, 'GOOD', 1),
       (2, 'Хочу посмотреть Господин Никто', false, 'NOT_ESTIMATE', 1);