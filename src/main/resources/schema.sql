create table user
(
    user_idx bigint generated by default as identity,
    user_id  varchar(255),
    name     varchar(255),
    password varchar(255),
    reg_no   varchar(255),
    primary key (user_idx)
);

create table join_available
(
    available_idx bigint generated by default as identity,
    name          varchar(255),
    reg_no        varchar(255),
    primary key (available_idx)
);

create table scrap_one
(
    scrap_one_idx  bigint generated by default as identity,
    user_idx       bigint not null,
    com_no         varchar(255),
    end_date       varchar(255),
    income_cate    varchar(255),
    income_details varchar(255),
    pay_date       varchar(255),
    scrap_company  varchar(255),
    start_date     varchar(255),
    total_pay      bigint,
    primary key (scrap_one_idx),
    foreign key (user_idx) references user (user_idx)
);

create table scrap_two
(
    scrap_two_idx bigint generated by default as identity,
    user_idx      bigint not null,
    tax_amount    varchar(255),
    total_used    bigint,
    primary key (scrap_two_idx),
    foreign key (user_idx) references user (user_idx)
);

create table scrap_list
(
    result_idx bigint generated by default as identity,
    user_idx   bigint not null,
    company    varchar(255),
    err_msg    varchar(255),
    svc_cd     varchar(255),
    primary key (result_idx),
    foreign key (user_idx) references user (user_idx)
);

create table scrap_response
(
    result_idx    bigint generated by default as identity,
    user_idx      bigint not null,
    app_ver       varchar(255),
    host_nm       varchar(255),
    worker_req_dt varchar(255),
    worker_res_dt varchar(255),
    primary key (result_idx),
    foreign key (user_idx) references user (user_idx)
);