create table poll
(
    id          bigserial primary key,
    name        varchar(255),
    start_date  date,
    end_date    date,
    description text
);

create table question
(
    id      bigserial primary key,
    content text,
    type    varchar(255),
    poll_id bigint
);

create table admin_role
(
    id       bigserial primary key,
    login    varchar(255),
    password varchar(255)
);

create table answer
(
    id          bigserial primary key,
    user_id     bigint,
    poll_id     bigint,
    question_id bigint,
    answer      text
);

insert into admin_role (login, password)
VALUES ('admin', 'aaddmmiin');

insert into poll (name, start_date, end_date, description)
VALUES ('Базовый опрос', '2020-01-01', '2023-01-01', 'Это наш первый опрос'),
       ('Базовый опрос номер 2', '2021-01-01', '2027-01-01', 'Это наш второй опрос');

insert into question (content, type, poll_id)
VALUES ('Что такое семья?', 'TEXT', 1),
       ('Женщина, которая родила ребенка - это: 1) мать, 2) бабушка, 2) тетка', 'SINGLE', 1),
       ('Членами семьи являются: 1) отец, 2) дед, 3) собака, 4) кошка', 'MULTIPLE', 1),
       ('Кого ты любишь больше? 1) маму, 2) папу, 3) кошку, 4) собаку', 'SINGLE', 2);

insert into answer (user_id, poll_id, question_id, answer)
VALUES (1, 1, 1, 'Это база'),
       (1, 1, 2, '1'),
       (1, 1, 3, '12'),
       (1, 2, 4, '1'),
       (2, 2, 4, '2');




