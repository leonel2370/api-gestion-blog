# API de Gestion d'un Blog (Articles & Commentaires)

API REST développée avec **Spring Boot** permettant de publier des articles et d'ajouter des commentaires (Projet 4 — Java Spring Boot).

## Fonctionnalités

- Créer un article (titre, contenu, date de publication automatique)
- Lire tous les articles ou un article spécifique (avec ses commentaires)
- Mettre à jour un article
- Supprimer un article (ses commentaires sont supprimés en cascade)
- Ajouter un commentaire sur un article et lister les commentaires d'un article

## Stack technologique

| Technologie | Usage |
|---|---|
| Java 21 + Spring Boot 3.5 | Framework applicatif |
| Spring Data JPA / Hibernate | Persistance |
| PostgreSQL | Base de données en production (Docker / Render) |
| H2 | Base en mémoire par défaut et pour les tests |
| Lombok | Réduction du boilerplate |
| springdoc-openapi | Documentation Swagger UI |

## Démarrage rapide

### 1. Sans Docker (H2 en mémoire)

```bash
mvn spring-boot:run
```

L'API démarre sur http://localhost:8080 — aucune configuration nécessaire.

### 2. Avec Docker Compose (API + PostgreSQL)

Copiez d'abord le template de variables d'environnement et adaptez les identifiants si besoin :

```bash
cp .env.example .env
docker compose up --build
```

- API : http://localhost:8080
- PostgreSQL : localhost:5432 par défaut (port ajustable via `POSTGRES_HOST_PORT` dans `.env` si ce port est déjà occupé — identifiants jamais versionnés)

> **Réseau restreint ?** Si le téléchargement de l'image `maven` n'aboutit pas,
> construisez le jar sur l'hôte puis lancez le stack sans rebuild :
>
> ```bash
> mvn package
> docker build -f Dockerfile.slim -t gestion-blog-api:latest .
> docker compose up --no-build
> ```

## Documentation Swagger

Une fois l'application démarrée :

- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8080/v3/api-docs

## Endpoints

| Méthode | Chemin | Description |
|---|---|---|
| POST | `/api/articles` | Créer un article |
| GET | `/api/articles` | Lister tous les articles |
| GET | `/api/articles/{id}` | Lire un article (avec commentaires) |
| PUT | `/api/articles/{id}` | Mettre à jour un article |
| DELETE | `/api/articles/{id}` | Supprimer un article |
| POST | `/api/articles/{articleId}/commentaires` | Ajouter un commentaire |
| GET | `/api/articles/{articleId}/commentaires` | Lister les commentaires d'un article |

### Exemples

Créer un article :

```bash
curl -X POST http://localhost:8080/api/articles \
  -H "Content-Type: application/json" \
  -d '{"titre": "Mon premier article", "contenu": "Contenu de l''article..."}'
```

Ajouter un commentaire :

```bash
curl -X POST http://localhost:8080/api/articles/1/commentaires \
  -H "Content-Type: application/json" \
  -d '{"contenu": "Super article !", "auteur": "Leonel"}'
```

### Format des réponses

Article :

```json
{
  "id": 1,
  "titre": "Mon premier article",
  "contenu": "Contenu de l'article...",
  "datePublication": "2026-09-21T12:00:00",
  "commentaires": [
    { "id": 1, "contenu": "Super article !", "auteur": "Leonel", "dateCreation": "2026-09-21T12:05:00", "articleId": 1 }
  ]
}
```

Erreurs au format RFC 7807 (Problem Detail) : `404` ressource introuvable, `400` erreur de validation.

## Tests

```bash
mvn test
```

Les tests d'intégration parcourent le cycle complet : création, lecture, mise à jour, suppression d'articles et gestion des commentaires (base H2 en mémoire).

## Structure du projet

```
src/main/java/com/k48/leonel/gestionblog/
├── GestionBlogApplication.java   # Point d'entrée
├── config/                       # Configuration Swagger/OpenAPI
├── controller/                   # Controllers REST
├── dto/                          # DTOs (records) + mapper
├── entity/                       # Entités JPA (Article, Commentaire)
├── exception/                    # Exceptions + handler global
├── repository/                   # Repositories Spring Data
└── service/                      # Logique métier
```

## Configuration & secrets

Aucune information sensible n'est versionnée dans le dépôt :

- **`.env`** (ignoré par git) contient les identifiants locaux — créez-le depuis `.env.example` : `cp .env.example .env`
- **`application.properties`** n'expose que des placeholders `${SPRING_DATASOURCE_*}` avec un fallback H2 en mémoire
- Sur Render, les identifiants PostgreSQL sont injectés automatiquement via `fromDatabase` (voir `render.yaml`)

## Déploiement

Un blueprint **Render** est fourni (`render.yaml`) : déploie l'API (Docker) + une base PostgreSQL managée et branche automatiquement les variables `SPRING_DATASOURCE_*` (aucun secret à saisir manuellement).
