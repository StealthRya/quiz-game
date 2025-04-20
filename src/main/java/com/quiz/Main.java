package com.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;








public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
        }).start(7000);

        // Redirection par défaut vers landing.html
        app.get("/", ctx -> ctx.redirect("/landing.html"));

        // Inscription
        app.post("/register", ctx -> {
            String pseudo = ctx.formParam("pseudo");
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            String confirmPassword = ctx.formParam("confirm-password");


            if (!password.equals(confirmPassword)) {
                ctx.status(400).result("Les mots de passe ne correspondent pas !");
                return;
            }

            try (Connection conn = DatabaseUtil.getConnection()) {
                // Étape 1 : Insérer dans la table users
                String queryUser = "INSERT INTO users (pseudo, email, password) VALUES (?, ?, ?)";
                PreparedStatement stmtUser = conn.prepareStatement(queryUser, PreparedStatement.RETURN_GENERATED_KEYS);
                stmtUser.setString(1, pseudo);
                stmtUser.setString(2, email);
                stmtUser.setString(3, password); // 🔒 À hasher plus tard
            
                int rows = stmtUser.executeUpdate();
                if (rows > 0) {
                    // Étape 2 : Récupérer l'id généré
                    ResultSet rs = stmtUser.getGeneratedKeys();
                    if (rs.next()) {
                        int userId = rs.getInt(1);
            
                        // Étape 3 : Créer le profil joueur associé
                        String queryProfile = "INSERT INTO player_profiles (user_id) VALUES (?)";
                        PreparedStatement stmtProfile = conn.prepareStatement(queryProfile);
                        stmtProfile.setInt(1, userId);
                        stmtProfile.executeUpdate();
            
                        // Envoi de mail + redirection
                        EmailSender.sendConfirmationEmail(email, pseudo, password);
                        ctx.redirect("/login.html?success=1");
                    } else {
                        ctx.status(500).result("Impossible de récupérer l'ID du nouvel utilisateur.");
                    }
                } else {
                    ctx.status(400).result("Erreur lors de l'inscription !");
                }
            } catch (Exception e) {
                ctx.status(500).result("Erreur serveur : " + e.getMessage());
            }
            
        });

        // Connexion
        app.post("/login", ctx -> {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            try (Connection conn = DatabaseUtil.getConnection()) {
                String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int playerId = rs.getInt("id");
                    String pseudo = rs.getString("pseudo");

                    // 🔐 Enregistrement dans la session
                    ctx.sessionAttribute("playerId", playerId);
                    ctx.sessionAttribute("pseudo", pseudo);

                    ctx.redirect("/dashboard.html");
                } else {
                    ctx.redirect("/login.html?error=1");
                }
            } catch (Exception e) {
                ctx.status(500).result("Erreur serveur : " + e.getMessage());
            }
        });

        // API pour dashboard
        app.get("/dashboard-data", ctx -> {
            Integer playerId = ctx.sessionAttribute("playerId");
        
            if (playerId == null) {
                ctx.status(401).json(Map.of("error", "Non autorisé"));
                return;
            }
        
            try (Connection conn = DatabaseUtil.getConnection()) {
                String query = "SELECT u.pseudo, p.total_score, p.current_level " +
                "FROM users u " +
                "JOIN player_profiles p ON u.id = p.user_id " +
                "WHERE u.id = ?";
 
        
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, playerId);
                ResultSet rs = stmt.executeQuery();
        
                if (rs.next()) {
                    String pseudo = rs.getString("pseudo");
                    int score = rs.getInt("total_score");
                    int level = rs.getInt("current_level");
        
                    Map<String, Object> response = new HashMap<>();
                    response.put("pseudo", pseudo);
                    response.put("score", score);
                    response.put("level", level);
        
                    ctx.json(response);
                } else {
                    ctx.status(404).json(Map.of("error", "Joueur introuvable."));
                }
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erreur serveur : " + e.getMessage()));
            }
        });

        //Deuxieme API pour dashboard
        app.get("/levels", LevelHandler::getAllLevels);

        //API pour la page level (Charger les niveaux et les defis correspondant au choix du user)
        app.get("/levelAndChallenge/:id", LevelHandler::getLevelAndChallenges);
        

        //Route pour gerer la soumission du code
        app.post("/submit",SubmissionHandler::handleSubmit);


         // Middleware CORS
         app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
        });

        


        // Déconnexion
        app.get("/logout", ctx -> {
            ctx.sessionAttribute("playerId", null);
            ctx.sessionAttribute("pseudo", null);
            ctx.redirect("/login.html");
        });
    }
}
