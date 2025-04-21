package com.quiz;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class SubmissionHandler {

    public static void handleSubmit(Context ctx) {
        CodeSubmission submission = ctx.bodyAsClass(CodeSubmission.class); // ✅ une seule fois

        Integer playerId = ctx.sessionAttribute("playerId");

        if (playerId == null) {
            ctx.status(401).json(Map.of("error", "Non connecté"));
            return;
        }

        int challengeId = submission.challengeId;
        String userCode = submission.code;

        Challenges challenge = ChallengeDao.getChallengeById(challengeId);
        if (challenge == null) {
            ctx.status(404).json(Map.of("success", false, "message", "Challenge introuvable"));
            return;
        }

        EvaluationResult result = CodeEvaluator.evaluateCode(userCode, challenge.expectedOutput);

        Map<String, Object> response = new HashMap<>();
        response.put("success", result.correct);
        response.put("output", result.output); // ✅ Affiche la sortie du programme
        response.put("message", result.correct ? "✅ Bonne réponse !" : "❌ Mauvaise réponse");

        PlayerProfileDao.addScoreToPlayer(playerId, 10);

        ctx.json(response);
    }
}
