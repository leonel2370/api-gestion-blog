package com.k48.leonel.gestionblog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Commentaire publié sur un article.
 */
@Entity
@Table(name = "commentaires")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false, updatable = false)
    private String auteur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    /** Article auquel ce commentaire est rattaché. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @PrePersist
    void prePersist() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
    }
}
