package com.k48.leonel.gestionblog.controller;

import com.k48.leonel.gestionblog.dto.ArticleRequest;
import com.k48.leonel.gestionblog.dto.ArticleResponse;
import com.k48.leonel.gestionblog.service.ArticleService;
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
 * Points d'entrée REST pour la gestion des articles.
 */
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Gestion des articles du blog")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @Operation(summary = "Créer un article", description = "Crée un article avec son titre et son contenu. La date de publication est renseignée automatiquement.")
    public ResponseEntity<ArticleResponse> creer(@Valid @RequestBody ArticleRequest request) {
        ArticleResponse created = articleService.creer(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Lister tous les articles", description = "Renvoie tous les articles, du plus récent au plus ancien.")
    public List<ArticleResponse> listerTous() {
        return articleService.listerTous();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire un article", description = "Renvoie l'article identifié avec ses commentaires.")
    public ArticleResponse obtenirParId(@PathVariable Long id) {
        return articleService.obtenirParId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un article", description = "Met à jour le titre et le contenu de l'article identifié.")
    public ArticleResponse mettreAJour(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        return articleService.mettreAJour(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un article", description = "Supprime l'article identifié ainsi que ses commentaires.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        articleService.supprimer(id);
    }
}
