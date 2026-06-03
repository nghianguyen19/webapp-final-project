document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;
    const toggleButton = document.getElementById("themeToggle");

    const savedTheme = localStorage.getItem("theme");

    if (savedTheme === "dark") {
        body.classList.add("dark-mode");
        updateToggleButton("dark");
    } else {
        updateToggleButton("light");
    }

    if (toggleButton) {
        toggleButton.addEventListener("click", function () {
            body.classList.toggle("dark-mode");

            if (body.classList.contains("dark-mode")) {
                localStorage.setItem("theme", "dark");
                updateToggleButton("dark");
            } else {
                localStorage.setItem("theme", "light");
                updateToggleButton("light");
            }
        });
    }

    function updateToggleButton(theme) {
        if (!toggleButton) {
            return;
        }

        if (theme === "dark") {
            toggleButton.textContent = "Light";
        } else {
            toggleButton.textContent = "Dark";
        }
    }
});