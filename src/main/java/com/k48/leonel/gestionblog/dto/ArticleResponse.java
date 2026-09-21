package com.k48.leonel.gestionblog.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Représentation d'un article renvoyée par l'API.
 */
public record ArticleResponse(
        Long id,
        String titre,
        String contenu,
        LocalDateTime datePublication,
        List<CommentaireResponse> commentaires
) {
}
