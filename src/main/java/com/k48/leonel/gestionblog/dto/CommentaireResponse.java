package com.k48.leonel.gestionblog.dto;

import java.time.LocalDateTime;

/**
 * Représentation d'un commentaire renvoyée par l'API.
 */
public record CommentaireResponse(
        Long id,
        String contenu,
        String auteur,
        LocalDateTime dateCreation,
        Long articleId
) {
}
