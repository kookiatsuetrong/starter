create database base default charset 'utf8';
create user alice identified with mysql_native_password by 'bob';
grant all on base.* to alice;
use base;
create table settings(
	code     integer unique not null auto_increment,
    name     character varying(200) unique not null,
    value    character varying(200)
);

insert into settings(name,value) values('base-domain', 'http://127.0.0.1:60000');
insert into settings(name,value) values('platform-name',  'Platform Name');

create table members(
	code         integer unique not null auto_increment,
	email        character varying(200) unique not null,
	first_name   character varying(200) not null,
	family_name  character varying(200) not null,
	password     character varying(200) not null,
	status       character varying(200) not null default 'registered',
	phone        character varying(200)
);

insert into members(email,first_name,family_name,password)
	values('user@email.com', 'User', 'Family', upper(sha2('user123', 512)));

create table activates(
	code     integer unique not null auto_increment,
	member   integer not null,
	secret   character varying(200) not null,
	created  timestamp default now()
);

create table resets(
	code     integer unique not null auto_increment,
	member   integer not null,
	secret   character varying(200) not null,
	created  timestamp default now()
);
