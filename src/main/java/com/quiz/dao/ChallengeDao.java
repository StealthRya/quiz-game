package com.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.quiz.model.Challenges;
import com.quiz.utils.DatabaseUtil;

public class ChallengeDao {
    public static Challenges getChallengeById(int id) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM challenges WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Challenges ch = new Challenges();
                ch.id = rs.getInt("id");
                ch.questionText = rs.getString("challenge_text");
                ch.expectedOutput = rs.getString("expected_output"); // 🔥 important ici
                return ch;
            }
        } catch (Exception e) {
            System.out.println("Erreur DB: " + e.getMessage());
        }
        return null;
    }
}
