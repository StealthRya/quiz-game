package com.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerProfileDao {

    // Méthode pour vérifier si le joueur a déjà validé un défi
    public static boolean hasPlayerValidatedChallenge(int playerId, int challengeId) {
        String query = "SELECT COUNT(*) FROM validated_challenges WHERE user_id = ? AND challenge_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, challengeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour ajouter un score au joueur
    public static void addScoreToPlayer(int playerId, int score) {
        String query = "UPDATE player_profiles SET total_score = total_score + ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, score);
            stmt.setInt(2, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour marquer un défi comme validé
    public static void markChallengeAsValidated(int playerId, int challengeId) {
        String query = "INSERT INTO validated_challenges (user_id, challenge_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, challengeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

