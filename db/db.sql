DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS vote_log;

CREATE TABLE accounts (
                    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nickname VARCHAR(50),
                    password VARCHAR(255),
                    title VARCHAR(255),
                    body_data TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    recommended INT DEFAULT 0,
                    notrecommended INT DEFAULT 0
);

CREATE TABLE vote_log (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          account_id BIGINT NOT NULL,
                          ip_address VARCHAR(50) NOT NULL,
                          action ENUM('RECOMMEND', 'NOT_RECOMMEND') NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          UNIQUE(account_id, ip_address) -- 하나의 게시글에 대해 하나의 IP는 한 번만
);


SELECT * from accounts