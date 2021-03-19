insert into usr (id, username, password, active)
    values (1, 'u', '$2a$08$9kskP6AygP2gWUmMRYBYvecfcvT73FhWdnBFPPDHlMLI4P3RJ55cu', true);

insert into user_role (user_id, roles)
    values (1, 'USER');