package com.quiz.controllers;

import com.quiz.service.RankingService;

import io.javalin.http.Context;

public class RankingController {
    public static void getRanking(Context ctx){
        ctx.json(RankingService.handleRank());
    }
}
