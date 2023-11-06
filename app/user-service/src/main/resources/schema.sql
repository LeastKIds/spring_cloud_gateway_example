create table app_user (
    id bigint not null,
    name varchar(50) not null,
    email varchar(50) not null unique,
    userId varchar(255) not null unique,
    encryptedPwd varchar(255) not null unique
);