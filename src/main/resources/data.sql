$2a$08$6c/..R0wL.Vy0JtE6yrTIuq45sRR/rWnvwFTsIbvmeWPyAa0fXWBO
$2a$08$9kskP6AygP2gWUmMRYBYvecfcvT73FhWdnBFPPDHlMLI4P3RJ55cu

insert into note (id, changed, estimate, title, user_id, watched)
values (5, '2021-03-18 20:14:17.539', 'POOR', 'test note', 1, true);

INSERT INTO Film (id, name, description, genre, released)
VALUES (1, 'Властелин колец', 'One ring to rule them all', 'Fantasy', 2001),
       (2, 'Господин Никто', 'Описание', 'Фантастика', 2009);

INSERT INTO Note (id, title, watched, estimate, film_id)
VALUES (1, 'Властелин колец - крутой фильм', true, 4, 1),
       (2, 'Хочу посмотреть Господин Никто', false, 0, 2);

INSERT INTO Commentary (id,  commentary, note_id)
VALUES (1, 'Смотрел с Михой в кинотеатре Заря', 1),
       (2, 'Кресла были жесткие :(', 1);

