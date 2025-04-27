const form = document.getElementById('registerForm');
const popup = document.getElementById('popup');

form.addEventListener('submit', async function(e) {
    e.preventDefault(); // empêcher le rechargement de la page

    const formData = new FormData(form);

    try {
        const response = await fetch('/register', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            showPopup(errorMsg);
        } else {
            // Redirection ou message de succès
            window.location.href = "/login.html";
        }
    } catch (error) {
        showPopup("Erreur serveur !");
    }
});

function showPopup(message) {
    popup.textContent = message;
    popup.style.display = 'block';
    setTimeout(() => {
        popup.style.display = 'none';
    }, 3000);
}
