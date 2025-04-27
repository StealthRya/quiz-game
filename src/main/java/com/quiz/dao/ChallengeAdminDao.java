package com.quiz.dao;


import com.quiz.model.ChallengeAdmin;
import com.quiz.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;




public class ChallengeAdminDao {

    public List<ChallengeAdmin> getAllChallengesList() {
        List<ChallengeAdmin> result = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT l.title AS levelTitle, c.challenge_text, c.expected_output " +
            "FROM challenges c " +
            "JOIN levels l ON c.level_id = l.id ";


            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String levelTitle = rs.getString("levelTitle");
                String challengeText = rs.getString("challenge_text");
                String expectedOutput = rs.getString("expected_output");

                ChallengeAdmin challenge = new ChallengeAdmin(levelTitle, challengeText, expectedOutput);
                result.add(challenge);
            }

        } catch (Exception e) {
            System.out.println("ERROR in ChallengeAdminDao.java: " + e);
        }

        return result;
    }
}
