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

        // Vérification si le joueur a déjà validé ce défi
        if (PlayerProfileDao.hasPlayerValidatedChallenge(playerId, challengeId)) {
            ctx.status(400).json(Map.of("success", false, "message", "Défi déjà validé"));
            return;
        }

        // Évaluation du code soumis
        EvaluationResult result = CodeEvaluator.evaluateCode(userCode, challenge.expectedOutput);

        Map<String, Object> response = new HashMap<>();
        response.put("success", result.correct);
        response.put("output", result.output); // ✅ Affiche la sortie du programme
        response.put("message", result.correct ? "✅ Bonne réponse !" : "❌ Mauvaise réponse");

        if (result.correct) {
            // Si la réponse est correcte, ajouter le score au joueur
            PlayerProfileDao.addScoreToPlayer(playerId, 10);

            // Bloquer l'accès au défi en l'enregistrant comme validé
            PlayerProfileDao.markChallengeAsValidated(playerId, challengeId);
        }

        ctx.json(response);
    }
}
