CREATE DATABASE chek_api_integration;

CREATE TABLE document (
                          id SERIAL PRIMARY KEY,
                          description JSONB,
                          doc_id VARCHAR(255) UNIQUE NOT NULL,
                          doc_status VARCHAR(50),
                          doc_type VARCHAR(50),
                          import_request BOOLEAN,
                          owner_inn VARCHAR(50),
                          participant_inn VARCHAR(50),
                          producer_inn VARCHAR(50),
                          production_date DATE,
                          production_type VARCHAR(50),
                          reg_date DATE,
                          reg_number VARCHAR(255)
);

CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         certificate_document VARCHAR(255),
                         certificate_document_date DATE,
                         certificate_document_number VARCHAR(255),
                         owner_inn VARCHAR(50),
                         producer_inn VARCHAR(50),
                         production_date DATE,
                         tnved_code VARCHAR(50),
                         uit_code VARCHAR(50),
                         uitu_code VARCHAR(50),
                         document_id INTEGER REFERENCES document(id) ON DELETE CASCADE
);

CREATE INDEX idx_doc_id ON document (doc_id);
CREATE INDEX idx_producer_inn ON document (producer_inn);
CREATE INDEX idx_product_document_id ON product (document_id);
