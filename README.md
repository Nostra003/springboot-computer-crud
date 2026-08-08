# springboot-computer-crud

[![CI](https://github.com/Nostra003/springboot-computer-crud/actions/workflows/ci.yml/badge.svg)](https://github.com/Nostra003/springboot-computer-crud/actions/workflows/ci.yml)

API REST Spring Boot pour gérer un parc d'ordinateurs (CRUD complet) : créer, lister, rechercher, mettre à jour et supprimer des fiches machines (processeur, RAM, disque dur, prix, adresse MAC).

## Stack technique

- **Java 17** / **Spring Boot 3.4**
- **Spring Web** — API REST
- **Spring Data JPA** — persistance
- **Bean Validation** — validation des payloads entrants
- **H2** (dev/tests, en mémoire) / **PostgreSQL** (prod)
- **Flyway** — migrations de schéma versionnées (profil `prod`)
- **springdoc-openapi** — documentation Swagger UI générée automatiquement
- **JUnit 5 / Mockito / MockMvc** — tests unitaires et d'intégration
- **GitHub Actions** — build et tests à chaque push/PR
- **Docker / Docker Compose** — exécution conteneurisée avec Postgres

## Lancer le projet

### En local (profil dev, H2 en mémoire — aucune installation de base de données requise)

```bash
./mvnw spring-boot:run
```

L'API démarre sur `http://localhost:8090`. Elle est pré-remplie avec 4 ordinateurs de démo au démarrage.

- **Interface web** (tableau + formulaire CRUD) : `http://localhost:8090/`
- Console H2 : `http://localhost:8090/h2-console` (JDBC URL : `jdbc:h2:mem:computers-db`, user `sa`, pas de mot de passe)
- Documentation Swagger UI : `http://localhost:8090/swagger-ui.html`

### Avec Docker Compose (profil prod, PostgreSQL persistant)

```bash
docker compose up --build
```

Lance l'API (profil `prod`) + une base PostgreSQL, avec le schéma géré par Flyway. L'API est disponible sur `http://localhost:8090`.

## Lancer les tests

```bash
./mvnw clean verify
```

C'est exactement ce que la CI GitHub Actions exécute à chaque push/PR sur `main`.

## Endpoints de l'API

Toutes les routes sont préfixées par `/api/computers`.

| Méthode | Route                        | Description                                      |
|---------|-------------------------------|---------------------------------------------------|
| POST    | `/api/computers`              | Créer un ordinateur                                |
| GET     | `/api/computers`               | Lister tous les ordinateurs (paginé : `?page=0&size=20`) |
| GET     | `/api/computers/{id}`          | Récupérer un ordinateur par id                     |
| PUT     | `/api/computers/{id}`          | Mettre à jour un ordinateur                        |
| DELETE  | `/api/computers/{id}`          | Supprimer un ordinateur                            |
| GET     | `/api/computers/price/{max}`   | Lister les ordinateurs dont le prix est < `max`    |
| GET     | `/api/computers/processor/{p}` | Lister les ordinateurs par type de processeur      |

### Exemple : créer un ordinateur

```bash
curl -X POST http://localhost:8090/api/computers \
  -H "Content-Type: application/json" \
  -d '{
        "proce": "i7",
        "ram": 16,
        "hardDrive": 1000,
        "price": 9500,
        "macAddress": "AA-BB-CC-DD-EE-FF"
      }'
```

### Réponses d'erreur

Toute erreur renvoie un JSON uniforme :

```json
{
  "timestamp": "2026-08-08T12:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Un ordinateur avec l'adresse MAC 'AA-BB-CC-DD-EE-FF' existe déjà",
  "path": "/api/computers"
}
```

| Cas                                   | Code HTTP |
|----------------------------------------|-----------|
| Adresse MAC déjà utilisée              | 409       |
| Ordinateur introuvable (id inconnu)    | 404       |
| Payload invalide (champs manquants...) | 400       |

## Structure du projet

```
src/main/java/com/example/controlej2e/
├── controller/    # Contrôleurs REST
├── service/       # Logique métier
├── dao/           # Repositories Spring Data JPA
├── entities/      # Entités JPA
├── dto/           # DTOs + mapping entité <-> DTO
├── config/        # Configuration (seeding de données de démo)
└── exception/     # Exceptions métier + gestion d'erreurs centralisée

src/main/resources/static/   # Interface web statique (HTML/CSS/JS vanilla,
                              # servie directement par Spring Boot sur "/")
```

## Licence

Ce projet est sous licence [MIT](LICENSE).
