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
             }
         });
     }

     // Call the function when the page is loaded
     window.addEventListener('Click', function () {
         hideNonMatchingRows();
     });