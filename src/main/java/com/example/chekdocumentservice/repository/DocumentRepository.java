package com.example.chekdocumentservice.repository;

import com.example.chekdocumentservice.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}