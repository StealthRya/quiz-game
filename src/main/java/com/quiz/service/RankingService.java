package com.quiz.service;

import com.quiz.model.RankingModel;
import com.quiz.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class RankingService {
   public static List<RankingModel> handleRank(){
    List<RankingModel> rankingList = new ArrayList();
    try(Connection conn = DatabaseUtil.getConnection()) {
        String query = "SELECT users.pseudo, player_profiles.total_score " +
               "FROM users " +
               "JOIN player_profiles ON users.id = player_profiles.user_id " +
               "ORDER BY player_profiles.total_score DESC";
//Du plus eleve au moins eleve

        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            String pseudo = rs.getString("pseudo");
            int score = rs.getInt("total_score");

            RankingModel model = new RankingModel(pseudo, score);
            rankingList.add(model);
        }
    } catch (Exception e) {
        System.out.println("ERROR in RankingService.java: "+e);
    }
    return rankingList;
   }
}
