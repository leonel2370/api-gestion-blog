package com.k48.leonel.gestionblog.repository;

import com.k48.leonel.gestionblog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux données des articles.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /** Tous les articles triés du plus récent au plus ancien. */
    List<Article> findAllByOrderByDatePublicationDesc();
}
