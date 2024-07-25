create table if not exists authors (
    id bigserial,
    full_name varchar(255) not null,
    primary key (id)
);

create table if not exists genres (
    id bigserial,
    name varchar(255) not null,
    primary key (id)
);

create table if not exists books (
    id bigserial,
    title varchar(255) not null,
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table if not exists comments (
    id bigserial,
    comment varchar(255) not null,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table if not exists users (
    id bigserial,
    login varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) not null,
    primary key (id)
);
/*
create table if not exists system_message (
    id integer not null,
    content varchar(255),
    primary key (id)
);
*/
-- ACL_SID – позволяет идентифицировать security identity (роль или пользователь):
-- id – суррогатный ключ
-- principal – определяет тип security identity (0/1 – роль/имя пользователя)
-- sid – содержит security identity
create table if not exists acl_sid (
    id bigint NOT NULL AUTO_INCREMENT,
    principal tinyint NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_1 UNIQUE (sid,principal)
    );

--create unique index acl_sid_principal_UNIQUE on acl_sid(sid, principal);

-- ACL_CLASS – идентифицирует тип сущности:
-- id – суррогатный ключ
-- class – содержит имя Java класса
create table if not exists acl_class (
    id bigint NOT NULL AUTO_INCREMENT,
    class varchar(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_2 UNIQUE (class)
    );

--create unique index acl_class_UNIQUE on acl_class(class);

-- ACL_OBJECT_IDENTITY – содержит информацию о всех сущностях системы
-- id – суррогатный ключ
-- object_id_class – ссылка на ACL_CLASS
-- object_id_identity – идентификатор бизнес сущности
-- parent_object – ссылка на родительский
-- owner_sid – ссылка на ACL_SID определяющая владельца объекта
create table if not exists acl_object_identity (
    id bigint NOT NULL AUTO_INCREMENT,
    object_id_class bigint NOT NULL,
    object_id_identity bigint NOT NULL,
    parent_object bigint DEFAULT NULL,
    owner_sid bigint DEFAULT NULL,
    entries_inheriting tinyint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_3 UNIQUE (object_id_class,object_id_identity)
    );

--create unique index acl_object_identity_UNIQUE on acl_object_identity(object_id_class, object_id_identity);


-- ACL_ENTRY – содержит права, назначенные для security identity на domain object
-- id – суррогатный ключ
-- acl_object_identity – ссылка на таблицу ACL_OBJECT_IDENTITY (что)
-- ace_order – порядок применения записи
-- sid – ссылка на ACL_SID (кому)
-- mask – маска, определяющая права
-- granting – определяет тип назначения (1 – разрешающее, 0 - запрещающее)
-- audit_success – определяет будет ли записываться в лог информация об удачном применении ACE
-- audit_failure - определяет будет ли записываться в лог информация об не удачном применении ACE
create table if not exists acl_entry (
    id bigint NOT NULL AUTO_INCREMENT,
    acl_object_identity bigint NOT NULL,
    ace_order int NOT NULL,
    sid bigint NOT NULL,
    mask int NOT NULL,
    granting tinyint NOT NULL,
    audit_success tinyint NOT NULL,
    audit_failure tinyint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity,ace_order)
    );

--create unique index acl_entry_UNIQUE on acl_entry(acl_object_identity, ace_order);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);