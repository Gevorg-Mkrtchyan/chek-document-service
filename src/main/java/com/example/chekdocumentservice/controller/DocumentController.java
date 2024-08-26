package com.example.chekdocumentservice.controller;

import com.example.chekdocumentservice.domain.entity.Document;
import com.example.chekdocumentservice.service.CrptApi;
import com.example.chekdocumentservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final CrptApi crptApi;
    private final String defaultSignature;

    public DocumentController(DocumentService documentService, CrptApi crptApi,@Value("${api.default.signature}") String defaultSignature) {
        this.documentService = documentService;
        this.crptApi = crptApi;
        this.defaultSignature = defaultSignature;
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Document>> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createDocument(@RequestBody Object document) {
        return crptApi.createDocument(document, defaultSignature);
    }
}
