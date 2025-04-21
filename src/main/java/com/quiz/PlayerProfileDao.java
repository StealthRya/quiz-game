package com.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlayerProfileDao {
      /**
     * Incrémente le score total d'un joueur de X points.
     *
     * @param playerId L'ID du joueur (lié à la table users)
     * @param points   Le nombre de points à ajouter (ex: 10)
     */
    public static void addScoreToPlayer(int playerId, int points) {
        String sql = "UPDATE player_profiles SET total_score = total_score + ? WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, points);      // 1er paramètre : combien de points on ajoute
            stmt.setInt(2, playerId);    // 2e paramètre : l’ID du joueur

            stmt.executeUpdate();        // Exécute la requête SQL

        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du score : " + e.getMessage());
        }
    }
}
