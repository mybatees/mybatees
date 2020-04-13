drop table if exists t_main;
create table t_main (
  main_id bigint primary key auto_increment,
  main_name varchar(20),
  t_one_one_id bigint,
  create_at datetime,
  update_at datetime
);

insert into t_main (main_id, main_name, t_one_one_id, create_at) values
(1001, 'main1', 2001, current_timestamp()),
(1002, 'main2', 2002, current_timestamp());

drop table if exists t_one;
create table t_one (
  one_id bigint,
  one_name varchar(20)
);

insert into t_one (one_id, one_name) values
(2001, 'one1'),
(2002, 'one2');

drop table if exists t_inverse_one;
create table t_inverse_one (
  inverse_one_id bigint,
  inverse_one_name varchar(20),
  t_main_main_id bigint
);

insert into t_inverse_one (inverse_one_id, inverse_one_name, t_main_main_id) values
(3001, 'inverseOne1', 1001),
(3002, 'inverseOne2', 1002);

drop table if exists t_foreign_one;
create table t_foreign_one (
  foreign_one_id bigint,
  foreign_one_name varchar(20)
);

insert into t_foreign_one (foreign_one_id, foreign_one_name) values
(4001, 'foreignOne1'),
(4002, 'foreignOne2');

drop table if exists t_main_foreign_one;
create table t_main_foreign_one (
  id bigint primary key auto_increment,
  t_main_main_id bigint,
  t_foreign_one_foreign_one_id bigint
);

insert into t_main_foreign_one (t_main_main_id, t_foreign_one_foreign_one_id) values
(1001, 4001),
(1002, 4002);

drop table if exists t_many;
create table t_many(
  many_id bigint,
  many_name varchar(20),
  t_main_main_id bigint
);

insert into t_many (many_id, many_name, t_main_main_id) values
(5001, 'many1', 1001),
(5002, 'many2', 1001),
(5003, 'many3', 1002),
(5004, 'many4', 1002);

drop table if exists t_foreign_many;
create table t_foreign_many (
  foreign_many_id bigint,
  foreign_many_name varchar(20)
);

insert into t_foreign_many (foreign_many_id, foreign_many_name) values
(6001, 'foreignMany1'),
(6002, 'foreignMany2'),
(6003, 'foreignMany3'),
(6004, 'foreignMany4');

drop table if exists t_main_foreign_many_list;
create table t_main_foreign_many_list (
  id bigint primary key auto_increment,
  t_main_main_id bigint,
  t_foreign_many_foreign_many_id bigint
);

insert into t_main_foreign_many_list (t_main_main_id, t_foreign_many_foreign_many_id) values
(1001, 6001),
(1001, 6002),
(1002, 6003),
(1002, 6004);