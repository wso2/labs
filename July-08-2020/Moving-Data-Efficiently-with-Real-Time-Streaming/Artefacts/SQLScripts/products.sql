-- create database
create database sales;
go;

use sales;
go;

-- create categories table and populate
create table categories (
    id int,
    category varchar(30) not null,
    primary key (id)
);
go;

insert into categories values (1, 'personal cleaning');
go;
insert into categories values (2, 'personal care');
go;
insert into categories values (3, 'oral care');
go;
insert into categories values (4, 'hair care');
go;
insert into categories values (5, 'cosmetics');
go;

-- create products table and populate
create table products (
    id int,
    name varchar(100),
    category_id int,
    quantity int,
    primary key (id)
);
go;

insert into products values (1, 'mouthwash', 3, 75);
go;
insert into products values (2, 'bar soap', 1, 100);
go;
insert into products values (3, 'toothpaste', 3, 50);
go;
insert into products values (4, 'foundation', 5, 40);
go;
insert into products values (5, 'body wash', 1, 100);
go;
insert into products values (6, 'hand sanitizer', 1, 80);
go;
insert into products values (7, 'conditioner', 4, 120);
go;
insert into products values (8, 'shampoo', 4, 150);
go;
insert into products values (9, 'eye makeup', 5, 40);
go;
insert into products values (10, 'liquid soap', 1, 60);
go;

-- enable/disable cdc at the database level
exec sys.sp_cdc_enable_db;
go;

exec sys.sp_cdc_disable_db;
go;

-- enable/disable cdc at the table level
exec sys.sp_cdc_enable_table
    @source_schema = N'dbo',  
    @source_name   = N'products',  
    @role_name     = null,
    @supports_net_changes = 0;
go;

exec sys.sp_cdc_disable_table
    @source_schema = N'dbo',  
    @source_name   = N'products',
    @capture_instance = N'dbo_products';
go;

-- check cdc status
exec sys.sp_cdc_help_change_data_capture;
go;

-- debug
select sys.fn_cdc_get_max_lsn();
go;



