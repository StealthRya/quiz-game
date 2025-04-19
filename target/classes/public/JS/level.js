let editor;

require.config({ paths: { vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@latest/min/vs' } });
require(['vs/editor/editor.main'], function () {
  editor = monaco.editor.create(document.getElementById('editor-container'), {
    value: `#include <stdio.h>\n\nint main() {\n    printf("Bonjour le monde");\n    return 0;\n}`,
    language: 'c',
    theme: 'vs-dark',
    fontSize: 14,
    automaticLayout: true
  });
});

document.getElementById('submit-code').addEventListener('click', async () => {
  const code = editor.getValue();

  const response = await fetch('/submit-code', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `code=${encodeURIComponent(code)}`
  });

  const output = await response.text();
  document.getElementById('output').textContent = output;
});

document.getElementById('next-question').addEventListener('click', () => {
  // Exemple de question suivante (à adapter dynamiquement depuis une base ou liste)
  document.getElementById('question-title').textContent = "Question 2";
  document.getElementById('question-text').textContent = "Écrivez un programme en C qui calcule la somme de deux entiers.";
  editor.setValue(`#include <stdio.h>\n\nint main() {\n    int a, b;\n    scanf("%d %d", &a, &b);\n    printf("%d", a + b);\n    return 0;\n}`);
  document.getElementById('output').textContent = "Cliquez sur \"Soumettre\" pour voir le résultat ici.";
});
