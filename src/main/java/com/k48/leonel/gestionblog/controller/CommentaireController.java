package com.k48.leonel.gestionblog.controller;

import com.k48.leonel.gestionblog.dto.CommentaireRequest;
import com.k48.leonel.gestionblog.dto.CommentaireResponse;
import com.k48.leonel.gestionblog.service.CommentaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Points d'entrée REST pour la gestion des commentaires d'un article.
 */
@RestController
@RequestMapping("/api/articles/{articleId}/commentaires")
@RequiredArgsConstructor
@Tag(name = "Commentaires", description = "Gestion des commentaires publiés sur un article")
public class CommentaireController {

    private final CommentaireService commentaireService;

    @PostMapping
    @Operation(summary = "Ajouter un commentaire", description = "Publie un commentaire sur l'article identifié.")
    public ResponseEntity<CommentaireResponse> ajouter(@PathVariable Long articleId,
                                                       @Valid @RequestBody CommentaireRequest request) {
        CommentaireResponse created = commentaireService.ajouter(articleId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Lister les commentaires d'un article", description = "Renvoie tous les commentaires de l'article, du plus récent au plus ancien.")
    public List<CommentaireResponse> listerParArticle(@PathVariable Long articleId) {
        return commentaireService.listerParArticle(articleId);
    }
}
