insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(comment, book_id)
values ('comment_1_1', 1), ('comment_1_2', 1),
       ('comment_2_1', 2), ('comment_2_2', 2),
       ('comment_3_1', 3), ('comment_3_2', 3)
        , ('comment_3_3', 3);

insert into users(login, password)
values('Pavel', 'PavelPass'),
      ('Vasia', 'VasiaPass')