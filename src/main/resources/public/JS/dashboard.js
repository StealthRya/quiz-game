// Départ à 30 minutes
let time = 30 * 60; // en secondes
const timerDisplay = document.getElementById("timer");

function updateTimer() {
    const minutes = Math.floor(time / 60);
    const seconds = time % 60;

    // Format MM:SS
    timerDisplay.textContent = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;

    if (time > 0) {
        time--;
    } else {
        clearInterval(timerInterval);
        timerDisplay.textContent = "Time's up!";
    }
}

// Mise à jour chaque seconde
const timerInterval = setInterval(updateTimer, 1000);
updateTimer(); // appel immédiat pour afficher 30:00 sans délai

// --- Chargement des données du joueur ---
fetch("/dashboard-data")
    .then(response => {
        if (!response.ok) throw new Error("Non autorisé ou erreur");
        return response.json();
    })
    .then(data => {
        console.log("Dashboard data loaded:", data);
        document.getElementById("player-pseudo").textContent = data.pseudo;
        document.getElementById("player-level").textContent = data.level;
        document.getElementById("player-score").textContent = data.score;
    })
    .catch(error => {
        console.error("Erreur lors du chargement du dashboard:", error);
        window.location.href = "./login.html"; // redirection en cas de problème
    });

// Chargement des niveaux
// Chargement des niveaux
fetch("/levels")
    .then(res => {
        if (!res.ok) throw new Error("Erreur lors du chargement des niveaux");
        return res.json();
    })
    .then(levels => {
        console.log("Levels loaded:", levels);
        const container = document.getElementById("levels-container");
        if (!container) {
            console.error("Container for levels not found");
            return;
        }
        container.innerHTML = ""; // Clear existing content

        if (levels.length === 0) {
            container.innerHTML = "<p>No levels available</p>";
            return;
        }

        levels.forEach(level => {
            console.log("Creating button for level:", level);
            const btn = document.createElement("button");
            btn.className = "stBtnLevels";  // Utilise le style btnLevel

            btn.onclick = () => location.href = `./level.html?id=${level.id}`;

            // Dynamique : Afficher simplement le title du niveau
            btn.innerHTML = `
                <div class="stBtnLevels">
                   <div class="info">
                     <h2>${level.id.toString().padStart(2, '0')}</h2>
                     <p>${level.title}</p> <!-- Afficher le titre du niveau -->
                   </div>
                </div>
            `;
            container.appendChild(btn);  // Ajouter le bouton au conteneur
        });
    })
    .catch(err => {
        console.error("Erreur lors du chargement des niveaux :", err);
        const container = document.getElementById("levels-container");
        if (container) {
            container.innerHTML = "<p>Error loading levels</p>";
        }
    });

