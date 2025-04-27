const levelNameInput = document.getElementById('level-name');

// Cette fonction est appelée directement par ton bouton onclick
async function addLevel() {
    const levelName = levelNameInput.value.trim();

    if (!levelName) {
        alert("Veuillez entrer un nom de niveau.");
        return;
    }

    const levelFormData = new FormData();
    levelFormData.append('levelName', levelName);

    try {
        const response = await fetch('/addLevel', {
            method: 'POST',
            body: levelFormData,
        });

        if (!response.ok) {
            console.log("ERROR: level data not sent");
            alert("Erreur lors de l'ajout du niveau.");
        } else {
            alert("Niveau ajouté avec succès!");
            window.location.href = "/challenges.html"; // Redirige après succès
        }
    } catch (error) {
        alert("Une erreur s'est produite lors de l'ajout du niveau.");
        console.error("Error:", error);
    }
}
