-- Users table
CREATE TABLE users (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    last_login_at DATETIME NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME NULL,
    created_by  VARCHAR(36) NULL,
    updated_by  VARCHAR(36) NULL,
    deleted_by  VARCHAR(36) NULL
);

-- Companies table
CREATE TABLE companies (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME NULL,
    created_by  VARCHAR(36) NULL,
    updated_by  VARCHAR(36) NULL,
    deleted_by  VARCHAR(36) NULL
);

-- User emails table
CREATE TABLE user_emails (
    id            VARCHAR(36) PRIMARY KEY,
    user_id       VARCHAR(36) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    provider_id   VARCHAR(100) NULL,
    provider_name VARCHAR(50) NULL,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    DATETIME NULL,
    created_by    VARCHAR(36) NULL,
    updated_by    VARCHAR(36) NULL,
    deleted_by    VARCHAR(36) NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Job applications table
CREATE TABLE job_applications (
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL,
    company_id          VARCHAR(36) NOT NULL,
    position            VARCHAR(200) NOT NULL,
    status              ENUM('SAVED', 'APPLIED', 'ASSESSMENT', 'GROUP_DISCUSSION',
                            'INTERVIEW', 'FINAL_INTERVIEW', 'OFFER', 'ACCEPTED',
                            'REJECTED', 'WITHDRAWN', 'UNKNOWN') NOT NULL DEFAULT 'SAVED',
    job_url             TEXT NULL,
    source              VARCHAR(100) NULL,
    applied_date_time   DATETIME NULL,
    important_date_time DATETIME NULL,
    important_note      TEXT NULL,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at          DATETIME NULL,
    created_by          VARCHAR(36) NULL,
    updated_by          VARCHAR(36) NULL,
    deleted_by          VARCHAR(36) NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_user_emails_user_id ON user_emails(user_id);
CREATE INDEX idx_job_applications_user_id ON job_applications(user_id);
CREATE INDEX idx_job_applications_company_id ON job_applications(company_id);
CREATE INDEX idx_job_applications_status ON job_applications(status);