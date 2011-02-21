/*
 * Sahara 3.0 Scheduling Server PostgreSQL Database Migration Script
 * (from version 2.x to 3.0) 
 *
 * @author Tejaswini Deshpande (tdeshpan)
 * @date 15th February 2011
 *
 * This migration script updates the Scheduling Server
 * version 2.x database (PostgreSQL) to version 3.0
 * Version 3.0 adds some new tables and updates few existing tables
 * to add new columns.
*/ 

/* Create new tables */
    create table bookings (
        id int8 not null unique,
        active bool not null,
        cancel_reason varchar(1024),
        code_reference varchar(1024),
        duration int4 not null,
        end_time timestamp not null,
        resource_type varchar(10) not null,
        start_time timestamp not null,
        user_name varchar(50) not null,
        user_namespace varchar(50) not null,
        request_caps_id int8,
        resource_permission_id int8 not null,
        rig_id int8,
        rig_type_id int8,
        session_id int8,
        user_id int8 not null,
        primary key (id)
    );
    create table rig_log (
        id  bigserial not null unique,
        new_state varchar(20) not null,
        old_state varchar(20) not null,
        reason varchar(255) not null,
        timestamp timestamp not null,
        rig_id int8 not null,
        primary key (id)
    );

    create table rig_offline_schedule (
        id int8 not null unique,
        active bool not null,
        end_time timestamp not null,
        reason varchar(255) not null,
        start_time timestamp not null,
        rig_id int8 not null,
        primary key (id)
    );

    create table rig_type_information (
        id int4 not null unique,
        description varchar(2055) not null,
        institution varchar(255) not null,
        short_description varchar(2055),
        usage_description varchar(2055),
        rig_type_id int8,
        primary key (id),
        unique (rig_type_id)
    );

    create table rig_type_media (
        id int4 not null unique,
        file_name varchar(255) not null,
        mime varchar(50) not null,
        rig_type_id int8 not null,
        primary key (id)
    );
    alter table bookings 
        add constraint FK7786033A4BD618F4 
        foreign key (rig_type_id) 
        references rig_type;

    alter table bookings 
        add constraint FK7786033AC793D867 
        foreign key (request_caps_id) 
        references request_capabilities;

    alter table bookings 
        add constraint FK7786033A8DD270B3 
        foreign key (rig_id) 
        references rig;

    alter table bookings 
        add constraint FK7786033ADC2CA001 
        foreign key (user_id) 
        references users;

    alter table bookings 
        add constraint FK7786033A5C779673 
        foreign key (session_id) 
        references session;

    alter table bookings 
        add constraint FK7786033AAACFAD7E 
        foreign key (resource_permission_id) 
        references resource_permission;
    alter table rig_log 
        add constraint FK478B83958DD270B3 
        foreign key (rig_id) 
        references rig;

    alter table rig_offline_schedule 
        add constraint FK1EE690C28DD270B3 
        foreign key (rig_id) 
        references rig;

    alter table rig_type_information 
        add constraint FK707946F64BD618F4 
        foreign key (rig_type_id) 
        references rig_type;

    alter table rig_type_media 
        add constraint FK811DE12E4BD618F4 
        foreign key (rig_type_id) 
        references rig_type;

/* Alter existing tables to add new columns */        
alter table session add duration int4 not null default 0;
alter table user_class add bookable bool not null default false;
alter table user_class add time_horizon int  default '0' not null;
alter table users add email varchar(100);
alter table users add first_name varchar(50);
alter table users add last_name varchar(50);
alter table resource_permission add use_activity_detection bool not null default false;
alter table resource_permission add maximum_bookings int default '0' not null;
alter table rig_type add set_up_time int default '0' not null;
alter table rig_type add tear_down_time int default '0' not null;

/* Create new sequence */
create sequence hibernate_sequence;

/* Drop existing sequences */
drop sequence academic_permission_id_seq cascade;
drop sequence config_id_seq cascade; 
drop sequence request_capabilities_id_seq cascade;
drop sequence resource_permission_id_seq cascade;
drop sequence rig_capabilities_id_seq cascade;
drop sequence rig_id_seq cascade;
drop sequence rig_type_id_seq cascade;
drop sequence session_id_seq cascade;
drop sequence user_class_id_seq cascade;
drop sequence user_lock_id_seq cascade;
drop sequence users_id_seq cascade;

/* Change the datatype for "id" columns in existing tables */
alter table academic_permission alter column id type int4;
alter table config alter column id type int4;
alter table request_capabilities alter column id type int8;
alter table resource_permission alter column id type int8;
alter table rig alter column id type int8;
alter table rig_capabilities alter column id type int8;
alter table rig_type alter column id type int8;
alter table session alter column id type int8;
alter table user_class alter column id type int8;
alter table user_lock alter column id type int8;
alter table users alter column id type int8;

