package com.example.chekdocumentservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Description description;

    @Column(name = "doc_id", unique = true, nullable = false)
    private String docId;

    @Column(name = "doc_status")
    private String docStatus;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "import_request")
    private Boolean importRequest;

    @Column(name = "owner_inn")
    private String ownerInn;

    @Column(name = "participant_inn")
    private String participantInn;

    @Column(name = "producer_inn")
    private String producerInn;

    @Column(name = "production_date")
    private LocalDate productionDate;

    @Column(name = "production_type")
    private String productionType;

    @Column(name = "reg_date")
    private LocalDate regDate;

    @Column(name = "reg_number")
    private String regNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private List<Product> products;

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class Description {
        @Column(name = "participant_inn")
        private String participantInn;
    }
}
