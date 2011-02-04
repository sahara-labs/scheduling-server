/*
 * Sahara 3.0 Scheduling Server MySQL Database Migration Script
 * (from version 2.x to 3.0) 
 *
 * @author Tejaswini Deshpande (tdeshpan)
 * @date 04th February 2011
 *
 * This migration script updates the Scheduling Server
 * version 2.x database (MySQL) to version 3.0
 * Version 3.0 adds some new tables and updates few existing tables
 * to add new columns.
*/ 

create table bookings (
    id bigint not null auto_increment unique,
    active bit not null,
    cancel_reason longtext,
    code_reference longtext,
    duration integer not null,
    end_time datetime not null,
    resource_type varchar(10) not null,
    start_time datetime not null,
    user_name varchar(50) not null,
    user_namespace varchar(50) not null,
    request_caps_id bigint,
    resource_permission_id bigint not null,
    rig_id bigint,
    rig_type_id bigint,
    session_id bigint,
    user_id bigint not null,
    primary key (id)
) type=InnoDB;

create table rig_log (
    id bigint not null auto_increment unique,
    new_state varchar(20) not null,
    old_state varchar(20) not null,
    reason varchar(255) not null,
    timestamp datetime not null,
    rig_id bigint not null,
    primary key (id)
) type=InnoDB;

create table rig_offline_schedule (
    id bigint not null auto_increment unique,
    active bit not null,
    end_time datetime,
    reason varchar(255),
    start_time datetime,
    rig_id bigint not null,
    primary key (id)
) type=InnoDB;


create table rig_type_information (
    id integer not null auto_increment unique,
    description longtext not null,
    institution varchar(255) not null,
    short_description longtext,
    usage_description longtext,
    rig_type_id bigint,
    primary key (id),
    unique (rig_type_id)
) type=InnoDB;

create table rig_type_media (
    id integer not null auto_increment unique,
    file_name varchar(255) not null,
    mime varchar(50) not null,
    rig_type_id bigint not null,
    primary key (id)
) type=InnoDB;

alter table bookings 
    add index FK7786033A4BD618F4 (rig_type_id), 
    add constraint FK7786033A4BD618F4 
    foreign key (rig_type_id) 
    references rig_type (id);

alter table bookings 
    add index FK7786033AC793D867 (request_caps_id), 
    add constraint FK7786033AC793D867 
    foreign key (request_caps_id) 
    references request_capabilities (id);

alter table bookings 
    add index FK7786033A8DD270B3 (rig_id), 
    add constraint FK7786033A8DD270B3 
    foreign key (rig_id) 
    references rig (id);

alter table bookings 
    add index FK7786033ADC2CA001 (user_id), 
    add constraint FK7786033ADC2CA001 
    foreign key (user_id) 
    references users (id);

alter table bookings 
    add index FK7786033A5C779673 (session_id), 
    add constraint FK7786033A5C779673 
    foreign key (session_id) 
    references session (id);

alter table bookings 
    add index FK7786033AAACFAD7E (resource_permission_id), 
    add constraint FK7786033AAACFAD7E 
    foreign key (resource_permission_id) 
    references resource_permission (id);

alter table rig_log 
    add index FK478B83958DD270B3 (rig_id), 
    add constraint FK478B83958DD270B3 
    foreign key (rig_id) 
    references rig (id);

alter table rig_offline_schedule 
    add index FK1EE690C28DD270B3 (rig_id), 
    add constraint FK1EE690C28DD270B3 
    foreign key (rig_id) 
    references rig (id);

alter table rig_type_information 
    add index FK707946F64BD618F4 (rig_type_id), 
    add constraint FK707946F64BD618F4 
    foreign key (rig_type_id) 
    references rig_type (id);

alter table rig_type_media 
    add index FK811DE12E4BD618F4 (rig_type_id), 
    add constraint FK811DE12E4BD618F4 
    foreign key (rig_type_id) 
    references rig_type (id);

alter table session add duration integer not null after code_reference;
alter table user_class add bookable bit not null after active;
alter table user_class add time_horizon int default '0' not null after queuable;
alter table users add email varchar(100) after id;
alter table users add first_name varchar(50) after email;
alter table users add last_name varchar(50) after first_name;
alter table resource_permission add use_activity_detection bit not null after id;
alter table resource_permission add maximum_bookings int default '0' not null after extension_duration;
alter table rig_type add set_up_time int default '0' not null after name;
alter table rig_type add tear_down_time int default '0' not null after set_up_time;
