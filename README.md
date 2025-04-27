# Quiz Game

Un jeu de quiz éducatif avec soumission de code, gestion des niveaux, score en base de données, interface web, et plus encore.  
Projet basé sur **Java** avec **Maven** et architecture **MVC** (Model - View - Controller).

---


---

## 📂 Dossiers et fichiers

### 🔧 `pom.xml`
- Fichier de configuration Maven.
- Gère les dépendances, la compilation, les plugins, le packaging, etc.

---

### 📁 `src/main/java/com/quiz/`

#### ✅ `Main.java`
- Point d’entrée de l’application Java.
- Initialise les composants (chargement, base de données, interface, etc.).

#### 📦 `dao/`
- **Data Access Objects**.
- Contient les classes responsables des interactions avec la base de données.
- Exemples :
  - `ChallengeDao.java` : récupération des challenges depuis la BDD.
  - `PlayerProfileDao.java` : gestion des profils des joueurs.

#### 📦 `model/`
- Représente les **entités du domaine**.
- Contient les classes "métier" utilisées dans toute l’application.
- Exemples :
  - `Challenges.java` : structure d’un challenge.
  - `CodeSubmission.java` : code soumis par l’utilisateur.
  - `EvaluationResult.java` : résultat d’une évaluation.
  - `Level.java` : niveaux de difficulté.

#### 📦 `service/`
- Contient la **logique métier** de l’application.
- Fait le lien entre le `model` et le `dao`.
- Exemples :
  - `CodeEvaluator.java` : évalue le code soumis.
  - `LevelHandler.java` : gère les niveaux.
  - `SubmissionHandler.java` : enregistre les réponses et met à jour les scores.

#### 📦 `utils/`
- Utilitaires réutilisables.
- Exemples :
  - `DatabaseUtil.java` : gestion de la connexion à la base.
  - `EmailSender.java` : envoi d’emails (confirmation, score, etc.).

---

### 📁 `src/main/resources/public/`

Ce dossier contient la **partie Vue (View)** de l’application.

#### 📁 `CSS/`
- Feuilles de style pour chaque page.
  - `admin.css`, `challenges.css`, `level.css`, etc.

#### 📁 `JS/`
- Scripts JavaScript pour gérer l’interactivité des pages.
  - `dashboard.js`, `level.js`, etc.

#### 📁 `images/`
- Images utilisées dans l’interface (login, background, etc.).

#### 📄 HTML Pages :
- `landing.html`, `login.html`, `dashboard.html`, `register.html`, etc.
- Interfaces utilisateurs avec leurs propres styles et scripts.

---

### 📁 `src/test/java/com/quiz/`

#### 🧪 `AppTest.java`
- Fichier de test unitaire généré par Maven.
- Contient des tests pour valider les fonctionnalités de l’application.

---

### 📁 `target/`
- Dossier généré par Maven après compilation.
- Contient les `.class`, `.jar`, fichiers HTML compilés, rapports de test, etc.
- **Ne pas modifier manuellement**.

---

## 🚀 Lancement de l'application

1. Assure-toi d’avoir Java et Maven installés.
2. Dans le dossier du projet :

```bash
mvn clean install
java -cp target/quiz-game-1.0-SNAPSHOT.jar com.quiz.Main
