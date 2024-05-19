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
    book_id bigint references books (id),
    primary key (id)
);