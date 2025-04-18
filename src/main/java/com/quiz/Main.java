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
        }).start(7070);

        // Redirection par défaut vers landing.html
        app.get("/", ctx -> ctx.redirect("/landing.html"));

        // Inscription
        app.post("/register", ctx -> {
            String pseudo = ctx.formParam("pseudo");
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            String confirmPassword = ctx.formParam("confirm-password");

            final String role = "player";

            if (!password.equals(confirmPassword)) {
                ctx.status(400).result("Les mots de passe ne correspondent pas !");
                return;
            }

            try (Connection conn = DatabaseUtil.getConnection()) {
                String query = "INSERT INTO users (pseudo,email,password,role) VALUES (?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, pseudo);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4,role); // 🔒 À hasher plus tard

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    EmailSender.sendConfirmationEmail(email, pseudo, password);
                    ctx.redirect("/login.html?success=1");
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
                String query = "SELECT * FROM users WHERE email = ? AND player_password = ?";
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
                String query = "SELECT pseudo, score FROM players WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, playerId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String pseudo = rs.getString("pseudo");
                    int score = rs.getInt("score");

                    Map<String, Object> response = new HashMap<>();
                    response.put("pseudo", pseudo);
                    response.put("score", score);
                    response.put("level", 1); // par défaut

                    ctx.json(response);
                } else {
                    ctx.status(404).json(Map.of("error", "Joueur introuvable."));
                }
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erreur serveur : " + e.getMessage()));
            }
        });

        // Déconnexion
        app.get("/logout", ctx -> {
            ctx.sessionAttribute("playerId", null);
            ctx.sessionAttribute("pseudo", null);
            ctx.redirect("/login.html");
        });
    }
}
