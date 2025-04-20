package com.quiz;

import io.javalin.http.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;




public class LevelHandler {
    public static void getLevelAndChallenges(Context ctx){
        //Lire l'id du niveau selectionner par le user depuis l'URL
        int levelId = Integer.parseInt(ctx.pathParam("id"));
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query ="SELECT * FROM levels WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, levelId);
            ResultSet rs = stmt.executeQuery();
           

            if (!rs.next()) {
                ctx.status(404).result("Niveau non trouvé");
                return;
            }

            Level levelAndChallenge = new Level();
            levelAndChallenge.id = rs.getInt("id");
            levelAndChallenge.title = rs.getString("title");
            levelAndChallenge.challenge = new ArrayList<>();

            //Recuperer les challenges correspondnat au level
            String challengeQuery = "SELECT * FROM challenges WHERE level_id = ?";
            PreparedStatement challengeStmt = conn.prepareStatement(challengeQuery);
            challengeStmt.setInt(1, levelId);
            ResultSet challengeRs = challengeStmt.executeQuery();

            

            while(challengeRs.next()){
                Challenges ch = new Challenges();
                ch.id = challengeRs.getInt("id");
                ch.questionText = challengeRs.getString("challenge_text");
                levelAndChallenge.challenge.add(ch);
            }


            //Envoyer le resultat
            ctx.json(levelAndChallenge);

       

        } catch (Exception e) {
            System.out.println("ERROR: Data base Connection failed: "+e);
        }
    }

    public static void getAllLevels(Context ctx) {
    try (Connection conn = DatabaseUtil.getConnection()) {
        String query = "SELECT * FROM levels";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ArrayList<Level> levels = new ArrayList<>();

        while (rs.next()) {
            Level level = new Level();
            level.id = rs.getInt("id");
            level.title = rs.getString("title");
            levels.add(level); // ne charge pas les challenges ici
        }

        ctx.json(levels);
    } catch (Exception e) {
        ctx.status(500).result("Erreur serveur: " + e.getMessage());
    }
}

}
