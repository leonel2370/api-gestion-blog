package com.k48.leonel.gestionblog.service;

import com.k48.leonel.gestionblog.dto.ArticleRequest;
import com.k48.leonel.gestionblog.dto.ArticleResponse;
import com.k48.leonel.gestionblog.dto.Mapper;
import com.k48.leonel.gestionblog.entity.Article;
import com.k48.leonel.gestionblog.exception.ResourceNotFoundException;
import com.k48.leonel.gestionblog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logique métier des articles : création, lecture, mise à jour, suppression.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Mapper mapper;

    /** Crée un article ; la date de publication est posée par l'entité. */
    public ArticleResponse creer(ArticleRequest request) {
        Article article = Article.builder()
                .titre(request.titre())
                .contenu(request.contenu())
                .build();
        return mapper.toResponse(articleRepository.save(article));
    }

    /** Renvoie tous les articles, du plus récent au plus ancien. */
    @Transactional(readOnly = true)
    public List<ArticleResponse> listerTous() {
        return articleRepository.findAllByOrderByDatePublicationDesc().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /** Renvoie un article par son identifiant. */
    @Transactional(readOnly = true)
    public ArticleResponse obtenirParId(Long id) {
        return mapper.toResponse(trouverArticle(id));
    }

    /** Met à jour le titre et/ou le contenu d'un article. */
    public ArticleResponse mettreAJour(Long id, ArticleRequest request) {
        Article article = trouverArticle(id);
        article.setTitre(request.titre());
        article.setContenu(request.contenu());
        return mapper.toResponse(articleRepository.save(article));
    }

    /** Supprime un article et, par cascade, ses commentaires. */
    public void supprimer(Long id) {
        Article article = trouverArticle(id);
        articleRepository.delete(article);
    }

    private Article trouverArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable : id=" + id));
    }
}
