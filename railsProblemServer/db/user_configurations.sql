create table user_configurations (
	id		integer unsigned not null auto_increment primary key,
	email_confirmation tinyint unsigned not null default 1,
        email_sender	text not null,
	created_on	timestamp not null,
	updated_on	timestamp not null
);
