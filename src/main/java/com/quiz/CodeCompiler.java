package com.quiz;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import io.javalin.http.Context;

public class CodeCompiler {

    public static void handleCodeSubmission(Context ctx) {
        String code = ctx.formParam("code");

        if (code == null || code.trim().isEmpty()) {
            ctx.result("Code vide.");
            return;
        }

        try {
            // Créer un fichier temporaire
            Path tempDir = Files.createTempDirectory("code_c");
            File sourceFile = new File(tempDir.toFile(), "main.c");
            Files.writeString(sourceFile.toPath(), code);

            // Compiler avec GCC
            File outputFile = new File(tempDir.toFile(), "main.out");
            Process compileProcess = new ProcessBuilder("gcc", sourceFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();

            String compileOutput = new String(compileProcess.getInputStream().readAllBytes());
            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode != 0) {
                ctx.result("Erreur de compilation :\n" + compileOutput);
                return;
            }

            // Exécuter le programme compilé
            Process runProcess = new ProcessBuilder(outputFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();

            String runOutput = new String(runProcess.getInputStream().readAllBytes());
            runProcess.waitFor();

            ctx.result("Sortie du programme :\n" + runOutput);
        } catch (Exception e) {
            ctx.result("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
