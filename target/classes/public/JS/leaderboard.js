// leaderboard.js

// Fonction pour charger les classements depuis le serveur
async function loadRankings() {
    try {
        const response = await fetch('http://localhost:7000/ranking'); // Remplace par ton URL si besoin
        const data = await response.json();

        const topPlayersDiv = document.getElementById('top-players');
        const rankingsBody = document.getElementById('rankings-body');

        topPlayersDiv.innerHTML = '';
        rankingsBody.innerHTML = '';

        if (data.length === 0) {
            // Afficher un message s'il n'y a aucun joueur
            const noDataMessage = document.createElement('div');
            noDataMessage.className = 'no-data-message';
            noDataMessage.textContent = 'Aucun joueur trouvé.';
            rankingsBody.appendChild(noDataMessage);
            return;
        }

        // Gérer les top 3
        const topClasses = ['gold', 'silver', 'bronze'];

        data.slice(0, 3).forEach((player, index) => {
            const div = document.createElement('div');
            div.className = `top-player ${topClasses[index]} animate-fade-in`;
            div.innerHTML = `
                <div class="rank">${index + 1}</div>
                <div class="crown"><i class="fas fa-crown"></i></div>
                <div class="username">${player.userPseudo}</div>
                <div class="score">${player.userScore}</div>
            `;
            topPlayersDiv.appendChild(div);
        });

        // Gérer les autres joueurs
        data.slice(3).forEach((player, index) => {
            const tr = document.createElement('tr');
            tr.className = 'animate-fade-in';
            tr.innerHTML = `
                <td>${index + 4}</td>
                <td>${player.userPseudo}</td>
                <td>${player.userScore}</td>
            `;
            rankingsBody.appendChild(tr);
        });

    } catch (error) {
        console.error('Erreur lors du chargement des classements :', error);
    }
}

// Appeler la fonction quand la page est prête
window.addEventListener('DOMContentLoaded', loadRankings);
