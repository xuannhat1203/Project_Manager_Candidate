create database managerCandidate;
use managerCandidate;
CREATE TABLE candidate
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(20),
    experience  INT         DEFAULT 0,
    gender      VARCHAR(10),
    status      VARCHAR(20) DEFAULT 'active',
    description TEXT,
    dob         DATE,
    CHECK (gender IN ('Nam', 'Nữ'))
);

-- Technology Table
CREATE TABLE technology
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Recruitment Position Table
CREATE TABLE recruitment_position
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(100) NOT NULL,
    description   TEXT,
    minSalary     DECIMAL(12, 2) CHECK (minSalary >= 0),
    maxSalary     DECIMAL(12, 2),
    minExperience INT      DEFAULT 0,
    createdDate   DATETIME DEFAULT CURRENT_TIMESTAMP,
    expiredDate   DATE         NOT NULL
);


-- Application Table
CREATE TABLE application
(
    id                     INT PRIMARY KEY AUTO_INCREMENT,
    candidateId            INT          NOT NULL,
    recruitmentPositionId  INT          NOT NULL,
    cvUrl                  VARCHAR(255) NOT NULL,
    progress               ENUM ('pending', 'handling', 'interviewing', 'done') DEFAULT 'pending',
    interviewRequestDate   DATETIME,
    interviewRequestResult VARCHAR(100),
    interviewLink          VARCHAR(255),
    interviewTime          DATETIME,
    interviewResult        VARCHAR(50),
    interviewResultNote    TEXT,
    destroyAt              DATETIME,
    createAt               DATETIME                                             DEFAULT CURRENT_TIMESTAMP,
    updateAt               DATETIME                                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    destroyReason          TEXT,
    FOREIGN KEY (candidateId) REFERENCES candidate (id),
    FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position (id)
);
ALTER TABLE application
    ADD COLUMN isConfirmed BOOLEAN DEFAULT FALSE;

-- Candidate Technology
CREATE TABLE candidate_technology
(
    candidateId  INT NOT NULL,
    technologyId INT NOT NULL,
    PRIMARY KEY (candidateId, technologyId),
    FOREIGN KEY (candidateId) REFERENCES candidate (id),
    FOREIGN KEY (technologyId) REFERENCES technology (id)
);

-- Recruitment Position Technology
CREATE TABLE recruitment_position_technology
(
    recruitmentPositionId INT NOT NULL,
    technologyId          INT NOT NULL,
    PRIMARY KEY (recruitmentPositionId, technologyId),
    FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position (id),
    FOREIGN KEY (technologyId) REFERENCES technology (id)
);

-- Admin Account
CREATE TABLE accountAdmin
(
    email    VARCHAR(255),
    password VARCHAR(255)
);
insert into accountAdmin (email, password)
values ('xuannhatvn211@gmail.com', sha2('123', 256));
-- Login Check Table
CREATE TABLE isCheckLogin
(
    email    VARCHAR(255),
    password VARCHAR(255)
);

delimiter \\
create procedure get_account_admin()
begin
    select *from accountAdmin where email = 'xuannhatvn211@gmail.com';
end \\
delimiter \\;
drop procedure get_account_admin;
delimiter \\
create procedure get_list_candidate()
begin
    select *from candidate;
end \\
delimiter \\;
delimiter \\
create procedure get_candidate_pagination(p_limit int, p_offset int)
begin
    select *
    from candidate
    order by id
    limit p_limit offset p_offset;
end \\
delimiter ;

delimiter \\
create procedure insert_into_isLogin(email_in varchar(2555), password_in varchar(255))
begin
    insert into isCheckLogin(email, password)
    values (email_in, password_in);
end \\
delimiter \\;

delimiter \\
create procedure get_is_check_login()
begin
    select *from isCheckLogin;
end \\
delimiter \\;
delimiter \\
create procedure delete_is_check_login(email_in varchar(255))
begin
    delete from ischecklogin where email = email_in;
end \\
delimiter \\;

delimiter \\
create procedure register_candidate(
    in p_name varchar(100),
    in p_email varchar(100),
    in p_password varchar(255),
    in p_phone varchar(20),
    in p_experience int,
    in p_gender varchar(10),
    in p_description text,
    in p_dob date
)
begin
    if exists (select 1 from candidate where email = p_email) then
        signal sqlstate '45000'
            set message_text = 'Email đã tồn tại';
    else
        insert into candidate (name, email, password, phone, experience, gender, status, description, dob)
        values (p_name, p_email, SHA2(p_password, 256), p_phone, p_experience, p_gender, 'active', p_description,
                p_dob);
    end if;
end \\
delimiter \\;

delimiter \\
create procedure get_technology_pagination(
    in page_number int,
    in page_size int
)
begin
    set @offset = (page_number - 1) * page_size;
    set @size = page_size;

    set @sql = 'select * from technology limit ?, ?';
    prepare stmt from @sql;
    execute stmt using @offset, @size;
    deallocate prepare stmt;
end \\
delimiter ;

delimiter \\
create procedure add_technology(technology_name varchar(100))
begin
    insert into technology (name) values (technology_name);
end \\
create procedure update_technology(technology_id int, technology_name varchar(100))
begin
    update technology
    set name = technology_name
    where id = technology_id;
end \\
create procedure get_all_technology()
begin
    select * from technology;
end \\
create procedure delete_technology(technology_id int)
begin
    update technology
    set name = concat(name, '_delete')
    where id = technology_id;
end \\
delimiter \\;
insert into technology (name)
values ('Java'),
       ('Python'),
       ('C++'),
       ('JavaScript'),
       ('PHP'),
       ('Ruby'),
       ('Go'),
       ('Swift'),
       ('Kotlin'),
       ('TypeScript');
delimiter \\
create procedure get_candidate_pagination(
    page_number INT,
    page_size INT
)
begin
    set @offset := (page_number - 1) * page_size;
    set @limit := page_size;

    set @sql := 'SELECT * FROM candidate LIMIT ?, ?';
    prepare stmt from @sql;
    execute stmt using @offset, @limit;
    deallocate prepare stmt;
end \\
create procedure changestatusaccountcandidate(in candidate_id int)
begin
    update candidate
    set status = case
                     when status = 'active' then 'block'
                     else 'active'
        end
    where id = candidate_id;
end \\
create procedure resetpassword(in candidate_id int)
begin
    declare new_password varchar(20);
    set new_password = substring(replace(uuid(), '-', ''), 1, 8);
    update candidate
    set password = new_password
    where id = candidate_id;
end \\
create procedure findCandidateByName(name_in varchar(255))
begin
    select * from candidate where name like concat('%', name_in, '%');
end \\
create procedure filterToExperience(min int, max int)
begin
    select *from candidate where experience >= min && experience <= max;
end \\
create procedure filterToAge(in min_age int, in max_age int)
begin
    select *
    from candidate
    where timestampdiff(year, dob, curdate()) between min_age and max_age;
end \\
create procedure filterToGender(gender_in varchar(10))
begin
    select *
    from candidate
    where gender = gender_in;
end \\
create procedure filterToTechnology(in name_in varchar(255))
begin
    select c.*
    from candidate c
             join candidate_technology ct on c.id = ct.candidateid
             join technology t on t.id = ct.technologyid
    where t.name like CONCAT('%', name_in, '%');
end \\
delimiter \\;
drop procedure filterToTechnology;
INSERT INTO candidate (name, email, password, phone, experience, gender, status, description, dob)
VALUES ('Nguyen Van A', 'vana@example.com', 'hashedpassword1', '0123456789', 2, 'Nam', 'active', 'Backend dev',
        '1995-05-12'),
       ('Tran Thi B', 'thib@example.com', 'hashedpassword2', '0987654321', 4, 'Nữ', 'active', 'Frontend expert',
        '1993-08-20'),
       ('Le Van C', 'vanc@example.com', 'hashedpassword3', '0911122233', 1, 'Nam', 'inactive', 'Junior developer',
        '1998-01-15'),
       ('Pham Thi D', 'thid@example.com', 'hashedpassword4', '0998877665', 3, 'Nữ', 'active', 'Mobile dev',
        '1992-12-30'),
       ('Hoang Van E', 'vane@example.com', 'hashedpassword5', '0934567890', 5, 'Nam', 'active', 'Team leader',
        '1989-03-05'),
       ('Do Thi F', 'thif@example.com', 'hashedpassword6', '0900000001', 2, 'Nữ', 'inactive', 'Data analyst',
        '1996-04-11'),
       ('Nguyen Van G', 'vang@example.com', 'hashedpassword7', '0933002211', 0, 'Nam', 'active', 'Fresher dev',
        '2000-06-01'),
       ('Tran Van H', 'vanh@example.com', 'hashedpassword8', '0944223344', 6, 'Nam', 'active', 'DevOps engineer',
        '1987-09-19'),
       ('Ly Thi I', 'thii@example.com', 'hashedpassword9', '0988112233', 3, 'Nữ', 'active', 'QA/QC', '1994-02-22'),
       ('Pham Van J', 'vanj@example.com', 'hashedpassword10', '0955667788', 1, 'Nam', 'active', 'Support engineer',
        '1999-10-10');
INSERT INTO recruitment_position (name, description, minSalary, maxSalary, minExperience, expiredDate)
VALUES ('Backend Developer', 'Node.js backend', 1000, 2000, 2, '2025-06-30'),
       ('Frontend Developer', 'React + UI/UX', 1200, 2500, 3, '2025-07-15'),
       ('Fullstack Developer', 'Frontend + Backend', 1500, 3000, 4, '2025-08-10'),
       ('Mobile Developer', 'Flutter/React Native', 1000, 1800, 2, '2025-09-01'),
       ('Data Analyst', 'SQL + Excel', 800, 1600, 1, '2025-07-31'),
       ('DevOps Engineer', 'CI/CD, AWS', 1800, 3500, 5, '2025-10-01'),
       ('QA/QC Engineer', 'Manual & Auto testing', 900, 2000, 2, '2025-07-20'),
       ('Project Manager', 'Manage team', 2000, 4000, 5, '2025-09-15'),
       ('System Admin', 'Server & infra', 1300, 2700, 3, '2025-10-10'),
       ('Support Engineer', 'Handle tech support', 700, 1500, 1, '2025-07-25');
INSERT INTO application (candidateId, recruitmentPositionId, cvUrl, progress, interviewRequestDate,
                         interviewRequestResult, interviewLink, interviewTime, interviewResult, interviewResultNote,
                         destroyAt, destroyReason)
VALUES (1, 1, 'http://cv.com/a.pdf', 'handling', '2025-04-01 09:00:00', 'Accepted', 'https://zoom.us/a',
        '2025-04-05 10:00:00', 'Passed', 'Good', NULL, NULL),
       (2, 2, 'http://cv.com/b.pdf', 'pending', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
       (3, 3, 'http://cv.com/c.pdf', 'interviewing', '2025-04-02 14:00:00', 'Accepted', 'https://zoom.us/c',
        '2025-04-06 15:00:00', NULL, NULL, NULL, NULL),
       (4, 4, 'http://cv.com/d.pdf', 'done', '2025-04-03 10:00:00', 'Rejected', 'https://zoom.us/d',
        '2025-04-07 11:00:00', 'Failed', 'Lack of experience', NULL, NULL),
       (5, 5, 'http://cv.com/e.pdf', 'pending', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
       (6, 6, 'http://cv.com/f.pdf', 'handling', '2025-04-04 13:00:00', 'Accepted', 'https://zoom.us/f',
        '2025-04-08 14:00:00', NULL, NULL, NULL, NULL),
       (7, 7, 'http://cv.com/g.pdf', 'interviewing', '2025-04-05 15:00:00', 'Accepted', 'https://zoom.us/g',
        '2025-04-09 16:00:00', NULL, NULL, NULL, NULL),
       (8, 8, 'http://cv.com/h.pdf', 'done', '2025-04-06 16:00:00', 'Accepted', 'https://zoom.us/h',
        '2025-04-10 10:00:00', 'Passed', 'Excellent leader', NULL, NULL),
       (9, 9, 'http://cv.com/i.pdf', 'handling', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
       (10, 10, 'http://cv.com/j.pdf', 'pending', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO candidate_technology (candidateId, technologyId)
VALUES (1, 2),
       (1, 1),
       (2, 3),
       (3, 1),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9);

INSERT INTO recruitment_position_technology (recruitmentPositionId, technologyId)
VALUES (1, 2),
       (1, 1),
       (2, 3),
       (2, 1),
       (3, 1),
       (3, 2),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7);



DELIMITER //

CREATE PROCEDURE insert_application(
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(12,2),
    IN p_maxSalary DECIMAL(12,2),
    IN p_minExperience INT,
    IN p_expiredDate DATE,
    IN p_technologyIds VARCHAR(255)
)
BEGIN
    DECLARE v_recruitmentPositionId INT;
    DECLARE v_techId VARCHAR(10);
    DECLARE v_pos INT DEFAULT 1;
    DECLARE v_nextPos INT;
    DECLARE v_len INT;
    IF p_name IS NULL OR p_name = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tên vị trí tuyển dụng không được để trống';
    END IF;
    IF p_minSalary < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Lương tối thiểu không được nhỏ hơn 0';
    END IF;

    IF p_expiredDate < CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày hết hạn không được nhỏ hơn ngày hiện tại';
    END IF;
    INSERT INTO recruitment_position (name, description, minSalary, maxSalary, minExperience, createdDate, expiredDate)
    VALUES (p_name, p_description, p_minSalary, p_maxSalary, p_minExperience, CURRENT_DATE(), p_expiredDate);

    SET v_recruitmentPositionId = LAST_INSERT_ID();
    SET v_len = LENGTH(p_technologyIds);
    WHILE v_pos <= v_len DO
            SET v_nextPos = LOCATE(',', p_technologyIds, v_pos);
            IF v_nextPos = 0 THEN
                SET v_nextPos = v_len + 1;
            END IF;

            SET v_techId = SUBSTRING(p_technologyIds, v_pos, v_nextPos - v_pos);
            INSERT INTO recruitment_position_technology (recruitmentPositionId, technologyId)
            VALUES (v_recruitmentPositionId, CAST(v_techId AS UNSIGNED));

            SET v_pos = v_nextPos + 1;
        END WHILE;
END //

DELIMITER ;
delimiter //
create procedure delete_recruitment_position(
    in p_id int
)
begin
    declare v_count int;
    select count(*) into v_count from application where recruitmentPositionId = p_id;
    if v_count > 0 then
        update recruitment_position
        set name = concat(name, '_deleted')
        where id = p_id;
    else
        delete from recruitment_position where id = p_id;
    end if;
end //

create procedure get_recruitment_positions(
    in page_number int,
    in page_size int
)
begin
    set @offset = (page_number - 1) * page_size;
    set @size = page_size;
    set @sql = 'select * from recruitment_position limit ?, ?';
    prepare stmt from @sql;
    execute stmt using @offset, @size;
    deallocate prepare stmt;
end //
create procedure update_recruitment_position(
    in p_id int,
    in p_name varchar(100),
    in p_description text,
    in p_minSalary decimal(12, 2),
    in p_maxSalary decimal(12, 2),
    in p_minExperience int,
    in p_expiredDate date
)
begin
    if p_name is null or p_name = '' then
        signal sqlstate '45000' set message_text = 'Tên vị trí tuyển dụng không được để trống';
    end if;

    if p_minSalary < 0 then
        signal sqlstate '45000' set message_text = 'Lương tối thiểu không được nhỏ hơn 0';
    end if;

    if p_expiredDate < current_date() then
        signal sqlstate '45000' set message_text = 'Ngày hết hạn không được nhỏ hơn ngày hiện tại';
    end if;
    update recruitment_position
    set name = p_name,
        description = p_description,
        minSalary = p_minSalary,
        maxSalary = p_maxSalary,
        minExperience = p_minExperience,
        expiredDate = p_expiredDate
    where id = p_id;
end //
delimiter ;
delimiter \\
create procedure get_all_position()
select *from recruitment_position;
delimiter \\;

delimiter //
create procedure get_application_pagination(
    in page_number int,
    in page_size int
)
begin
    set @offset = (page_number - 1) * page_size;
    select * from application
    where destroyAt is null
    limit @offset, page_size;
end //
delimiter ;

delimiter \\
create procedure filterByResult(result_value varchar(255))
begin
    select * from application
    where interviewResult = result_value
      and destroyAt is null;
end;

delimiter \\;
delimiter \\
create procedure filterByProcess(process_value varchar(205))
begin
    select * from application
    where progress = process_value
      and destroyAt is null;
end \\
delimiter \\;
delimiter \\
create procedure destroyInterview(id_in int, reason_in varchar(255))
begin
    update application
    set destroyAt = current_date,
        destroyReason = reason_in
    where id = id_in;
end;
delimiter \\;
delimiter \\
create procedure showDetail(id_in int)
begin
    declare value_process varchar(255);
    select progress into value_process from application  where id = id_in;
    if value_process = 'pending' then
        update application
            set progress = 'handling'
        where id = id_in;
    end if;
end \\
delimiter \\;
delimiter \\
create procedure interviewing(
    id_in int,
    linkInterview varchar(255),
    interviewTime_in datetime
)
begin
    update application
    set interviewRequestDate = current_date(),
        interviewLink = linkInterview,
        interviewTime = interviewTime_in,
        progress = 'interviewing'
    where id = id_in;
end;
delimiter \\;
delimiter \\
create procedure completeInrerview(id_in int,interviewResultNote_in varchar(255), interviewResult_in varchar(255))
begin
    update application
        set interviewResultNote = interviewResultNote_in,
            interviewResult = interviewResult_in,
            progress = 'done'
        where id =id_in;
end \\
delimiter \\;
delimiter \\
create procedure get_all_application()
select *from application;
delimiter \\
delimiter  \\
create procedure get_detail_application(id_in int)
begin
    select *from application where id = id_in;
end \\
delimiter \\;


delimiter \\
create procedure change_personal_information(id_in int, name_in varchar(255),email_in varchar(255),phone_in varchar(25),experience_in int, gender_in varchar(10))
begin
    update candidate
        set name = name_in,
            email = email_in,
            phone = phone_in,
            experience = experience_in,
            gender = gender_in
        where id = id_in;
end \\
delimiter \\;
delimiter \\
create procedure change_password(id_in int,password_in varchar(25))
begin
    update candidate
        set password = password_in
    where id = id_in;
end \\
delimiter \\
DELIMITER $$

-- 1) Danh sách đơn ứng tuyển của 1 ứng viên, phân trang
CREATE PROCEDURE get_applications_by_candidate(
    IN p_candidate_id INT,
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE offset_val INT DEFAULT (p_page - 1) * p_page_size;

    SELECT a.id                                                    AS application_id,
           a.candidateId                                           AS candidate_id,
           a.recruitmentPositionId                                 AS recruitment_position_id,
           a.progress                                              AS progress,
           a.interviewResult                                       AS interview_result,
           a.createAt                                              AS created_at,
           a.destroyAt
    FROM application AS a
    WHERE a.candidateId = p_candidate_id
    ORDER BY a.createAt DESC
    LIMIT p_page_size OFFSET offset_val;
END$$
DELIMITER $$
drop procedure get_applications_by_candidate;
-- 2) Chi tiết 1 đơn ứng tuyển
CREATE PROCEDURE get_all_interview(
    IN p_application_id INT
)
BEGIN
    SELECT
        a.id                     AS application_id,
        a.candidateId            AS candidate_id,
        a.recruitmentPositionId  AS recruitment_position_id,
        rp.name                  AS recruitment_position_name,
        a.cvUrl                  AS cv_url,
        a.progress               AS progress,
        a.interviewRequestDate   AS interview_request_date,
        a.interviewRequestResult AS interview_request_result,
        a.interviewLink          AS interview_link,
        a.interviewTime          AS interview_time,
        a.interviewResult        AS interview_result,
        a.interviewResultNote    AS interview_result_note,
        a.isConfirmed            AS is_confirmed,
        a.destroyAt              as status,
        a.createAt               AS created_at,
        a.updateAt               AS updated_at
    FROM application AS a
             JOIN recruitment_position AS rp
                  ON a.recruitmentPositionId = rp.id
    WHERE a.id = p_application_id;
END$$
drop procedure get_all_interview;
DELIMITER ;

drop procedure get_all_interview;
DELIMITER \\

CREATE PROCEDURE get_application_details(IN application_id INT)
BEGIN
    SELECT
        a.id AS application_id,
        a.candidateId,
        rp.name AS recruitment_position_name,
        a.progress,
        a.interviewRequestDate,
        a.interviewRequestResult,
        a.interviewLink,
        a.interviewTime,
        a.interviewResult,
        a.interviewResultNote,
        IF(a.destroyAt IS NOT NULL, 'Đã hủy', 'Đang hoạt động') AS status
    FROM application a
             JOIN recruitment_position rp ON a.recruitmentPositionId = rp.id
    WHERE a.id = application_id;
END \\

DELIMITER ;

drop procedure get_all_interview;
delimiter \\
create procedure  confirm_interview_request(app_id int,result_in varchar(255))
begin
    update application
        set interviewRequestResult = result_in
    where id = app_id;
end \\
delimiter \\;
delimiter \\
create procedure get_candidate(email_in varchar(255))
begin
    select *from candidate where email = email_in;
end \\
delimiter \\;
delimiter \\
create procedure confirmInterview(id_in int)
update application
    set isConfirmed = true where id =id_in;
delimiter \\;

DELIMITER \\

CREATE PROCEDURE getactiveapplications(
    IN page INT,
    IN pageSize INT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (page - 1) * pageSize;

    SELECT
        id AS recruitment_position_id,
        name AS recruitment_position_name,
        description,
        minSalary,
        maxSalary,
        minExperience,
        createdDate,
        expiredDate
    FROM
        recruitment_position
    WHERE
        expiredDate >= CURRENT_DATE()
    ORDER BY createdDate DESC
    LIMIT pageSize OFFSET offsetValue;
END \\

DELIMITER ;


drop procedure getactiveapplications;

DELIMITER \\

CREATE PROCEDURE get_active_recruitment_positions(
    IN page INT,
    IN pageSize INT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (page - 1) * pageSize;

    SELECT
        id AS recruitment_position_id,
        name AS recruitment_position_name,
        description,
        minSalary,
        maxSalary,
        minExperience,
        createdDate,
        expiredDate
    FROM
        recruitment_position
    WHERE
        expiredDate >= CURRENT_DATE()
    ORDER BY name ASC
    LIMIT pageSize OFFSET offsetValue;
END \\

DELIMITER ;


delimiter //;
drop procedure get_active_recruitment_positions;
create procedure getapplicationdetail(application_id int)
begin
    select
        a.id as application_id,
        c.id as candidate_id,
        c.name as candidate_name,
        c.email as candidate_email,
        rp.id as recruitment_position_id,
        rp.name as recruitment_position_name,
        rp.description as recruitment_position_description,
        rp.minSalary,
        rp.maxSalary,
        rp.minExperience,
        a.cvUrl,
        a.progress,
        a.interviewRequestDate,
        a.interviewRequestResult,
        a.interviewLink,
        a.interviewTime,
        a.interviewResult,
        a.interviewResultNote,
        a.destroyAt,
        a.destroyReason,
        a.createAt,
        a.updateAt
    from
        application a
            join candidate c on a.candidateId = c.id
            join recruitment_position rp on a.recruitmentPositionId = rp.id
    where
        a.id = application_id;
end //;
delimiter //
create procedure applyforapplication(in application_id int, in new_cv_url varchar(255))
begin
    update application
    set
        cvUrl = new_cv_url,
        progress = 'pending'
    where id = application_id;
end //
delimiter ;
DELIMITER //
CREATE PROCEDURE applyForApplication(
    IN p_candidate_id INT,
    IN p_recruitment_position_id INT,
    IN p_cv_url VARCHAR(255)
)
BEGIN
    INSERT INTO application(candidateId, recruitmentPositionId, cvUrl, progress, createAt)
    VALUES (p_candidate_id, p_recruitment_position_id, p_cv_url, 'pending', NOW());
END //
DELIMITER ;
DELIMITER //

CREATE PROCEDURE insert_application(
    IN candidate_id INT,
    IN recruitment_position_id INT,
    IN cv_url VARCHAR(255)
)
BEGIN
    DECLARE existing_count INT;
    SELECT COUNT(*)
    INTO existing_count
    FROM application
    WHERE candidateId = candidate_id
      AND recruitmentPositionId = recruitment_position_id
      AND (
        progress != 'done'
            OR (destroyAt IS NULL AND isConfirmed = 0)
        );

    IF existing_count > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ứng viên đã ứng tuyển vị trí này (chưa hoàn tất hoặc chưa xác nhận).';
    ELSE
        INSERT INTO application (
            candidateId, recruitmentPositionId, cvUrl, progress, createAt, updateAt
        )
        VALUES (
                   candidate_id, recruitment_position_id, cv_url, 'pending', NOW(), NOW()
               );
    END IF;
END //
DELIMITER ;
drop procedure insert_application;
DELIMITER //
CREATE PROCEDURE get_recruitment_position_by_id(IN input_id INT)
BEGIN
    SELECT *
    FROM recruitment_position
    WHERE id = input_id;
END;
//
DELIMITER ;
DELIMITER //
CREATE PROCEDURE get_applications_by_candidate_id(IN input_candidate_id INT)
BEGIN
    SELECT *
    FROM application
    WHERE candidateId = input_candidate_id;
END;
//
DELIMITER ;
delimiter  \\
create procedure get_status_candidate(email_in varchar(255))
begin
    select status from candidate where email = email_in;
end \\
delimiter \\;
