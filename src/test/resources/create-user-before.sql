delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
(1, 'u', '$2a$08$6c/..R0wL.Vy0JtE6yrTIuq45sRR/rWnvwFTsIbvmeWPyAa0fXWBO', true);

insert into user_role(user_id, roles) values
(1, 'USER');
