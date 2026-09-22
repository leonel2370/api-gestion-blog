package com.k48.leonel.gestionblog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Article de blog.
 * Un article possède un titre, un contenu, une date de publication
 * et une liste de commentaires.
 */
@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
    @Column(nullable = false, length = 200)
    private String titre;

    @NotBlank(message = "Le contenu est obligatoire")
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    /** Date de publication de l'article (renseignée automatiquement à la création). */
    @Column(nullable = false)
    private LocalDateTime datePublication;

    /** Commentaires attachés à cet article. */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Commentaire> commentaires = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (datePublication == null) {
            datePublication = LocalDateTime.now();
        }
    }
}
