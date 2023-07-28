function hideNonMatchingRows() {
    var bookingRows = document.querySelectorAll('#booking-table tbody tr');
    var accRows = document.querySelectorAll('#acc-table tbody tr');

    bookingRows.forEach(function (bookingRow) {
        var accomTitle = bookingRow.querySelector('td:nth-child(2)').innerText.trim();
        var matchingRowExists = false;

        accRows.forEach(function (accRow) {
            var accName = accRow.querySelector('td:nth-child(3)').innerText.trim();
            if (accomTitle === accName) {
                matchingRowExists = true;
            }
        });

        if (!matchingRowExists) {
            bookingRow.style.display = 'none';
            bookingRow.style.color = 'white'; // Add this line to set the font color to white
        }
    });
}

// Call the function when the page is loaded
window.addEventListener('load', function () {
    hideNonMatchingRows();
});
     
     
     
     
     
     
     
     
      document.addEventListener("DOMContentLoaded", function() {
    const usernameElement = document.getElementById("username");
    if (usernameElement) {
      usernameElement.textContent = "비밀이지롱~";
    }
  });
  
  
  
   document.addEventListener("DOMContentLoaded", function () {
        const accSelect = document.getElementById("accSelect");
        const bookingTable = document.getElementById("booking-table").getElementsByTagName("tbody")[0].getElementsByTagName("tr");

        accSelect.addEventListener("change", function () {
            const selectedAccName = accSelect.value;
            for (const row of bookingTable) {
                const accNameCell = row.getElementsByTagName("td")[1];
                if (selectedAccName === "모두" || accNameCell.textContent === selectedAccName) {
                    row.style.display = "table-row";
                } else {
                    row.style.display = "none";
                }
            }
        });
    });