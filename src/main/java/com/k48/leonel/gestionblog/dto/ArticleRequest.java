package com.k48.leonel.gestionblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Requête de création / mise à jour d'un article.
 */
public record ArticleRequest(
        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
        String titre,

        @NotBlank(message = "Le contenu est obligatoire")
        String contenu
) {
}
