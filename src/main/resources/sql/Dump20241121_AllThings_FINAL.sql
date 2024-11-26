
use ssafyweb;

-- 최하위 참조 테이블부터 삭제
DROP TABLE IF EXISTS schedule_places;
DROP TABLE IF EXISTS answers;

-- 상위 참조 테이블 삭제
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS comments;

-- 상위 테이블 삭제
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS guguns;

-- 최상위 테이블 삭제
DROP TABLE IF EXISTS sidos;
DROP TABLE IF EXISTS contenttypes;
DROP TABLE IF EXISTS members;

USE ssafyweb;

-- members
CREATE TABLE members (
                         user_id INT AUTO_INCREMENT PRIMARY KEY,  -- 회원 고유번호, 자동 증가
                         name VARCHAR(50) NOT NULL,                -- 이름
                         gender ENUM('M', 'F') NOT NULL,           -- 성별
                         email VARCHAR(100) NOT NULL UNIQUE,       -- 이메일 (로그인 ID), 유일해야 함
                         password VARCHAR(255) NOT NULL,           -- 비밀번호
                         birth_date DATE NOT NULL,                 -- 생년월일
                         address VARCHAR(255) NOT NULL,            -- 주소
                         join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 가입일, 현재 시간 자동 저장
                         role ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',  -- 사용자 역할, 기본값 'ROLE_USER'
                         profile_image VARCHAR(255),               -- 프로필 이미지 (선택 사항)
                         status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'  -- 상태, 기본값 'ACTIVE'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO members (user_id, name, gender, email, password, birth_date, address, role)
VALUES
    (1, '김보라', 'F', 'hong@naver.com', '12345678', '1990-05-10', '서울특별시 강남구 역삼동 123-45', 'ROLE_USER'),
    (2, '김민지', 'F', 'admin@naver.com', '12345678', '1985-08-15', '서울특별시 마포구 합정동 678-90', 'ROLE_ADMIN'),
    (3, '김지수', 'M', 'comjisu0311@naver.com', '123', '2000-03-05', '서울특별시 성북구 123-45', 'ROLE_USER'),
    (4, '오지석', 'M', 'ohjiseok12@naver.com', '123', '1995-05-10', '부산광역시 서구 동구동 123-45', 'ROLE_USER'),
    (5, '이학준', 'M', 'hakjungood@gmail.com', '123', '1997-05-10', '서울특별시 양천구 신정 7동 123-45', 'ROLE_USER');

-- contenttypes
CREATE TABLE `contenttypes` (
                                `content_type_id` int NOT NULL,
                                `content_type_name` varchar(45) DEFAULT NULL,
                                PRIMARY KEY (`content_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `contenttypes` VALUES (12,'관광지'),(14,'문화시설'),(15,'축제공연행사'),(25,'여행코스'),(28,'레포츠'),(32,'숙박'),(38,'쇼핑'),(39,'음식점');

-- sidos
CREATE TABLE `sidos` (
                         `no` int NOT NULL AUTO_INCREMENT,
                         `sido_code` int DEFAULT NULL,
                         `sido_name` varchar(20) DEFAULT NULL,
                         PRIMARY KEY (`no`),
                         UNIQUE KEY `sido_code_UNIQUE` (`sido_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sidos` VALUES (1,1,'서울'),(2,2,'인천'),(3,3,'대전'),(4,4,'대구'),(5,5,'광주'),(6,6,'부산'),(7,7,'울산'),(8,8,'세종특별자치시'),(9,31,'경기도'),(10,32,'강원특별자치도'),(11,33,'충청북도'),(12,34,'충청남도'),(13,35,'경상북도'),(14,36,'경상남도'),(15,37,'전북특별자치도'),(16,38,'전라남도'),(17,39,'제주도');

-- guguns
CREATE TABLE `guguns` (
                          `no` int NOT NULL AUTO_INCREMENT,
                          `sido_code` int DEFAULT NULL,
                          `gugun_code` int DEFAULT NULL,
                          `gugun_name` varchar(20) DEFAULT NULL,
                          PRIMARY KEY (`no`),
                          UNIQUE KEY `unique_sido_gugun` (`sido_code`, `gugun_code`),
                          CONSTRAINT `guguns_sido_to_sidos_cdoe_fk` FOREIGN KEY (`sido_code`) REFERENCES `sidos` (`sido_code`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `guguns` VALUES (1,1,1,'강남구'),(2,1,2,'강동구'),(3,1,3,'강북구'),(4,1,4,'강서구'),(5,1,5,'관악구'),(6,1,6,'광진구'),(7,1,7,'구로구'),(8,1,8,'금천구'),(9,1,9,'노원구'),(10,1,10,'도봉구'),(11,1,11,'동대문구'),(12,1,12,'동작구'),(13,1,13,'마포구'),(14,1,14,'서대문구'),(15,1,15,'서초구'),(16,1,16,'성동구'),(17,1,17,'성북구'),(18,1,18,'송파구'),(19,1,19,'양천구'),(20,1,20,'영등포구'),(21,1,21,'용산구'),(22,1,22,'은평구'),(23,1,23,'종로구'),(24,1,24,'중구'),(25,1,25,'중랑구'),(26,2,1,'강화군'),(27,2,2,'계양구'),(28,2,3,'미추홀구'),(29,2,4,'남동구'),(30,2,5,'동구'),(31,2,6,'부평구'),(32,2,7,'서구'),(33,2,8,'연수구'),(34,2,9,'옹진군'),(35,2,10,'중구'),(36,3,1,'대덕구'),(37,3,2,'동구'),(38,3,3,'서구'),(39,3,4,'유성구'),(40,3,5,'중구'),(41,4,1,'남구'),(42,4,2,'달서구'),(43,4,3,'달성군'),(44,4,4,'동구'),(45,4,5,'북구'),(46,4,6,'서구'),(47,4,7,'수성구'),(48,4,8,'중구'),(49,4,9,'군위군'),(50,5,1,'광산구'),(51,5,2,'남구'),(52,5,3,'동구'),(53,5,4,'북구'),(54,5,5,'서구'),(55,6,1,'강서구'),(56,6,2,'금정구'),(57,6,3,'기장군'),(58,6,4,'남구'),(59,6,5,'동구'),(60,6,6,'동래구'),(61,6,7,'부산진구'),(62,6,8,'북구'),(63,6,9,'사상구'),(64,6,10,'사하구'),(65,6,11,'서구'),(66,6,12,'수영구'),(67,6,13,'연제구'),(68,6,14,'영도구'),(69,6,15,'중구'),(70,6,16,'해운대구'),(71,7,1,'중구'),(72,7,2,'남구'),(73,7,3,'동구'),(74,7,4,'북구'),(75,7,5,'울주군'),(76,8,1,'세종특별자치시'),(77,31,1,'가평군'),(78,31,2,'고양시'),(79,31,3,'과천시'),(80,31,4,'광명시'),(81,31,5,'광주시'),(82,31,6,'구리시'),(83,31,7,'군포시'),(84,31,8,'김포시'),(85,31,9,'남양주시'),(86,31,10,'동두천시'),(87,31,11,'부천시'),(88,31,12,'성남시'),(89,31,13,'수원시'),(90,31,14,'시흥시'),(91,31,15,'안산시'),(92,31,16,'안성시'),(93,31,17,'안양시'),(94,31,18,'양주시'),(95,31,19,'양평군'),(96,31,20,'여주시'),(97,31,21,'연천군'),(98,31,22,'오산시'),(99,31,23,'용인시'),(100,31,24,'의왕시'),(101,31,25,'의정부시'),(102,31,26,'이천시'),(103,31,27,'파주시'),(104,31,28,'평택시'),(105,31,29,'포천시'),(106,31,30,'하남시'),(107,31,31,'화성시'),(108,32,1,'강릉시'),(109,32,2,'고성군'),(110,32,3,'동해시'),(111,32,4,'삼척시'),(112,32,5,'속초시'),(113,32,6,'양구군'),(114,32,7,'양양군'),(115,32,8,'영월군'),(116,32,9,'원주시'),(117,32,10,'인제군'),(118,32,11,'정선군'),(119,32,12,'철원군'),(120,32,13,'춘천시'),(121,32,14,'태백시'),(122,32,15,'평창군'),(123,32,16,'홍천군'),(124,32,17,'화천군'),(125,32,18,'횡성군'),(126,33,1,'괴산군'),(127,33,2,'단양군'),(128,33,3,'보은군'),(129,33,4,'영동군'),(130,33,5,'옥천군'),(131,33,6,'음성군'),(132,33,7,'제천시'),(133,33,8,'진천군'),(134,33,9,'청원군'),(135,33,10,'청주시'),(136,33,11,'충주시'),(137,33,12,'증평군'),(138,34,1,'공주시'),(139,34,2,'금산군'),(140,34,3,'논산시'),(141,34,4,'당진시'),(142,34,5,'보령시'),(143,34,6,'부여군'),(144,34,7,'서산시'),(145,34,8,'서천군'),(146,34,9,'아산시'),(147,34,11,'예산군'),(148,34,12,'천안시'),(149,34,13,'청양군'),(150,34,14,'태안군'),(151,34,15,'홍성군'),(152,34,16,'계룡시'),(153,35,1,'경산시'),(154,35,2,'경주시'),(155,35,3,'고령군'),(156,35,4,'구미시'),(157,35,6,'김천시'),(158,35,7,'문경시'),(159,35,8,'봉화군'),(160,35,9,'상주시'),(161,35,10,'성주군'),(162,35,11,'안동시'),(163,35,12,'영덕군'),(164,35,13,'영양군'),(165,35,14,'영주시'),(166,35,15,'영천시'),(167,35,16,'예천군'),(168,35,17,'울릉군'),(169,35,18,'울진군'),(170,35,19,'의성군'),(171,35,20,'청도군'),(172,35,21,'청송군'),(173,35,22,'칠곡군'),(174,35,23,'포항시'),(175,36,1,'거제시'),(176,36,2,'거창군'),(177,36,3,'고성군'),(178,36,4,'김해시'),(179,36,5,'남해군'),(180,36,6,'마산시'),(181,36,7,'밀양시'),(182,36,8,'사천시'),(183,36,9,'산청군'),(184,36,10,'양산시'),(185,36,12,'의령군'),(186,36,13,'진주시'),(187,36,14,'진해시'),(188,36,15,'창녕군'),(189,36,16,'창원시'),(190,36,17,'통영시'),(191,36,18,'하동군'),(192,36,19,'함안군'),(193,36,20,'함양군'),(194,36,21,'합천군'),(195,37,1,'고창군'),(196,37,2,'군산시'),(197,37,3,'김제시'),(198,37,4,'남원시'),(199,37,5,'무주군'),(200,37,6,'부안군'),(201,37,7,'순창군'),(202,37,8,'완주군'),(203,37,9,'익산시'),(204,37,10,'임실군'),(205,37,11,'장수군'),(206,37,12,'전주시'),(207,37,13,'정읍시'),(208,37,14,'진안군'),(209,38,1,'강진군'),(210,38,2,'고흥군'),(211,38,3,'곡성군'),(212,38,4,'광양시'),(213,38,5,'구례군'),(214,38,6,'나주시'),(215,38,7,'담양군'),(216,38,8,'목포시'),(217,38,9,'무안군'),(218,38,10,'보성군'),(219,38,11,'순천시'),(220,38,12,'신안군'),(221,38,13,'여수시'),(222,38,16,'영광군'),(223,38,17,'영암군'),(224,38,18,'완도군'),(225,38,19,'장성군'),(226,38,20,'장흥군'),(227,38,21,'진도군'),(228,38,22,'함평군'),(229,38,23,'해남군'),(230,38,24,'화순군'),(231,39,1,'남제주군'),(232,39,2,'북제주군'),(233,39,3,'서귀포시'),(234,39,4,'제주시');

-- boards
CREATE TABLE `boards` (
                          `article_no` int NOT NULL AUTO_INCREMENT,
                          `user_id` varchar(50) NOT NULL,
                          `subject` varchar(255) NOT NULL,
                          `content` text NOT NULL,
                          `hit` int NOT NULL,
                          `register_time` datetime NOT NULL,
                          PRIMARY KEY (`article_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `boards` VALUES (1,'ssafy','첫 번째 게시글 업데이트','이것은 첫 번째 게시글의 새로운 내용입니다.',27,'2024-10-01 10:00:00'),(2,'ssafy','두 번째 게시글 업데이트','이것은 두 번째 게시글의 새로운 내용입니다.',14,'2024-10-02 11:15:00'),(3,'ssafy','세 번째 게시글 업데이트','이것은 세 번째 게시글의 새로운 내용입니다.',32,'2024-10-03 12:30:00'),(4,'ssafy','네 번째 게시글 업데이트','이것은 네 번째 게시글의 새로운 내용입니다.',8,'2024-10-04 13:00:00'),(5,'ssafy','다섯 번째 게시글 업데이트','이것은 다섯 번째 게시글의 새로운 내용입니다.',23,'2024-10-05 14:45:00'),(6,'ssafy','여섯 번째 게시글 업데이트','이것은 여섯 번째 게시글의 새로운 내용입니다.',19,'2024-10-06 15:10:00'),(7,'ssafy','일곱 번째 게시글 업데이트','이것은 일곱 번째 게시글의 새로운 내용입니다.',5,'2024-10-07 16:25:00'),(8,'ssafy','여덟 번째 게시글 업데이트','이것은 여덟 번째 게시글의 새로운 내용입니다.',38,'2024-10-08 17:55:00'),(9,'ssafy','아홉 번째 게시글 업데이트','이것은 아홉 번째 게시글의 새로운 내용입니다.',11,'2024-10-09 18:20:00'),(10,'ssafy','열 번째 게시글 업데이트','이것은 열 번째 게시글의 새로운 내용입니다.',30,'2024-10-10 19:10:00'),(11,'ssafy','열한 번째 게시글 업데이트','이것은 열한 번째 게시글의 새로운 내용입니다.',24,'2024-10-11 20:45:00'),(12,'ssafy','열두 번째 게시글 업데이트','이것은 열두 번째 게시글의 새로운 내용입니다.',16,'2024-10-12 21:35:00'),(13,'ssafy','열세 번째 게시글 업데이트','이것은 열세 번째 게시글의 새로운 내용입니다.',4,'2024-10-13 22:10:00'),(14,'ssafy','열네 번째 게시글 업데이트','이것은 열네 번째 게시글의 새로운 내용입니다.',36,'2024-10-14 23:05:00'),(15,'ssafy','열다섯 번째 게시글 업데이트','이것은 열다섯 번째 게시글의 새로운 내용입니다.',20,'2024-10-15 10:50:00'),(16,'ssafy','열여섯 번째 게시글 업데이트','이것은 열여섯 번째 게시글의 새로운 내용입니다.',29,'2024-10-16 11:20:00'),(17,'ssafy','열일곱 번째 게시글 업데이트','이것은 열일곱 번째 게시글의 새로운 내용입니다.',13,'2024-10-17 12:05:00');

-- notices
CREATE TABLE notices (
                         notice_id INT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         content TEXT,
                         user_id INT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         image_url VARCHAR(255)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO notices (title, content, user_id, image_url)
VALUES
    ('디지털 관광주민증 전용 크리스마스 열차 여행상품', '다가오는 크리스마스, 다양한 이벤트 참여하고 혜택 챙겨가세요', 2, 'https://ifh.cc/g/OwSt4M.jpg'),
    ('대한민국 구석구석 만족도 조사 이벤트', '이벤트 참여해주시면 감사하겠습니다', 2, 'https://ifh.cc/g/WTQpq6.jpg');

-- comments
CREATE TABLE comments (
                          comment_id INT AUTO_INCREMENT PRIMARY KEY,
                          author_id INT NOT NULL,
                          attraction_id INT NOT NULL,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          content TEXT NOT NULL,
                          FOREIGN KEY (author_id) REFERENCES members(user_id) ON DELETE CASCADE,
                          FOREIGN KEY (attraction_id) REFERENCES attractions(no) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO comments (author_id, attraction_id, content)
VALUES
    (1, 17066, '지난번 친구들이랑 갔었는데 너무 좋았어요 다음에 또 오고 싶네요'),
    (3, 17066, '부산가면 또 바다죠. 해운대 만한 곳이 없습니다'),
    (4, 17066, '제가 부산사람이지만 역시 부산은 해운대입니다'),
    (5, 17066, '여기에 진짜 맛있는 국밥집이 있어요. 강추입니다 강추');


-- favorites 
CREATE TABLE favorites (
                           favorite_id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT NOT NULL,
                           attraction_id INT NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES members(user_id) ON DELETE CASCADE,
                           FOREIGN KEY (attraction_id) REFERENCES attractions(no) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO favorites (user_id, attraction_id) VALUES
    (1, 17001),
    (1, 17236),
    (1, 17880),
    (1, 17933),
    (1, 17058),
    (1, 16755),
    (1, 17060),
    (1, 19120),
    (1, 17061),
    (1, 16830),
    (1, 16935),
    (1, 18455);

-- questions
CREATE TABLE questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY, -- 질문 고유 ID
    user_id INT NOT NULL,                       -- 질문 작성자 ID
    title VARCHAR(255) NOT NULL,                   -- 질문 제목
    content TEXT NOT NULL,                         -- 질문 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 질문 작성 시간
    is_answered BOOLEAN DEFAULT FALSE NOT NULL,     -- 답변 여부
    CONSTRAINT fk_questions_user FOREIGN KEY (user_id) REFERENCES members(user_id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE answers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,      -- 답변 고유 ID
    question_id INT NOT NULL,                   -- 질문 ID
    user_id INT NOT NULL,                       -- 답변 작성자 ID (관리자)
    content TEXT NOT NULL,                         -- 답변 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 답변 작성 시간
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO questions (user_id, title, content)
VALUES 
(1, 'AI 코디 추천 관련 질문입니다.', '되게 효율적으로 잘 사용하고 있는데 성별 선택과 같이 좀 더 옵션적인 부분 추가 해주셨으면 좋겠습니다. 혹시라도 추가 예정이시라면 나오는 기간 알 수 있을까요?'),
(1, '추가되지 않는 관광지에 대한 질문입니다.', '현재 새로 생긴 관광지에 대해서는 정보가 없는데 따로 추가하지 않는 이유가 있을까요? 궁금합니다.');

INSERT INTO answers (question_id, user_id, content)
VALUES 
(1, 2, '그 부분에 대해서는 지금 담당자와 협의하에 추가할 예정입니다. 추후 업데이트 시에 공지로 알려드리겠습니다.'),
(2, 2, '현재 추가되는 관광지에 있어서는 1달에 1번 데이터 갱신중에 있습니다. 금방 업데이트가 될 것이니 걱정하지 마세요');

-- schedules 
CREATE TABLE schedules (
                           schedule_id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT NOT NULL,
                           title VARCHAR(255) NOT NULL,
                           memo TEXT,
                           total_cost INT DEFAULT 0,
                           start_date DATE NOT NULL,
                           end_date DATE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           CONSTRAINT fk_member_schedule FOREIGN KEY (user_id) REFERENCES members(user_id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO schedules (user_id, title, memo, total_cost, start_date, end_date)
VALUES
    (1, '부산 2박 3일 여행', '부산에서의 힐링 여행 일정', 400000, '2024-11-28', '2024-11-30'),
    (1, '강릉 2박 3일 여행', '강릉에서의 힐링 여행 일정', 300000, '2024-12-01', '2024-12-03'),
    (1, '가평 1박 2일 여행', '가평에서의 힐링 여행 일정', 150000, '2024-12-02', '2024-12-03');

-- schedule_places 
CREATE TABLE schedule_places (
                                 place_id INT AUTO_INCREMENT PRIMARY KEY,
                                 schedule_id INT NOT NULL,
                                 attraction_id INT NOT NULL,
                                 visit_time TIMESTAMP NOT NULL,
                                 memo TEXT,
                                 cost INT DEFAULT 0,
                                 visit_order INT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO schedule_places (schedule_id, attraction_id, visit_time, memo, cost, visit_order) 
VALUES
    (1, 16755, '2024-11-28 09:00:00', '광안리 해수욕장에서 아침 산책', 0, 1),
    (1, 16846, '2024-11-28 12:00:00', '해운대 미포 끝집에서 점심 식사', 100000, 2),
    (1, 17055, '2024-11-28 15:00:00', '해동 용궁사 방문', 0, 3),
    (1, 17045, '2024-11-29 09:00:00', '태종대에서 전망대 관람', 0, 1),
    (1, 17842, '2024-11-29 12:00:00', '남포동 국제시장 탐방 및 점심', 100000, 2),
    (1, 16923, '2024-11-29 15:00:00', '송도 구름다리와 해상 케이블카 체험', 100000, 3),
    (1, 16864, '2024-11-30 09:00:00', '감천문화마을 산책 및 사진 촬영', 0, 1),
    (1, 18061, '2024-11-30 12:00:00', '자갈치 시장에서 해산물 점심', 100000, 2),
    (1, 16899, '2024-11-30 15:00:00', '부산 타워에서 야경 감상', 0, 3);

INSERT INTO schedule_places (schedule_id, attraction_id, visit_time, memo, cost, visit_order) 
VALUES
    (2, 21930, '2024-12-01 09:00:00', '강릉 3.1운동 기념공원', 100000, 1),
    (2, 10042, '2024-12-02 15:00:00', '강릉생태막회집', 100000, 1),
    (2, 10043, '2024-12-03 09:00:00', '강릉형제장칼국수', 100000, 1);

INSERT INTO schedule_places (schedule_id, attraction_id, visit_time, memo, cost, visit_order) 
VALUES
    (3, 20269, '2024-12-02 09:00:00', '가평 영양잣마을', 100000, 1),
    (3, 20268, '2024-12-02 15:00:00', '가평 연하리향나무', 100000, 1),
    (3, 20255, '2024-12-03 09:00:00', '황가평양냉면', 0, 1);
