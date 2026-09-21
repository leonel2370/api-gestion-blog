package com.k48.leonel.gestionblog.service;

import com.k48.leonel.gestionblog.dto.CommentaireRequest;
import com.k48.leonel.gestionblog.dto.CommentaireResponse;
import com.k48.leonel.gestionblog.dto.Mapper;
import com.k48.leonel.gestionblog.entity.Article;
import com.k48.leonel.gestionblog.entity.Commentaire;
import com.k48.leonel.gestionblog.exception.ResourceNotFoundException;
import com.k48.leonel.gestionblog.repository.ArticleRepository;
import com.k48.leonel.gestionblog.repository.CommentaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logique métier des commentaires : ajout sur un article et consultation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final ArticleRepository articleRepository;
    private final Mapper mapper;

    /** Ajoute un commentaire sur l'article identifié. */
    public CommentaireResponse ajouter(Long articleId, CommentaireRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable : id=" + articleId));

        Commentaire commentaire = Commentaire.builder()
                .contenu(request.contenu())
                .auteur(request.auteur())
                .article(article)
                .build();

        // Maintient la coherence des deux cotes de la relation bidirectionnelle
        article.getCommentaires().add(commentaire);

        return mapper.toResponse(commentaireRepository.save(commentaire));
    }

    /** Renvoie tous les commentaires d'un article (du plus récent au plus ancien). */
    @Transactional(readOnly = true)
    public List<CommentaireResponse> listerParArticle(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFoundException("Article introuvable : id=" + articleId);
        }
        return commentaireRepository.findByArticleIdOrderByDateCreationDesc(articleId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
