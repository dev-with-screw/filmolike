INSERT INTO Film (id, name, description, genre, released)
VALUES (1, 'Властелин колец', 'One ring to rule them all', 'Fantasy', 2001),
       (2, 'Господин Никто', 'Описание', 'Фантастика', 2009);

INSERT INTO Note (id, title, watched, estimate, film_id)
VALUES (1, 'Властелин колец - крутой фильм', true, 4, 1),
       (2, 'Хочу посмотреть Господин Никто', false, 0, 2);

INSERT INTO Commentary (id,  commentary, note_id)
VALUES (1, 'Смотрел с Михой в кинотеатре Заря', 1),
       (2, 'Кресла были жесткие :(', 1);

