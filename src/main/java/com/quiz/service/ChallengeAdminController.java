package com.quiz.service;

import com.quiz.dao.ChallengeAdminDao;
import com.quiz.model.ChallengeAdmin;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class ChallengeAdminController {
    private ChallengeAdminDao challengeAdminDao = new ChallengeAdminDao();

    public void setupRoutes(Javalin app) {
        // Register the route for getting all challenges
        app.get("/api/adminChallenges", this::getAllChallenges);
    }

    // This method will be triggered when the endpoint is hit
    private void getAllChallenges(Context ctx) {
        try {
            // Get the list of challenges from the DAO
            List<ChallengeAdmin> challenges = challengeAdminDao.getAllChallengesList();

            // Send the challenges list as a JSON response
            ctx.json(challenges);

        } catch (Exception e) {
            // Handle any errors
            ctx.status(500).result("Error fetching challenges: " + e.getMessage());
        }
    }
}
