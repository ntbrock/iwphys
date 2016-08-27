create table users (
	id		integer unsigned not null auto_increment primary key,
  	login		varchar(40) not null,
	name		varchar(128) not null,
	admin		integer(1) not null default 0,
	activated	integer(1) not null default 0,
	email		varchar(80) not null,
  	cypher		text not null,
	salt		char(40) not null,
	token		char(10) not null,
	token_expiry	timestamp not null,
	created_on	timestamp not null,
	updated_on	timestamp not null,
	lock_version	integer not null default 0,
	index (login),
	index (email)
);
