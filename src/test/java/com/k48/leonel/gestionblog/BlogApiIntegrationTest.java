package com.k48.leonel.gestionblog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration de l'API (controller -> service -> repository -> H2).
 * Parcourt le cycle de vie complet d'un article et de ses commentaires.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlogApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String articleJson(String titre) {
        return """
                {"titre": "%s", "contenu": "Contenu de l'article %s"}""".formatted(titre, titre);
    }

    @Test
    @DisplayName("POST /api/articles crée un article avec date de publication")
    void creerArticle() throws Exception {
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson("Premier article")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titre").value("Premier article"))
                .andExpect(jsonPath("$.datePublication").isNotEmpty())
                .andExpect(jsonPath("$.commentaires").isArray());
    }

    @Test
    @DisplayName("POST /api/articles sans titre renvoie 400")
    void creerArticleSansTitre() throws Exception {
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contenu\": \"sans titre\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/articles renvoie la liste des articles")
    void listerArticles() throws Exception {
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(articleJson("Article liste")));
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(articleJson("Article liste 2")));

        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/articles/{id} lit un article, 404 si inconnu")
    void lireArticle() throws Exception {
        String location = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson("Article a lire")))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Article a lire"));

        mockMvc.perform(get("/api/articles/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/articles/{id} met a jour un article")
    void mettreAJourArticle() throws Exception {
        String location = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson("Titre initial")))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson("Titre modifie")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Titre modifie"));
    }

    @Test
    @DisplayName("DELETE /api/articles/{id} supprime un article")
    void supprimerArticle() throws Exception {
        String location = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson("Article a supprimer")))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/articles/{id}/commentaires ajoute un commentaire et GET le liste")
    void ajouterEtListerCommentaires() throws Exception {
        String location = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson("Article commente")))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(post(location + "/commentaires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contenu\": \"Super article !\", \"auteur\": \"Leonel\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.contenu").value("Super article !"))
                .andExpect(jsonPath("$.auteur").value("Leonel"))
                .andExpect(jsonPath("$.articleId").isNumber());

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentaires.length()").value(1))
                .andExpect(jsonPath("$.commentaires[0].auteur").value("Leonel"));

        mockMvc.perform(get(location + "/commentaires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(post("/api/articles/99999/commentaires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contenu\": \"orphan\", \"auteur\": \"x\"}"))
                .andExpect(status().isNotFound());
    }
}
