package com.quiz;

import java.io.*;
import java.nio.file.*;

public class CodeEvaluator {

    // 1. Écrire le code dans un fichier temporaire
    public static String writeCodeToFile(String userCode) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String codeFilePath = tempDir + "/user_code.c";
        Files.write(Paths.get(codeFilePath), userCode.getBytes());
        return codeFilePath;
    }

    // 2. Compiler le code avec gcc
    public static String compileCode(String sourcePath) throws IOException, InterruptedException {
        String outputPath = sourcePath.replace(".c", ".out");
        ProcessBuilder pb = new ProcessBuilder("gcc", sourcePath, "-o", outputPath);
        Process process = pb.start();
        process.waitFor();

        if (process.exitValue() != 0) {
            String error = new String(process.getErrorStream().readAllBytes());
            throw new RuntimeException("Erreur de compilation : " + error);
        }

        return outputPath;
    }

    // 3. Exécuter le programme compilé et capturer la sortie
    public static String executeCode(String binaryPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(binaryPath);
        pb.redirectErrorStream(true); // stderr inclus dans stdout
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();
        return output.toString().trim(); // Nettoyage
    }

    // 🔁 Nouvelle méthode qui retourne le résultat + la sortie du code
    public static EvaluationResult evaluateCode(String userCode, String expectedOutput) {
        try {
            String codePath = writeCodeToFile(userCode);
            String binaryPath = compileCode(codePath);
            String output = executeCode(binaryPath);

            boolean correct = output.equals(expectedOutput.trim());
            return new EvaluationResult(correct, output);
        } catch (Exception e) {
            return new EvaluationResult(false, "Erreur: " + e.getMessage());
        }
    }
}
