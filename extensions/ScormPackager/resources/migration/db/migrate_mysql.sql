
 create table bookings (
        id bigint not null auto_increment unique,
        active bit not null,
        cancel_reason longtext,
        duration integer not null,
        resource_id bigint,
        resource_name longtext,
        resource_type varchar(10) not null,
        start_time datetime not null,
        user_name varchar(50) not null,
        user_namespace varchar(50) not null,
        session_id bigint,
        user_id bigint,
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

 alter table resource_permission add use_activity_detection bit not null after id;

 alter table rig_log change new_state new_state varchar(20) not null;

 alter table rig_log change old_state old_state varchar(20) not null;

 alter table rig_log change reason reason longtext not null;

 alter table session change assigned_rig_name assigned_rig_name varchar(20);

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

