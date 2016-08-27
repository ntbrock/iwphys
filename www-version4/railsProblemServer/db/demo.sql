create database model_security_demo;
use model_security_demo;
source schema.sql;
grant all on model_security_demo.* to 'm_s_demo'@'localhost';
