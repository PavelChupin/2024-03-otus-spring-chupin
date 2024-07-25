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

-- ACL_SID – позволяет идентифицировать security identity (роль или пользователь):
-- id – суррогатный ключ
-- principal – определяет тип security identity (0/1 – роль/имя пользователя)
-- sid – содержит security identity
insert into acl_sid (id, principal, sid) values
(1, 1, 'admin'),
(2, 1, 'user'),
(3, 0, 'ROLE_LIBRARIAN');

-- ACL_CLASS – идентифицирует тип сущности:
-- id – суррогатный ключ
-- class – содержит имя Java класса
INSERT INTO acl_class (id, class) values
(1, 'ru.otus.hw.models.Book');

-- ACL_OBJECT_IDENTITY – содержит информацию о всех сущностях системы
-- id – суррогатный ключ
-- object_id_class – ссылка на ACL_CLASS
-- object_id_identity – идентификатор бизнес сущности
-- parent_object – ссылка на родительский
-- owner_sid – ссылка на ACL_SID определяющая владельца объекта
insert into acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) values
(1, 1, 1, NULL, 3, 0),
(2, 1, 2, NULL, 3, 0),
(3, 1, 3, NULL, 3, 0);

-- ACL_ENTRY – содержит права, назначенные для security identity на domain object
-- id – суррогатный ключ
-- acl_object_identity – ссылка на таблицу ACL_OBJECT_IDENTITY (что)
-- ace_order – порядок применения записи
-- sid – ссылка на ACL_SID (кому)
-- mask – маска, определяющая права
            -- 0 - read permission
            -- 1 – write permission
            -- 2 – create permission
            -- 3 – delete permission
            -- 4 – administer permission
-- granting – определяет тип назначения (1 – разрешающее, 0 - запрещающее)
-- audit_success – определяет будет ли записываться в лог информация об удачном применении ACE
-- audit_failure - определяет будет ли записываться в лог информация об не удачном применении ACE
insert into acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) values
-- Право библиотекаря на чтение книги 1
(1, 1, 1, 3, 1, 1, 1, 1),
-- Право библиотекаря на запись книги 1
(2, 1, 2, 3, 2, 1, 1, 1),
-- Право пользователя на чтение книги 1
(3, 1, 3, 2, 1, 1, 1, 1),
-- Право библиотекаря на чтение книги 2
(4, 2, 1, 3, 1, 1, 1, 1),
-- Право библиотекаря на запись книги 2
(5, 2, 2, 3, 2, 1, 1, 1),
-- Право пользователя на чтение книги 2
(6, 2, 3, 2, 1, 1, 1, 1),
-- Право библиотекаря на чтение книги 3
(7, 3, 1, 3, 1, 1, 1, 1),
-- Право библиотекаря на запись книги 3
(8, 3, 2, 3, 2, 1, 1, 1);

insert into users(login, password, role)
values('admin', 'admin', 'ADMIN'),
      ('user', 'user', 'USER'),
      ('lib', 'lib', 'LIBRARIAN');
