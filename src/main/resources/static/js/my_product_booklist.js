
     
    document.addEventListener("DOMContentLoaded", function() {
        // Get the select element
        const selectElement = document.getElementById("accSelect");

        // Add an event listener to the select element
        selectElement.addEventListener("change", function() {
            // Get the selected value
            const selectedValue = selectElement.value;

            // Get all the table rows
            const tableRows = document.querySelectorAll("#booking-table tbody tr");

            // Loop through each row and check if the accommodation title matches the selected value
            tableRows.forEach(function(row) {
                const accomTitleCell = row.querySelector("td:nth-child(2)"); // Assuming the accommodation title is in the second column

                if (selectedValue === "모두" || accomTitleCell.textContent === selectedValue) {
                    // If the selected value is "모두" or the accommodation title matches the selected value, show the row
                    row.style.display = "table-row";
                } else {
                    // Otherwise, hide the row
                    row.style.display = "none";
                }
            });
        });
    });
     
         document.addEventListener("DOMContentLoaded", function() {
        // Get the select element
        const selectElement = document.getElementById("accSelect");
        
        // Get the no match message element
        const noMatchMessage = document.getElementById("noMatchMessage");

        // Add an event listener to the select element
        selectElement.addEventListener("change", function() {
            // Get the selected value
            const selectedValue = selectElement.value;

            // Get all the table rows
            const tableRows = document.querySelectorAll("#booking-table tbody tr");
            
            // Initialize a variable to keep track of whether there are matching rows or not
            let hasMatchingRows = false;

            // Loop through each row and check if the accommodation title matches the selected value
            tableRows.forEach(function(row) {
                const accomTitleCell = row.querySelector("td:nth-child(2)"); // Assuming the accommodation title is in the second column

                if (selectedValue === "모두" || accomTitleCell.textContent === selectedValue) {
                    // If the selected value is "모두" or the accommodation title matches the selected value, show the row
                    row.style.display = "table-row";
                    hasMatchingRows = true;
                } else {
                    // Otherwise, hide the row
                    row.style.display = "none";
                }
            });

            // Show/hide the no match message based on whether there are matching rows
            if (hasMatchingRows) {
                noMatchMessage.style.display = "none";
            } else {
                noMatchMessage.style.display = "block";
            }
        });
    });
    
    
    
 
  /*
  
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
    
    */