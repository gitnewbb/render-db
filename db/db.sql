create table accounts
(
    account_id int auto_increment primary key,
    nickname   varchar(255) not null unique
);

insert into accounts(nickname)
values
    ('one'),
    ('two'),
    ('three'),
    ('four'),
    ('five'),
    ('six'),
    ('seven'),
    ('eight'),
    ('nine'),
    ('ten');

