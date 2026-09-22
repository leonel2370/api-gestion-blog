package com.k48.leonel.gestionblog.repository;

import com.k48.leonel.gestionblog.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux données des commentaires.
 */
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    /**
     * Renvoie les commentaires d'un article triés du plus récent au plus ancien.
     */
    List<Commentaire> findByArticleIdOrderByDateCreationDesc(Long articleId);
}
