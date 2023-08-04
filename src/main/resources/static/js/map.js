document.addEventListener("DOMContentLoaded", function() {
    const areas = document.querySelectorAll(".area");

    areas.forEach(area => {
        area.addEventListener("mouseenter", function() {
            area.style.backgroundColor = "rgba(0, 0, 255, 0.5)"; // 원하는 색상으로 변경
        });

        area.addEventListener("mouseleave", function() {
            area.style.backgroundColor = "transparent"; // 마우스 떠났을 때 원래 색상으로 변경
        });
    });
});