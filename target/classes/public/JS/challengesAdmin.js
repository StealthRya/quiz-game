document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/adminChallenges')
        .then(response => response.json())
        .then(challenges => {
            displayChallengesGroupedByLevel(challenges);
        })
        .catch(error => {
            console.error('Erreur lors du chargement des défis:', error);
            alert('Impossible de charger les défis.');
        });
});

function displayChallengesGroupedByLevel(challenges) {
    const container = document.getElementById('challenge-list');
    container.innerHTML = '';

    const levelsMap = new Map();

    // Regrouper les défis par niveau
    challenges.forEach(challenge => {
        const level = challenge.levelTitle;
        if (!levelsMap.has(level)) {
            levelsMap.set(level, []);
        }
        levelsMap.get(level).push(challenge);
    });

    // Affichage
    levelsMap.forEach((challenges, level) => {
        const levelDiv = document.createElement('div');
        levelDiv.className = 'level-item';

        const levelHeader = document.createElement('div');
        levelHeader.className = 'level-actions';

        const levelTitle = document.createElement('h2');
        levelTitle.textContent = `Niveau : ${level}`;
        levelTitle.style.color = 'var(--text-color)';

        const editLevelBtn = document.createElement('button');
        editLevelBtn.textContent = '✏️ Modifier';
        editLevelBtn.className = 'edit-challenge-btn';

        const deleteLevelBtn = document.createElement('button');
        deleteLevelBtn.textContent = '🗑️ Supprimer';
        deleteLevelBtn.className = 'delete-level-btn';

        levelHeader.appendChild(editLevelBtn);
        levelHeader.appendChild(deleteLevelBtn);

        levelDiv.appendChild(levelTitle);
        levelDiv.appendChild(levelHeader);

        const challengeList = document.createElement('div');
        challengeList.className = 'challenge-list';

        challenges.forEach(challenge => {
            const challengeItem = document.createElement('div');
            challengeItem.className = 'challenge-item';

            const challengeText = document.createElement('div');
            challengeText.className = 'challenge-text';
            challengeText.textContent = challenge.challengeText;

            const expectedOutput = document.createElement('div');
            expectedOutput.className = 'expected-output';
            expectedOutput.textContent = challenge.expectedOutput;

            const actions = document.createElement('div');
            actions.className = 'challenge-actions';

            const editBtn = document.createElement('button');
            editBtn.textContent = '✏️ Modifier';
            editBtn.className = 'edit-challenge-btn';

            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = '🗑️ Supprimer';
            deleteBtn.className = 'delete-challenge-btn';

            actions.appendChild(editBtn);
            actions.appendChild(deleteBtn);

            challengeItem.appendChild(challengeText);
            challengeItem.appendChild(expectedOutput);
            challengeItem.appendChild(actions);

            challengeList.appendChild(challengeItem);
        });

        levelDiv.appendChild(challengeList);
        container.appendChild(levelDiv);
    });
}
