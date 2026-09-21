package com.k48.leonel.gestionblog.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Requête d'ajout d'un commentaire sur un article.
 */
public record CommentaireRequest(
        @NotBlank(message = "Le contenu du commentaire est obligatoire")
        String contenu,

        @NotBlank(message = "L'auteur est obligatoire")
        String auteur
) {
}
