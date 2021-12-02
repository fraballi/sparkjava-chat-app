DROP TABLE IF EXISTS UserResource;
DROP TABLE IF EXISTS MessageResource;
DROP TABLE IF EXISTS Content;

CREATE TABLE IF NOT EXISTS UserResource
(
    id       integer primary key,
    username varchar(25) not null unique,
    password varchar(25) not null
);

CREATE TABLE IF NOT EXISTS MessageResource
(
    id        integer primary key,
    sender    integer     not null,
    recipient integer     not null,
    timestamp varchar(50) not null,
    foreign key (sender) references UserResource (id),
    foreign key (recipient) references UserResource (id)
);

CREATE TABLE IF NOT EXISTS Content
(
    id        integer primary key,
    messageId integer     not null,
    type      varchar(10) not null,
    text      text        not null,
    foreign key (messageId) references MessageResource (id) on delete cascade
);
