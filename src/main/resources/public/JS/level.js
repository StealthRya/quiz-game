function getLevelIdFromURL() {
  const params = new URLSearchParams(window.location.search);
  return params.get('id');
}

let currentQuestionIndex = 0;
let challenges = [];

document.addEventListener("DOMContentLoaded", async () => {
  const levelId = getLevelIdFromURL();
  if (!levelId) return alert("Aucun niveau spécifié dans l'URL");

  try {
    const response = await fetch(`http://localhost:7000/levelAndChallenge/${levelId}`);
    if (!response.ok) throw new Error("Niveau non trouvé");
    
    const levelData = await response.json();
    document.getElementById("level-title").textContent = `Level ${levelData.id}: ${levelData.title}`;

    challenges = levelData.challenge;
    document.getElementById("total-questions").textContent = challenges.length;

    displayQuestion();

    loadMonacoEditor();

  } catch (error) {
    alert("Erreur lors du chargement des données : " + error.message);
  }
});

function displayQuestion() {
  if (!challenges.length) return;

  const question = challenges[currentQuestionIndex];
  document.getElementById("question-number").textContent = currentQuestionIndex + 1;
  document.getElementById("question-text").textContent = question.questionText;
}

function loadMonacoEditor() {
  require.config({ paths: { vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@latest/min/vs' } });
  require(['vs/editor/editor.main'], function () {
    window.editor = monaco.editor.create(document.getElementById('editor-container'), {
      value: `#include <stdio.h>

int main() {
    // Tapez votre code ici
    return 0;
}
`,
      language: 'c',
      theme: 'vs-dark',
      automaticLayout: true
    });
  });
}

document.getElementById("prev-question").addEventListener("click", () => {
  if (currentQuestionIndex > 0) {
    currentQuestionIndex--;
    displayQuestion();
  }
});

document.getElementById("next-question").addEventListener("click", () => {
  if (currentQuestionIndex < challenges.length - 1) {
    currentQuestionIndex++;
    displayQuestion();
  }
});

document.getElementById("submit-code").addEventListener("click", async () => {
  const userCode = window.editor.getValue();
  const levelId = getLevelIdFromURL();
  const challenge = challenges[currentQuestionIndex];
  const challengeId = challenge.id;

  try {
    const response = await fetch('http://localhost:7000/submit', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        levelId,
        challengeId,
        code: userCode
      })
    });

    const result = await response.json();

    // Affichage complet du message et du output utilisateur
    document.getElementById("output").textContent =
      `++Sortie du programme :\n${result.output}\n\n++ Résultat : ${result.message}`;

  } catch (error) {
    document.getElementById("output").textContent = "❌ Erreur : " + error.message;
  }
});
