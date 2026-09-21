package com.k48.leonel.gestionblog.dto;

import com.k48.leonel.gestionblog.entity.Article;
import com.k48.leonel.gestionblog.entity.Commentaire;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Conversions Entité &lt;-&gt; DTO pour les articles et les commentaires.
 */
@Component
public class Mapper {

    public ArticleResponse toResponse(Article article) {
        List<CommentaireResponse> commentaires = article.getCommentaires().stream()
                .map(this::toResponse)
                .toList();

        return new ArticleResponse(
                article.getId(),
                article.getTitre(),
                article.getContenu(),
                article.getDatePublication(),
                commentaires
        );
    }

    public CommentaireResponse toResponse(Commentaire commentaire) {
        return new CommentaireResponse(
                commentaire.getId(),
                commentaire.getContenu(),
                commentaire.getAuteur(),
                commentaire.getDateCreation(),
                commentaire.getArticle() != null ? commentaire.getArticle().getId() : null
        );
    }
}
